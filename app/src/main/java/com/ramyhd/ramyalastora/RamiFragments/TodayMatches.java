package com.ramyhd.ramyalastora.RamiFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.InterstitialAd;
import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.adapters.MyMatchesPaginationAdapter;
import com.ramyhd.ramyalastora.admob.MobileAdsInterface;
import com.ramyhd.ramyalastora.classes.responses.matches.BodyData;
import com.ramyhd.ramyalastora.classes.responses.matches.Data;
import com.ramyhd.ramyalastora.classes.responses.matches.MatchData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener5;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener55;
import com.ramyhd.ramyalastora.listeners.PaginationScrollListener;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramyhd.ramyalastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;
import static com.ramyhd.ramyalastora.RamiFragments.SortParticipatingTeamsFragment.TEAM_ID1;

public class TodayMatches extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private ArrayList<MatchData> matchList = new ArrayList<>();
    private MyMatchesPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    //pagination
    private static int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    private View v;
    private AppSharedPreferences appSharedPreferences;
    private EditText eTSearch;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private String matchesData_LoadNext;
    public static int TEAM_MATCHES_VIEW_lEAGUE = 0;
    private String matchDate;
    private int type, teamId, leagueId;

    @SuppressLint("StaticFieldLeak")
    public TodayMatches() {
        // Required empty public constructor
    }

    public static TodayMatches newInstance(int type, String matchDate, int teamId, int leagueId) {
        TodayMatches fragment = new TodayMatches();
        Bundle args = new Bundle();
        args.putInt(Constants.type, type);
        args.putString(Constants.matchDate, matchDate);
        args.putInt(Constants.teamId, teamId);
        args.putInt(Constants.leagueId, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_today_matches, container, false);
        /*/////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int imagesTeam1[] = {
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2
        };
        int imagesTeam2[] = {
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1
        };

        String teamName1[] = {
                "ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام"
        };

        String teamName2[] = {
                "ليستر سيتي", "توتنهام", "ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي","توتنهام"
        };

        String time[] = {"37","15","50","60","55","25","20","45","54","65","47","48"};

        int resultTeam1[] = {2,4,0,5,2,2,1,4,1,3,3,0};

        int resultTeam2[] = {1,0,0,2,2,4,2,3,1,2,2,1};

        // 1 = underway, 2 = comming, 0 = ended .
        String matchStatus[] = {
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.ended),
                getString(R.string.ended),
                getString(R.string.ended)};


        for (int i = 0; i < imagesTeam1.length; i++) {
            Match match = new Match(imagesTeam1[i], imagesTeam2[i], teamName1[i], teamName2[i], time[i], resultTeam1[i], resultTeam2[i], matchStatus[i]);
            matchList.add(match);
        }

        Log.d(Constants.Log+"cases", matchList.size()+"");

        adapter = new MyMatchesAdapter(matchList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener5() {
            FragmentTransaction fragmentTransaction = null;
            @Override
            public void onItemClick(Match item) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, MatchDetailsFragment.newInstance(item.getNameTeam1(),
                                                                                                        item.getNameTeam2()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        /////////////////////////////////////////////////////////////////*/

        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        eTSearch = view.findViewById(R.id.search);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);

        //TODO //////////////////// start pagination code and settings////////////////////////////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ///////////// end settings ///////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));
        } else {
            appSharedPreferences = new AppSharedPreferences(getContext());
        }
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        adapter = new MyMatchesPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_today_matches_inter));

        adapter.setOnClickListener(new OnItemClickListener5() {
            @Override
            public void onItemClick(MatchData item) {
                BACK_FRAGMENTS = 1;
                Log.d(Constants.Log + "md", item.getLeagueActive().getLeague().getTitle());
                FragmentTransaction fragmentTransaction = null;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, MatchDetailsFragment.newInstance(item));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
            }
        });

        adapter.setOnClickListener(new OnItemClickListener55() {
            @Override
            public void onItemClick(MatchData item) {
                TEAM_ID1 = -1;
                BACK_FRAGMENTS = 1;
                Log.d(Constants.Log + "leaguematches", item.getLeagueActive().getLeague().getTitle());
                FragmentTransaction fragmentTransaction = null;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, LeagueDetailsFragment.newInstance(
                        item, 1, "", "", 0));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
            }
        });

        loadFirstPage();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage();
            }
        });

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        });

        //TODO //////////// end pagination code /////////////////////////////////////////////

        return view;
    }

    private Call<BodyData> callTopRatedMoviesApi() {
        int userId = appSharedPreferences.readInteger(Constants.userid);
        Log.d(Constants.Log + "user_id", "user id = " + userId);
        Call<BodyData> matches = null;
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en", "US"));
        String matchesTodayDate = null;
        gc.add(Calendar.DATE, 0);
        matchesTodayDate = df.format(gc.getTime());
        Log.d(Constants.Log + "posTab", "TodayDate = " + matchesTodayDate);
        if (getContext() != null) {
            if (getArguments() == null) {
                Log.d(Constants.Log + "posTab", "(getArguments() == null)");
                matches = apiInterface.getMatches(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        matchesTodayDate,
                        userId,
                        currentPage);
            } else {
                matchDate = getArguments().getString(Constants.matchDate);
                type = getArguments().getInt(Constants.type);
                teamId = getArguments().getInt(Constants.teamId);
                leagueId = getArguments().getInt(Constants.leagueId);
                if (type == 1) {
                    TEAM_MATCHES_VIEW_lEAGUE = 0;
                    Log.d(Constants.Log + "posTab", "(type == 1) = today matches");
                    if (matchDate != null) {

                        Log.d(Constants.Log + "posTab", "(getArguments() != null)");
                        matches = apiInterface.getMatches(getString(R.string.api_key),
                                getString(R.string.api_username),
                                getString(R.string.api_password),
                                matchDate,
                                userId,
                                currentPage);
                    }
                }else if (type == 2){
                    TEAM_MATCHES_VIEW_lEAGUE = 1;
                    Log.d(Constants.Log + "posTab", "(type == 2) = team matches");
                    matches = apiInterface.getTeamMatches(getString(R.string.api_key),
                            getString(R.string.api_username),
                            getString(R.string.api_password),
                            teamId,
                            currentPage);
                }else if (type == 3){
                    TEAM_MATCHES_VIEW_lEAGUE = 0;
                    Log.d(Constants.Log + "posTab", "(type == 3) = league matches");
                    matches = apiInterface.getMatchesInLeague(getString(R.string.api_key),
                            getString(R.string.api_username),
                            getString(R.string.api_password),
                            leagueId,
                            currentPage);
                }
            }
        }
        return matches;
    }

    private ArrayList<MatchData> fetchResults(Response<BodyData> response) {
        BodyData BodyData = response.body();
        Data homeItemsObject = BodyData.getData();
        return homeItemsObject.getData();
    }

    private void loadFirstPage() {
        Log.d(Constants.Log, "loadFirstPage");
        ///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////
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
        callTopRatedMoviesApi().enqueue(new Callback<BodyData>() {
            @Override
            public void onResponse(@NotNull Call<BodyData> call, @NotNull Response<BodyData> response) {

                BodyData resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    indicatorView.hide();
                    boolean status = resource.getStatus();
                    int code = response.code();
                    Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    Log.d(Constants.Log + "res", "if (response.code() == 200)");

//                    progress.setVisibility(View.INVISIBLE);
                    swiperefresh.setRefreshing(false);
                    adapter.clear();
                    matchList = resource.getData().getData();
                    TOTAL_PAGES = resource.getData().getLastPage();
                    Log.d(Constants.Log + "size", "size = " + matchList.size() + "");
                    adapter.addAll(matchList);
                    if (matchList.size() == 0) {
                        Log.d(Constants.Log + "size", "matchList.size() == 0 = " + matchList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "matchList.size() != 0 = " + matchList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }
                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

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
            public void onFailure(Call<BodyData> call, Throwable t) {
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

    public void loadNextPage() {

        Log.d(Constants.Log, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<BodyData>() {
            @Override
            public void onResponse(Call<BodyData> call, Response<BodyData> response) {

                BodyData resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "BodyData", " | Code = " + code);

                if (response.code() == 200) {

                    matchList = fetchResults(response);
                    Log.d(Constants.Log + "sdata", adapter.getItemCount() + " fetch");

                    if (isLoading) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        adapter.addAll(matchList);
                    }

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                } else {

                }
            }

            @Override
            public void onFailure(Call<BodyData> call, Throwable t) {
                t.printStackTrace();
                try {
                    if (getContext() != null) {
                        Log.d(Constants.Log, t.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
