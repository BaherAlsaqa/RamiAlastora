package com.ramialastora.ramialastora.adapters;

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

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.news.NewsData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemCheckedListener1;
import com.ramialastora.ramialastora.listeners.OnItemClickListener1;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyNewsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<NewsData> FavoriteList;
    private ArrayList<NewsData> FavoriteListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener1 listener1;
    private OnItemCheckedListener1 listener2;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyNewsPaginationAdapter(Context context) {
        this.context = context;
        FavoriteList = new ArrayList<>();
    }

    public ArrayList<NewsData> getFavoriteList() {
        return FavoriteList;
    }

    public void setFavoriteList(ArrayList<NewsData> FavoriteList) {
        this.FavoriteList = FavoriteList;
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
        View v1 = inflater.inflate(R.layout.news_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final NewsData news = FavoriteList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                Picasso.get().load(Constants.imageBaseURL+news.getImage())
                        .placeholder(R.drawable.news_holder)
                        .resize(800, 400)
                        .into(holder1.newsImage);
                holder1.time.setText(news.getDate());
                holder1.title.setText(news.getTitle());

                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(news);
                    }
                };

                holder1.clnews.setOnClickListener(listener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return FavoriteList == null ? 0 : FavoriteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == FavoriteList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<NewsData> list) {
        if ((list.size() == 0)) {
//            FavoriteItemsFragment.loadNext();
            this.FavoriteListFilter = list;

        } else {
            this.FavoriteListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(NewsData r) {
        FavoriteList.add(r);
        notifyItemInserted(FavoriteList.size() - 1);
    }

    public void addAll(ArrayList<NewsData> FavoriteList) {
        for (NewsData result : FavoriteList) {
            add(result);
        }
    }

    public void remove(NewsData r) {
        int position = FavoriteList.indexOf(r);
        if (position > -1) {
            FavoriteList.remove(position);
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
        add(new NewsData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = FavoriteList.size() - 1;
        NewsData result = getItem(position);

        if (result != null) {
            FavoriteList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NewsData getItem(int position) {
        return FavoriteList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ImageView newsImage;
        TextView time, title;
        ConstraintLayout clnews;

        public Holder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsimage);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            clnews = itemView.findViewById(R.id.clnews);
        }
    }

    public void setOnClickListener(OnItemClickListener1 listener) {
        this.listener1 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<NewsData> orders) {
        FavoriteList = new ArrayList<>();
        FavoriteList.addAll(orders);
        notifyDataSetChanged();
    }


}