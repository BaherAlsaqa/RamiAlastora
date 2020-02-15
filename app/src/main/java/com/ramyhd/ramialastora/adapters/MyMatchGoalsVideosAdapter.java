package com.ramyhd.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.classes.responses.match_goals_video.MatchGoalsVideoData;
import com.ramyhd.ramialastora.listeners.OnItemClickListener10;

import java.util.List;

public class MyMatchGoalsVideosAdapter extends RecyclerView.Adapter<MyMatchGoalsVideosAdapter.ViewHolder> {
    List<MatchGoalsVideoData> matchGoalsVideoDataList;
    Context context;
    private OnItemClickListener10 listener1;

    public MyMatchGoalsVideosAdapter(List<MatchGoalsVideoData> matchGoalsVideoDataList, Context context) {
        this.matchGoalsVideoDataList = matchGoalsVideoDataList;
        this.context = context;
    }

    @Override
    public MyMatchGoalsVideosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_goals_videos_item_style, viewGroup, false);
        MyMatchGoalsVideosAdapter.ViewHolder holderR = new MyMatchGoalsVideosAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyMatchGoalsVideosAdapter.ViewHolder holder, int position) {
        final MatchGoalsVideoData matchGoalsVideoData = matchGoalsVideoDataList.get(position);

        holder.videoTitle.setText(matchGoalsVideoData.getTitle());

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(matchGoalsVideoData);
            }
        };

        holder.clVideo.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return matchGoalsVideoDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        ConstraintLayout clVideo;

        public ViewHolder(View itemView) {
            super(itemView);

            videoTitle = itemView.findViewById(R.id.videotitle);
            clVideo = itemView.findViewById(R.id.clurlvideo);

        }
    }

    public void setOnClickListener(OnItemClickListener10 listener) {
        this.listener1 = listener;
    }

}