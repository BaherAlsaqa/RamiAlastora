package com.ramyhd.ramyalastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.classes.responses.UserResp;
import com.ramyhd.ramyalastora.classes.responses.objects.Favorite;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemCheckedListener1;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener6;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramyhd.ramyalastora.RamiFragments.FavoriteItemsFragment.TYPE;

public class MyFavoritesPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<Favorite> FavoriteList;
    private ArrayList<Favorite> FavoriteListFilter;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener6 listener1;
    private OnItemCheckedListener1 listener2;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;
    private Holder holder1;

    public MyFavoritesPaginationAdapter(Context context) {
        this.context = context;
        FavoriteList = new ArrayList<>();
    }

    public ArrayList<Favorite> getFavoriteList() {
        return FavoriteList;
    }

    public void setFavoriteList(ArrayList<Favorite> FavoriteList) {
        this.FavoriteList = FavoriteList;
    }

    @NotNull
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
        View v1 = inflater.inflate(R.layout.favorite_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        final Favorite favorite = FavoriteList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                holder1 = (Holder) holder;

                holder1.favorite.setOnCheckedChangeListener(null);
                holder1.favorite.setOnClickListener(null);
                Log.d(Constants.Log + "fav", position + " | " + favorite.getFavorite());
                if (favorite.getFavorite() == 1) {
                    holder1.favorite.setChecked(true);
                    Log.d(Constants.Log + "fav", "holder1.favorite.setChecked(true)");
                } else if (favorite.getFavorite() == 0){
                    holder1.favorite.setChecked(false);
                    Log.d(Constants.Log + "fav", "holder1.favorite.setChecked(false)");
                }

                Picasso.get().load(Constants.imageBaseURL + favorite.getLogo())
                        .resize(200, 200)
                        .into(holder1.teamImage);
                Picasso.get().load(R.drawable.leagues_teams_holder)
                        .resize(100, 100)
                        .into(holder1.leagueImage);
                holder1.sort.setText("");
                if (favorite.getParticipatingLeagues() != null) {
                    Log.d(Constants.Log + "size", "getParticipatingLeagues size = " +
                            favorite.getParticipatingLeagues().size());
                    if (favorite.getParticipatingLeagues().size() > 0) {
                        if (TYPE == 1) {//get teams without search
                            Picasso.get().load(Constants.imageBaseURL +
                                    favorite.getParticipatingLeagues().get(0).getImage())
                                    .resize(100, 100)
                                    .into(holder1.leagueImage);
                        } else if (TYPE == 2) {// get teams with search
                            if (favorite.getParticipatingLeagues() != null)
                            if (favorite.getParticipatingLeagues().get(0).getLeague() != null)
                            if (favorite.getParticipatingLeagues().get(0).getLeague().getImage() != null) {
                                Picasso.get().load(Constants.imageBaseURL +
                                        favorite.getParticipatingLeagues().get(0).getLeague().getImage())
                                        .resize(100, 100)
                                        .into(holder1.leagueImage);
                            }
                        }
                        assert favorite.getParticipatingLeagues() != null;
                        if (favorite.getParticipatingLeagues().get(0).getSorting() != null) {
                            holder1.sort.setText(favorite.getParticipatingLeagues().get(0).getSorting() + "");
                        }else{
                            holder1.sort.setText("0");
                        }
                    }
                }
                holder1.teamName.setText(favorite.getName());

                View.OnClickListener listener = v -> listener1.onItemClick(favorite);

                final int user_id = appSharedPreferences.readInteger(Constants.userid);

                holder1.favorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    holder1.favorite.setOnCheckedChangeListener(null);
                    holder1.favorite.setOnClickListener(null);
                    if (TYPE == 1) {//without search
                        if (!isChecked) {
                            addorRemoveFavorite(user_id,
                                    favorite.getTeamId(),
                                    0,
                                    favorite);//Remove Favorite
                        } else {
                            addorRemoveFavorite(user_id,
                                    favorite.getTeamId(),
                                    1,
                                    favorite);//Add Favorite
                        }
                    }else if (TYPE == 2){//with search
                        if (!isChecked) {
                            addorRemoveFavorite(user_id,
                                    favorite.getId(),
                                    0,
                                    favorite);//Remove Favorite
                        } else {
                            addorRemoveFavorite(user_id,
                                    favorite.getId(),
                                    1,
                                    favorite);//Add Favorite
                        }
                    }
                });

                holder1.constraintLayout.setOnClickListener(listener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return FavoriteList == null ? 0 : FavoriteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == FavoriteList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void setList(ArrayList<Favorite> list) {
        if ((list.size() == 0)) {
//            FavoriteItemsFragment.loadNext();
            this.FavoriteListFilter = list;

        } else {
            this.FavoriteListFilter = list;

        }
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Favorite r) {
        FavoriteList.add(r);
        notifyItemInserted(FavoriteList.size() - 1);
    }

    public void addAll(ArrayList<Favorite> FavoriteList) {
        for (Favorite result : FavoriteList) {
            add(result);
        }
    }

    public void remove(Favorite r) {
        int position = FavoriteList.indexOf(r);
        if (position > -1) {
            FavoriteList.remove(position);
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
        add(new Favorite());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = FavoriteList.size() - 1;
        Favorite result = getItem(position);

        if (result != null) {
            FavoriteList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Favorite getItem(int position) {
        return FavoriteList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ImageView teamImage, leagueImage;
        TextView teamName, sort;
        ConstraintLayout constraintLayout;
        CheckBox favorite;

        Holder(View itemView) {
            super(itemView);
            teamImage = itemView.findViewById(R.id.teamimage);
            leagueImage = itemView.findViewById(R.id.leagueimage);
            teamName = itemView.findViewById(R.id.teamname);
            sort = itemView.findViewById(R.id.sort);
            constraintLayout = itemView.findViewById(R.id.clteam);
            favorite = itemView.findViewById(R.id.favorite);
        }
    }

    public void setOnClickListener(OnItemClickListener6 listener) {
        this.listener1 = listener;
    }

    public void setOnCheckedListener(OnItemCheckedListener1 listener) {
        this.listener2 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<Favorite> orders) {
        FavoriteList = new ArrayList<>();
        FavoriteList.addAll(orders);
        notifyDataSetChanged();
    }

    private void addorRemoveFavorite(int user_id, int team_id, int type, Favorite favorite) {
        Log.d(Constants.Log + "addorRemove", "create fav team = " + team_id);
        Call<UserResp> call;
        call = apiInterface.addFavorite(
                context.getString(R.string.api_key),
                context.getString(R.string.api_username),
                context.getString(R.string.api_password),
                user_id,
                team_id,
                type);
        call.enqueue(new Callback<UserResp>() {
            @Override
            public void onResponse(@NotNull Call<UserResp> call, @NotNull Response<UserResp> response) {
                UserResp resource = response.body();
                Log.d(Constants.Log, "Code = " + response.code());
                if (response.code() == 201) {//created
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);


                        holder1.favorite.setOnCheckedChangeListener(null);
                        holder1.favorite.setOnClickListener(null);
                        favorite.setFavorite(1);
                        holder1.favorite.setChecked(true);
                    }
                } else if (response.code() == 202) {//removed
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);

                        holder1.favorite.setOnCheckedChangeListener(null);
                        holder1.favorite.setOnClickListener(null);
                        holder1.favorite.setChecked(false);
                        favorite.setFavorite(0);
                    }
                } else {
                    if (type == 0) {
                        Log.d(Constants.Log + "fav", "(type == 0)");

                        holder1.favorite.setOnCheckedChangeListener(null);
                        holder1.favorite.setOnClickListener(null);
                        holder1.favorite.setChecked(true);
                    } else if (type == 1) {
                        Log.d(Constants.Log + "fav", "(type == 1)");

                        holder1.favorite.setOnCheckedChangeListener(null);
                        holder1.favorite.setOnClickListener(null);
                        holder1.favorite.setChecked(false);
                    }
                    Log.d(Constants.Log, "create user error code = " + response.code());
                }


            }

            @Override
            public void onFailure(Call<UserResp> call, Throwable t) {
                Log.d(Constants.Log, "onFailure = " + t.getMessage());
                call.cancel();
                if (type == 0) {

                    holder1.favorite.setOnCheckedChangeListener(null);
                    holder1.favorite.setOnClickListener(null);
                    holder1.favorite.setChecked(true);
                } else if (type == 1) {

                    holder1.favorite.setOnCheckedChangeListener(null);
                    holder1.favorite.setOnClickListener(null);
                    holder1.favorite.setChecked(false);
                }
            }
        });
    }


}