package com.ramyhd.ramialastora.RamiFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.classes.responses.players_details.ParticipatingLeague;
import com.ramyhd.ramialastora.classes.responses.players_details.PlayerDetailsBody;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.retrofit.APIClient;
import com.ramyhd.ramialastora.retrofit.APIInterface;
import com.ramyhd.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerDetailsPlayerFragment extends Fragment {

    private View view;
    private AppSharedPreferences appSharedPreferences;
    private ArrayList<ParticipatingLeague> sharedArrayList = null;
    private APIInterface apiInterface;
    private AVLoadingIndicatorView indicatorView;
    private SwipeRefreshLayout swipeRefreshLayout;
    View noInternet, emptyData, error;
    private TextView goals, goalsHisTeam, truePelanties, falsePelanties, yellowCard, redCard;
    private int playerId;
    private ParticipatingLeague participatingLeague;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_details_player, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));
        }else{
            appSharedPreferences = new AppSharedPreferences(getContext());
        }

        Spinner spinner = view.findViewById(R.id.leagueslist);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.dark_green), PorterDuff.Mode.SRC_ATOP);
        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);
        goals = view.findViewById(R.id.goalsvalue);
        goalsHisTeam = view.findViewById(R.id.goalshisteamvalue);
        truePelanties = view.findViewById(R.id.truepenaltiesvalue);
        falsePelanties = view.findViewById(R.id.falsepenaltiesvalue);
        yellowCard = view.findViewById(R.id.yellowcardvalue);
        redCard = view.findViewById(R.id.redcardvalue);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        playerId = appSharedPreferences.readInteger(Constants.playerId);
        sharedArrayList = appSharedPreferences.readArray(Constants.pLeaguesArray);
        Log.d(Constants.Log+"shared", "sharedArrayList.size() = "+sharedArrayList.size());
        ArrayList<ParticipatingLeague> arrayList = new ArrayList<>();
        for (int i=0; i<sharedArrayList.size(); i++){
            ParticipatingLeague participatingLeague = new ParticipatingLeague(sharedArrayList.get(i).getId(),
                    sharedArrayList.get(i).getLeague(), sharedArrayList.get(i).getLeague().getTitle());
            arrayList.add(participatingLeague);
        }
        ArrayAdapter<ParticipatingLeague> arrayAdapter = new ArrayAdapter<ParticipatingLeague>(getContext(), android.R.layout.simple_spinner_item, arrayList){
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "fonts/thesans_bold.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);
                ((TextView) v).setTextColor(Color.parseColor("#031C02"));
                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {

                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thesans_bold.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                participatingLeague = (ParticipatingLeague) parent.getSelectedItem();
                loadPlayerDetails(playerId, participatingLeague.getId());
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPlayerDetails(playerId, participatingLeague.getId());
            }
        });

        return view;
    }

    private Call<PlayerDetailsBody> callTopRatedMoviesApi(int playerId, int leagueId) {
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

    private void loadPlayerDetails(int playerId, int leagueId) {
        ////////////////////////////
        indicatorView.show();
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        ////////////////////////////
        if (!isNetworkConnected()) {
            swipeRefreshLayout.setRefreshing(false);
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.hide();
        }
        callTopRatedMoviesApi(playerId, leagueId).enqueue(new Callback<PlayerDetailsBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PlayerDetailsBody> call, Response<PlayerDetailsBody> response) {

                PlayerDetailsBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "PlayerDetailsBody", " | Code = " + code);

                if (response.code() == 200) {
                    swipeRefreshLayout.setRefreshing(false);
                    indicatorView.hide();
                    assert resource != null;

                    if (resource.getData().size() > 0) {
                        Log.d(Constants.Log + "size", "newsList.size() != 0 = " + resource.getData().size() + "");
                        emptyData.setVisibility(View.GONE);

                        goals.setText(resource.getData().get(0).getTotalGoal().toString());
                        goalsHisTeam.setText(resource.getData().get(0).getTotalOG().toString());
                        truePelanties.setText(resource.getData().get(0).getPenaltiesTrue().toString());
                        falsePelanties.setText(resource.getData().get(0).getPenaltiesFalse().toString());
                        yellowCard.setText(resource.getData().get(0).getCardYellow().toString());
                        redCard.setText(resource.getData().get(0).getCardRed().toString());

                        appSharedPreferences.writeArray(Constants.pLeaguesArray, resource.getData().get(0).getTeams().get(0).getParticipatingLeagues());
                    } else {
                        Log.d(Constants.Log + "size", "newsList.size() == 0 = " + resource.getData().size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 404) {
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    swipeRefreshLayout.setRefreshing(false);
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.hide();
                } else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    swipeRefreshLayout.setRefreshing(false);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
