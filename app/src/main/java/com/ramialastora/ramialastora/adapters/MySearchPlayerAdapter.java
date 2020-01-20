package com.ramialastora.ramialastora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.search.Player;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener13;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MySearchPlayerAdapter extends RecyclerView.Adapter<MySearchPlayerAdapter.ViewHolder> {
    ArrayList<Player> playerList;
    Context context;
    private OnItemClickListener13 listener13;

    public MySearchPlayerAdapter(ArrayList<Player> playerList, Context context) {
        this.playerList = playerList;
        this.context = context;
    }

    @Override
    public MySearchPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_player_item_style, viewGroup, false);
        MySearchPlayerAdapter.ViewHolder holderR = new MySearchPlayerAdapter.ViewHolder(view);

        return holderR;
    }

    @Override
    public void onBindViewHolder(final MySearchPlayerAdapter.ViewHolder holder, int position) {
            Player player = playerList.get(position);
            Picasso.get().load(Constants.imageBaseURL + player.getImage())
                    .resize(200, 200)
                    .placeholder(R.drawable.players_holder)
                    .into(holder.image);

            final View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener13.onItemClick(player);
                }
            };

            holder.clitem.setOnClickListener(listener2);
    }

    @Override
    public int getItemCount() {
            return playerList.size();
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

    public void setOnClickListener(OnItemClickListener13 listener) {
        this.listener13 = listener;
    }
}