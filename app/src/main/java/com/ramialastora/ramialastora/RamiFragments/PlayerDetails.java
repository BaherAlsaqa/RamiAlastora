package com.ramialastora.ramialastora.RamiFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.admob.MobileAdsInterface;
import com.ramialastora.ramialastora.classes.responses.players_details.ParticipatingLeague;
import com.ramialastora.ramialastora.classes.responses.players_details.PlayerDetailsBody;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.PLAYER_OR_TEAM_NAME;

public class PlayerDetails extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView playerImage, teamImage1, teamImage2;
    private TextView playerNumber, playerName, playerAge, playerCenter;
    private int playerId, leagueId, type;
    private APIInterface apiInterface;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private AppSharedPreferences appSharedPreferences;
    private ConstraintLayout constraintLayout;

    public static PlayerDetails newInstance(int leagueId, int playerId, int type) {
        PlayerDetails fragment = new PlayerDetails();
        Bundle args = new Bundle();
        args.putInt(Constants.leagueId, leagueId);
        args.putInt(Constants.playerId, playerId);
        args.putInt(Constants.type, type);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onPause() {
        super.onPause();
        Log.d(Constants.Log, "PlayerDetails.onPause()");
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            playerId = getArguments().getInt(Constants.playerId);
            leagueId = getArguments().getInt(Constants.leagueId);
            type = getArguments().getInt(Constants.type);
        }
    }

    @SuppressLint("Assert")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_player_details, container, false);

        ((RamiMain) getActivity()).changeToolbarBackground(R.color.white);
        ((RamiMain) getActivity()).setLightStatusBar(view, getActivity());
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu_dark);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        playerImage = view.findViewById(R.id.playerimage);
        teamImage1 = view.findViewById(R.id.teamimage1);
        teamImage2 = view.findViewById(R.id.teamimage2);
        playerNumber = view.findViewById(R.id.playernumber);
        playerName = view.findViewById(R.id.playername);
        playerAge = view.findViewById(R.id.playerage);
        playerCenter = view.findViewById(R.id.playercenter);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);
        constraintLayout = view.findViewById(R.id.constraintlayout);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_player_details_banner), view);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loadPlayerDetails();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            assert false;
            Objects.requireNonNull(view).setFocusableInTouchMode(true);
            Objects.requireNonNull(view).requestFocus();

            Objects.requireNonNull(view).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        Log.d(Constants.Log, "if( keyCode == KeyEvent.KEYCODE_BACK )");
                        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((RamiMain) getActivity()).badgeandCheckedDrawer();
                        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStack();//back one fragment
                        return true;
                    }
                    return false;
                }
            });
        }
        return view;
    }

    private void setupTabText() {
        String[] exams = {getString(R.string.details), getString(R.string.matches),
                getString(R.string.sort)};
        for (int i = 0; i <= 2; i++) {
            LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_header, null);
            TextView tabTitle = tabOne.findViewById(R.id.title);
            tabTitle.setText(exams[i]);
            if (i == 0)
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabOne);
            } else {
                tabLayout.getTabAt(i).setCustomView(tabOne);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager1, int teamId) {
        if (type == 0) {// type = 1 with team details just
            appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.playerF);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new PlayerDetailsPlayerFragment(), getString(R.string.details));
        adapter.addFrag(TodayMatches.newInstance(2, "", teamId, 0), getString(R.string.matches));
        adapter.addFrag(leagueScorersFragment.newInstance(leagueId, 0, 2), getString(R.string.sort));
        viewPager1.setAdapter(adapter);
        viewPager1.invalidate();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back_dark:
                Log.d(Constants.Log, "case R.id.action_back_dark:");
                /*((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                ((RamiMain) getActivity()).badgeandCheckedDrawer();
                ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);*/
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
                break;
            case R.id.action_share:
                Log.d(Constants.Log, "case R.id.action_share:");
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
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private Call<PlayerDetailsBody> callTopRatedMoviesApi() {
        Call<PlayerDetailsBody> news = null;
        if (getContext() != null) {
            news = apiInterface.getPlayerDetailsInLeague(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    playerId,
                    leagueId);
        }
        return news;
    }

    private void loadPlayerDetails() {
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
        callTopRatedMoviesApi().enqueue(new Callback<PlayerDetailsBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PlayerDetailsBody> call, Response<PlayerDetailsBody> response) {

                PlayerDetailsBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "PlayerDetailsBody", " | Code = " + code);

                if (response.code() == 200) {
                    indicatorView.hide();
                    assert resource != null;

                    if (resource.getData().size() > 0) {
                        Log.d(Constants.Log + "size", "newsList.size() != 0 = " + resource.getData().size() + "");
                        emptyData.setVisibility(View.GONE);

                        Picasso.get().load(Constants.imageBaseURL + resource.getData().get(0).getImage()).into(playerImage);
                        if (resource.getData().get(0).getTeams().size() > 0) {
                            Picasso.get().load(Constants.imageBaseURL + resource.getData().get(0).getTeams().get(0).getLogo()).into(teamImage1);
                            teamImage2.setVisibility(View.GONE);
                        }
                        if (resource.getData().get(0).getTeams().size() > 1) {
                            Picasso.get().load(Constants.imageBaseURL + resource.getData().get(0).getTeams().get(1).getLogo()).into(teamImage2);
                            teamImage2.setVisibility(View.VISIBLE);
                        }
                        playerNumber.setText(resource.getData().get(0).getNumber() + "");
                        playerName.setText(resource.getData().get(0).getName());
                        PLAYER_OR_TEAM_NAME = resource.getData().get(0).getName();//TO BACK FROM MENU
//                    playerAge.setText(resource.getData().get(0).getDatebirth());
                        String[] age = resource.getData().get(0).getDatebirth().split("-", 3);
                        if (getActivity() != null)
                            playerAge.setText(getAge(Integer.parseInt(age[0]), Integer.parseInt(age[1]), Integer.parseInt(age[2])) + " " + getString(R.string.years));

                        playerCenter.setText(resource.getData().get(0).getPosition());
                        ArrayList<ParticipatingLeague> participatingLeagues1 = new ArrayList<>();
                        for (int x = 0; x < resource.getData().size(); x++) {
                            Log.d(Constants.Log + "3for", "x = " + x);
                            for (int y = 0; y < resource.getData().get(x).getTeams().size(); y++) {
                                Log.d(Constants.Log + "3for", "y = " + y
                                        + " participating league = " + resource.getData().get(x).getTeams().get(y).getParticipatingLeagues().size());
                                for (int z = 0; z < resource.getData().get(x).getTeams().get(y).getParticipatingLeagues().size(); z++) {
                                    Log.d(Constants.Log + "3for", "z = " + z);
                                    //TODO ////////add ParticipatingLeague to shared preferences array/////////
                                    participatingLeagues1.add(resource.getData().get(x).getTeams().get(y).getParticipatingLeagues().get(z));
                                    appSharedPreferences.writeArray(Constants.pLeaguesArray, participatingLeagues1);
                                }
                            }
                        }
                        //TODO /////////// start code to tabs layout view pager /////////////
                        if (getActivity() != null) {
                            setupViewPager(viewPager, resource.getData().get(0).getTeams().get(0).getId());
                            tabLayout.setupWithViewPager(viewPager);
                            setupTabText();
                        }

                        /////////////////////////////////////////////////////////////////
                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                LinearLayout view2 = (LinearLayout) tab.getCustomView();
                                assert view2 != null;
                                TextView tabTitle = view2.findViewById(R.id.title);
                                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {
                                LinearLayout view2 = (LinearLayout) tab.getCustomView();
                                assert view2 != null;
                                TextView tabTitle = view2.findViewById(R.id.title);
                                tabTitle.setTextColor(getResources().getColor(R.color.text_gray1));
                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });
                        //TODO ////////////// End code tabs layout view pager /////////
                    } else {
                        Log.d(Constants.Log + "size", "newsList.size() == 0 = " + resource.getData().size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 404) {
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.hide();
                } else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    indicatorView.hide();
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PlayerDetailsBody> call, Throwable t) {
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

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;

        return Integer.toString(ageInt);
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
