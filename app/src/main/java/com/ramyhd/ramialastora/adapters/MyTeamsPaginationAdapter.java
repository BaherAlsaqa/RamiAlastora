package com.ramyhd.ramialastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.classes.responses.teams.TeamsData;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.listeners.OnItemCheckedListener1;
import com.ramyhd.ramialastora.listeners.OnItemClickListener;
import com.ramyhd.ramialastora.retrofit.APIClient;
import com.ramyhd.ramialastora.retrofit.APIInterface;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyTeamsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<TeamsData> teamsList;
    private ArrayList<TeamsData> teamsListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener listener1;
    private OnItemCheckedListener1 listener2;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyTeamsPaginationAdapter(Context context) {
        this.context = context;
        teamsList = new ArrayList<>();
    }

    public ArrayList<TeamsData> getteamsList() {
        return teamsList;
    }

    public void setteamsList(ArrayList<TeamsData> teamsList) {
        this.teamsList = teamsList;
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
        View v1 = inflater.inflate(R.layout.teams_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final TeamsData teams = teamsList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                holder1.favorite.setVisibility(View.GONE);

                Picasso.get().load(Constants.imageBaseURL+teams.getLogo()).into(holder1.team);
                if (teams.getFavorite() == 1){
                    holder1.favorite.setVisibility(View.VISIBLE);
                }else if (teams.getFavorite() == 0){
                    holder1.favorite.setVisibility(View.GONE);
                }

                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(teams);
                    }
                };

                holder1.clteam.setOnClickListener(listener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return teamsList == null ? 0 : teamsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == teamsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<TeamsData> list) {
        if ((list.size() == 0)) {
//            teamsItemsFragment.loadNext();
            this.teamsListFilter = list;

        } else {
            this.teamsListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(TeamsData r) {
        teamsList.add(r);
        notifyItemInserted(teamsList.size() - 1);
    }

    public void addAll(ArrayList<TeamsData> teamsList) {
        for (TeamsData result : teamsList) {
            add(result);
        }
    }

    public void remove(TeamsData r) {
        int position = teamsList.indexOf(r);
        if (position > -1) {
            teamsList.remove(position);
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
        add(new TeamsData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = teamsList.size() - 1;
        TeamsData result = getItem(position);

        if (result != null) {
            teamsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public TeamsData getItem(int position) {
        return teamsList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ImageView team, favorite;
        ConstraintLayout clteam;

        public Holder(View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.teamimage);
            favorite = itemView.findViewById(R.id.favorite);
            clteam = itemView.findViewById(R.id.clteam);
        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener1 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<TeamsData> orders) {
        teamsList = new ArrayList<>();
        teamsList.addAll(orders);
        notifyDataSetChanged();
    }


}