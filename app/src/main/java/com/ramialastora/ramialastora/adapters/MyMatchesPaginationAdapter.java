package com.ramialastora.ramialastora.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.classes.responses.UserResp;
import com.ramialastora.ramialastora.classes.responses.matches.MatchData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener5;
import com.ramialastora.ramialastora.listeners.OnItemClickListener55;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiFragments.TodayMatches.TEAM_MATCHES_VIEW_lEAGUE;

public class MyMatchesPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<MatchData> MatchList;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener5 listener1;
    private OnItemClickListener55 listener2;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;
    private int[] statusUnderway;
    private int[] statusComming;
    private int[] statusEnd;
    private String[] str;
    private int status;
    private int y = 0;

    public MyMatchesPaginationAdapter(Context context) {
        this.context = context;
        MatchList = new ArrayList<>();
    }

    public ArrayList<MatchData> getMatchList() {
        return MatchList;
    }

    public void setMatchList(ArrayList<MatchData> MatchList) {
        this.MatchList = MatchList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        statusUnderway = new int[getMatchList().size()];
        statusComming = new int[getMatchList().size()];
        statusEnd = new int[getMatchList().size()];
        str = new String[2];
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
        View v1 = inflater.inflate(R.layout.match_item_style, parent, false);
        viewHolder = new Holder(v1);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(context);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MatchData match = getMatchList().get(position);
        status = match.getStatus();
        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                //visibilities GONE
                holder1.constraintleague.setVisibility(View.GONE);
                holder1.constraintUnderway.setVisibility(View.GONE);
                holder1.constraintComming.setVisibility(View.GONE);
                holder1.constraintEnded.setVisibility(View.GONE);
                Picasso.get().load(Constants.imageBaseURL + match.getFirstTeam().getLogo())
                        .placeholder(R.drawable.leagues_teams_holder)
                        .resize(200, 200)
                        .into(holder1.imageTeam1);
                Picasso.get().load(Constants.imageBaseURL + match.getSecondTeam().getLogo())
                        .placeholder(R.drawable.leagues_teams_holder)
                        .resize(200, 200)
                        .into(holder1.imageTeam2);
                if (match.getFirstTeam().getName().length() > 13)
                    holder1.Team1name.setText(match.getFirstTeam().getName().substring(0, 13));
                else holder1.Team1name.setText(match.getFirstTeam().getName());
                if (match.getSecondTeam().getName().length() > 13)
                    holder1.Team2name.setText(match.getSecondTeam().getName().substring(0, 13));
                else holder1.Team2name.setText(match.getSecondTeam().getName());

                if (match.getResult() != null) {
                    Log.d(Constants.Log + "result", match.getResult());
                    String[] split = match.getResult().split("-", 2);
                    Log.d(Constants.Log + "result", split[0] + "-" + split[1]);
                    str[0] = split[0];
                    str[1] = split[1];
                    Log.d(Constants.Log + "result", split[0] + "-" + split[1]);
                }

                if (position == 0) {
                    switch (status) {
                        case 0:
                            Log.d(Constants.Log + "status", "(position == 0)(status == 0)");
                            appSharedPreferences.writeInteger(Constants.todayStatus, position);
                            break;
                        case 1:
                            Log.d(Constants.Log + "status", "(position == 0)(status == 1)");
                            appSharedPreferences.writeInteger(Constants.tomorrowStatus, position);
                            break;
                        case 2:
                            appSharedPreferences.writeInteger(Constants.yesterdayStatus, position);
                            break;
                    }
                    appSharedPreferences.writeInteger(Constants.status, match.getStatus());
                } else {

                    int prevStatus = appSharedPreferences.readInteger(Constants.status);
                    if (status != prevStatus) {
                        appSharedPreferences.writeInteger(Constants.status, match.getStatus());
                        switch (status) {
                            case 0:
                                Log.d(Constants.Log + "status", "(status == 0)");
                                appSharedPreferences.writeInteger(Constants.todayStatus, position);
                                break;
                            case 1:
                                Log.d(Constants.Log + "status", "(status == 1)");
                                appSharedPreferences.writeInteger(Constants.tomorrowStatus, position);
                                break;
                            case 2:
                                appSharedPreferences.writeInteger(Constants.yesterdayStatus, position);
                                break;
                        }
                    }
                }

                switch (match.getStatus()) {
                    case 0:
                        int x = appSharedPreferences.readInteger(Constants.todayStatus);
                        if (position == x) {
                            holder1.constraintStatus.setVisibility(View.VISIBLE);
                        } else {
                            holder1.constraintStatus.setVisibility(View.GONE);
                        }
                        Log.d(Constants.Log + "status", "statusUnderway.length = " + statusUnderway.length);
                        Log.d(Constants.Log + "status", "0 |" + position + "");
                        //Status Icon
                        holder1.commingEndedIcon.setVisibility(View.GONE);
                        holder1.underwayFramLayout.setVisibility(View.VISIBLE);
                        /////////////////////////////////////////////////////////
                        if (match.getResult() != null) {
                            holder1.underwayResultTeam1.setText(str[0]);
                            holder1.underwayResultTeam2.setText(str[1]);
                        } else {
                            holder1.underwayResultTeam1.setText("0");
                            holder1.underwayResultTeam2.setText("0");
                        }
                        /*//////////////////////calc time//////////////////////
//                      SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
                        Date date1 = new Date();
                        String[] time = match.getStartTime().split(":", 2);
                        String minutes = time[1].substring(0, 2);
                        Log.d(Constants.Log+"time", "hour = "+time[0]+" | minutes = "+minutes);
                        Date date = getDate(Integer.parseInt(time[0]), Integer.parseInt(minutes));
                        long mills =  date1.getTime() - date.getTime();
                            Log.d(Constants.Log+"Data1", ""+date1.getTime());
                            Log.d(Constants.Log+"Data", ""+date.getTime());
                            int hours = (int) (mills/(1000 * 60 * 60));
                            int mins = (int) (mills/(1000*60)) % 60;

                            String diff = hours + ":" + mins; // updated value every1 second*/
                        String[] time = match.getElapsedTime().split(" ", 2);
                        holder1.timeUnderway.setText(time[0].trim());


                        //visibility
                        holder1.constraintUnderway.setVisibility(View.VISIBLE);
                        holder1.constraintComming.setVisibility(View.GONE);
                        holder1.constraintEnded.setVisibility(View.GONE);
                        holder1.matchStatus.setText(context.getString(R.string.underway));
                        break;
                    case 1:
                        int y = appSharedPreferences.readInteger(Constants.tomorrowStatus);
                        if (position == y) {
                            holder1.constraintStatus.setVisibility(View.VISIBLE);
                        } else {
                            holder1.constraintStatus.setVisibility(View.GONE);
                        }
                        Log.d(Constants.Log + "status", "statusComming.length = " + statusComming.length);
                        Log.d(Constants.Log + "status", "1 |" + position + "");
                        //Status Icon
                        holder1.commingEndedIcon.setVisibility(View.VISIBLE);
                        holder1.underwayFramLayout.setVisibility(View.INVISIBLE);
                        holder1.commingEndedIcon.setImageResource(R.drawable.ic_update);
                        //////////////////////////////////////////////////////////////////
                        holder1.liveMatchStatus.setText(context.getString(R.string.live));
                        holder1.commingTime.setText(match.getStartTime());
                        //visibility
                        holder1.constraintUnderway.setVisibility(View.GONE);
                        holder1.constraintComming.setVisibility(View.VISIBLE);
                        holder1.constraintEnded.setVisibility(View.GONE);
                        holder1.matchStatus.setText(context.getString(R.string.comming));
                        break;
                    case 2:
                        int z = appSharedPreferences.readInteger(Constants.yesterdayStatus);
                        if (position == z) {
                            holder1.constraintStatus.setVisibility(View.VISIBLE);
                        } else {
                            holder1.constraintStatus.setVisibility(View.GONE);
                        }
                        Log.d(Constants.Log + "status", "statusEnd.length = " + statusEnd.length);
                        Log.d(Constants.Log + "status", "2 |" + position + "");
                        //Status Icon
                        holder1.commingEndedIcon.setVisibility(View.VISIBLE);
                        holder1.underwayFramLayout.setVisibility(View.INVISIBLE);
                        holder1.commingEndedIcon.setImageResource(R.drawable.ic_alarm_on);
                        //////////////////////////////////////////////////////////////////////
                        if (match.getResult() != null) {
                            if (Integer.parseInt(str[0]) > Integer.parseInt(str[1])) {
                                holder1.finalResultTeam1.setBackgroundResource(R.drawable.shape_final_result_winner);
                                holder1.finalResultTeam1.setTextColor(Color.parseColor("#00B8A9"));
                                holder1.finalResultTeam2.setBackgroundResource(R.drawable.shape_final_result_loser);
                                holder1.finalResultTeam2.setTextColor(Color.parseColor("#FE4B4B"));
                            } else if (Integer.parseInt(str[0]) < Integer.parseInt(str[1])) {
                                holder1.finalResultTeam2.setBackgroundResource(R.drawable.shape_final_result_winner);
                                holder1.finalResultTeam2.setTextColor(Color.parseColor("#00B8A9"));
                                holder1.finalResultTeam1.setBackgroundResource(R.drawable.shape_final_result_loser);
                                holder1.finalResultTeam1.setTextColor(Color.parseColor("#FE4B4B"));
                            } else {
                                holder1.finalResultTeam1.setBackgroundResource(R.drawable.shape_final_result_equal);
                                holder1.finalResultTeam1.setTextColor(Color.parseColor("#E0BD55"));
                                holder1.finalResultTeam2.setBackgroundResource(R.drawable.shape_final_result_equal);
                                holder1.finalResultTeam2.setTextColor(Color.parseColor("#E0BD55"));
                            }
                            holder1.finalResultTeam1.setText(str[0]);
                            holder1.finalResultTeam2.setText(str[1]);
                        } else {
                            holder1.finalResultTeam1.setBackgroundResource(R.drawable.shape_final_result_equal);
                            holder1.finalResultTeam1.setTextColor(Color.parseColor("#E0BD55"));
                            holder1.finalResultTeam2.setBackgroundResource(R.drawable.shape_final_result_equal);
                            holder1.finalResultTeam2.setTextColor(Color.parseColor("#E0BD55"));
                            holder1.finalResultTeam1.setText("0");
                            holder1.finalResultTeam2.setText("0");
                        }
                        //visibility
                        holder1.constraintUnderway.setVisibility(View.GONE);
                        holder1.constraintComming.setVisibility(View.GONE);
                        holder1.constraintEnded.setVisibility(View.VISIBLE);
                        holder1.matchStatus.setText(context.getString(R.string.ended));
                        break;
                }

                //TODO /////////////// Team Matches With View League //////////////
                if (TEAM_MATCHES_VIEW_lEAGUE == 1) {
                    holder1.constraintleague.setVisibility(View.VISIBLE);
                    Picasso.get().load(Constants.imageBaseURL+match.getLeagueActive().getLeague().getImage())
                            .placeholder(R.drawable.leagues_teams_holder)
                            .resize(100, 100)
                            .into(holder1.leagueImage);
                    holder1.leagueName.setText(match.getLeagueActive().getLeague().getTitle());
                } else if (TEAM_MATCHES_VIEW_lEAGUE == 0) {
                    holder1.constraintleague.setVisibility(View.GONE);
                }
                //TODO ///////////////End Team Matches With View League //////////////
                final View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(match);
                    }
                };
                final View.OnClickListener listener3 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener2.onItemClick(match);
                    }
                };

                holder1.constraintLayout2.setOnClickListener(listener);
                holder1.constraintleague.setOnClickListener(listener3);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    public static Date getDate(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public int getItemCount() {
        return getMatchList() == null ? 0 : getMatchList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getMatchList().size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(MatchData r) {
        getMatchList().add(r);
        notifyItemInserted(getMatchList().size() - 1);
    }

    public void addAll(ArrayList<MatchData> FavoriteList) {
        for (MatchData result : FavoriteList) {
            add(result);
        }
    }

    public void remove(MatchData r) {
        int position = getMatchList().indexOf(r);
        if (position > -1) {
            getMatchList().remove(position);
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
        add(new MatchData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = getMatchList().size() - 1;
        MatchData result = getItem(position);

        if (result != null) {
            getMatchList().remove(position);
            notifyItemRemoved(position);
        }
    }

    public MatchData getItem(int position) {
        return getMatchList().get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout2;
        ImageView imageTeam1, imageTeam2, commingEndedIcon, leagueImage;
        TextView Team1name, Team2name;
        //underway status and comming and ended
        TextView matchStatus;
        //underway
        TextView underwayResultTeam1, underwayResultTeam2, timeUnderway;
        //ended
        TextView finalResultTeam1, finalResultTeam2;
        //comming
        TextView liveMatchStatus, commingTime;
        //team matches
        TextView leagueName;
        //
        ConstraintLayout constraintUnderway, constraintComming, constraintEnded, constraintStatus, constraintleague;
        FrameLayout underwayFramLayout;

        public Holder(View itemView) {
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
            constraintleague = itemView.findViewById(R.id.constraintleague);
            leagueImage = itemView.findViewById(R.id.leagueimage);
            leagueName = itemView.findViewById(R.id.leaguename);
        }
    }

    public void setOnClickListener(OnItemClickListener5 listener) {
        this.listener1 = listener;
    }

    public void setOnClickListener(OnItemClickListener55 listener) {
        this.listener2 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<MatchData> orders) {
        MatchList = new ArrayList<>();
        getMatchList().addAll(orders);
        notifyDataSetChanged();
    }

    private void addorRemoveFavorite(int user_id, int team_id, int type) {
        Log.d(Constants.Log, "createUser");
        Call<UserResp> call = null;
        call = apiInterface.addFavorite(
                context.getString(R.string.api_key),
                context.getString(R.string.api_username),
                context.getString(R.string.api_password),
                user_id,
                team_id,
                type);
        call.enqueue(new Callback<UserResp>() {
            @Override
            public void onResponse(Call<UserResp> call, Response<UserResp> response) {
                UserResp resource = response.body();
                Log.d(Constants.Log, "Code = " + response.code());
                if (response.code() == 201) {//created
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    }
                } else if (response.code() == 202) {//accepted
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    }
                } else {
                    Log.d(Constants.Log, "create user error code = " + response.code());
                }


            }

            @Override
            public void onFailure(Call<UserResp> call, Throwable t) {
                if (t != null) {
                    Log.d(Constants.Log, "onFailure = " + t.getMessage());
                }
                call.cancel();
            }
        });
    }


}