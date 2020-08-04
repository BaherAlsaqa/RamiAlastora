package com.ramyhd.ramyalastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.responses.notifications.NotificationData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemCheckedListener1;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener8;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;

import java.util.ArrayList;

public class MyNotificationPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<NotificationData> notificationList;
    private ArrayList<NotificationData> notificationListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener8 listener1;
    private OnItemCheckedListener1 listener2;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyNotificationPaginationAdapter(Context context) {
        this.context = context;
        notificationList = new ArrayList<>();
    }

    public ArrayList<NotificationData> getnotificationList() {
        return notificationList;
    }

    public void setnotificationList(ArrayList<NotificationData> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.notification_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final NotificationData notification = notificationList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                holder1.title.setText(notification.getTitle());
                holder1.time.setText(notification.getDate());
                if (notification.getType() != null) {
                    if (notification.getType().equalsIgnoreCase(Constants.keyGoal_1) |
                            notification.getType().equalsIgnoreCase(Constants.keyPalantiy_1)) {
                        holder1.type.setImageResource(R.drawable.ic_goal_notification);
                    } else if (notification.getType().equalsIgnoreCase(Constants.keyCard_2) |
                            notification.getType().equalsIgnoreCase(Constants.keyMatch_2) |
                            notification.getType().equalsIgnoreCase(Constants.keyNews_2)) {
                        holder1.type.setImageResource(R.drawable.ic_player_notification);
                    }
                }

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(notification);
                    }
                };

                holder1.constraintLayout.setOnClickListener(listener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == notificationList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<NotificationData> list) {
        if ((list.size() == 0)) {
//            notificationItemsFragment.loadNext();
            this.notificationListFilter = list;

        } else {
            this.notificationListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(NotificationData r) {
        notificationList.add(r);
        notifyItemInserted(notificationList.size() - 1);
    }

    public void addAll(ArrayList<NotificationData> notificationList) {
        for (NotificationData result : notificationList) {
            add(result);
        }
    }

    public void remove(NotificationData r) {
        int position = notificationList.indexOf(r);
        if (position > -1) {
            notificationList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NotificationData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = notificationList.size() - 1;
        NotificationData result = getItem(position);

        if (result != null) {
            notificationList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NotificationData getItem(int position) {
        return notificationList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        TextView title, time;
        ImageView type;
        ConstraintLayout constraintLayout;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationtitle);
            time = itemView.findViewById(R.id.notificationtime);
            type = itemView.findViewById(R.id.notificationtype);
            constraintLayout = itemView.findViewById(R.id.clitem);
        }
    }

    public void setOnClickListener(OnItemClickListener8 listener) {
        this.listener1 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<NotificationData> orders) {
        notificationList = new ArrayList<>();
        notificationList.addAll(orders);
        notifyDataSetChanged();
    }


}