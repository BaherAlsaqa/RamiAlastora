package com.ramyhd.ramialastora.RamiFragments;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.RamiActivities.FromNotification;
import com.ramyhd.ramialastora.RamiActivities.RamiMain;
import com.ramyhd.ramialastora.classes.responses.matches.MatchData;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ramyhd.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueDetailsFragment extends Fragment {

    private TabLayout tabLayout;
    private MatchData matchData;
    private ConstraintLayout constraintLayout5;
    private ImageView leagueImage;
    private TextView leagueName;
    private AppSharedPreferences appSharedPreferences;
    private String leagueImageValue, leagueTitle;
    private int leagueId, type;

    public static LeagueDetailsFragment newInstance(MatchData matchData, int type, String leagueImage, String leagueTitle, int leagueId) {
        LeagueDetailsFragment fragment = new LeagueDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.matchdata, matchData);
        args.putString(Constants.leagueImage, leagueImage);
        args.putString(Constants.leagueName, leagueTitle);
        args.putInt(Constants.leagueId, leagueId);
        args.putInt(Constants.type, type);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onPause() {
        super.onPause();
        Log.d(Constants.Log, "MatchDetailsFragment.onPause()");
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BACK_FRAGMENTS > 0) {
            Log.d(Constants.Log + "oncreate", "oncreate league details fragment");
            setHasOptionsMenu(true);
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_league_details, container, false);

        try {
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            ((RamiMain) getActivity()).changeToolbarBackground(R.color.white);
            ((RamiMain) getActivity()).setLightStatusBar(view, getActivity());
            ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu_dark);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            ((FromNotification) getActivity()).changeToolbarBackground(R.color.white);
            ((FromNotification) getActivity()).setLightStatusBar(view, getActivity());
            ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu_dark);
        }

        tabLayout = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        constraintLayout5 = view.findViewById(R.id.constraintLayout5);
        leagueImage = view.findViewById(R.id.leagueimage);
        leagueName = view.findViewById(R.id.leaguename);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(getContext());

        if (getArguments() != null) {
            leagueImageValue = getArguments().getString(Constants.leagueImage);
            leagueTitle = getArguments().getString(Constants.leagueName);
            leagueId = getArguments().getInt(Constants.leagueId);
            type = getArguments().getInt(Constants.type);

            if (type == 1) {//from match
                matchData = getArguments().getParcelable(Constants.matchdata);
                assert matchData != null;
                if (matchData.getLeagueActive() != null) {
                    if (matchData.getLeagueActive().getLeague() != null) {
                        Picasso.get().load(Constants.imageBaseURL + matchData.getLeagueActive().getLeague().getImage())
                                .placeholder(R.drawable.leagues_teams_holder)
                                .resize(400, 400)
                                .into(leagueImage);
                        leagueName.setText(matchData.getLeagueActive().getLeague().getTitle());
                    }
                }
            } else if (type == 2) {//from search
                Picasso.get().load(Constants.imageBaseURL + leagueImageValue)
                        .placeholder(R.drawable.leagues_teams_holder)
                        .resize(400, 400)
                        .into(leagueImage);
                leagueName.setText(leagueTitle);
            }
        }
        constraintLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabText();

        /////////////////////////////////////////////////////////////////
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout view2 = (LinearLayout) tab.getCustomView();
                TextView tabTitle = view2.findViewById(R.id.title);
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout view2 = (LinearLayout) tab.getCustomView();
                TextView tabTitle = view2.findViewById(R.id.title);
                tabTitle.setTextColor(getResources().getColor(R.color.text_gray1));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log + "back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    try {
                        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, "", 3);
                        ((RamiMain) getActivity()).badgeandCheckedDrawer();
                        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, "", 3);
                        ((FromNotification) getActivity()).badgeandCheckedDrawer();
                        ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
                    }
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    getParentFragmentManager().popBackStack();//back one fragment
                    return true;
                }
                return false;
            }
        });
        //TODO ///////////End back/////////////

        return view;
    }

    public static Date getDate(String date) {
        String[] date1 = date.split("-", 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(date1[0].trim()));
        cal.set(Calendar.MONTH, Integer.parseInt(date1[1].trim()));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date1[2].trim()));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void setupTabText() {
        String[] exams = {getString(R.string.matches), getString(R.string.teamsorting), getString(R.string.menu_scorer),
                getString(R.string.menu_latest_news)};
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
        appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.leagueF);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //in Type Param =  1 => Today Matches and send match date,,, 2 => Team Matches and send team id
        // ,,, 3 => league matches send league id
        if (type == 1) {//from match
            adapter.addFrag(TodayMatches.newInstance(3, "", 0
                    , matchData.getLeagueActive().getId()), getString(R.string.matches));
            //type == 1 from leagues fragment to view banner ads;
            adapter.addFrag(SortParticipatingTeamsFragment.newInstance(0,0
                    , matchData.getLeagueActive().getLeague().getTitle(),
                    matchData.getLeagueActive().getId(), 0, 0), getString(R.string.menu_latest_news));
            adapter.addFrag(leagueScorersFragment.
                    newInstance(matchData.getLeagueActive().getId(), 0, 1), getString(R.string.teamsorting));
            // type 1 = team details news , but type 2 = match news, but type 3 = league details news;
            adapter.addFrag(TeamLatestNewsFragment.newInstance(3, 0, 0
                    , matchData.getLeagueActive().getId()), getString(R.string.menu_scorer));
        }else if (type == 2){// from search
            adapter.addFrag(TodayMatches.newInstance(3, "", 0
                    , leagueId), getString(R.string.matches));
            adapter.addFrag(SortParticipatingTeamsFragment.newInstance(0, 0
                    , leagueTitle,
                    leagueId, 0, 0), getString(R.string.menu_latest_news));
            adapter.addFrag(leagueScorersFragment.
                    newInstance(leagueId, 0, 1), getString(R.string.teamsorting));
            // type 1 = team details news , but type 2 = match news, but type 3 = league details news;
            adapter.addFrag(TeamLatestNewsFragment.newInstance(3, 0, 0
                    , leagueId), getString(R.string.menu_scorer));

        }
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
        if (BACK_FRAGMENTS > 0) {
            try {
                ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            }
            Log.d(Constants.Log + "menu", "onCreateOptionsMenu");
            BACK_FRAGMENTS = 0;
        } else {
            try {
                ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, "", 3);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, "", 3);
            }
            Log.d(Constants.Log + "menu", "BACK_FRAGMENTS = 0");
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back_dark:
                Log.d(Constants.Log + "menu", "league details fragment | action_back_dark");
                break;
            case R.id.action_share:
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

}
