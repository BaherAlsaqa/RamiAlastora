package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.adapters.MyFavoritesPaginationAdapter;
import com.ramialastora.ramialastora.classes.responses.BodyData;
import com.ramialastora.ramialastora.classes.responses.DataPaginate;
import com.ramialastora.ramialastora.classes.responses.objects.Favorite;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener6;
import com.ramialastora.ramialastora.listeners.PaginationScrollListener;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteItemsFragment extends Fragment {

    private MyFavoritesPaginationAdapter adapter;
    private ArrayList<Favorite> favoriteList = new ArrayList<>();
    private ArrayList<Favorite> favoriteListSearch = new ArrayList<>();
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
    private EditText etSearch;
    public static int TYPE;
    String[] str = {"", "", "", ""};
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private RecyclerView mRecyclerView;
    private boolean refresh = false;

    public FavoriteItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_items, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        etSearch = view.findViewById(R.id.search);
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
        adapter = new MyFavoritesPaginationAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener6() {
            @Override
            public void onItemClick(Favorite item) {
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        TYPE = 1;
        loadFirstPage("");

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                etSearch.setText("");
                loadFirstPage("");
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
                            loadNextPage(etSearch.getText().toString());
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equalsIgnoreCase("")) {
                    TYPE = 2;
                    loadFirstPage(s.toString());
                }
                /*//                searchFilter(s.toString());
                Log.d(Constants.Log+"sdata", "onTextChanged = "+count);*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(Constants.Log + "sdata", "afterTextChanged = " + s.length());
                if (s.toString().equalsIgnoreCase("")) {
                    TYPE = 1;
                    if (!refresh) {
                        loadFirstPage("");
                    }else{
                        refresh = false;
                    }
                }
                /*if (s.toString().equals("")) {
                    favoriteList.clear();
                    adapter.clear();
                    favoriteListSearch.clear();
                    currentPage = PAGE_START;
                    isLoading = false;
                    isLastPage = false;
                }*/
            }
        });

        return view;
    }

    public void searchFilter(String TextFilter) {
        ArrayList<Favorite> fetchfavoriteList = new ArrayList<>();
        Log.e(Constants.Log + "sdata", favoriteList.size() + " |" + TextFilter + "| ");
        int textLength = TextFilter.length();
        for (int i = 0; i < favoriteList.size(); i++) {
            String resultNamelist = favoriteList.get(i).getName().trim();
            if (!TextFilter.equals("")) {
                int xx = 0;
                for (String ss : TextFilter.split(" ", 2)) {
                    str[xx] = ss;
                    xx++;
                }
                if (textLength <= resultNamelist.length()) {
                    if (resultNamelist.toLowerCase(Locale.getDefault()).contains(str[0].toLowerCase()) & resultNamelist.toLowerCase(Locale.getDefault()).contains(str[1].toLowerCase())) {
                        favoriteListSearch.add(favoriteList.get(i));
                        Log.e(Constants.Log + "sdata", "after added to contains" + favoriteListSearch.size() + "");
                    }
                }
            } else {
                favoriteListSearch.add(favoriteList.get(i));
                Log.e(Constants.Log + "sdata", "(TextFilter.equals()" + favoriteListSearch.size());
            }
        }
        Log.e(Constants.Log + "sdata", favoriteListSearch.size() + "before load next");
        if (textLength != 0) {
            if (currentPage < TOTAL_PAGES) {

                Log.e(Constants.Log + "sdata", "(currentPage < TOTAL_PAGES)");
                isLastPage = false;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            loadNextPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
                adapter.clear();
                adapter.addAll(favoriteListSearch);
                adapter.notifyDataSetChanged();
                Log.e(Constants.Log + "sdata", favoriteListSearch.size() + "after load next");
                searchFilter(TextFilter);
            } else if (currentPage == TOTAL_PAGES) {
                Log.e(Constants.Log + "sdata", "(currentPage == TOTAL_PAGES)");
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                //////////////////////
                adapter.clear();
                adapter.addAll(favoriteListSearch);
                adapter.notifyDataSetChanged();
                /////////////////////
                favoriteListSearch.clear();
                ///////////////
                Log.e(Constants.Log + "sdata", favoriteListSearch.size() + "after current == total and clear searchList");
            }
        }
    }

    private Call<BodyData> callTopRatedMoviesApi(String searchText) {
        int user_id = appSharedPreferences.readInteger(Constants.userid);
        if (TYPE == 1) {//to get teams without search
            return apiInterface.getAllTeamsToFavorite(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    user_id,
                    currentPage);
        } else {// else if (TYPE == 2) >>> get teams with search
            return apiInterface.teamSearch(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    searchText,
                    user_id,
                    currentPage);
        }
    }

    private ArrayList<Favorite> fetchResults(Response<BodyData> response) {
        BodyData BodyData = response.body();
        DataPaginate homeItemsObject = BodyData.getDataPaginate();
        return homeItemsObject.getData();
    }

    private void loadFirstPage(String searchText) {
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
        if (!isNetworkConnected()) {
            swiperefresh.setRefreshing(false);
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.hide();
        }
        callTopRatedMoviesApi(searchText).enqueue(new Callback<BodyData>() {
            @Override
            public void onResponse(@NotNull Call<BodyData> call, @NotNull Response<BodyData> response) {

                BodyData resource = response.body();
                Log.d(Constants.Log + "res", "onResponse");
                if (response.code() == 200) {
                    indicatorView.hide();
                    String status = resource.isStatus();
                    int code = response.code();
                    Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                    Log.d(Constants.Log + "res", "if (response.code() == 200)");

//                    progress.setVisibility(View.INVISIBLE);
                    swiperefresh.setRefreshing(false);
                    adapter.clear();
                    favoriteList = resource.getDataPaginate().getData();
                    TOTAL_PAGES = resource.getDataPaginate().getLastPage();
                    Log.d(Constants.Log, "size = " + favoriteList.size() + "");
                    adapter.addAll(favoriteList);
                    if (favoriteList.size() == 0) {
                        emptyData.setVisibility(View.VISIBLE);
                        adapter.clear();
                    }
                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                } else if (response.code() == 404) {
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    adapter.clear();
                    indicatorView.hide();
                    swiperefresh.setRefreshing(false);

                } else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200) and (response.code() == 404)");
                    indicatorView.hide();
                    error.setVisibility(View.VISIBLE);
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

    public void loadNextPage(String searchText) {

        Log.d(Constants.Log, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(searchText).enqueue(new Callback<BodyData>() {
            @Override
            public void onResponse(Call<BodyData> call, Response<BodyData> response) {

                BodyData resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "BodyData", " | Code = " + code);

                if (response.code() == 200) {

                    favoriteList = fetchResults(response);
                    Log.d(Constants.Log + "sdata", adapter.getItemCount() + " fetch");

                    if (isLoading) {
                        try {
                            adapter.removeLoadingFooter();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isLoading = false;
                        adapter.addAll(favoriteList);
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
