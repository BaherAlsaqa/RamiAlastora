package com.ramialastora.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.search.League;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener14;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MySearchLeagueAdapter extends RecyclerView.Adapter<MySearchLeagueAdapter.ViewHolder> {
    private ArrayList<League> leagueList;
    private Context context;
    private OnItemClickListener14 listener14;

    public MySearchLeagueAdapter(ArrayList<League> leagueList, Context context) {
        this.leagueList = leagueList;
        this.context = context;
    }

    @Override
    public MySearchLeagueAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_league_item_style, viewGroup, false);
        MySearchLeagueAdapter.ViewHolder holderR = new MySearchLeagueAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MySearchLeagueAdapter.ViewHolder holder, int position) {
        League league = leagueList.get(position);
        Picasso.get().load(Constants.imageBaseURL + league.getImage())
                .resize(200, 200)
                .placeholder(R.drawable.leagues_teams_holder)
                .into(holder.image);

        final View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener14.onItemClick(league);
            }
        };

        holder.clitem.setOnClickListener(listener3);
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
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

    public void setOnClickListener(OnItemClickListener14 listener) {
        this.listener14 = listener;
    }
}