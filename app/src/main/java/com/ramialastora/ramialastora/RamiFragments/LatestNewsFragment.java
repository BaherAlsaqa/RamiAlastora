package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.adapters.MyNewsPaginationAdapter;
import com.ramialastora.ramialastora.adapters.MyTeamsPaginationAdapter;
import com.ramialastora.ramialastora.classes.responses.news.Data;
import com.ramialastora.ramialastora.classes.responses.news.NewsBody;
import com.ramialastora.ramialastora.classes.responses.news.NewsData;
import com.ramialastora.ramialastora.classes.responses.teams.TData;
import com.ramialastora.ramialastora.classes.responses.teams.TeamsBody;
import com.ramialastora.ramialastora.classes.responses.teams.TeamsData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener;
import com.ramialastora.ramialastora.listeners.OnItemClickListener1;
import com.ramialastora.ramialastora.listeners.PaginationScrollListener;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestNewsFragment extends Fragment {

    private View view;
    private ArrayList<TeamsData> teamsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyTeamsPaginationAdapter teamsAdapter;
    private ArrayList<NewsData> newsList = new ArrayList<>();
    private RecyclerView teamsRecyclerView;
    private MyNewsPaginationAdapter newsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager teamsLinearLayoutManager;
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
    private int newsType = 1;
    private int teamId;
    private NewsData newsData;

    public LatestNewsFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onPause() {
        super.onPause();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
    }*/

    public LatestNewsFragment newInstance(NewsData newsData) {
        LatestNewsFragment fragment = new LatestNewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.newsData, newsData);
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
        view = inflater.inflate(R.layout.fragment_latest_news, container, false);

        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);

        mRecyclerView = view.findViewById(R.id.newsrecyclerview);
        teamsRecyclerView = view.findViewById(R.id.teamsrecyclerview);
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

        newsAdapter.setOnClickListener(new OnItemClickListener1() {
            @Override
            public void onItemClick(NewsData item) {
                openFragmentDetails(item);
            }
        });

        userId = appSharedPreferences.readInteger(Constants.userid);

