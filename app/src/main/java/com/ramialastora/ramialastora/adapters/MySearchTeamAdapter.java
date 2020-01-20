package com.ramialastora.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.search.Team;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener12;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MySearchTeamAdapter extends RecyclerView.Adapter<MySearchTeamAdapter.ViewHolder> {
    ArrayList<Team> teamList;
    Context context;
    private OnItemClickListener12 listener12;

    public MySearchTeamAdapter(ArrayList<Team> teamList, Context context) {
        this.teamList = teamList;
        this.context = context;
    }

    @Override
    public MySearchTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_team_item_style, viewGroup, false);
        MySearchTeamAdapter.ViewHolder holderR = new MySearchTeamAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MySearchTeamAdapter.ViewHolder holder, int position) {
            Team team = teamList.get(position);
            Picasso.get().load(Constants.imageBaseURL + team.getLogo())
                    .resize(200, 200)
                    .placeholder(R.drawable.leagues_teams_holder)
                    .into(holder.image);

            final View.OnClickListener listener1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener12.onItemClick(team);
                }
            };

            holder.clitem.setOnClickListener(listener1);
    }

    @Override
    public int getItemCount() {
            return teamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ConstraintLayout clitem;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            clitem = itemView.findViewById(R.id.clitem);

        }
    }

    public void setOnClickListener(OnItemClickListener12 listener) {
        this.listener12 = listener;
    }
}