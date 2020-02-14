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
import com.ramialastora.ramialastora.classes.responses.scorers.ScorersData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener7;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ramialastora.ramialastora.RamiFragments.ScorersFragment.PLAYER_ID;

public class MyScorersAdapter extends RecyclerView.Adapter<MyScorersAdapter.ViewHolder> {
    List<ScorersData> scorersData;
    Context context;
    private OnItemClickListener7 listener1;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyScorersAdapter(List<ScorersData> scorersData, Context context) {
        this.scorersData = scorersData;
        this.context = context;
    }

    @Override
    public MyScorersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item_style, viewGroup, false);
        MyScorersAdapter.ViewHolder holderR = new MyScorersAdapter.ViewHolder(view);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyScorersAdapter.ViewHolder holder1, int position) {
        final ScorersData scorers = scorersData.get(position);

        holder1.sort.setText(position + 1 + "");
        Picasso.get().load(Constants.imageBaseURL + scorers.getImage())
                .placeholder(R.drawable.players_holder)
                .resize(200, 200)
                .into(holder1.playerImage);

        if (scorers.getName().length() > 12)
            holder1.playerName.setText(scorers.getName().substring(0, 12));
        else holder1.playerName.setText(scorers.getName());

        holder1.playerCenter.setText(scorers.getTeamName() + "");
        holder1.goals.setText(scorers.getTotalGoal() + "");
        holder1.truePelanties.setText(scorers.getPenaltiesTrue() + "");
        holder1.falsePelanties.setText(scorers.getPenaltiesFalse() + "");

        if (scorers.getCards().size() > 0) {
            if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))) {
                holder1.yellowCard.setVisibility(View.VISIBLE);
            } else if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.red))) {
                holder1.redCard.setVisibility(View.VISIBLE);
            } else if (scorers.getCards().get(0).equalsIgnoreCase(context.getString(R.string.yellow))
                    & scorers.getCards().get(1).equalsIgnoreCase(context.getString(R.string.red))) {
                holder1.redCard.setVisibility(View.VISIBLE);
                holder1.yellowCard.setVisibility(View.VISIBLE);
            }
                    /*for (int x=0; x>scorers.getCards().size(); x++){
                        if ()
                    }*/
        } else {
            holder1.redCard.setVisibility(View.INVISIBLE);
            holder1.yellowCard.setVisibility(View.INVISIBLE);
        }

        //TODO ////////////// Selected on team in same page ///////////

        if (position % 2 == 0) {
            holder1.clScorer.setBackgroundResource(R.color.text_gray2);
        } else {
            holder1.clScorer.setBackgroundResource(R.color.white);
        }

        if (PLAYER_ID != 0) {
            if (PLAYER_ID == scorers.getPlayerId()) {
                holder1.clScorer.setBackgroundResource(R.color.orange_lite);
            }
        }

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(scorers);
            }
        };

        holder1.clScorer.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return scorersData.size();
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

    public void setOnClickListener(OnItemClickListener7 listener) {
        this.listener1 = listener;
    }

}