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
import com.ramialastora.ramialastora.classes.responses.scorers.ScorersData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener7;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ramialastora.ramialastora.RamiFragments.ScorersFragment.PLAYER_ID;

public class MyScorersPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<ScorersData> scorersList;
    private ArrayList<ScorersData> scorersListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener7 listener1;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public MyScorersPaginationAdapter(Context context) {
        this.context = context;
        scorersList = new ArrayList<>();
    }

    public ArrayList<ScorersData> getScorersList() {
        return scorersList;
    }

    public void setScorersList(ArrayList<ScorersData> scorersList) {
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
        View v1 = inflater.inflate(R.layout.player_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ScorersData scorers = scorersList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                holder1.sort.setText(position+1 + "");
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
                }else{
                    holder1.redCard.setVisibility(View.INVISIBLE);
                    holder1.yellowCard.setVisibility(View.INVISIBLE);
                }

                //TODO ////////////// Selected on team in same page ///////////

                if (position %2 == 0){
                    holder1.clScorer.setBackgroundResource(R.color.text_gray2);
                }else {
                    holder1.clScorer.setBackgroundResource(R.color.white);
                }

                if (PLAYER_ID != 0){
                    if (PLAYER_ID == scorers.getPlayerId()){
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

    public void setList(ArrayList<ScorersData> list) {
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

    public void add(ScorersData r) {
        scorersList.add(r);
        notifyItemInserted(scorersList.size() - 1);
    }

    public void addAll(ArrayList<ScorersData> scorersList) {
        for (ScorersData result : scorersList) {
            add(result);
        }
    }

    public void remove(ScorersData r) {
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
        add(new ScorersData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = scorersList.size() - 1;
        ScorersData result = getItem(position);

        if (result != null) {
            scorersList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ScorersData getItem(int position) {
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

        TextView sort, playerName, playerCenter, goals, truePelanties, falsePelanties;
        ImageView playerImage, yellowCard, redCard;
        ConstraintLayout clScorer;

        public Holder(View itemView) {
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

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<ScorersData> orders) {
        scorersList = new ArrayList<>();
        scorersList.addAll(orders);
        notifyDataSetChanged();
    }


}