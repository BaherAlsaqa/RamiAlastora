package com.ramyhd.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.classes.TeamsSort;
import com.ramyhd.ramialastora.listeners.OnItemClickListener3;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTeamsSortAdapter extends RecyclerView.Adapter<MyTeamsSortAdapter.ViewHolder> {

    List<TeamsSort> teamsSortList;
    Context context;
    private OnItemClickListener3 listener1;

    public MyTeamsSortAdapter(List<TeamsSort> providerList, Context context) {
        this.teamsSortList = providerList;
        this.context = context;
    }

    @Override
    public MyTeamsSortAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_sort_item_style, viewGroup, false);
        MyTeamsSortAdapter.ViewHolder holderR = new MyTeamsSortAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyTeamsSortAdapter.ViewHolder holder, int position) {
        final TeamsSort teamsSort = teamsSortList.get(position);

        holder.sort.setText(teamsSort.sort+"");
        Picasso.get().load(teamsSort.image).into(holder.teamsSortImage);
        holder.teameName.setText(teamsSort.teamName);
        holder.played.setText(teamsSort.played+"");
        holder.won.setText(teamsSort.won+"");
        holder.drew.setText(teamsSort.drew+"");
        holder.loser.setText(teamsSort.loser+"");
        holder.scored.setText(teamsSort.scored+"");
        holder.ownNet.setText(teamsSort.ownNet+"");
        holder.rest.setText(teamsSort.rest+"");
        holder.point.setText(teamsSort.points+"");

        if (position %2 == 0){
            holder.constraintLayout.setBackgroundResource(R.color.text_gray2);
        }else {
            holder.constraintLayout.setBackgroundResource(R.color.white);
        }

        /*final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(teamsSort);
            }
        };

        holder.constraintLayout.setOnClickListener(listener);*/

    }

    @Override
    public int getItemCount() {
        return teamsSortList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView teamsSortImage;
        TextView sort, teameName, played, won, drew, loser, scored, ownNet, rest, point;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            sort = itemView.findViewById(R.id.teamsort);
            teamsSortImage = itemView.findViewById(R.id.teamsortimage);
            teameName = itemView.findViewById(R.id.teamname);
            played = itemView.findViewById(R.id.played);
            won = itemView.findViewById(R.id.won);
            drew = itemView.findViewById(R.id.drew);
            loser = itemView.findViewById(R.id.loser);
            scored = itemView.findViewById(R.id.scored);
            ownNet = itemView.findViewById(R.id.ownnet);
            rest = itemView.findViewById(R.id.rest);
            point = itemView.findViewById(R.id.points);
            constraintLayout = itemView.findViewById(R.id.clteamsort);


        }
    }
    public void setOnClickListener(OnItemClickListener3 listener) {
        this.listener1 = listener;
    }
}