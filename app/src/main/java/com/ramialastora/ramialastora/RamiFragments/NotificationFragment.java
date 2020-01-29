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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.InterstitialAd;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.adapters.MyNotificationPaginationAdapter;
import com.ramialastora.ramialastora.admob.MobileAdsInterface;
import com.ramialastora.ramialastora.classes.responses.notifications.Data;
import com.ramialastora.ramialastora.classes.responses.notifications.NotificationBody;
import com.ramialastora.ramialastora.classes.responses.notifications.NotificationData;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener8;
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
public class NotificationFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyNotificationPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NotificationData> notificationList = new ArrayList<>();
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

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(getContext());

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_notification_banner), view);

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
        adapter = new MyNotificationPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_notification_inter));

        adapter.setOnClickListener(new OnItemClickListener8() {
            @Override
            public void onItemClick(NotificationData item) {
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

        //TODO //////////// end pagination code /////////////////////////////////////////////

        /*/////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int type[] = {
                1,2,1,2,1,2,1,2,1,2,1,2
        };

        String title[] = {
                "تيتي : نيمار سعيد ولا شيئ يستحق العناء", "فاتي : يحصل على الجنسية الإسبانية","هدف (بايرن ميونخ) كولن 0 : 1 بايرن ميونخ","تيتي : نيمار سعيد ولا شيئ يستحق العناء", "فاتي : يحصل على الجنسية الإسبانية","هدف (بايرن ميونخ) كولن 0 : 1 بايرن ميونخ","تيتي : نيمار سعيد ولا شيئ يستحق العناء", "فاتي : يحصل على الجنسية الإسبانية","هدف (بايرن ميونخ) كولن 0 : 1 بايرن ميونخ","تيتي : نيمار سعيد ولا شيئ يستحق العناء", "فاتي : يحصل على الجنسية الإسبانية","هدف (بايرن ميونخ) كولن 0 : 1 بايرن ميونخ"
        };

        String time[] = {
                "منذ 3 دقائق","منذ 30 دقيقة","منذ ساعة","منذ يوم","منذ يوم","منذ 2 يوم","منذ 3 ايام","من 3 ايام","منذ 4 ايام","منذ 5 ايام","منذ 5 ايام","منذ 5 ايام",
        };


        for (int i = 0; i < type.length; i++) {
            Notification notification = new Notification(title[i], time[i], type[i]);
            notificationList.add(notification);
        }

        Log.d(Constants.Log+"cases", notificationList.size()+"");

        adapter = new MyNotificationsAdapter(notificationList, view.getContext());
        mRecyclerView.setAdapter(adapter);
        /////////////////////////////////////////////////////////////////*/

        return view;
    }

    private Call<NotificationBody> callTopRatedMoviesApi() {
        Call<NotificationBody> matches = null;
        Log.d(Constants.Log + "user_id", "user id = " + userId);
        if (getContext() != null) {
            matches = apiInterface.getNotifications(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    userId,
                    currentPage);
        }
        return matches;
    }

    private ArrayList<NotificationData> fetchResults(Response<NotificationBody> response) {
        NotificationBody NotificationBody = response.body();
        Data homeItemsObject = NotificationBody.getData();
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
        callTopRatedMoviesApi().enqueue(new Callback<NotificationBody>() {
            @Override
            public void onResponse(@NotNull Call<NotificationBody> call, @NotNull Response<NotificationBody> response) {

                NotificationBody resource = response.body();
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
                    adapter.addAll(notificationList);
                    if (notificationList.size() == 0) {
                        Log.d(Constants.Log + "size", "notificationList.size() == 0 = " + notificationList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "notificationList.size() != 0 = " + notificationList.size() + "");
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
            public void onFailure(Call<NotificationBody> call, Throwable t) {
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

        callTopRatedMoviesApi().enqueue(new Callback<NotificationBody>() {
            @Override
            public void onResponse(Call<NotificationBody> call, Response<NotificationBody> response) {

                NotificationBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "NotificationBody", " | Code = " + code);

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
            public void onFailure(Call<NotificationBody> call, Throwable t) {
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
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_notification, "", 0);
            BACK_FRAGMENTS = 0;
        }
    }
}
