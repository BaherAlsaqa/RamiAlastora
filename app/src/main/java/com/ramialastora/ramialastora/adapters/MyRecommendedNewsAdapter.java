package com.ramialastora.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.news.RelatedNews;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener2;
import com.ramialastora.ramialastora.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecommendedNewsAdapter extends RecyclerView.Adapter<MyRecommendedNewsAdapter.ViewHolder> {
    ArrayList<RelatedNews> newsList;
    Context context;
    private OnItemClickListener2 listener1;

    public MyRecommendedNewsAdapter(ArrayList<RelatedNews> providerList, Context context) {
        this.newsList = providerList;
        this.context = context;
    }

    @Override
    public MyRecommendedNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_news_item_style, viewGroup, false);
        MyRecommendedNewsAdapter.ViewHolder holderR = new MyRecommendedNewsAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyRecommendedNewsAdapter.ViewHolder holder, int position) {
        final RelatedNews news = newsList.get(position);

        Picasso.get().load(Constants.imageBaseURL+news.getImage())
                .transform(new RoundedCornersTransform())
                .into(holder.newsImage);
        holder.newstitle.setText(news.getTitle());

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(news);
            }
        };

        holder.clRecommendedRecommendedNews.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView time, newstitle;
        ConstraintLayout clRecommendedRecommendedNews;

        public ViewHolder(View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.newsimage);
            newstitle = itemView.findViewById(R.id.newstitle);
            clRecommendedRecommendedNews = itemView.findViewById(R.id.clrecommendednews);

        }
    }

    public void setOnClickListener(OnItemClickListener2 listener) {
        this.listener1 = listener;
    }
}