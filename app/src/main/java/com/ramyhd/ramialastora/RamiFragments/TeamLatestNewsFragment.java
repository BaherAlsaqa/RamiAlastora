package com.ramyhd.ramialastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.InterstitialAd;
import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.adapters.MyNewsPaginationAdapter;
import com.ramyhd.ramialastora.admob.MobileAdsInterface;
import com.ramyhd.ramialastora.classes.responses.news.Data;
import com.ramyhd.ramialastora.classes.responses.news.NewsBody;
import com.ramyhd.ramialastora.classes.responses.news.NewsData;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.listeners.OnItemClickListener1;
import com.ramyhd.ramialastora.listeners.PaginationScrollListener;
import com.ramyhd.ramialastora.retrofit.APIClient;
import com.ramyhd.ramialastora.retrofit.APIInterface;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramyhd.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamLatestNewsFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private ArrayList<NewsData> newsList = new ArrayList<>();
    private MyNewsPaginationAdapter newsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private APIInterface apiInterface;
    private SwipeRefreshLayout swiperefresh;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    //News pagination
    private static int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    //Teams pagination
    private static int teams_PAGE_START = 1;
    private boolean teamsisLoading = false;
    private boolean teamsisLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int teams_TOTAL_PAGES = 2;
    private int teamsCurrentPage = teams_PAGE_START;
    private AppSharedPreferences appSharedPreferences;
    private int userId;
    private int type;
    private int teamId1;
    private int teamId2;
    private int leagueId;
    private NewsData newsData;

    public TeamLatestNewsFragment() {
        // Required empty public constructor
    }

    public static TeamLatestNewsFragment newInstance(int type, int teamId1, int teamId2, int leagueId) {
        TeamLatestNewsFragment fragment = new TeamLatestNewsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.type, type);
        args.putInt(Constants.teamId1, teamId1);
        args.putInt(Constants.teamId2, teamId2);
        args.putInt(Constants.leagueId, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setHasOptionsMenu(true);

        if (getArguments() != null){
            type = getArguments().getInt(Constants.type);
            teamId1 = getArguments().getInt(Constants.teamId1);
            teamId2 = getArguments().getInt(Constants.teamId2);
            leagueId = getArguments().getInt(Constants.leagueId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_news_team, container, false);

        /*((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_latest_news, "", 0);*/

        mRecyclerView = view.findViewById(R.id.newsrecyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(getContext());

        //TODO //////////////////// Start pagination code and settings////////////////////////////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ///////////// end settings ///////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));
        } else {
            appSharedPreferences = new AppSharedPreferences(getContext());
        }
        //TODO /////Start news data ///////
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        newsAdapter = new MyNewsPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(newsAdapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(newsAdapter);

        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_team_latest_news_inter));

        newsAdapter.setOnClickListener(new OnItemClickListener1() {
            @Override
            public void onItemClick(NewsData item) {
                openFragmentDetails(item);

                MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
            }
        });

        userId = appSharedPreferences.readInteger(Constants.userid);

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

        //TODO //////////// End pagination code /////////////////////////////////////////////
        //TODO /////End news data ///////

        /*/////////////////////////////add data to recyclerview/////////////////////////////
        newsRecyclerView = view.findViewById(R.id.newsrecyclerview);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int newsimages[] = {
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
                R.drawable.news_image,
        };

        String times[] = {"منذ 6 دقائق","منذ 6 دقائق","منذ 7 دقائق",
                "منذ 9 دقائق","منذ 15 دقيقة","من 1 ساعة","منذ 5 ساعات",
                "منذ 10 ساعات","منذ 1 يوم"};

        String title[] = {"تشكيلة الهلال للكلاسيكو أمام الإتحاد","تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                "تشكيلة الهلال للكلاسيكو أمام الإتحاد","تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                "تشكيلة الهلال للكلاسيكو أمام الإتحاد","تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                "تشكيلة الهلال للكلاسيكو أمام الإتحاد","تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                "تشكيلة الهلال للكلاسيكو أمام الإتحاد"};

        for (int i = 0; i < newsimages.length; i++) {
            News news = new News(newsimages[i], times[i], title[i]);
            newsList.add(news);
        }

        newsAdapter = new MyNewsAdapter(newsList, view.getContext());
        newsRecyclerView.setAdapter(newsAdapter);

        newsAdapter.setOnClickListener(new OnItemClickListener1() {
            FragmentTransaction fragmentTransaction = null;
            @Override
            public void onItemClick(News item) {

                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new NewsDetailsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        /////////////////////////////////////////////////////////////////*/

        return view;
    }

    public void openFragmentDetails(NewsData newsData) {
        BACK_FRAGMENTS = 1;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new NewsDetailsFragment().newInstance(newsData, "0"));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private Call<NewsBody> callTopRatedMoviesApi() {
        Call<NewsBody> news = null;
        Log.d(Constants.Log + "user_id", "user id = " + userId);
        if (getContext() != null) {
            if (type == 1) {
                // team details news
                news = apiInterface.getNews(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        teamId1,
                        currentPage);
            }else if (type == 2){
                // match news
                news = apiInterface.getMatchNews(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        teamId1,
                        teamId2,
                        currentPage);
            }else if (type == 3){
                // league details news
                news = apiInterface.getLeagueNews(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        leagueId,
                        currentPage);
            }
        }
        return news;
    }

    private ArrayList<NewsData> fetchResults(Response<NewsBody> response) {
        NewsBody NewsBody = response.body();
        Data homeItemsObject = NewsBody.getData();
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
        callTopRatedMoviesApi().enqueue(new Callback<NewsBody>() {
            @Override
            public void onResponse(@NotNull Call<NewsBody> call, @NotNull Response<NewsBody> response) {

                NewsBody resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    indicatorView.hide();
                    boolean status = resource.getStatus();
                    int code = response.code();
                    Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    Log.d(Constants.Log + "res", "if (response.code() == 200)");

//                    progress.setVisibility(View.INVISIBLE);
                    swiperefresh.setRefreshing(false);
                    newsAdapter.clear();
                    newsList = resource.getData().getData();
                    TOTAL_PAGES = resource.getData().getLastPage();
                    Log.d(Constants.Log + "size", "size = " + newsList.size() + "");
                    newsAdapter.addAll(newsList);
                    if (newsList.size() == 0) {
                        Log.d(Constants.Log + "size", "newsList.size() == 0 = " + newsList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "newsList.size() != 0 = " + newsList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }
                    if (currentPage < TOTAL_PAGES) newsAdapter.addLoadingFooter();
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
            public void onFailure(Call<NewsBody> call, Throwable t) {
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

        callTopRatedMoviesApi().enqueue(new Callback<NewsBody>() {
            @Override
            public void onResponse(Call<NewsBody> call, Response<NewsBody> response) {

                NewsBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "NewsBody", " | Code = " + code);

                if (response.code() == 200) {

                    newsList = fetchResults(response);
                    Log.d(Constants.Log + "sdata", newsAdapter.getItemCount() + " fetch");

                    if (isLoading) {
                        newsAdapter.removeLoadingFooter();
                        isLoading = false;
                        newsAdapter.addAll(newsList);
                    }

                    if (currentPage != TOTAL_PAGES) newsAdapter.addLoadingFooter();
                    else isLastPage = true;
                } else {

                }
            }

            @Override
            public void onFailure(Call<NewsBody> call, Throwable t) {
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
