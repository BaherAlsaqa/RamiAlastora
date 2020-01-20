package com.ramialastora.ramialastora.adapters;

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

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.leagues.LeagueData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener4;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyLeaguesPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<LeagueData> LeagueList;
    private ArrayList<LeagueData> LeagueListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener4 listener1;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyLeaguesPaginationAdapter(Context context) {
        this.context = context;
        LeagueList = new ArrayList<>();
    }

    public ArrayList<LeagueData> getLeagueList() {
        return LeagueList;
    }

    public void setLeagueList(ArrayList<LeagueData> LeagueList) {
        this.LeagueList = LeagueList;
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
        View v1 = inflater.inflate(R.layout.league_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final LeagueData league = LeagueList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                Picasso.get().load(Constants.imageBaseURL + league.getImage()).into(holder1.leagueImage);
                holder1.leagueName.setText(league.getTitle());
                holder1.sort.setText(league.getLeaguesActive().get(0).getSorting());

                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(league);
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
        return LeagueList == null ? 0 : LeagueList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == LeagueList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<LeagueData> list) {
        if ((list.size() == 0)) {
//            LeagueItemsFragment.loadNext();
            this.LeagueListFilter = list;

        } else {
            this.LeagueListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(LeagueData r) {
        LeagueList.add(r);
        notifyItemInserted(LeagueList.size() - 1);
    }

    public void addAll(ArrayList<LeagueData> LeagueList) {
        for (LeagueData result : LeagueList) {
            add(result);
        }
    }

    public void remove(LeagueData r) {
        int position = LeagueList.indexOf(r);
        if (position > -1) {
            LeagueList.remove(position);
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
        add(new LeagueData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = LeagueList.size() - 1;
        LeagueData result = getItem(position);

        if (result != null) {
            LeagueList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public LeagueData getItem(int position) {
        return LeagueList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ImageView leagueImage;
        TextView leagueName, sort;
        ConstraintLayout constraintLayout;

        public Holder(View itemView) {
            super(itemView);
            leagueImage = itemView.findViewById(R.id.leagueimage);
            leagueName = itemView.findViewById(R.id.leaguename);
            sort = itemView.findViewById(R.id.sort);
            constraintLayout = itemView.findViewById(R.id.clleague);
        }
    }

    public void setOnClickListener(OnItemClickListener4 listener) {
        this.listener1 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<LeagueData> orders) {
        LeagueList = new ArrayList<>();
        LeagueList.addAll(orders);
        notifyDataSetChanged();
    }


}