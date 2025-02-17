package com.ramyhd.ramyalastora.RamiFragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.RamiActivities.FromNotification;
import com.ramyhd.ramyalastora.RamiActivities.RamiMain;
import com.ramyhd.ramyalastora.admob.MobileAdsInterface;
import com.ramyhd.ramyalastora.classes.responses.matches.MatchData;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchDetailsFragment extends Fragment {

    private TabLayout tabLayout;
    private MatchData matchData;
    private ConstraintLayout constraintLayout5;
    private ImageView leagueImage, team1Image, team2Image;
    private TextView leagueName, playground, teamName1, teamName2, time, firstTeam, secondTeam, dash, remainingTime, statusDetails;
    private AppSharedPreferences appSharedPreferences;
    private FrameLayout underwayFramLayout;
    public static int BACK_FRAGMENTS;

    public static MatchDetailsFragment newInstance(MatchData matchData) {
        MatchDetailsFragment fragment = new MatchDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.matchdata, matchData);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.Log+"oncreate1", "oncreate1");
        setHasOptionsMenu(true);

    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_details, container, false);

        try {
            ((RamiMain) Objects.requireNonNull(getActivity())).changeToolbarBackground(R.color.white);
            RamiMain.setLightStatusBar(view, getActivity());
            ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu_dark);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) Objects.requireNonNull(getActivity())).changeToolbarBackground(R.color.white);
            FromNotification.setLightStatusBar(view, getActivity());
            ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu_dark);
        }

        tabLayout = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        constraintLayout5 = view.findViewById(R.id.constraintLayout5);
        leagueImage = view.findViewById(R.id.leagueimage);
        team1Image = view.findViewById(R.id.team1image);
        team2Image = view.findViewById(R.id.team2image);
        leagueName = view.findViewById(R.id.leaguename);
        playground = view.findViewById(R.id.playground);
        teamName1 = view.findViewById(R.id.teamname1);
        teamName2 = view.findViewById(R.id.teamname2);
        time = view.findViewById(R.id.time);
        firstTeam = view.findViewById(R.id.firstteam);
        secondTeam = view.findViewById(R.id.secondteam);
        dash = view.findViewById(R.id.dash);
        remainingTime = view.findViewById(R.id.remainingtime);
        underwayFramLayout = view.findViewById(R.id.underwayframlayout);
        statusDetails = view.findViewById(R.id.statusdetails);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_match_details_banner), view);

        if (getArguments() != null) {
            matchData = getArguments().getParcelable(Constants.matchdata);
            assert matchData != null;
            if (matchData.getLeagueActive() != null) {
                if (matchData.getLeagueActive().getLeague() != null) {
                    Picasso.get().load(Constants.imageBaseURL + matchData.getLeagueActive().getLeague().getImage())
                            .placeholder(R.drawable.leagues_teams_holder)
                            .resize(100, 100)
                            .into(leagueImage);
                }
            }

            Picasso.get().load(Constants.imageBaseURL + matchData.getFirstTeam().getLogo())
                    .placeholder(R.drawable.leagues_teams_holder)
                    .resize(400, 400)
                    .into(team1Image);
            Picasso.get().load(Constants.imageBaseURL + matchData.getSecondTeam().getLogo())
                    .placeholder(R.drawable.leagues_teams_holder)
                    .resize(400, 400)
                    .into(team2Image);
            leagueName.setText(matchData.getLeagueActive().getLeague().getTitle());
            playground.setText(matchData.getPlayground().getName());
            teamName1.setText(matchData.getFirstTeam().getName());
            teamName2.setText(matchData.getSecondTeam().getName());

        /*//////////////////////calc time//////////////////////
//                      SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
        Date date1 = new Date();
        String time = matchData.getStartDate();
        Log.d(Constants.Log+"date1", "date = "+time);
        Date date = getDate(time);
        long mills =  date1.getTime() - date.getTime();
        Log.d(Constants.Log+"Data2", ""+date1.getTime());
        Log.d(Constants.Log+"Data3", ""+date.getTime());
        int hours = (int) (mills/(1000 * 60 * 60));
        int mins = (int) (mills/(1000*60)) % 60;
        int days = hours/24;

        String timeResult = hours + ":" + mins; // updated value every1 second*/

            firstTeam.setVisibility(View.GONE);
            secondTeam.setVisibility(View.GONE);
            dash.setVisibility(View.GONE);

            if (matchData.getStatus() == 1) {
                // comming
                statusDetails.setVisibility(View.GONE);
                underwayFramLayout.setVisibility(View.GONE);
                time.setText(matchData.getStartTime());
                if(matchData.getRemainingTime() != null) {
                    String[] time = matchData.getRemainingTime().split(" ", 2);
                    remainingTime.setText(getString(R.string.remaining) + " " + time[0].trim() + " " + time[1].trim());
                }
            } else if (matchData.getStatus() == 0) {
                // underway
                statusDetails.setVisibility(View.VISIBLE);
                statusDetails.setText(matchData.getStatusDetails());
                underwayFramLayout.setVisibility(View.VISIBLE);
                time.setText(getString(R.string.underway));
                time.setTextColor(Color.parseColor("#00B8A9"));

                String[] time = matchData.getElapsedTime().split(" ", 2);
                remainingTime.setText(getString(R.string.elapsed) + " " + time[0].trim() + " " + time[1].trim());
                remainingTime.setTextColor(Color.parseColor("#557AE0"));
            } else if (matchData.getStatus() == 2) {
                // ended
                statusDetails.setVisibility(View.GONE);
                underwayFramLayout.setVisibility(View.GONE);
                firstTeam.setVisibility(View.VISIBLE);
                secondTeam.setVisibility(View.VISIBLE);
                dash.setVisibility(View.VISIBLE);
                time.setVisibility(View.GONE);
                if (matchData.getResult() != null) {
                    String[] result = matchData.getResult().split("-", 2);
                    String r2 = result[0];
                    String r1 = result[1];
//                    String finalResult = r1+" - "+r2;
                    firstTeam.setText(r1);
                    secondTeam.setText(r2);
                } else {
//                    String finalResult = "0"+" - "+"0";
                    firstTeam.setText("0");
                    secondTeam.setText("0");
                }
                remainingTime.setText(matchData.getStartDate());
            }
            //TODO ///////////////////pass data to details match////////////////
            appSharedPreferences.writeString(Constants.league, matchData.getLeagueActive().getLeague().getTitle());
            appSharedPreferences.writeString(Constants.tour, matchData.getTour());
            appSharedPreferences.writeString(Constants.playground, matchData.getPlayground().getName());
            if (matchData.getGetChannel() != null) {
                appSharedPreferences.writeString(Constants.channel, matchData.getGetChannel().getName());
            }
            if (matchData.getGetChannel1() != null) {
                appSharedPreferences.writeString(Constants.channel1, matchData.getGetChannel1().getName());
            }
            if (matchData.getSportCommentator() != null) {
                appSharedPreferences.writeString(Constants.voiceCommentator, matchData.getSportCommentator().getName());
            }
            if (matchData.getSportCommentator1() != null) {
                appSharedPreferences.writeString(Constants.voiceCommentator1, matchData.getSportCommentator1().getName());
            }
            appSharedPreferences.writeString(Constants.matchTime, matchData.getStartTime());
            appSharedPreferences.writeString(Constants.matchDate, matchData.getStartDate());
            if (matchData.getLiveVideoUrl() != null)
                appSharedPreferences.writeString(Constants.liveURL, matchData.getLiveVideoUrl());
            if (matchData.getMatchVideoUrl() != null)
                appSharedPreferences.writeString(Constants.urlVideo, matchData.getMatchVideoUrl());
            appSharedPreferences.writeInteger(Constants.isShowLiveVideo, matchData.getIsShowLiveVideo());
            appSharedPreferences.writeString(Constants.teamName1, matchData.getFirstTeam().getName());
            appSharedPreferences.writeString(Constants.teamName2, matchData.getSecondTeam().getName());
        }
        constraintLayout5.setOnClickListener(v -> { });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabText();

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

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                String back = appSharedPreferences.readString(Constants.backFragmentCurrent);
                if (back.equalsIgnoreCase(Constants.leagueF)){
                    FragmentTransaction fragmentTransaction = null;
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack(null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);//Clear from back stack
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new MainFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    try {
                        ((RamiMain) Objects.requireNonNull(getActivity())).menuBackIcon(
                                R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((RamiMain) getActivity()).badgeandCheckedDrawer();
                        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((FromNotification) Objects.requireNonNull(getActivity())).menuBackIcon(
                                R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((FromNotification) getActivity()).badgeandCheckedDrawer();
                        ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
                    }
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    getParentFragmentManager().popBackStack();//back one fragment
                }
                return true;
            }
            return false;
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
        String[] exams = {getString(R.string.details), getString(R.string.matchgoals), getString(R.string.news),
                getString(R.string.sort), getString(R.string.menu_scorer)};
        for (int i = 0; i <= 4; i++) {
            @SuppressLint("InflateParams") LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_header, null);
            TextView tabTitle = tabOne.findViewById(R.id.title);
            tabTitle.setText(exams[i]);
            if (i == 0)
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabOne);
        }
    }

    private void setupViewPager(ViewPager viewPager1) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFrag(new DetailsMatchDetailsFragment(), getString(R.string.details));
        adapter.addFrag(MatchGoalsVideosFragment.newInstance(matchData.getId()), getString(R.string.matchgoals));
        // type 1 = team details news , but type 2 = match news, but type 3 = league details news;
        adapter.addFrag(TeamLatestNewsFragment.newInstance(2, matchData.getFirstTeam().getId(),
                matchData.getSecondTeam().getId(), 0), getString(R.string.news));
        adapter.addFrag(SortParticipatingTeamsFragment.newInstance(0, 0, matchData.getLeagueActive().getLeague().getTitle(),
                matchData.getLeagueActive().getId(), matchData.getFirstTeam().getId(),
                matchData.getSecondTeam().getId()), getString(R.string.sort));
        appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.scorersF);
        adapter.addFrag(MatchScorersFragment.
                newInstance(matchData.getId(), matchData.getLeagueActive().getId()), getString(R.string.menu_scorer));
        viewPager1.setAdapter(adapter);
        viewPager1.invalidate();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NotNull
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
                ((RamiMain) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_back_dark, 0, "", 2);
            }
            Log.d(Constants.Log + "menu11", "onCreateOptionsMenu");
            BACK_FRAGMENTS = 0;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back_dark:

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
