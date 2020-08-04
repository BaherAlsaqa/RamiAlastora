package com.ramyhd.ramyalastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ramyhd.ramyalastora.adapters.MyLeaguesPaginationAdapter;
import com.ramyhd.ramyalastora.admob.MobileAdsInterface;
import com.ramyhd.ramyalastora.classes.responses.leagues.Data;
import com.ramyhd.ramyalastora.classes.responses.leagues.LeagueBody;
import com.ramyhd.ramyalastora.classes.responses.leagues.LeagueData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.listeners.OnItemClickListener4;
import com.ramyhd.ramyalastora.listeners.PaginationScrollListener;
import com.ramyhd.ramyalastora.retrofit.APIClient;
import com.ramyhd.ramyalastora.retrofit.APIInterface;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramyhd.ramyalastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;
import static com.ramyhd.ramyalastora.RamiFragments.ScorersFragment.PLAYER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaguesFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyLeaguesPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<LeagueData> notificationList = new ArrayList<>();
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    //pagination
    private static int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    AppSharedPreferences appSharedPreferences;
    private int userId;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private EditText etSearch;
    private int type;

    public LeaguesFragment() {
        // Required empty public constructor
    }

    public LeaguesFragment newInstance(String param1) {
        LeaguesFragment fragment = new LeaguesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leagues, container, false);

        try {
            ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
            ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
            ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        }

        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);
        etSearch = view.findViewById(R.id.search);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_leagues_banner), view);

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
        adapter = new MyLeaguesPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_leagues_inter));

        adapter.setOnClickListener(new OnItemClickListener4() {
            @Override
            public void onItemClick(LeagueData item) {
                if (getArguments() != null){
                    Log.d(Constants.Log+"league", "(getArguments() != null)");
                    String scorersOrPTeams = getArguments().getString(ARG_PARAM1);
                    if (scorersOrPTeams.equals(Constants.scorers)){
                        PLAYER_ID = 0;
                        Log.d(Constants.Log+"league", "(scorersOrPTeams.equals(Constants.scorers))");
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, ScorersFragment.
                                newInstance(item.getLeaguesActive().get(0).getId(), 1, 1));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else if (scorersOrPTeams.equals(Constants.pTeams)){
                        Log.d(Constants.Log+"league", "(scorersOrPTeams.equals(Constants.pTeams))");
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        //type == 1 from leagues fragment to view banner ads;
                        fragmentTransaction.replace(R.id.nav_host_fragment, SortParticipatingTeamsFragment.
                                newInstance(1,1, item.getTitle(), item.getLeaguesActive().get(0).getId(), 0, 0));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
            }
        });

        userId = appSharedPreferences.readInteger(Constants.userid);

        loadFirstPage(type = 1, "");

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                etSearch.setText("");
                loadFirstPage(type, "");

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
                            loadNextPage(type, etSearch.getText().toString());
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

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    try {
                        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((RamiMain) getActivity()).badgeandCheckedDrawer();
                        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                        ((RamiMain) getActivity()).checkMainFragmentOnDrawer();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((FromNotification) getActivity()).badgeandCheckedDrawer();
                        ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
                        ((FromNotification) getActivity()).checkMainFragmentOnDrawer();
                    }
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    getParentFragmentManager().popBackStack();//back one fragment
                    return true;
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equalsIgnoreCase("")) {
                    loadFirstPage(type = 2, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")){
                    loadFirstPage(type = 1, "");
                }
            }
        });
        //TODO ///////////End back/////////////

        //TODO //////////// end pagination code /////////////////////////////////////////////
        /*/////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
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

        String leagueName[] = {
                "الدوري الإسباني", "الدوري الإنجليزي","الدوري الإيطالي", "الدوري الألماني","دوري أبطال أوروبا", "الدوري الفرنسي","الدوري السعودي", "دوري نجوم قطر","الدوري الأوروبي", "دوري نجوم قطر","الدوري الألماني", "الدوري الفرنسي"
        };

        String sort[] = {"2","4","10","5","2","9","1","4","2","8","3","7"};


        for (int i = 0; i < imagesLeagues.length; i++) {
            League league = new League();
            leagueList.add(league);
        }

        Log.d(Constants.Log+"cases", leagueList.size()+"");

        adapter = new MyLeaguesAdapter(leagueList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener4() {
            FragmentTransaction fragmentTransaction = null;
            @Override
            public void onItemClick(League item) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, TeamsSortFragment.newInstance(item.getTitle()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        /////////////////////////////////////////////////////////////////*/

        return view;
    }

    private Call<LeagueBody> callTopRatedMoviesApi(int type, String searchText) {
        Call<LeagueBody> leagueBodyCall = null;
        Log.d(Constants.Log + "user_id", "user id = " + userId);
        if (getContext() != null) {
            if (type == 1) {//get leagues without search
                leagueBodyCall = apiInterface.getLeagues(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        currentPage);
            }else if (type == 2){//search leagues
                leagueBodyCall = apiInterface.leagueSearch(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        searchText,
                        currentPage);
            }
        }
        return leagueBodyCall;
    }

    private ArrayList<LeagueData> fetchResults(Response<LeagueBody> response) {
        LeagueBody LeagueBody = response.body();
        Data data = LeagueBody.getData();
        return data.getData();
    }

    private void loadFirstPage(int type, String searchText) {
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
        callTopRatedMoviesApi(type, searchText).enqueue(new Callback<LeagueBody>() {
            @Override
            public void onResponse(@NotNull Call<LeagueBody> call, @NotNull Response<LeagueBody> response) {

                LeagueBody resource = response.body();
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
                    notificationList = resource.getData().getData();
                    TOTAL_PAGES = resource.getData().getLastPage();
                    Log.d(Constants.Log + "size", "size = " + notificationList.size() + "");
                    Collections.sort(notificationList, LeagueData.BY_SORTING_ALPHABETICAL);
                    adapter.addAll(notificationList);
                    if (notificationList.size() == 0) {
                        Log.d(Constants.Log + "size", "notificationList.size() == 0 = " + notificationList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                        adapter.clear();
                    } else {
                        Log.d(Constants.Log + "size", "notificationList.size() != 0 = " + notificationList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }
                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }else if (response.code() == 404){
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    adapter.clear();
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
            public void onFailure(Call<LeagueBody> call, Throwable t) {
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

    public void loadNextPage(int type, String searchText) {

        Log.d(Constants.Log, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(type, searchText).enqueue(new Callback<LeagueBody>() {
            @Override
            public void onResponse(Call<LeagueBody> call, Response<LeagueBody> response) {

                LeagueBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "LeagueBody", " | Code = " + code);

                if (response.code() == 200) {

                    notificationList = fetchResults(response);
                    Log.d(Constants.Log + "sdata", adapter.getItemCount() + " fetch");

                    if (isLoading) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        adapter.addAll(notificationList);
                    }

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                } else {

                }
            }

            @Override
            public void onFailure(Call<LeagueBody> call, Throwable t) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (BACK_FRAGMENTS > 0) {
            try {
                ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_league_sort, "", 0);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_league_sort, "", 0);
            }
            BACK_FRAGMENTS = 0;
        }
    }

}
