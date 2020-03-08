package com.ramyhd.ramialastora.RamiFragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.InterstitialAd;
import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.RamiActivities.OutLink;
import com.ramyhd.ramialastora.admob.MobileAdsInterface;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsMatchDetailsFragment extends Fragment {

    private View view;
    private TextView league, tour, playground, channel, voiceCommentator, matchTime, matchDate, matchTeam1Team2;
    private AppSharedPreferences appSharedPreferences;
    private ConstraintLayout clURLVideo, live;

    public DetailsMatchDetailsFragment() {
        // Required empty public constructor
    }

    public MatchDetailsFragment newInstance(String league, String tour, String playground, String channel,
                                            String channel1, String voiceCommentator, String voiceCommentator1, String matchTime,
                                            String matchDate, String liveURL, String urlVideo) {
        MatchDetailsFragment fragment = new MatchDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.league, league);
        args.putString(Constants.tour, tour);
        args.putString(Constants.playground, playground);
        args.putString(Constants.channel, channel);
        args.putString(Constants.channel1, channel1);
        args.putString(Constants.voiceCommentator, voiceCommentator);
        args.putString(Constants.voiceCommentator1, voiceCommentator1);
        args.putString(Constants.matchTime, matchTime);
        args.putString(Constants.matchDate, matchDate);
        args.putString(Constants.urlVideo, urlVideo);
        args.putString(Constants.liveURL, liveURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_details_match_details, container, false);

        league = view.findViewById(R.id.league);
        tour = view.findViewById(R.id.tour);
        playground = view.findViewById(R.id.playground);
        channel = view.findViewById(R.id.channel);
        voiceCommentator = view.findViewById(R.id.voice_commentator);
        matchTime = view.findViewById(R.id.matchtime);
        matchDate = view.findViewById(R.id.matchdate);
        clURLVideo = view.findViewById(R.id.clurlvideo);
        matchTeam1Team2 = view.findViewById(R.id.matchteam1team2);
        live = view.findViewById(R.id.live);

        //Initializing
        appSharedPreferences = new AppSharedPreferences(getContext());

        String leagueValue = appSharedPreferences.readString(Constants.league);
        String tourValue = appSharedPreferences.readString(Constants.tour);
        String playgroundValue = appSharedPreferences.readString(Constants.playground);
        String channelValue = appSharedPreferences.readString(Constants.channel);
        String channel1Value = appSharedPreferences.readString(Constants.channel1);
        String voiceCommentatorValue = appSharedPreferences.readString(Constants.voiceCommentator);
        String voiceCommentator1Value = appSharedPreferences.readString(Constants.voiceCommentator1);
        String matchTimeValue = appSharedPreferences.readString(Constants.matchTime);
        String matchDateValue = appSharedPreferences.readString(Constants.matchDate);
        String urlLiveVideo = appSharedPreferences.readString(Constants.liveURL);
        String urlVideoValue = appSharedPreferences.readString(Constants.urlVideo);
        int isShowLiveVideo = appSharedPreferences.readInteger(Constants.isShowLiveVideo);
        String teamName1 = appSharedPreferences.readString(Constants.teamName1);
        String teamName2 = appSharedPreferences.readString(Constants.teamName2);

        league.setText(leagueValue);
        tour.setText(tourValue);
        playground.setText(playgroundValue);
        channel.setText(channelValue + " + " + channel1Value);
        voiceCommentator.setText(voiceCommentatorValue + " + " + voiceCommentator1Value);
        matchTime.setText(matchTimeValue);
        matchTeam1Team2.setText(getString(R.string.match)+" "+teamName1+" "+getString(R.string.vs)+" "+teamName2);
        //TODO//////////////////////Select from CalendarView/////////////////////////
        String[] startDate = matchDateValue.split("-", 3);
        GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(startDate[0].trim()), Integer.parseInt(startDate[1].trim()) - 1, Integer.parseInt(startDate[2].trim()));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en", "US"));
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
        String currentDateandDay = matchesDate + " / " + day;
        matchDate.setText(currentDateandDay);

        InterstitialAd interstitialAd =
                MobileAdsInterface.interstitialAds(getContext(), 1, getString(R.string.fragment_details_match_details_inter));

        if (urlLiveVideo != null) {
            if (!urlLiveVideo.equalsIgnoreCase("")) {
                if (isShowLiveVideo == 1) {
                    live.setVisibility(View.VISIBLE);

                    live.setOnClickListener(v -> {
                        startActivity(new Intent(getContext(), OutLink.class).putExtra(Constants.link, urlLiveVideo));
                        MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                    /*FragmentTransaction fragmentTransaction = null;
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, WebViewVideo.newInstance(urlLiveVideo));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLiveVideo));
                    startActivity(browserIntent);*/
                    });
                }else{
                    live.setVisibility(View.GONE);
                }
            } else {
                live.setVisibility(View.GONE);
            }
        }else{
            live.setVisibility(View.GONE);
        }
        if (urlVideoValue != null) {
            if (!urlVideoValue.equalsIgnoreCase("")) {
                    clURLVideo.setVisibility(View.VISIBLE);

                    clURLVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), OutLink.class).putExtra(Constants.link, urlVideoValue));

                            MobileAdsInterface.showInterstitialAd(interstitialAd, getContext());
                        /*FragmentTransaction fragmentTransaction = null;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, WebViewVideo.newInstance(urlVideoValue));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/
                        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideoValue));
                        startActivity(browserIntent);*/
                        }
                    });
            } else {
                clURLVideo.setVisibility(View.GONE);
            }
        }else{
            clURLVideo.setVisibility(View.GONE);
        }
        //TODO//////////////////////End//////////////////////

        return view;
    }

}
