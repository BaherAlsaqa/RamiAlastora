package com.ramyhd.ramialastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.classes.responses.participating_teams.ParticipatingTeamsData;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.listeners.OnItemClickListener3;
import com.ramyhd.ramialastora.retrofit.APIClient;
import com.ramyhd.ramialastora.retrofit.APIInterface;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ramyhd.ramialastora.RamiFragments.SortParticipatingTeamsFragment.TEAM_ID1;
import static com.ramyhd.ramialastora.RamiFragments.SortParticipatingTeamsFragment.TEAM_ID2;

public class MyParticipatingTeamsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<ParticipatingTeamsData> scorersList;
    private ArrayList<ParticipatingTeamsData> scorersListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener3 listener1;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyParticipatingTeamsPaginationAdapter(Context context) {
        this.context = context;
        scorersList = new ArrayList<>();
    }

    public ArrayList<ParticipatingTeamsData> getScorersList() {
        return scorersList;
    }

    public void setScorersList(ArrayList<ParticipatingTeamsData> scorersList) {
        this.scorersList = scorersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.team_sort_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ParticipatingTeamsData pTeamsData = scorersList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                if (pTeamsData.getRanking() != null) {
                    holder1.teamssort.setText(pTeamsData.getRanking() + "");
                }else{
                    holder1.teamssort.setText("0");
                }
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

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return scorersList == null ? 0 : scorersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == scorersList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<ParticipatingTeamsData> list) {
        if ((list.size() == 0)) {
//            scorersItemsFragment.loadNext();
            this.scorersListFilter = list;

        } else {
            this.scorersListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(ParticipatingTeamsData r) {
        scorersList.add(r);
        notifyItemInserted(scorersList.size() - 1);
    }

    public void addAll(ArrayList<ParticipatingTeamsData> scorersList) {
        for (ParticipatingTeamsData result : scorersList) {
            add(result);
        }
    }

    public void remove(ParticipatingTeamsData r) {
        int position = scorersList.indexOf(r);
        if (position > -1) {
            scorersList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ParticipatingTeamsData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = scorersList.size() - 1;
        ParticipatingTeamsData result = getItem(position);

        if (result != null) {
            scorersList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ParticipatingTeamsData getItem(int position) {
        return scorersList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ImageView teamsSortImage;
        TextView teamssort, teameName, played, won, drew, loser, scored, ownNet, rest, point;
        ConstraintLayout constraintLayout;

        public Holder(View itemView) {
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

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<ParticipatingTeamsData> orders) {
        scorersList = new ArrayList<>();
        scorersList.addAll(orders);
        notifyDataSetChanged();
    }


}