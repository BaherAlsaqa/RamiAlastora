package com.ramyhd.ramyalastora.RamiFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ramyhd.ramyalastora.R;
import com.ramyhd.ramyalastora.RamiActivities.FromNotification;
import com.ramyhd.ramyalastora.RamiActivities.RamiMain;
import com.ramyhd.ramyalastora.interfaces.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private View view;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onPause() {
        super.onPause();
        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
        ((RamiMain) getActivity()).badgeandCheckedDrawer();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        try {
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_settings, "", 0);
        } catch (Exception e) {
            e.printStackTrace();
            ((FromNotification) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.menu_settings, "", 0);
        }

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

}
