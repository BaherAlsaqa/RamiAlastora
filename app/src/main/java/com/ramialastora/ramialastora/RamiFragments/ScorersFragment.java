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
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.adapters.MyScorersPaginationAdapter;
import com.ramialastora.ramialastora.classes.responses.scorers.Data;
import com.ramialastora.ramialastora.classes.responses.scorers.ScorersBody;
import com.ramialastora.ramialastora.classes.responses.scorers.ScorersData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener7;
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

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.BACK_FROM_SCORERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScorersFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyScorersPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ScorersData> scorersList = new ArrayList<>();
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
    private int userId, type;
    private int leagueId;
    private int backButton;
    public static int PLAYER_ID;
    private FrameLayout flLeaguesList;
    private ConstraintLayout cLayout;

    public ScorersFragment() {
        // Required empty public constructor
    }

    public static ScorersFragment newInstance(int leagueId, int backButton, int type) {
        ScorersFragment fragment = new ScorersFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.leagueId, leagueId);
        args.putInt(Constants.backButton, backButton);
        args.putInt(Constants.type, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        BACK_FROM_SCORERS = 1;
    }

    /*@Override
    public void onPause() {
        super.onPause();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scorers, container, false);

        flLeaguesList = view.findViewById(R.id.flleagueslist);
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

        if (getArguments() != null) {
            type = getArguments().getInt(Constants.type);
            if (type == 2) {//from player details
                flLeaguesList.setVisibility(View.VISIBLE);
            } else if (type == 1) {//from league details
                flLeaguesList.setVisibility(View.GONE);
            }
        }

        //Initializing
        appSharedPreferences = new AppSharedPreferences(getContext());
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdLoader adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();



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
        adapter = new MyScorersPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener7() {
            @Override
            public void onItemClick(ScorersData item) {
                PLAYER_ID = item.getPlayerId();
                appSharedPreferences.writeInteger(Constants.playerId, item.getPlayerId());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, PlayerDetails.newInstance(leagueId, item.getPlayerId(), 0));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

        //TODO //////////// end pagination code /////////////////////////////////////////////

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_league_sort, "", 1);
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
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int sorts[] = {
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
            Scorers scorers = new Scorers(sorts[i], images[i], names[i], centers[i], yellow[i], red[i], goals[i], trueP[i], falseP[i]);
            scorersList.add(scorers);
        }

        Log.d(Constants.Log+"cases", scorersList.size()+"");

        adapter = new MyScorersAdapter(scorersList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener7() {
            FragmentTransaction fragmentTransaction;
            @Override
            public void onItemClick(Scorers item) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, PlayerDetails.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        /////////////////////////////////////////////////////////////////*/

        return view;
    }

    private Call<ScorersBody> callTopRatedMoviesApi() {
        Call<ScorersBody> scorersBodyCall = null;
        if (getArguments() != null) {
            leagueId = getArguments().getInt(Constants.leagueId);
            Log.d(Constants.Log + "user_id", "user id = " + userId);
            if (getContext() != null) {
                scorersBodyCall = apiInterface.getScorers(getString(R.string.api_key),
                        getString(R.string.api_username),
                        getString(R.string.api_password),
                        leagueId,
                        currentPage);
            }
        }
        return scorersBodyCall;
    }

    private ArrayList<ScorersData> fetchResults(Response<ScorersBody> response) {
        ScorersBody ScorersBody = response.body();
        Data data = ScorersBody.getData();
        return data.getData();
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
        callTopRatedMoviesApi().enqueue(new Callback<ScorersBody>() {
            @Override
            public void onResponse(@NotNull Call<ScorersBody> call, @NotNull Response<ScorersBody> response) {

                ScorersBody resource = response.body();
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
                    scorersList = resource.getData().getData();
                    TOTAL_PAGES = resource.getData().getLastPage();
                    Log.d(Constants.Log + "size", "size = " + scorersList.size() + "");
                    adapter.addAll(scorersList);
                    if (scorersList.size() == 0) {
                        Log.d(Constants.Log + "size", "scorersList.size() == 0 = " + scorersList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "scorersList.size() != 0 = " + scorersList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }
                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

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
            public void onFailure(Call<ScorersBody> call, Throwable t) {
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

        callTopRatedMoviesApi().enqueue(new Callback<ScorersBody>() {
            @Override
            public void onResponse(Call<ScorersBody> call, Response<ScorersBody> response) {

                ScorersBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "ScorersBody", " | Code = " + code);

                if (response.code() == 200) {

                    scorersList = fetchResults(response);
                    Log.d(Constants.Log + "sdata", adapter.getItemCount() + " fetch");

                    if (isLoading) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        adapter.addAll(scorersList);
                    }

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                } else {

                }
            }

            @Override
            public void onFailure(Call<ScorersBody> call, Throwable t) {
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
        if (getArguments() != null) {
            backButton = getArguments().getInt(Constants.backButton);
            if (backButton != 0) {
                Log.d(Constants.Log+"omenu", "onCreateOptionsMenu");
                ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_scorer, "", 0);
                backButton = 0;
            }
        }
    }
}
