package com.example.xue.myqq.adapter;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xue.myqq.R;

import java.util.List;

public class TrendsSpaceAdapter extends RecyclerView.Adapter<TrendsSpaceAdapter.ViewHold> {

    private List<String> mSata;//传入RecyclerView的列表

    public TrendsSpaceAdapter(List<String> Sata) {
        this.mSata = Sata;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recyclerview_trends_spaces, viewGroup, false);
        ViewHold viewHold = new ViewHold(view);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold viewHolder, int i) {
        Log.d("Trends :", "space " + i + mSata.get(i));
        viewHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(mSata.get(i)));
    }

    @Override
    public int getItemCount() {
        return mSata.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageview_trends_space);
        }
    }
}
