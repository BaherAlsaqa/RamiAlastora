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
import com.ramialastora.ramialastora.classes.responses.objects.League;
import com.ramialastora.ramialastora.listeners.OnItemClickListener4;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyLeaguesAdapter extends RecyclerView.Adapter<MyLeaguesAdapter.ViewHolder> {
    List<League> leagueList;
    Context context;
    private OnItemClickListener4 listener1;

    public MyLeaguesAdapter(List<League> providerList, Context context) {
        this.leagueList = providerList;
        this.context = context;
    }

    @Override
    public MyLeaguesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.league_item_style, viewGroup, false);
        MyLeaguesAdapter.ViewHolder holderR = new MyLeaguesAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MyLeaguesAdapter.ViewHolder holder, int position) {
        final League league = leagueList.get(position);

        Picasso.get().load(league.image).into(holder.leagueImage);
        /*holder.leagueName.setText(league.name);
        holder.sort.setText(league.sort);*/

        /*final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(league);
            }
        };

        holder.constraintLayout.setOnClickListener(listener);*/
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView leagueImage;
        TextView leagueName, sort;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
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
}