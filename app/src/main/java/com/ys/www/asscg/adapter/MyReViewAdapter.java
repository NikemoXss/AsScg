package com.ys.www.asscg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ys.www.asscg.R;
import com.ys.www.asscg.activity.BidItem;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/23.
 */

public class MyReViewAdapter extends RecyclerView.Adapter<MyReViewAdapter.ViewHolder> {

    List<Map<String, Object>> list;
    Context context;
    private LayoutInflater mInflater;

    public MyReViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_indexfragment, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        if (!"1".equals(list.get(position).get("repayment_type").toString())) {// 1表示天标
            // 其余数值表示月标
            holder.title_icon_r.setBackgroundResource(R.drawable.image_r2);
            holder.title_icon_l.setBackgroundResource(R.drawable.title_i2);
            holder.item_bg.setBackgroundResource(R.drawable.item_bg_2);
        } else {
            holder.title_icon_r.setBackgroundResource(R.drawable.image_r1);
            holder.title_icon_l.setBackgroundResource(R.drawable.title_i1);
            holder.item_bg.setBackgroundResource(R.drawable.item_bg_1);
        }
        holder.list_progress.setProgress((int) Double.parseDouble(list.get(position).get("progress_tz_sx").toString()));
        holder.list_borrow_name.setText(list.get(position).get("item_tzbt_sx").toString());
        holder.list_borrow_money.setText(list.get(position).get("item_jkje_sx").toString());
        holder.list_has_borrow.setText(list.get(position).get("has_borrow_sx").toString());
        holder.list_borrow_duration.setText(list.get(position).get("item_tzqx_sx").toString());
        holder.list_borrow_interest_rate.setText(list.get(position).get("item_nhl_sx").toString());
        holder.list_jindu.setText("进度:" + list.get(position).get("progress_tz_sx").toString() + "%");

        holder.item_bg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Long id = (Long) list.get(position).get("id");
                int type = (Integer) list.get(position).get("type");
                String string = (String) list.get(position).get("repayment_type");
//                Toast.makeText(context, "hahaha"+id, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, BidItem.class);
				intent.putExtra("id", id);
				intent.putExtra("type", type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView list_borrow_name, list_borrow_money, list_has_borrow, list_borrow_duration, list_borrow_interest_rate,
                list_jindu;
        ProgressBar list_progress;
        ImageView title_icon_r, title_icon_l;
        FrameLayout item_bg;

        public ViewHolder(View itemView) {
            super(itemView);
            list_borrow_name = (TextView) itemView.findViewById(R.id.list_borrow_name);
            list_borrow_money = (TextView) itemView.findViewById(R.id.list_borrow_money);
            list_has_borrow = (TextView) itemView.findViewById(R.id.list_has_borrow);
            list_borrow_duration = (TextView) itemView.findViewById(R.id.list_borrow_duration);
            list_borrow_interest_rate = (TextView) itemView.findViewById(R.id.list_borrow_interest_rate);
            list_jindu = (TextView) itemView.findViewById(R.id.list_jindu);
            list_progress = (ProgressBar) itemView.findViewById(R.id.list_progress);
            title_icon_r = (ImageView) itemView.findViewById(R.id.title_icon_r);
            title_icon_l = (ImageView) itemView.findViewById(R.id.title_icon_l);
            item_bg = (FrameLayout) itemView.findViewById(R.id.item_bg);
        }
    }
}
