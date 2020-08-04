package com.ramyhd.ramyalastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.Notification;

import java.util.List;

public class MyNotificationsAdapter extends RecyclerView.Adapter<MyNotificationsAdapter.ViewHolder> {
    List<Notification> notificationList;
    Context context;

    public MyNotificationsAdapter(List<Notification> providerList, Context context) {
        this.notificationList = providerList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item_style, viewGroup, false);
        ViewHolder holderR = new ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyNotificationsAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.title.setText(notification.title);
        holder.time.setText(notification.time);
        if (notification.type == 1) {
            holder.type.setImageResource(R.drawable.ic_player_notification);
        }else{
            holder.type.setImageResource(R.drawable.ic_goal_notification);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        ImageView type;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notificationtitle);
            time = itemView.findViewById(R.id.notificationtime);
            type = itemView.findViewById(R.id.notificationtype);

        }
    }
}