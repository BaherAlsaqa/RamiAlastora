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
import com.ramyhd.ramyalastora.classes.responses.participating_teams.ParticipatingTeamsData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener3;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ramyhd.ramyalastora.RamiFragments.SortParticipatingTeamsFragment.TEAM_ID1;
import static com.ramyhd.ramyalastora.RamiFragments.SortParticipatingTeamsFragment.TEAM_ID2;

public class MyParticipatingTeamsAdapter extends RecyclerView.Adapter<MyParticipatingTeamsAdapter.ViewHolder> {
    List<ParticipatingTeamsData> participatingTeamsData;
    Context context;
    private OnItemClickListener3 listener1;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyParticipatingTeamsAdapter(List<ParticipatingTeamsData> participatingTeamsData, Context context) {
        this.participatingTeamsData = participatingTeamsData;
        this.context = context;
    }

    @Override
    public MyParticipatingTeamsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_sort_item_style, viewGroup, false);
        MyParticipatingTeamsAdapter.ViewHolder holderR = new MyParticipatingTeamsAdapter.ViewHolder(view);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);

        return holderR;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyParticipatingTeamsAdapter.ViewHolder holder1, int position) {
        final ParticipatingTeamsData pTeamsData = participatingTeamsData.get(position);

        holder1.teamssort.setText(position+1+"");
        Picasso.get().load(Constants.imageBaseURL+pTeamsData.getLogo()).into(holder1.teamsSortImage);
        if (pTeamsData.getName().length() > 14)
            holder1.teameName.setText(pTeamsData.getName().substring(0, 14));
        else holder1.teameName.setText(pTeamsData.getName());
        holder1.played.setText(pTeamsData.getCountMatchesPlayed()+"");
        holder1.won.setText(pTeamsData.getCountMatchesWon()+"");
        holder1.drew.setText(pTeamsData.getCountMatchesDraw()+"");
        holder1.loser.setText(pTeamsData.getCountMatchesLost()+"");
        holder1.scored.setText(pTeamsData.getCountGoals()+"");
        holder1.ownNet.setText(pTeamsData.getCountGoalsIn()+"");
        holder1.rest.setText(pTeamsData.getCountGoals()-pTeamsData.getCountGoalsIn()+"");
        holder1.point.setText(pTeamsData.getPoints()+"");

        if (position %2 == 0){
            holder1.constraintLayout.setBackgroundResource(R.color.text_gray2);
        }else {
            holder1.constraintLayout.setBackgroundResource(R.color.white);
        }

        //TODO ////////////// Selected on team in same page ///////////
        if (TEAM_ID1 != 0){
            if (TEAM_ID1 == pTeamsData.getTemaId()){
                holder1.constraintLayout.setBackgroundResource(R.color.orange_lite);
            }
        }
        if (TEAM_ID2 != 0){
            if (TEAM_ID2 == pTeamsData.getTemaId()){
                holder1.constraintLayout.setBackgroundResource(R.color.orange_lite);
            }
        }

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(pTeamsData);
            }
        };

        holder1.constraintLayout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return participatingTeamsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView teamsSortImage;
        TextView teamssort, teameName, played, won, drew, loser, scored, ownNet, rest, point;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            teamssort = itemView.findViewById(R.id.teamsort);
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