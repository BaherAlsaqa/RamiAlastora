package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.InterstitialAd;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.OutLink;
import com.ramialastora.ramialastora.adapters.MyMatchGoalsVideosAdapter;
import com.ramialastora.ramialastora.admob.MobileAdsInterface;
import com.ramialastora.ramialastora.classes.responses.match_goals_video.MatchGoalsVideoBody;
import com.ramialastora.ramialastora.classes.responses.match_goals_video.MatchGoalsVideoData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener10;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
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
public class MatchGoalsVideosFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyMatchGoalsVideosAdapter adapter;
    private List<MatchGoalsVideoData> teamPlayersList = new ArrayList<>();
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private int matchId;

    public MatchGoalsVideosFragment() {
        // Required empty public constructor
    }

    public static MatchGoalsVideosFragment newInstance(int matchId) {
        MatchGoalsVideosFragment fragment = new MatchGoalsVideosFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.matchId, matchId);
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
        view = inflater.inflate(R.layout.fragment_match_goals_videos, container, false);

//        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_scorer, "", 0);

        /////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

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
            MatchGoalsVideoData teamPlayers = new MatchGoalsVideoData(sorts[i], images[i], names[i], centers[i], yellow[i], red[i], goals[i], trueP[i], falseP[i]);
            teamPlayersList.add(teamPlayers);
        }

        Log.d(Constants.Log+"cases", teamPlayersList.size()+"");*/


        /////////////////////////////////////////////////////////////////

        return view;
    }

    private Call<MatchGoalsVideoBody> callTopRatedMoviesApi() {
        Call<MatchGoalsVideoBody> teamPlayersBodyCall = null;
        if (getArguments() != null) {
            matchId = getArguments().getInt(Constants.matchId);
            Log.d(Constants.Log + "matchid", "match Id = " + matchId);
            if (getContext() != null) {
                teamPlayersBodyCall = apiInterface.getMatchGoalsVideo(getString(R.string.api_key),
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
        callTopRatedMoviesApi().enqueue(new Callback<MatchGoalsVideoBody>() {
            @Override
            public void onResponse(@NotNull Call<MatchGoalsVideoBody> call, @NotNull Response<MatchGoalsVideoBody> response) {

                MatchGoalsVideoBody resource = response.body();
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
                    adapter = new MyMatchGoalsVideosAdapter(teamPlayersList, view.getContext());
                    mRecyclerView.setAdapter(adapter);

                    InterstitialAd interstitialAd =
                            MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_Match_goals_videos_inter));

                    adapter.setOnClickListener(new OnItemClickListener10() {
                        @Override
                        public void onItemClick(MatchGoalsVideoData item) {
                            startActivity(new Intent(getContext(), OutLink.class).putExtra(Constants.link, item.getUrl()));

                            MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
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

                }else if (response.code() == 404){
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
            public void onFailure(Call<MatchGoalsVideoBody> call, Throwable t) {
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
