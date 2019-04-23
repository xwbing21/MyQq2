package com.example.xue.myqq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/22 11:05
 **/
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>
        implements View.OnClickListener {

    private static final String TAG = "PersonAdapter";

    private List<Contact> mContactList;
    private LayoutInflater mInflater;

    private MyItemClickListener listener;

    public ContactAdapter(Context context) {
        this(context, new ArrayList<Contact>());
    }

    public ContactAdapter(Context context, List<Contact> personList) {
        mInflater = LayoutInflater.from(context);
        this.mContactList = personList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.rv_item_contact, viewGroup, false);
        return new ContactHolder(view, listener);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {
        Contact contact = mContactList.get(i);
        if (contact.getPortrait() != null) {
            contactHolder.portrait.setImageBitmap(contact.getPortrait());
        }
        contactHolder.name.setText(contact.getName());
        contactHolder.phone.setText(contact.getPhoneNum());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public void setData(List<Contact> contactList) {
        this.mContactList.clear();
        this.mContactList = contactList;
        notifyDataSetChanged();
    }

    /**
     * 根据首字符 确定位置
     *
     * @param sign String
     * @return 位置
     */
    public int getFirstPositionByChar(String sign) {
        int size = mContactList.size();
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "getFirstPositionByChar: " + mContactList.get(i).getSortKey());
            if (mContactList.get(i).getSortKey().equals(sign)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {

    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView phone;
        ImageView portrait;

        MyItemClickListener listener;

        ContactHolder(@NonNull View itemView, MyItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_rv_item_contact);
            phone = itemView.findViewById(R.id.tv_phone_rv_item_contact);
            portrait = itemView.findViewById(R.id.iv_portrait_rv_item_contact);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
