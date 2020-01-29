package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.adapters.MyRecommendedNewsAdapter;
import com.ramialastora.ramialastora.admob.MobileAdsInterface;
import com.ramialastora.ramialastora.classes.responses.news.NewsData;
import com.ramialastora.ramialastora.classes.responses.news.OneNewsBody;
import com.ramialastora.ramialastora.classes.responses.news.RelatedNews;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener2;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

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
public class NewsDetailsFragment extends Fragment {

    private View view;
    private ArrayList<RelatedNews> newsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyRecommendedNewsAdapter adapter;
    private Button newsShare, newsSource;
    private NewsData newsData;
    private TextView title, newsdetails;
    private ImageView newsimage2;
    private APIInterface apiInterface;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private InterstitialAd interstitialAd;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    public NewsDetailsFragment newInstance(NewsData newsData) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
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
        view = inflater.inflate(R.layout.fragment_news_details, container, false);

        ((RamiMain) getActivity()).changeToolbarBackground(R.color.white);
        ((RamiMain) getActivity()).setLightStatusBar(view, getActivity());
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu_dark);

        newsShare = view.findViewById(R.id.share);
        newsSource = view.findViewById(R.id.newssource);
        title = view.findViewById(R.id.title);
        newsimage2 = view.findViewById(R.id.newsimage2);
        newsdetails = view.findViewById(R.id.newsdetails);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);

        ////////////////////////////
        indicatorView.hide();
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        ////////////////////////////

        apiInterface = APIClient.getClient().create(APIInterface.class);

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_news_details_banner), view);

        interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_news_details_inter));

        if (getArguments() != null) {
            newsData = (NewsData) getArguments().getParcelable(Constants.newsData);
            title.setText(newsData.getTitle());
            Picasso.get().load(Constants.imageBaseURL + newsData.getImage())
                    .placeholder(R.drawable.news_holder)
                    .into(newsimage2);
            newsdetails.setText(Html.fromHtml(newsData.getDetails()));

            newsShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.app_name) + " " + "\n\n" + " " +
                                    getString(R.string.sharemessage) + " " + "\n\n: " +
                                    "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                    }
                    MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                }
            });

            newsSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), getString(R.string.news_source), Toast.LENGTH_SHORT).show();

                    MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                }
            });

            /////////////////////////////add data to recyclerview/////////////////////////////
            mRecyclerView = view.findViewById(R.id.recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            /*int images[] = {
                    R.drawable.salah,
                    R.drawable.salah1,
                    R.drawable.salah,
                    R.drawable.salah1,
                    R.drawable.salah,
                    R.drawable.salah1,
                    R.drawable.salah,
                    R.drawable.salah1,
                    R.drawable.salah,
            };
            String title[] = {"تشكيلة الهلال للكلاسيكو أمام الإتحاد", "تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                    "تشكيلة الهلال للكلاسيكو أمام الإتحاد", "تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                    "تشكيلة الهلال للكلاسيكو أمام الإتحاد", "تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                    "تشكيلة الهلال للكلاسيكو أمام الإتحاد", "تشكيلة الهلال للكلاسيكو أمام الإتحاد",
                    "تشكيلة الهلال للكلاسيكو أمام الإتحاد"};

            for (int i = 0; i < images.length; i++) {
                RecommendedNews news = new RecommendedNews(images[i], title[i]);
                newsList.add(news);
            }*/
            newsList.addAll(newsData.getRelatedNews());
            adapter = new MyRecommendedNewsAdapter(newsList, view.getContext());
            mRecyclerView.setAdapter(adapter);

            adapter.setOnClickListener(new OnItemClickListener2() {
                @Override
                public void onItemClick(RelatedNews item) {
                    loadOneNews(item.getId());

                    MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                }
            });
            /////////////////////////////////////////////////////////////////

        }

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


        return view;
    }

    private Call<OneNewsBody> callTopRatedMoviesApi(int newsId) {
        Call<OneNewsBody> news = null;
        Log.d(Constants.Log + "news_id", "news id = " + newsId);
        if (getContext() != null) {
            news = apiInterface.getOneNews(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    newsId);
        }
        return news;
    }

    public void loadOneNews(int newsId) {
        ////////////////////////////
        indicatorView.show();
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        ////////////////////////////
        if (!isNetworkConnected()) {
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.hide();
        }
        callTopRatedMoviesApi(newsId).enqueue(new Callback<OneNewsBody>() {
            @Override
            public void onResponse(Call<OneNewsBody> call, Response<OneNewsBody> response) {

                OneNewsBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "OneNewsBody", " | Code = " + code);

                if (response.code() == 200) {
                    indicatorView.hide();
                    assert resource != null;
                    newsList = resource.getData().getRelatedNews();
                    newsData = resource.getData();
                    FragmentTransaction fragmentTransaction = null;
//                    getChildFragmentManager().popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    }else{
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    }
                    fragmentTransaction.replace(R.id.nav_host_fragment, new NewsDetailsFragment().newInstance(newsData));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    if (newsList.size() == 0) {
                        Log.d(Constants.Log + "size", "newsList.size() == 0 = " + newsList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "newsList.size() != 0 = " + newsList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }

                }else if (response.code() == 404){
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.hide();
                }else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    indicatorView.hide();
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<OneNewsBody> call, Throwable t) {
                Log.d(Constants.Log + "55", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.Log + "res", "onFailure = " + t.getMessage() + "");
                if (t instanceof IOException) {
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                }
                call.cancel();
                indicatorView.hide();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
        if (BACK_FRAGMENTS > 0) {
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, R.string.createddatevalue, "", 2);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back_dark:

                break;
        }

        return super.onOptionsItemSelected(item);

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
