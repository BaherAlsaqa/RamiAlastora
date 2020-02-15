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
import com.ramyhd.ramialastora.classes.responses.team_players.TeamPlayersData;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.listeners.OnItemClickListener9;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyTeamPlayersAdapter extends RecyclerView.Adapter<MyTeamPlayersAdapter.ViewHolder> {
    List<TeamPlayersData> scorersList;
    Context context;
    private OnItemClickListener9 listener1;

    public MyTeamPlayersAdapter(List<TeamPlayersData> providerList, Context context) {
        this.scorersList = providerList;
        this.context = context;
    }

    @Override
    public MyTeamPlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item_style, viewGroup, false);
        MyTeamPlayersAdapter.ViewHolder holderR = new MyTeamPlayersAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyTeamPlayersAdapter.ViewHolder holder, int position) {
        final TeamPlayersData teamPlayersData = scorersList.get(position);

        holder.yellowCard.setVisibility(View.INVISIBLE);
        holder.redCard.setVisibility(View.INVISIBLE);

        holder.sort.setText(position+1+"");
        Picasso.get().load(Constants.imageBaseURL+teamPlayersData.getImage())
                .placeholder(R.drawable.players_holder)
                .resize(200, 200)
                .into(holder.playerImage);

        if (teamPlayersData.getName().length() > 14)
            holder.playerName.setText(teamPlayersData.getName().substring(0, 14));
        else holder.playerName.setText(teamPlayersData.getName());

        holder.playerCenter.setText(teamPlayersData.getPosition().getName()+"");
        holder.goals.setText(teamPlayersData.getTotalGoal()+"");
        holder.truePelanties.setText(teamPlayersData.getCountInPenaltys()+"");
        holder.falsePelanties.setText(teamPlayersData.getCountPenaltys()+"");

        if (teamPlayersData.getCards().size() > 0) {
            if (teamPlayersData.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))) {
                holder.yellowCard.setVisibility(View.VISIBLE);
            } else if (teamPlayersData.getCards().get(0).equalsIgnoreCase(context.getString(R.string.red))) {
                holder.redCard.setVisibility(View.VISIBLE);
            } else if (teamPlayersData.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))
                    & teamPlayersData.getCards().get(1).equalsIgnoreCase(context.getString(R.string.red))) {
                holder.redCard.setVisibility(View.VISIBLE);
                holder.yellowCard.setVisibility(View.VISIBLE);
            }
        }else{
            holder.yellowCard.setVisibility(View.INVISIBLE);
            holder.redCard.setVisibility(View.INVISIBLE);
        }

        if (position %2 == 0){
            holder.clScorer.setBackgroundResource(R.color.text_gray2);
        }else {
            holder.clScorer.setBackgroundResource(R.color.white);
        }


        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(teamPlayersData);
            }
        };

        holder.clScorer.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return scorersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sort, playerName, playerCenter, goals, truePelanties, falsePelanties;
        ImageView playerImage, yellowCard, redCard;
        ConstraintLayout clScorer;

        public ViewHolder(View itemView) {
            super(itemView);

            sort = itemView.findViewById(R.id.sort);
            playerName = itemView.findViewById(R.id.playername);
            playerCenter = itemView.findViewById(R.id.playercenter);
            goals = itemView.findViewById(R.id.playergoals);
            truePelanties = itemView.findViewById(R.id.truepenalties);
            falsePelanties = itemView.findViewById(R.id.falsepenalties);
            playerImage = itemView.findViewById(R.id.playerimage);
            yellowCard = itemView.findViewById(R.id.yellowcard);
            redCard = itemView.findViewById(R.id.redcard);
            clScorer = itemView.findViewById(R.id.cl_scorer);

        }
    }

    public void setOnClickListener(OnItemClickListener9 listener) {
        this.listener1 = listener;
    }

}