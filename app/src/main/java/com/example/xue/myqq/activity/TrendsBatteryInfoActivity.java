package com.example.xue.myqq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.xue.myqq.R;

import java.text.DecimalFormat;

public class TrendsBatteryInfoActivity extends AppCompatActivity {

    private TextView mTextView_battery_capacity;
    private TextView mTextView_battery_status;
    private TextView mTextView_remaining_battery;
    private TextView mTextView_current_now;
    private TextView mTextView_battery_temperature;
    private TextView mTextView_battery_voltage;
    private TextView mTextView_battery_total_erengy;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends_batteryinfo);

        //获取用于展示电池信息的组件
        mTextView_battery_capacity = findViewById(R.id.battery_capacity_value);
        mTextView_battery_status = findViewById(R.id.battery_status_value);
        mTextView_remaining_battery = findViewById(R.id.remaining_battery_value);
        mTextView_current_now = findViewById(R.id.current_now_value);
        mTextView_battery_voltage = findViewById(R.id.battery_voltage_value);
        mTextView_battery_temperature = findViewById(R.id.battery_temperature_value);
        mTextView_battery_total_erengy = findViewById(R.id.battery_total_energy_value);

        //bundle中获取数据，从MainActivity(TrendsFragment)中传来
        Intent intent = getIntent();
        bundle = intent.getExtras();
        String temp = bundle.getString("PROPERTY_STATUS");
        double status_battery_number = Double.parseDouble(temp);
        String status_battery;
        switch ((int) status_battery_number) {
            case 2:
                status_battery = "CHARGING";
                break;
            case 3:
                status_battery = "DISCHARGING";
                break;
            case 6:
                status_battery = "FULL";
                break;
            default:
                status_battery = "UNKNOWN";
                break;
        }

        getInfo();

        //将电池信息数据格式化，并展示
        int battery_capacity = (int) (Double.parseDouble
                (bundle.getString("CHARGE_COUNTER")) / 1000);
        int current_now = (int) (Double.parseDouble
                (bundle.getString("BATTERY_PROPERTY_CURRENT_NOW")) / 1000);
        int remaining_battery = (int) (Double.parseDouble
                (bundle.getString("PROPERTY_CAPACITY")) / 1);
        int battery_total_erengy = (int) (Double.parseDouble(bundle.getString("BATTERY")) / 1);
        mTextView_battery_capacity.setText(battery_capacity + "mhA");
        mTextView_remaining_battery.setText(String.valueOf(remaining_battery) + "%");
        mTextView_current_now.setText(current_now + "mA");
        mTextView_battery_status.setText(status_battery);
        mTextView_battery_total_erengy.setText(battery_total_erengy + "mAh");
    }


    //通过BroadcastReceiver来获取电池的温度，电压信息，并填入相应textview中
    private void getInfo() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                DecimalFormat df = new DecimalFormat("0.0");
                double voltage = intent.getIntExtra("voltage", 0) / 1000.0;
                int temperature = intent.getIntExtra("temperature", 0) / 10;
                mTextView_battery_temperature.setText(String.valueOf(temperature) + "\u2103");
                mTextView_battery_voltage.setText(String.valueOf(df.format(voltage)) + "V");
            }

        };
        registerReceiver(receiver, filter);
    }
}
