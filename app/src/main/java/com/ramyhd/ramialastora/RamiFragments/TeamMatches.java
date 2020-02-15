package com.ramyhd.ramialastora.RamiFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.adapters.MyMatchesAdapter;
import com.ramyhd.ramialastora.classes.Match;
import com.ramyhd.ramialastora.interfaces.Constants;

import java.util.ArrayList;
import java.util.List;

public class TeamMatches extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private MyMatchesAdapter adapter;
    private List<Match> matchList = new ArrayList<>();

    public TeamMatches() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_today_matches, container, false);

        /////////////////////////////add data to recyclerview/////////////////////////////
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        int imagesTeam1[] = {
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2
        };
        int imagesTeam2[] = {
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1,
                R.drawable.team2,
                R.drawable.team1
        };

        String teamName1[] = {
                "ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام"
        };

        String teamName2[] = {
                "ليستر سيتي", "توتنهام", "ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي", "توتنهام","ليستر سيتي","توتنهام"
        };

        String time[] = {"37","15","50","60","55","25","20","45","54","65","47","48"};

        int resultTeam1[] = {2,4,0,5,2,2,1,4,1,3,3,0};

        int resultTeam2[] = {1,0,0,2,2,4,2,3,1,2,2,1};

        // 1 = underway, 2 = comming, 0 = ended .
        String matchStatus[] = {
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.underway),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.comming),
                getString(R.string.ended),
                getString(R.string.ended),
                getString(R.string.ended)};


        for (int i = 0; i < imagesTeam1.length; i++) {
            Match match = new Match(imagesTeam1[i], imagesTeam2[i], teamName1[i], teamName2[i], time[i], resultTeam1[i], resultTeam2[i], matchStatus[i]);
            matchList.add(match);
        }

        Log.d(Constants.Log+"cases", matchList.size()+"");

        adapter = new MyMatchesAdapter(matchList, view.getContext());
        mRecyclerView.setAdapter(adapter);
        /////////////////////////////////////////////////////////////////

        return view;
    }
}
