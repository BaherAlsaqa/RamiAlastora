package com.ramyhd.ramyalastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.News;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener1;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ViewHolder> {
    List<News> newsList;
    Context context;
    private OnItemClickListener1 listener1;

    public MyNewsAdapter(List<News> providerList, Context context) {
        this.newsList = providerList;
        this.context = context;
    }

    @Override
    public MyNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item_style, viewGroup, false);
        MyNewsAdapter.ViewHolder holderR = new MyNewsAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyNewsAdapter.ViewHolder holder, int position) {
        final News news = newsList.get(position);

        Picasso.get().load(news.image).into(holder.newsImage);
        holder.time.setText(news.time);
        holder.title.setText(news.title);

        /*final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(news);
            }
        };*/

//        holder.clnews.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView time, title;
        ConstraintLayout clnews;

        public ViewHolder(View itemView) {
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
}