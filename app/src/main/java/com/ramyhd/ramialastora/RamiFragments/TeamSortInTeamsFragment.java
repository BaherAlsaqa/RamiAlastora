package com.ramyhd.ramialastora.RamiFragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.adapters.MyTeamsSortAdapter;
import com.ramyhd.ramialastora.classes.TeamsSort;
import com.ramyhd.ramialastora.interfaces.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamSortInTeamsFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private MyTeamsSortAdapter adapter;
    private List<TeamsSort> leagueList = new ArrayList<>();
    private String mParam1;
    private static final String ARG_PARAM1 = "param1";

    public static TeamSortInTeamsFragment newInstance(String param1) {
        TeamSortInTeamsFragment fragment = new TeamSortInTeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sort_participating_teams, container, false);

        Log.e("teamsSortFragment", "teamsSortFragment :" + mParam1);

//        ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, 0, mParam1, 3);

        /////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int sort[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int imagesLeagues[] = {
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1,
                R.drawable.league_1
        };

        String teamsNames[] = {
                "بايرن ميونخ", "برشلونة", "ريال مدريد", "غرناطة", "ريال سويسداد", "اشبيلية", "اتليتكو مدريد", "فياريال", "ليفانتي", "أوساسونا", "خيتافي", "فالنسيا"
        };

        int played[] = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int won[] = {2, 3, 2, 3, 2, 1, 2, 3, 3, 3, 2, 2};
        int drew[] = {2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1};
        int loser[] = {1, 1, 1, 0, 1, 2, 1, 1, 0, 1, 1, 2};
        int scored[] = {6, 10, 9, 5, 2, 4, 4, 10, 5, 2, 3, 6};
        int ownNet[] = {3, 5, 4, 4, 2, 4, 2, 8, 1, 1, 2, 4};
        int rest[] = {3, 5, 5, 1, 0, 0, 2, 2, 4, 1, 1, 2};
        int points[] = {9, 10, 9, 11, 10, 6, 9, 10, 11, 10, 9, 6};


        for (int i = 0; i < imagesLeagues.length; i++) {
            TeamsSort teamsSort = new TeamsSort(sort[i], imagesLeagues[i], teamsNames[i], played[i], won[i], drew[i], loser[i], scored[i], ownNet[i], rest[i], points[i]);
            leagueList.add(teamsSort);
        }

        Log.d(Constants.Log + "cases", leagueList.size() + "");

        adapter = new MyTeamsSortAdapter(leagueList, view.getContext());
        mRecyclerView.setAdapter(adapter);

        /*adapter.setOnClickListener(new OnItemClickListener3() {
            FragmentTransaction fragmentTransaction = null;

            @Override
            public void onItemClick(TeamsSort item) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, TeamDetailsFragment.newInstance(item.getTeamName()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/
        /////////////////////////////////////////////////////////////////

        return view;
    }

}
