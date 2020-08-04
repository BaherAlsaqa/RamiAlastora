package com.ramyhd.ramyalastora.RamiFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.RamiActivities.FromNotification;
import com.ramyhd.ramyalastora.RamiActivities.RamiMain;
import com.ramyhd.ramyalastora.admob.MobileAdsInterface;
import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ramyhd.ramyalastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppSharedPreferences appSharedPreferences;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onPause() {
        super.onPause();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);

        //Initialization
        appSharedPreferences = new AppSharedPreferences(requireContext());

        MobileAdsInterface.bannerAds(getContext(), getString(R.string.fragment_favorite_banner), view);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabText();

        try {
            ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.fav_toolbar);
            ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.fav_toolbar);
            ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
        }

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

        //TODO ///// Start back one fragment ////////
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.d(Constants.Log+"back", "if( keyCode == KeyEvent.KEYCODE_BACK ) || details match details");
                    try {
                        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((RamiMain) getActivity()).badgeandCheckedDrawer();
                        ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                        ((RamiMain) getActivity()).checkMainFragmentOnDrawer();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                        ((FromNotification) getActivity()).badgeandCheckedDrawer();
                        ((FromNotification) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                        ((FromNotification) getActivity()).drawerIcon(R.drawable.ic_menu);
                        ((FromNotification) getActivity()).checkMainFragmentOnDrawer();
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

    private void setupTabText() {
        String[] exams = {getString(R.string.menu_favotire), getString(R.string.all_teams)};
        for (int i = 0; i <= 1; i++) {
            LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_header, null);
            TextView tabTitle = tabOne.findViewById(R.id.title);
            tabTitle.setText(exams[i]);

            boolean firstopen = appSharedPreferences.readBoolean(Constants.firstopenfavorite);
            if (!firstopen){
                Log.d(Constants.Log + "first", "(!!!firstopen)");
                if (i == 1) {
                    Log.d(Constants.Log + "first", "(i == 1)");
                    tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                    appSharedPreferences.writeBoolean(Constants.firstopenfavorite, true);
                }
            }else {
                Log.d(Constants.Log + "first", "(firstopen)");
                if (i == 0) {
                    Log.d(Constants.Log + "first", "(i == 0)");
                    tabTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void setupViewPager(ViewPager viewPager1) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new FavoriteTeamsItemsFragment(), getString(R.string.menu_favotire));
        adapter.addFrag(new FavoriteItemsFragment(), getString(R.string.all_teams));
        viewPager1.setAdapter(adapter);
        viewPager1.invalidate();
        boolean firstopen = appSharedPreferences.readBoolean(Constants.firstopenfavorite);
        if (!firstopen) {
            viewPager1.setCurrentItem(1);
        }
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
        super.onCreateOptionsMenu(menu, inflater);
        if (BACK_FRAGMENTS > 0) {
            try {
                ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_favotire, "", 0);
            } catch (Exception e) {
                e.printStackTrace();
                ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_favotire, "", 0);
            }
            BACK_FRAGMENTS = 0;
        }
    }
}
