package com.ramyhd.ramyalastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.InterstitialAd;
import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.RamiActivities.FromNotification;
import com.ramyhd.ramyalastora.RamiActivities.RamiMain;
import com.ramyhd.ramyalastora.adapters.MyParticipatingTeamsAdapter;
import com.ramyhd.ramyalastora.admob.MobileAdsInterface;
import com.ramyhd.ramyalastora.classes.responses.participating_teams.ParticipatingTeamsBody;
import com.ramyhd.ramyalastora.classes.responses.participating_teams.ParticipatingTeamsData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener3;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortParticipatingTeamsFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyParticipatingTeamsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ParticipatingTeamsData> pTeamsDataList = new ArrayList<>();
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    /*//pagination
    private static int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;*/
    AppSharedPreferences appSharedPreferences;
    private int userId, leagueId, backButton, type;
    private String leagueName;
    public static int TEAM_ID1 = 0;
    public static int TEAM_ID2 = 0;
    private ConstraintLayout cLayout;

    public static SortParticipatingTeamsFragment newInstance(int type, int backButton, String leagueName, int leagueId, int teamId1, int teamId2) {
        SortParticipatingTeamsFragment fragment = new SortParticipatingTeamsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.leagueName, leagueName);
        args.putInt(Constants.backButton, backButton);
        args.putInt(Constants.leagueId, leagueId);
        args.putInt(Constants.teamId1, teamId1);
        args.putInt(Constants.teamId2, teamId2);
        args.putInt(Constants.type, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            leagueName = getArguments().getString(Constants.leagueName);
            backButton = getArguments().getInt(Constants.backButton);
            leagueId = getArguments().getInt(Constants.leagueId);
            TEAM_ID1 = getArguments().getInt(Constants.teamId1);
            TEAM_ID2 = getArguments().getInt(Constants.teamId2);
            type = getArguments().getInt(Constants.type);
            Log.d(Constants.Log + "pteam", "league name = " + leagueName + " league id = " +
                    leagueId + "team id1 = " + TEAM_ID1 + " team id2 = " + TEAM_ID2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sort_participating_teams, container, false);

        Log.e("teamsSortFragment", "teamsSortFragment :" + backButton);

        if (backButton > 0) {
            try {
                ((RamiMain) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_back, 0, leagueName, 3);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_back, 0, leagueName, 3);
            }
        }
            mRecyclerView = view.findViewById(R.id.recyclerview);
            swiperefresh = view.findViewById(R.id.swiperefresh);
            indicatorView = view.findViewById(R.id.avi);
            noInternet = view.findViewById(R.id.nointernet);
            emptyData = view.findViewById(R.id.emptydata);
            error = view.findViewById(R.id.error);
            cLayout = view.findViewById(R.id.clayout);

            cLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //Initializing
            appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

            if (type == 1)
                MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_participating_team_banner), view);

        /*//TODO //////////////////// start pagination code and settings////////////////////////////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ///////////// end settings ///////////////*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));
            } else {
                appSharedPreferences = new AppSharedPreferences(getContext());
            }
            linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            apiInterface = APIClient.getClient().create(APIInterface.class);

            swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);

            userId = appSharedPreferences.readInteger(Constants.userid);

            loadPTeamsData();

            swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadPTeamsData();
                }
            });

        /*mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(Constants.Log + "addOnScroll", "addOnScrollListener");

                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(Constants.Log + "addOnScroll", "addOnScrollListener");
                        //TODO java.lang.NullPointerException: Attempt to invoke interface method 'void retrofit2.Call.enqueue(retrofit2.Callback)' on a null object reference
                        try {
                            loadNextPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/

            //TODO //////////// end pagination code /////////////////////////////////////////////
        
        /*/////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int sort[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int imagesLeagues[] = {
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1
        };

        String teamsNames[] = {
                "بايرن ميونخ", "برشلونة", "ريال مدريد", "غرناطة", "ريال سويسداد", "اشبيلية", "اتليتكو مدريد", "فياريال", "ليفانتي", "أوساسونا", "خيتافي", "فالنسيا"
        };

        int played[] = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int won[] = {2, 3, 2, 3, 2, 1, 2, 3, 3, 3, 2, 2};
        int drew[] = {2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1};
        int loser[] = {1, 1, 1, 0, 1, 2, 1, 1, 0, 1, 1, 2};
        int scored[] = {6, 10, 9, 5, 2, 4, 4, 10, 5, 2, 3, 6};
        int ownNet[] = {3, 5, 4, 4, 2, 4, 2, 8, 1, 1, 2, 4};
        int rest[] = {3, 5, 5, 1, 0, 0, 2, 2, 4, 1, 1, 2};
        int points[] = {9, 10, 9, 11, 10, 6, 9, 10, 11, 10, 9, 6};


        for (int i = 0; i < imagesLeagues.length; i++) {
            TeamsSort teamsSort = new TeamsSort(sort[i], imagesLeagues[i], teamsNames[i], played[i], won[i], drew[i], loser[i], scored[i], ownNet[i], rest[i], points[i]);
            leagueList.add(teamsSort);
        }

        Log.d(Constants.Log + "cases", leagueList.size() + "");

        adapter = new MyParticipatingTeamsPaginationAdapter(leagueList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener3() {
            FragmentTransaction fragmentTransaction = null;

            @Override
            public void onItemClick(ParticipatingTeamsData item) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, TeamDetailsFragment.newInstance(item.getTeamName()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        /////////////////////////////////////////////////////////////////*/

            return view;
        }

        private Call<ParticipatingTeamsBody> callTopRatedMoviesApi () {
            Call<ParticipatingTeamsBody> participatingTeamsBodyCall = null;
            Log.d(Constants.Log + "user_id", "user id = " + userId);
            if (getContext() != null) {
                participatingTeamsBodyCall = apiInterface.getPaticipatingTeams(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        leagueId,
                        userId);
            }
            return participatingTeamsBodyCall;
        }

        private ArrayList<ParticipatingTeamsData> fetchResults
        (Response < ParticipatingTeamsBody > response) {
            ParticipatingTeamsBody ParticipatingTeamsBody = response.body();
//        Data data = ParticipatingTeamsBody.getData();
            assert ParticipatingTeamsBody != null;
            return ParticipatingTeamsBody.getData();
        }

        private void loadPTeamsData () {
            Log.d(Constants.Log, "loadFirstPage");
        /*///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////*/
            indicatorView.show();
            noInternet.setVisibility(View.GONE);
            emptyData.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (!isNetworkConnected()) {
                swiperefresh.setRefreshing(false);
                noInternet.setVisibility(View.VISIBLE);
                indicatorView.hide();
            }
            callTopRatedMoviesApi().enqueue(new Callback<ParticipatingTeamsBody>() {
                @Override
                public void onResponse(@NotNull Call<ParticipatingTeamsBody> call, @NotNull Response<ParticipatingTeamsBody> response) {

                    ParticipatingTeamsBody resource = response.body();
                    Log.d(Constants.Log + "res", "onResponse");
                    if (response.code() == 200) {
                        try {
                            indicatorView.hide();
                            boolean status = resource.getStatus();
                            int code = response.code();
                            Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                            Log.d(Constants.Log + "res", "if (response.code() == 200)");

//                    progress.setVisibility(View.INVISIBLE);
                            swiperefresh.setRefreshing(false);
                            pTeamsDataList.clear();
                            pTeamsDataList = resource.getData();
//                        TOTAL_PAGES = resource.getData().getLastPage();

                            Log.d(Constants.Log + "size", "size = " + pTeamsDataList.size() + "");
                            adapter = new MyParticipatingTeamsAdapter(pTeamsDataList, view.getContext());
                            mRecyclerView.setAdapter(adapter);
                            InterstitialAd interstitialAd =
                                    MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_sort_participating_teams_inter));

                            adapter.setOnClickListener(new OnItemClickListener3() {
                                @Override
                                public void onItemClick(ParticipatingTeamsData item) {
                                    appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.teamF);
                                    FragmentTransaction fragmentTransaction = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                        fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                                    } else {
                                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    }
                                    fragmentTransaction.replace(R.id.nav_host_fragment, TeamDetailsFragment
                                            .newInstance(item.getFavorite(), item.getTemaId(), item.getName(), item.getLogo(), item.getCountry(), leagueName, leagueId));
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                                }
                            });
                            if (pTeamsDataList != null)
                                if (pTeamsDataList.size() == 0) {
                                    Log.d(Constants.Log + "size", "pTeamsDataList.size() == 0 = " + pTeamsDataList.size() + "");
                                    emptyData.setVisibility(View.VISIBLE);
                                } else {
                                    Log.d(Constants.Log + "size", "pTeamsDataList.size() != 0 = " + pTeamsDataList.size() + "");
                                    emptyData.setVisibility(View.GONE);
                                }
                        /*if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;*/
                        } catch (Exception e) {
                            e.printStackTrace();
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
                public void onFailure(Call<ParticipatingTeamsBody> call, Throwable t) {
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

//    public void loadNextPage() {
//
//        Log.d(Constants.Log, "loadNextPage: " + currentPage);
//
//        callTopRatedMoviesApi().enqueue(new Callback<ParticipatingTeamsBody>() {
//            @Override
//            public void onResponse(Call<ParticipatingTeamsBody> call, Response<ParticipatingTeamsBody> response) {
//
//                ParticipatingTeamsBody resource = response.body();
//
//                int code = response.code();
//
//                Log.d(Constants.Log + "PTeamsBody", " | Code = " + code);
//
//                if (response.code() == 200) {
//
//                    pTeamsDataList = fetchResults(response);
//                    Log.d(Constants.Log + "sdata", adapter.getItemCount() + " fetch");
//
//                    if (isLoading) {
//                        adapter.removeLoadingFooter();
//                        isLoading = false;
//                        adapter.addAll(pTeamsDataList);
//                    }
//
//                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                    else isLastPage = true;
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ParticipatingTeamsBody> call, Throwable t) {
//                t.printStackTrace();
//                try {
//                    if (getContext() != null) {
//                        Log.d(Constants.Log, t.getMessage());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

        private boolean isNetworkConnected () {
            ConnectivityManager cm = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
            } else {
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            }

            return cm.getActiveNetworkInfo() != null;
        }

    }
