package com.example.xue.myqq.fragment;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.xue.myqq.R;
import com.example.xue.myqq.activity.TrendsBatteryInfoActivity;
import com.example.xue.myqq.activity.TrendsGalleryActivity;
import com.example.xue.myqq.activity.TrendsSpaceActivity;
import com.example.xue.myqq.base.App;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class TrendsFragment extends Fragment implements View.OnClickListener {

    private Button mButtonTrendsSpace;  //动态按钮
    private Button mButtonTrendsGallery;//图片集按钮
    private Button mButtonTrendsBattery;//电池信息按钮

    //double数组存储电池信息，共5个数据依次为
    //电池容量，剩余电量百分比，电池状态{2，充电中；3，未充电}，瞬时电流正值放电，出厂时电量（mAh）
    private double[] mBatteryInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, null, false);

        //获取按钮组件
        mButtonTrendsBattery = view.findViewById(R.id.button_trends_battery_id);
        mButtonTrendsSpace = view.findViewById(R.id.button_trends_space_id);
        mButtonTrendsGallery = view.findViewById(R.id.button_trends_gallery_id);
        //设置点击事件监听
        mButtonTrendsGallery.setOnClickListener(this);
        mButtonTrendsSpace.setOnClickListener(this);
        mButtonTrendsBattery.setOnClickListener(this);
        return view;
    }


    /*设置点击事件，点击不同的按钮进入不同的Activity
     * mButtonTrendsBattery进入TrendsBatteryActivity;
     * mButtonTrendsSpace进入TrendsSpaceActivity
     * mButtonTrendsGallery进入TrendsGalleryActivity
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (ActivityCompat.checkSelfPermission(App.getInstance(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(App.getInstance(), "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
        switch (v.getId()) {
            case R.id.button_trends_space_id:
                intent.setClass(getActivity().getApplicationContext(), TrendsSpaceActivity.class);
                startActivity(intent);
                break;
            case R.id.button_trends_gallery_id:
                intent.setClass(getActivity().getApplicationContext(),
                        TrendsGalleryActivity.class);
                startActivity(intent);
                break;
            case R.id.button_trends_battery_id:
                intent.setClass(getActivity().getApplicationContext(),
                        TrendsBatteryInfoActivity.class);
                Bundle bundle;
                bundle = getBatteryInfoBundle();
                sendNotification();
                intent.setClass(getActivity().getApplicationContext(),
                        TrendsBatteryInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //发送通知，将剩余电量及状态显示在通知栏上。
    private void sendNotification() {
        String id = "ch_1";
        String des = "ch_1";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationManager notificationManager = (NotificationManager) getActivity().
                getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(id, des, importance);
        notificationManager.createNotificationChannel(notificationChannel);

        mBatteryInfo = new double[5];
        mBatteryInfo = getBatteryInfo(getContext());

        //isCharge表示电池的具体状态，{充电中CHARGING，未充电DISCHARGING，已满FULL，未知UNKNOWN}
        //依据状态的数值来给isCharge
        String isCharge;
        switch ((int) mBatteryInfo[2]) {
            case 2:
                isCharge = "CHARGING";
                break;
            case 3:
                isCharge = "DISCHARGING";
                break;
            case 5:
                isCharge = "Full";
                break;
            default:
                isCharge = "UNKNOWN";
                break;
        }

        Intent intent = new Intent(getContext(), getActivity().getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(getContext(), id)
                .setSmallIcon(R.drawable.ic_qq_icon)
                .setContentText(isCharge + ". Remaining battery is " + (int) mBatteryInfo[1] + "%")
                .setContentTitle("Battery info")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(100, notification);
    }

    /*方法：获取电池信息
     *double数组（长为5）存储电池信息，依次为
     *电池容量，剩余电量百分比，电池状态{2，充电中；3，未充电}，瞬时电流正值放电，出厂时电量（mAh）
     */
    public double[] getBatteryInfo(Context context) {

        //通过BatteryManager类获取电池容量，剩余电量百分比，电池状态，瞬时电流正值放电
        final double[] batteryInfo = new double[6];
        BatteryManager batteryManager =
                (BatteryManager) getActivity().getSystemService(BATTERY_SERVICE);
        batteryInfo[0] = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        batteryInfo[1] = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        batteryInfo[2] = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
        batteryInfo[3] = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        //通过反射获取出厂时电量（mAh）
        Object mPowerProfile;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);
            batteryInfo[4] = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryInfo;
    }

    //将电池信息数据（double数组）填入到Bundle中去，再通过intent传递给BatteryActivity
    public Bundle getBatteryInfoBundle() {
        Bundle BatteryInfoBundle = new Bundle();
        double[] batteryInfo = getBatteryInfo(getActivity().getApplicationContext());
        BatteryInfoBundle.putString("CHARGE_COUNTER", String.valueOf(batteryInfo[0]));
        BatteryInfoBundle.putString("PROPERTY_CAPACITY", String.valueOf(batteryInfo[1]));
        BatteryInfoBundle.putString("PROPERTY_STATUS", String.valueOf(batteryInfo[2]));
        BatteryInfoBundle.putString("BATTERY_PROPERTY_CURRENT_NOW", String.valueOf(batteryInfo[3]));
        BatteryInfoBundle.putString("BATTERY", String.valueOf(batteryInfo[4]));
        return BatteryInfoBundle;
    }
}
