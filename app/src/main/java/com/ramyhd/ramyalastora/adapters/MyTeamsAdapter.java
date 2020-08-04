package com.ramyhd.ramyalastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.Teams;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTeamsAdapter extends RecyclerView.Adapter<MyTeamsAdapter.ViewHolder> {
    List<Teams> teamsList;
    Context context;
    private OnItemClickListener listener1;

    public MyTeamsAdapter(List<Teams> providerList, Context context) {
        this.teamsList = providerList;
        this.context = context;
    }

    @Override
    public MyTeamsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teams_item_style, viewGroup, false);
        MyTeamsAdapter.ViewHolder holderR = new MyTeamsAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyTeamsAdapter.ViewHolder holder, int position) {
        final Teams teams = teamsList.get(position);

        Picasso.get().load(teams.image).into(holder.team);

        /*final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(teams);
            }
        };

        holder.clteam.setOnClickListener(listener);*/
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView team;
        ConstraintLayout clteam;

        public ViewHolder(View itemView) {
            super(itemView);

            team = itemView.findViewById(R.id.teamimage);
            clteam = itemView.findViewById(R.id.clteam);

        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener1 = listener;
    }
}