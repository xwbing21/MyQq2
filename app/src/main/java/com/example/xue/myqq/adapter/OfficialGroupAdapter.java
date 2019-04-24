package com.example.xue.myqq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.xue.myqq.R;

import java.util.List;

/**
 * TODO
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/24 12:35
 **/
public class OfficialGroupAdapter extends RecyclerView.Adapter<OfficialGroupAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private OnItemClickListener mOnItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_official_name);
        }
    }

    public OfficialGroupAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_official, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // TODO: 绑定组件的事件
        holder.textView.setText(list.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
