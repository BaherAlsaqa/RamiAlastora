package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.adapters.MyMatchScorersAdapter;
import com.ramialastora.ramialastora.classes.responses.scorers.MatchScorersBody;
import com.ramialastora.ramialastora.classes.responses.scorers.MatchScorersData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener11;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchScorersFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyMatchScorersAdapter adapter;
    private List<MatchScorersData> teamPlayersList = new ArrayList<>();
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private int matchId, leagueId;
    private AppSharedPreferences appSharedPreferences;
    private FrameLayout flLeaguesList;

    public MatchScorersFragment() {
        // Required empty public constructor
    }

    public static MatchScorersFragment newInstance(int matchId, int leagueId) {
        MatchScorersFragment fragment = new MatchScorersFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.matchId, matchId);
        args.putInt(Constants.leagueId, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onPause() {
        super.onPause();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scorers, container, false);

//        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_scorer, "", 0);

        /////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);
        flLeaguesList = view.findViewById(R.id.flleagueslist);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        flLeaguesList.setVisibility(View.GONE);

        loadMatchGoalsVideos();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMatchGoalsVideos();
            }
        });

        /*int sorts[] = {
                1,2,3,4,5,6,7,8,9,10,11,12
        };

        int images[] = {
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1,
                R.drawable.player1
        };

        String names[] = {
                "كريم بنزيما","جيرارد مورينو","لورينزو مورن","دانيال باريخو","راؤول غارسيا","لويس سواريز","روجر مارتي","جيرارد مورينو","لورينزو مورن","راؤول غارسيا","لويس سواريز","روجر مارتي سالفادور"
        };

        String centers[] = {
                "ريال مدريد","فياريال","ريال بيتيس","فالنسيا","اتلتيك بيلباو","برشلونة","ليفانتي","فياريال","فالنسيا","اتلتيك بيلباو","برشلونة","ليفانتي",
        };

        int yellow[] = {
                0,0,1,0,0,1,0,0,0,1,1,0
        };

        int red[] = {
                0,0,1,1,0,1,1,0,0,1,0,0
        };

        int goals[] = {
                5,5,5,3,3,5,4,6,2,1,4,3
        };

        int trueP[] = {
                0,0,0,3,1,0,2,1,0,1,0,0
        };

        int falseP[] = {
                0,0,0,0,0,0,0,0,0,0,0,0
        };


        for (int i = 0; i < images.length; i++) {
            MatchScorersData teamPlayers = new MatchScorersData(sorts[i], images[i], names[i], centers[i], yellow[i], red[i], goals[i], trueP[i], falseP[i]);
            teamPlayersList.add(teamPlayers);
        }

        Log.d(Constants.Log+"cases", teamPlayersList.size()+"");*/


        /////////////////////////////////////////////////////////////////

        return view;
    }

    private Call<MatchScorersBody> callTopRatedMoviesApi() {
        Call<MatchScorersBody> teamPlayersBodyCall = null;
        if (getArguments() != null) {
            matchId = getArguments().getInt(Constants.matchId);
            Log.d(Constants.Log + "matchid", "match Id = " + matchId);
            if (getContext() != null) {
                teamPlayersBodyCall = apiInterface.getPlayersGoalsMatch(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        matchId);
            }
        }
        return teamPlayersBodyCall;
    }

    private void loadMatchGoalsVideos() {
        Log.d(Constants.Log, "loadFirstPage");
        indicatorView.show();
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        if (!isNetworkConnected()) {
            swiperefresh.setRefreshing(false);
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.hide();
        }
        callTopRatedMoviesApi().enqueue(new Callback<MatchScorersBody>() {
            @Override
            public void onResponse(@NotNull Call<MatchScorersBody> call, @NotNull Response<MatchScorersBody> response) {

                MatchScorersBody resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    indicatorView.hide();
                    boolean status = resource.getStatus();
                    int code = response.code();
                    Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    Log.d(Constants.Log + "res", "if (response.code() == 200)");

                    swiperefresh.setRefreshing(false);
                    teamPlayersList.clear();
                    teamPlayersList.addAll(resource.getData());
                    adapter = new MyMatchScorersAdapter(teamPlayersList, view.getContext());
                    mRecyclerView.setAdapter(adapter);

                    adapter.setOnClickListener(new OnItemClickListener11() {
                        @Override
                        public void onItemClick(MatchScorersData item) {
                            if (getArguments() != null) {
                                leagueId = getArguments().getInt(Constants.leagueId);
                                appSharedPreferences.writeInteger(Constants.playerId, item.getPlayerId());

                                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, PlayerDetails.newInstance(leagueId, item.getPlayerId(), 0));
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                    });
                    Log.d(Constants.Log + "size", "size = " + teamPlayersList.size() + "");
                    if (teamPlayersList.size() == 0) {
                        Log.d(Constants.Log + "size", "teamPlayersList.size() == 0 = " + teamPlayersList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "teamPlayersList.size() != 0 = " + teamPlayersList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }

                } else if (response.code() == 404) {
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.hide();
                    swiperefresh.setRefreshing(false);
                } else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    indicatorView.hide();
                    error.setVisibility(View.VISIBLE);
                    swiperefresh.setRefreshing(false);
                }


            }

            @Override
            public void onFailure(Call<MatchScorersBody> call, Throwable t) {
                Log.d(Constants.Log + "55", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.Log + "res", "onFailure = " + t.getMessage() + "");
                if (t instanceof IOException) {
                    swiperefresh.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                }
                call.cancel();
                indicatorView.hide();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        } else {
            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        return cm.getActiveNetworkInfo() != null;
    }

}
