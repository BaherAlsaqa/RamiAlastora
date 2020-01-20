package com.ramialastora.ramialastora.RamiFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.classes.responses.UserResp;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.PLAYER_OR_TEAM_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailsFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String teamName, teamLogo, country;
    private int favoriteValue, teamId, leagueId;
    private ImageView ivTeamLogo;
    private TextView countryValue, favoriteText;
    private CheckBox favorite;
    private String leagueName;
    private ConstraintLayout constraintLayout5;
    private APIInterface apiInterface;
    private AppSharedPreferences appSharedPreferences;

    public static TeamDetailsFragment newInstance(int favoriteValue, int teamId, String teamName, String teamLogo, String country, String leagueName, int leagueId) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.favorite, favoriteValue);
        args.putInt(Constants.teamId, teamId);
        args.putString(Constants.teamName, teamName);
        args.putString(Constants.teamLogo, teamLogo);
        args.putString(Constants.country, country);
        args.putString(Constants.leagueName, leagueName);
        args.putInt(Constants.leagueId, leagueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            favoriteValue = getArguments().getInt(Constants.favorite);
            teamId = getArguments().getInt(Constants.teamId);
            teamName = getArguments().getString(Constants.teamName);
            teamLogo = getArguments().getString(Constants.teamLogo);
            country = getArguments().getString(Constants.country);
            leagueId = getArguments().getInt(Constants.leagueId);
            leagueName = getArguments().getString(Constants.leagueName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_team_details, container, false);

        PLAYER_OR_TEAM_NAME = teamName;

        ((RamiMain) Objects.requireNonNull(getActivity())).changeToolbarBackground(R.drawable.back_team_up);
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, teamName, 3);
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        ivTeamLogo = view.findViewById(R.id.teamlogo);
        countryValue = view.findViewById(R.id.country2);
        favorite = view.findViewById(R.id.favorite);
        favoriteText = view.findViewById(R.id.favoritetext);
        constraintLayout5 = view.findViewById(R.id.constraintLayout5);

        //Initialization
        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        int userId = appSharedPreferences.readInteger(Constants.userid);

        if (favoriteValue == 0){
            favorite.setChecked(false);
            favoriteText.setText(R.string.add_favorite);
        }else if (favoriteValue == 1){
            favorite.setChecked(true);
            favoriteText.setText(R.string.favorite);
        }

        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getActivity() != null) {
                    try {
                        if (!isChecked) {
                            addorRemoveFavorite(userId,
                                    teamId,
                                    0);//Remove Favorite
                        } else {

                            addorRemoveFavorite(userId,
                                    teamId,
                                    1);//Add Favorite
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        constraintLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Picasso.get().load(Constants.imageBaseURL+teamLogo)
                .placeholder(R.drawable.leagues_teams_holder)
                .into(ivTeamLogo);
        countryValue.setText(country);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabText();

        /////////////////////////////////////////////////////////////////
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout view2  = (LinearLayout) tab.getCustomView();
                TextView tabTitle = view2.findViewById(R.id.title);
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout view2  = (LinearLayout) tab.getCustomView();
                TextView tabTitle = view2.findViewById(R.id.title);
                tabTitle.setTextColor(getResources().getColor(R.color.text_gray1));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void addorRemoveFavorite(int user_id,int team_id, int type) {
        Log.d(Constants.Log+"addorRemove", "create fav team = "+team_id);
        Call<UserResp> call = null;
        call = apiInterface.addFavorite(
                getString(R.string.api_key),
                getString(R.string.api_username),
                getString(R.string.api_password),
                user_id,
                team_id,
                type);
        call.enqueue(new Callback<UserResp>() {
            @Override
            public void onResponse(Call<UserResp> call, Response<UserResp> response) {
                UserResp resource = response.body();
                Log.d(Constants.Log, "Code = " + response.code());
                if (response.code() == 201) {//created
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                        favoriteText.setText(R.string.favorite);
                        favorite.setChecked(true);
                    }
                } else if (response.code() == 202){//removed
                    if (resource != null) {
                        Boolean status = resource.getStatus();
                        String code = response.code() + "";
                        String error = resource.getError();
                        Log.d(Constants.Log, "Status = " + status + " | Code = " + code);
                        favoriteText.setText(R.string.add_favorite);
                        favorite.setChecked(false);
                    }
                } else{
                    if (type == 0) {
                        favorite.setChecked(true);
                    }else if (type == 1){
                        favorite.setChecked(false);
                    }
                    Log.d(Constants.Log, "create user error code = "+response.code());
                }


            }

            @Override
            public void onFailure(Call<UserResp> call, Throwable t) {
                if (t != null) {
                    Log.d(Constants.Log, "onFailure = " + t.getMessage());
                }
                call.cancel();
                if (type == 0) {
                    Log.d(Constants.Log+"fav", "(type == 0)");
                    favorite.setChecked(true);
                }else if (type == 1){
                    Log.d(Constants.Log+"fav", "(type == 1)");
                    favorite.setChecked(false);
                }
            }
        });
    }

    private void setupTabText() {
        String[] exams = {getString(R.string.matches), getString(R.string.menu_latest_news),
                          getString(R.string.sort), getString(R.string.players)};
        for (int i = 0; i <= 3; i++) {
            LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_header, null);
            TextView tabTitle = tabOne.findViewById(R.id.title);
            tabTitle.setText(exams[i]);
            if (i == 0)
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void setupViewPager(ViewPager viewPager1) {
        appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.teamF);
        TeamDetailsFragment.ViewPagerAdapter adapter = new TeamDetailsFragment.ViewPagerAdapter(getChildFragmentManager());
        //in Type Param =  1 => Today Matches and send match date,,, 2 => Team Matches and send team id
        // ,,, 3 => league matches send league id
        adapter.addFrag(TodayMatches.newInstance(2, "", teamId, 0), getString(R.string.matches));
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // type 1 = team details news , but type 2 = match news, but type 3 = league details news;
        adapter.addFrag(TeamLatestNewsFragment.newInstance(1, teamId, 0, 0), getString(R.string.menu_latest_news));
        adapter.addFrag(SortParicipatingTeamsFragment.newInstance(0, leagueName, leagueId, teamId, 0), getString(R.string.sort));
        adapter.addFrag(TeamPlayersFragment.newInstance(teamId, leagueId, 1), getString(R.string.players));
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

}
