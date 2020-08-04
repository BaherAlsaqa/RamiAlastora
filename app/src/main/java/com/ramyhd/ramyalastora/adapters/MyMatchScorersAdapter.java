package com.ramyhd.ramyalastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.responses.scorers.MatchScorersData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener11;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMatchScorersAdapter extends RecyclerView.Adapter<MyMatchScorersAdapter.ViewHolder> {
    List<MatchScorersData> scorersDataList;
    Context context;
    private OnItemClickListener11 listener1;

    public MyMatchScorersAdapter(List<MatchScorersData> scorers, Context context) {
        this.scorersDataList = scorers;
        this.context = context;
    }

    @Override
    public MyMatchScorersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item_style, viewGroup, false);
        MyMatchScorersAdapter.ViewHolder holderR = new MyMatchScorersAdapter.ViewHolder(view);

        return holderR;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyMatchScorersAdapter.ViewHolder holder, int position) {
        final MatchScorersData scorers = scorersDataList.get(position);

        holder.sort.setText(position+1 + "");
        Picasso.get().load(Constants.imageBaseURL + scorers.getImage())
                .placeholder(R.drawable.players_holder)
                .resize(200, 200)
                .into(holder.playerImage);

        if (scorers.getName().length() > 14)
            holder.playerName.setText(scorers.getName().substring(0, 14));
        else holder.playerName.setText(scorers.getName());

        holder.playerCenter.setText(scorers.getTeamName() + "");
        holder.goals.setText(scorers.getTotalGoal() + "");
        holder.truePelanties.setText(scorers.getCountPenaltys() + "");
        holder.falsePelanties.setText(scorers.getCountInPenaltys() + "");

        if (scorers.getCards().size() > 0) {
            if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))) {
                holder.yellowCard.setVisibility(View.VISIBLE);
            } else if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.red))) {
                holder.redCard.setVisibility(View.VISIBLE);
            } else if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))
                    & scorers.getCards().get(1).equalsIgnoreCase(context.getString(R.string.red))) {
                holder.redCard.setVisibility(View.VISIBLE);
                holder.yellowCard.setVisibility(View.VISIBLE);
            }
                    /*for (int x=0; x>scorers.getCards().size(); x++){
                        if ()
                    }*/
        }else{
            holder.redCard.setVisibility(View.INVISIBLE);
            holder.yellowCard.setVisibility(View.INVISIBLE);
        }

        if (position %2 == 0){
            holder.clScorer.setBackgroundResource(R.color.text_gray2);
        }else {
            holder.clScorer.setBackgroundResource(R.color.white);
        }

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(scorers);
            }
        };

        holder.clScorer.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return scorersDataList.size();
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

    public void setOnClickListener(OnItemClickListener11 listener) {
        this.listener1 = listener;
    }

}