package com.ramialastora.ramialastora.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.Match;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener5;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMatchesAdapter extends RecyclerView.Adapter<MyMatchesAdapter.ViewHolder> {
    List<Match> matchList;
    Context context;
    int statusKey1[];
    int statusKey2[];
    int statusKey3[];
    private OnItemClickListener5 listener1;

    public MyMatchesAdapter(List<Match> providerList, Context context) {
        this.matchList = providerList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_item_style, viewGroup, false);
        ViewHolder holderR = new ViewHolder(view);
        statusKey1 = new int[matchList.size()];
        statusKey2 = new int[matchList.size()];
        statusKey3 = new int[matchList.size()];
        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyMatchesAdapter.ViewHolder holder, int position) {
        final Match match = matchList.get(position);

        for (int x=0; x<matchList.size(); x++){
            if (match.matchStatus.equalsIgnoreCase(context.getString(R.string.underway))){
                statusKey1[x] = position;
            }else if (match.matchStatus.equalsIgnoreCase(context.getString(R.string.comming))){
                statusKey2[x] = position;
            }else if (match.matchStatus.equalsIgnoreCase(context.getString(R.string.ended))){
                statusKey3[x] = position;
            }
        }
        if (position == statusKey1[0]){
            holder.constraintStatus.setVisibility(View.VISIBLE);
            //Status Icon
            holder.commingEndedIcon.setVisibility(View.GONE);
            holder.underwayFramLayout.setVisibility(View.VISIBLE);

        }else if (position == statusKey2[0]){
            holder.constraintStatus.setVisibility(View.VISIBLE);
            //Status Icon
            holder.commingEndedIcon.setVisibility(View.VISIBLE);
            holder.underwayFramLayout.setVisibility(View.INVISIBLE);
            holder.commingEndedIcon.setImageResource(R.drawable.ic_update);

        }else if (position == statusKey3[0]){
            Log.d(Constants.Log+"st", "holder.constraintStatus.setVisibility(View.GONE);");
            holder.constraintStatus.setVisibility(View.VISIBLE);
            //Status Icon
            holder.commingEndedIcon.setVisibility(View.VISIBLE);
            holder.underwayFramLayout.setVisibility(View.INVISIBLE);
            holder.commingEndedIcon.setImageResource(R.drawable.ic_alarm_on);

        }else{
            Log.d(Constants.Log+"st", "holder.constraintStatus.setVisibility(View.GONE);");
            holder.constraintStatus.setVisibility(View.GONE);
        }
        //visibilities GONE
        holder.constraintUnderway.setVisibility(View.GONE);
        holder.constraintComming.setVisibility(View.GONE);
        holder.constraintEnded.setVisibility(View.GONE);
        Picasso.get().load(match.imageTeam1).into(holder.imageTeam1);
        Picasso.get().load(match.imageTeam2).into(holder.imageTeam2);
        holder.Team1name.setText(match.nameTeam1);
        holder.Team2name.setText(match.nameTeam2);
        holder.matchStatus.setText(match.matchStatus);
        if (match.matchStatus.equals(context.getString(R.string.underway))) {
            holder.underwayResultTeam1.setText(match.resultTeam1 + "");
            holder.underwayResultTeam2.setText(match.resultTeam2 + "");
            holder.timeUnderway.setText(match.time);
            //visibility
            holder.constraintUnderway.setVisibility(View.VISIBLE);
            holder.constraintComming.setVisibility(View.GONE);
            holder.constraintEnded.setVisibility(View.GONE);
        } else if (match.matchStatus.equals(context.getString(R.string.comming))) {
            holder.liveMatchStatus.setText(context.getString(R.string.live));
            holder.commingTime.setText(match.time);
            //visibility
            holder.constraintUnderway.setVisibility(View.GONE);
            holder.constraintComming.setVisibility(View.VISIBLE);
            holder.constraintEnded.setVisibility(View.GONE);
        } else if (match.matchStatus.equals(context.getString(R.string.ended))) {
            holder.finalResultTeam1.setText(match.resultTeam1 + "");
            holder.finalResultTeam2.setText(match.resultTeam2 + "");
            //visibility
            holder.constraintUnderway.setVisibility(View.GONE);
            holder.constraintComming.setVisibility(View.GONE);
            holder.constraintEnded.setVisibility(View.VISIBLE);
        }

        /*final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(match);
            }
        };*/

//        holder.constraintLayout2.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout2;
        ImageView imageTeam1, imageTeam2, commingEndedIcon;
        TextView Team1name, Team2name;
        //underway status and comming and ended
        TextView matchStatus;
        //underway
        TextView underwayResultTeam1, underwayResultTeam2, timeUnderway;
        //ended
        TextView finalResultTeam1, finalResultTeam2;
        //comming
        TextView liveMatchStatus, commingTime;
        ConstraintLayout constraintUnderway, constraintComming, constraintEnded, constraintStatus;
        FrameLayout underwayFramLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            constraintLayout2 = itemView.findViewById(R.id.constraintLayout2);
            imageTeam1 = itemView.findViewById(R.id.team1image);
            imageTeam2 = itemView.findViewById(R.id.team2image);
            Team1name = itemView.findViewById(R.id.team1name);
            Team2name = itemView.findViewById(R.id.team2name);
            matchStatus = itemView.findViewById(R.id.statusvalue);
            underwayResultTeam1 = itemView.findViewById(R.id.team1result);
            underwayResultTeam2 = itemView.findViewById(R.id.team2result);
            timeUnderway = itemView.findViewById(R.id.time_underway);
            constraintUnderway = itemView.findViewById(R.id.constraintunderway);
            constraintComming = itemView.findViewById(R.id.constraintcomming);
            liveMatchStatus = itemView.findViewById(R.id.livematchstatus);
            commingTime = itemView.findViewById(R.id.timecomming);
            constraintEnded = itemView.findViewById(R.id.constraintended);
            finalResultTeam1 = itemView.findViewById(R.id.resultfinalteam1);
            finalResultTeam2 = itemView.findViewById(R.id.resultfinalteam2);
            commingEndedIcon = itemView.findViewById(R.id.commingendedicon);
            underwayFramLayout = itemView.findViewById(R.id.underwayframlayout);
            constraintStatus = itemView.findViewById(R.id.constraintstatus);

        }
    }

    public void setOnClickListener(OnItemClickListener5 listener) {
        this.listener1 = listener;
    }

}