//        loadFirstPage();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                teamsLoadFirstPage();
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

        //TODO /////Start teams data ///////
        teamsLinearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        teamsAdapter = new MyTeamsPaginationAdapter(view.getContext());
        teamsRecyclerView.setLayoutManager(teamsLinearLayoutManager);
        teamsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamsRecyclerView.setAdapter(teamsAdapter);

        teamsAdapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(TeamsData item) {
                newsType = 2;
                teamId = item.getTeamId();
                loadFirstPage();
            }
        });

        teamsLoadFirstPage();

        teamsRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(Constants.Log + "addOnScroll", "addOnScrollListener");

                teamsisLoading = true;
                teamsCurrentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(Constants.Log + "addOnScroll", "addOnScrollListener");

                        try {
                            teamsLoadNextPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return teams_TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return teamsisLastPage;
            }

            @Override
            public boolean isLoading() {
                return teamsisLoading;
            }
        });
        //TODO //////////// End teams data ////////////

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                    ((RamiMain) getActivity()).badgeandCheckedDrawer();
                    ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                    ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                    ((RamiMain) getActivity()).checkMainFragmentOnDrawer();
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();//back one fragment
                    return true;
                }
                return false;
            }
        });
        //TODO ///////////End back/////////////

        /*/////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.teamsrecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        int images[] = {
                R.drawable.team3,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team3,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team3,
                R.drawable.team1,
                R.drawable.team2,
        };

        for (int i = 0; i < images.length; i++) {
            Teams home = new Teams(images[i]);
            teamsList.add(home);
        }

        adapter = new MyTeamsAdapter(teamsList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Teams item) {
                Toast.makeText(getContext(), item.getImage()+"", Toast.LENGTH_SHORT).show();
            }
        });
        /////////////////////////////////////////////////////////////////*/

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
        if (BACK_FRAGMENTS > 0) {
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_latest_news, "", 0);
            Log.d(Constants.Log + "menu", "onCreateOptionsMenu");
            BACK_FRAGMENTS = 0;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void openFragmentDetails(NewsData newsData){
        BACK_FRAGMENTS = 1;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new NewsDetailsFragment().newInstance(newsData));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private Call<NewsBody> callTopRatedMoviesApi(int type) {
        Call<NewsBody> news = null;
        Log.d(Constants.Log + "user_id", "user id = " + userId);
            if (getContext() != null) {
                if (type == 1) {//get all news
                    news = apiInterface.getNews(getString(R.string.api_key),
                            getString(R.string.api_username),
                            getString(R.string.api_password),
                            currentPage);
                } else if (type == 2) {//get news by team id
                    news = apiInterface.getNews(getString(R.string.api_key),
                            getString(R.string.api_username),
                            getString(R.string.api_password),
                            teamId,
                            currentPage);
                }
            }
        return news;
    }

    private Call<TeamsBody> teamsCallTopRatedMoviesApi() {
        Call<TeamsBody> teams = null;
        Log.d(Constants.Log + "user_id", "user id = " + userId);
        if (getContext() != null) {
            teams = apiInterface.getTeams(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    userId,
                    teamsCurrentPage);
        }
        return teams;
    }

    private ArrayList<NewsData> fetchResults(Response<NewsBody> response) {
        NewsBody NewsBody = response.body();
        Data homeItemsObject = NewsBody.getData();
        return homeItemsObject.getData();
    }

    private ArrayList<TeamsData> teamsFetchResults(Response<TeamsBody> response) {
        TeamsBody teamsBody = response.body();
        TData teamsItemsObject = teamsBody.getData();
        return teamsItemsObject.getData();
    }

    private void loadFirstPage() {
        Log.d(Constants.Log, "loadFirstPage");
        ///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////
        indicatorView.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (!isNetworkConnected()) {
            swiperefresh.setRefreshing(false);
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(View.GONE);
        }
        callTopRatedMoviesApi(newsType).enqueue(new Callback<NewsBody>() {
            @Override
            public void onResponse(@NotNull Call<NewsBody> call, @NotNull Response<NewsBody> response) {

                NewsBody resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    indicatorView.setVisibility(View.GONE);
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

                }else if (response.code() == 404){
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.setVisibility(View.GONE);
                    swiperefresh.setRefreshing(false);
                }else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    indicatorView.setVisibility(View.GONE);
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
                indicatorView.setVisibility(View.GONE);
            }
        });
    }

    public void loadNextPage() {

        Log.d(Constants.Log, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(newsType).enqueue(new Callback<NewsBody>() {
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

    private void teamsLoadFirstPage() {
        Log.d(Constants.Log, "loadFirstPage");
        ///////////////TODO pagination settings//////////////
        teamsisLoading = false;
        teamsisLastPage = false;
        teamsCurrentPage = teams_PAGE_START;
        ////////////////////////////
        teamsCallTopRatedMoviesApi().enqueue(new Callback<TeamsBody>() {
            @Override
            public void onResponse(@NotNull Call<TeamsBody> call, @NotNull Response<TeamsBody> response) {

                TeamsBody resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    try {
                        loadFirstPage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean status = resource.getStatus();
                    int code = response.code();
                    Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    Log.d(Constants.Log + "res", "if (response.code() == 200)");

//                    progress.setVisibility(View.INVISIBLE);
                    teamsAdapter.clear();
                    teamsList = resource.getData().getData();
                    teams_TOTAL_PAGES = resource.getData().getLastPage();
                    Log.d(Constants.Log + "size", "size = " + newsList.size() + "");
//                    Collections.sort(teamsList, TeamsData.Comparators.favorite);
                    teamsAdapter.addAll(teamsList);
                    if (newsList.size() == 0) {
                        Log.d(Constants.Log + "size", "newsList.size() == 0 = " + newsList.size() + "");
                    } else {
                        Log.d(Constants.Log + "size", "newsList.size() != 0 = " + newsList.size() + "");
                    }
                    if (teamsCurrentPage < teams_TOTAL_PAGES) teamsAdapter.addLoadingFooter();
                    else teamsisLastPage = true;

                } else if (response.code() == 404){
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    teamsAdapter.clear();
                }else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                }


            }

            @Override
            public void onFailure(Call<TeamsBody> call, Throwable t) {
                Log.d(Constants.Log + "55", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.Log + "res", "onFailure = " + t.getMessage() + "");
                call.cancel();
            }
        });
    }

    public void teamsLoadNextPage() {

        Log.d(Constants.Log, "loadNextPage: " + teamsCurrentPage);

        teamsCallTopRatedMoviesApi().enqueue(new Callback<TeamsBody>() {
            @Override
            public void onResponse(Call<TeamsBody> call, Response<TeamsBody> response) {

                TeamsBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "NewsBody", " | Code = " + code);

                if (response.code() == 200) {

                    teamsList = teamsFetchResults(response);
                    Log.d(Constants.Log + "sdata", teamsAdapter.getItemCount() + " fetch");

                    if (teamsisLoading) {
                        teamsAdapter.removeLoadingFooter();
                        teamsisLoading = false;
//                        Collections.sort(teamsList, TeamsData.Comparators.favorite);
                        teamsAdapter.addAll(teamsList);
                    }

                    if (teamsCurrentPage != teams_TOTAL_PAGES) teamsAdapter.addLoadingFooter();
                    else teamsisLastPage = true;
                } else {

                }
            }

            @Override
            public void onFailure(Call<TeamsBody> call, Throwable t) {
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
