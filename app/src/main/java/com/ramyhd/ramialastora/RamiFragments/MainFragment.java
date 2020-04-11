package com.ramyhd.ramialastora.RamiFragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.RamiActivities.FromNotification;
import com.ramyhd.ramialastora.RamiActivities.RamiMain;
import com.ramyhd.ramialastora.admob.MobileAdsInterface;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConstraintLayout clChangeDate;
    public static int tagTabs;
    private TodayMatches todayMatches;
    private TextView matchDate;
    MainFragment mainFragment;
    private String[] tabsText;
    private String[] tabsDates;
    private TextView btnReturnDate;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


    public MainFragment() {
        // Required empty public constructor
    }

    public MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment getInstance() {
        return mainFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(Constants.Log + "1", "onAttach");
        try {
            ((RamiMain) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_search
                    , R.string.app_name, "", 1);
            ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
            ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) Objects.requireNonNull(getActivity())).menuBackIcon(R.menu.toolbar_search
                    , R.string.app_name, "", 1);
            ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
            ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        clChangeDate = view.findViewById(R.id.clchangedate);
        matchDate = view.findViewById(R.id.matchdate);
        btnReturnDate = view.findViewById(R.id.returndate);

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.rami_main_banner), view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
//            matchDate.setText(mParam1);
        }
        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.main_fragment_inter));
        btnReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReturnDate.setVisibility(View.GONE);
                getParentFragmentManager().popBackStackImmediate();
//                MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
            }
        });

        clChangeDate.setOnClickListener(v -> {
            try {
                ((RamiMain) Objects.requireNonNull(getActivity())).expandCloseSheet(0);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) Objects.requireNonNull(getActivity())).expandCloseSheet(0);
            }
            MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
        });

        tabLayout.setupWithViewPager(viewPager);
        setupTabText();

        todayMatches = new TodayMatches();
        /////////////////////////////////////////////////////////////////
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout view2 = (LinearLayout) tab.getCustomView();
                assert view2 != null;
                TextView tabTitle = view2.findViewById(R.id.title);
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                //////////////////////////////////////////////////////////////////
                tagTabs = tab.getPosition();
                /*String s = getChildFragmentManager().getFragments().size()+"";
                Log.d(Constants.Log+"fragsize", s);
                TodayMatches yourFragmentinstance = (TodayMatches) getChildFragmentManager().getFragments().get(tab.getPosition());
                yourFragmentinstance.callLoadFirstPage();*/

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

        return view;
    }

    public static Date getDate(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void setupTabText() {
        if (getArguments() != null) {
            Log.d(Constants.Log+"arg", "(getArguments() != null)");
            mParam1 = getArguments().getString(ARG_PARAM1);
            String[] date = mParam1.split("/", 2);
            Log.d(Constants.Log+"param", "[0] = "+date[0]+"[1] = "+date[1]);
            String[] dmy = date[0].split("-", 3);
            Log.d(Constants.Log+"param", "[0]mdy = "+dmy[0]+"[1]mdy = "+dmy[1]+"[2]mdy = "+dmy[2]);
            //TODO//////////////////////Select from CalendarView/////////////////////////
            GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(dmy[2].trim()), Integer.parseInt(dmy[1].trim())-1, Integer.parseInt(dmy[0].trim()));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                    new Locale("en","US"));
            String matchesDate = null;
            gc.add(Calendar.DATE, 0);
            matchesDate = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate);
            //TODO///////////////////Add date to sort////////
            int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
            String day = null;
            switch (dayOfWeek) {
                case 1:
                    day = "الاحد";
                    break;
                case 2:
                    day = "الاثنين";
                    break;
                case 3:
                    day = "الثلاثاء";
                    break;
                case 4:
                    day = "الاربعاء";
                    break;
                case 5:
                    day = "الخميس";
                    break;
                case 6:
                    day = "الجمعة";
                    break;
                case 7:
                    day = "السبت";
                    break;
            }
            String currentDateandDay = matchesDate+" / "+day;
            matchDate.setText(currentDateandDay);
            //TODO//////////////////////End//////////////////////
            //////////////
            String matchesDate1 = null;
            gc.add(Calendar.DATE, 1);
            matchesDate1 = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate1);
            //////////////
            String matchesDate2 = null;
            gc.add(Calendar.DATE, -2);
            matchesDate2 = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate2);
            //TODO/////////////////////////////////End//////////////////////////////////////
            tabsText = new String[]{matchesDate, matchesDate1, matchesDate2};
            tabsDates = new String[]{matchesDate, matchesDate1, matchesDate2};
            Log.d(Constants.Log+"tabs", matchesDate+matchesDate1+matchesDate2);
            //TODO//////////////////////Visiblity btnReturnSort//////////////////////////
            btnReturnDate.setVisibility(View.VISIBLE);
        }else{
            //TODO//////////////////////Select from Default Tabs/////////////////////////
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                    new Locale("en","US"));
            String matchesDate = null;
            gc.add(Calendar.DATE, 0);
            matchesDate = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate);
            //TODO///////////////////Add date to sort////////
            int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
            String day = null;
            switch (dayOfWeek) {
                case 1:
                    day = "الاحد";
                    break;
                case 2:
                    day = "الاثنين";
                    break;
                case 3:
                    day = "الثلاثاء";
                    break;
                case 4:
                    day = "الاربعاء";
                    break;
                case 5:
                    day = "الخميس";
                    break;
                case 6:
                    day = "الجمعة";
                    break;
                case 7:
                    day = "السبت";
                    break;
            }
            String currentDateandDay = matchesDate+" / "+day;
            matchDate.setText(currentDateandDay);
            btnReturnDate.setVisibility(View.GONE);
            //TODO////////////////////End//////////////////////
            //////////////
            String matchesDate1 = null;
            gc.add(Calendar.DATE, 1);
            matchesDate1 = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate1);
            //////////////
            String matchesDate2 = null;
            gc.add(Calendar.DATE, -2);
            matchesDate2 = df.format(gc.getTime());
            Log.d(Constants.Log + "posTab", "TodayDate = " + matchesDate2);
            //TODO///////////////////End/////////////////////
            Log.d(Constants.Log+"arg", "(getArguments() == null)");
            tabsText = new String[]{getString(R.string.todaymatches), getString(R.string.tomorrowmatches), getString(R.string.yesterdaymatches)};
            tabsDates = new String[]{matchesDate, matchesDate1, matchesDate2};
        }
        setupViewPager(viewPager);
        for (int i = 0; i <= 2; i++) {
            LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_header, null);
            TextView tabTitle = tabOne.findViewById(R.id.title);
            tabTitle.setText(tabsText[i]);
            if (i == 0)
                tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void setupViewPager(ViewPager viewPager1) {
        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));
        appSharedPreferences.writeString(Constants.backFragmentCurrent, Constants.mainF);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //in Type Param =  1 => Today Matches and send match date,,, 2 => Team Matches and send team id

        adapter.addFrag(TodayMatches.newInstance(1, tabsDates[0], 0, 0), getString(R.string.todaymatches));
        adapter.addFrag(new TomorrowMatches().newInstance(tabsDates[1]), getString(R.string.tomorrowmatches));
        adapter.addFrag(new YesterdayMatches().newInstance(tabsDates[2]), getString(R.string.yesterdaymatches));
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
