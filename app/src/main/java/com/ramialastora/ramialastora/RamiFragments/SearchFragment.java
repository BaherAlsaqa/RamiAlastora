package com.ramialastora.ramialastora.RamiFragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.RamiActivities.RamiMain;
import com.ramialastora.ramialastora.adapters.MySearchLeagueAdapter;
import com.ramialastora.ramialastora.adapters.MySearchPlayerAdapter;
import com.ramialastora.ramialastora.adapters.MySearchTeamAdapter;
import com.ramialastora.ramialastora.classes.responses.search.League;
import com.ramialastora.ramialastora.classes.responses.search.Player;
import com.ramialastora.ramialastora.classes.responses.search.SearchBody;
import com.ramialastora.ramialastora.classes.responses.search.Team;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.listeners.OnItemClickListener12;
import com.ramialastora.ramialastora.listeners.OnItemClickListener13;
import com.ramialastora.ramialastora.listeners.OnItemClickListener14;
import com.ramialastora.ramialastora.retrofit.APIClient;
import com.ramialastora.ramialastora.retrofit.APIInterface;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.FROM_BACK;
import static com.ramialastora.ramialastora.RamiFragments.MatchDetailsFragment.BACK_FRAGMENTS;
import static com.ramialastora.ramialastora.RamiFragments.ScorersFragment.PLAYER_ID;
import static com.ramialastora.ramialastora.RamiFragments.SortParicipatingTeamsFragment.TEAM_ID1;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private View view;
    private ArrayList<Team> teamList =null;
    private ArrayList<Player> playerList = null;
    private ArrayList<League> leagueList = null;
    private RecyclerView teamRecyclerView;
    private RecyclerView playerRecyclerView;
    private RecyclerView leagueRecyclerView;
    private MySearchTeamAdapter teamAdapter;
    private MySearchPlayerAdapter playerAdapter;
    private MySearchLeagueAdapter leagueAdapter;
    private APIInterface apiInterface;
    private AVLoadingIndicatorView indicatorView;
    View noInternet, emptyData, error;
    private EditText search;
    private AppSharedPreferences appSharedPreferences;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        indicatorView = view.findViewById(R.id.avi);
        noInternet = view.findViewById(R.id.nointernet);
        emptyData = view.findViewById(R.id.emptydata);
        error = view.findViewById(R.id.error);
        search = view.findViewById(R.id.search);
        teamRecyclerView = view.findViewById(R.id.teamsrecyclerview);
        playerRecyclerView = view.findViewById(R.id.playersrecyclerview);
        leagueRecyclerView = view.findViewById(R.id.leaguesrecyclerview);

        ////////////////////////////
        indicatorView.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        ////////////////////////////

        apiInterface = APIClient.getClient().create(APIInterface.class);
        appSharedPreferences = new AppSharedPreferences(Objects.requireNonNull(getContext()));

        teamList = new ArrayList<>();
        playerList = new ArrayList<>();
        leagueList = new ArrayList<>();

        teamRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        leagueRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        playerAdapter = new MySearchPlayerAdapter(playerList, view.getContext());
        teamAdapter = new MySearchTeamAdapter(teamList, view.getContext());
        leagueAdapter = new MySearchLeagueAdapter(leagueList, view.getContext());

        teamAdapter.setOnClickListener(new OnItemClickListener12() {
            @Override
            public void onItemClick(Team item) {
                Log.d(Constants.Log+"adapter", item.getName()+" "+item.getCountry().getAr_name());
//                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                FragmentTransaction fragmentTransaction = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                }else{
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                }
                fragmentTransaction.replace(R.id.nav_host_fragment, TeamDetailsFragment
                        .newInstance(item.getFavorite(), item.getId(), item.getName(), item.getLogo(), item.getCountry().getAr_name()
                                , item.getParticipatingLeagueArrayList().get(0).getLeagueName()
                                , item.getParticipatingLeagueArrayList().get(0).getId()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        playerAdapter.setOnClickListener(new OnItemClickListener13() {
            @Override
            public void onItemClick(Player item) {
                Log.d(Constants.Log+"adapter", item.getName()+" "+item.getTeam().get(0).getName());
//                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                PLAYER_ID = item.getId();
                FROM_BACK = Constants.search;
                appSharedPreferences.writeInteger(Constants.playerId, item.getId());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, PlayerDetails.newInstance(
                        item.getTeam().get(0).getParticipatingLeagues().get(0).getId()
                        , item.getId(), 0));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        leagueAdapter.setOnClickListener(new OnItemClickListener14() {
            @Override
            public void onItemClick(League item) {
                Log.d(Constants.Log+"adapter", item.getTitle()+" "+item.getLeaguesActiveArrayList().get(0).getId());
//                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                TEAM_ID1 = -1;
                BACK_FRAGMENTS = 1;
                Log.d(Constants.Log + "leaguematches", item.getTitle());
                FragmentTransaction fragmentTransaction = null;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, LeagueDetailsFragment.newInstance(null, 2, item.getImage(), item.getTitle(), item.getLeaguesActiveArrayList().get(0).getId()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(Constants.Log+"on", s.toString());
                if (s.toString().length() > 0 || !s.toString().equals("")) {
                    Log.d(Constants.Log+"on", s.toString());
                    getSearchResult(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(Constants.Log+"after", s.toString());
                if (s.toString().length() == 0 || s.toString().equals("")){
                    Log.d(Constants.Log+"after", "(s.toString().length() == 0 || s.toString().equals())");
                    emptyData.setVisibility(View.VISIBLE);
                    teamList.clear();
                    playerList.clear();
                    leagueList.clear();
                }
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
                    ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_search, R.string.app_name, "", 1);
                    ((RamiMain) getActivity()).badgeandCheckedDrawer();
                    ((RamiMain) getActivity()).changeToolbarBackground(R.drawable.back_toolbar);
                    ((RamiMain) getActivity()).drawerIcon(R.drawable.ic_menu);
                    ((RamiMain) getActivity()).checkMainFragmentOnDrawer();
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//back on main
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();//back one fragment
                    return true;
                }
                return false;
            }
        });
        //TODO ///////////End back/////////////

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (BACK_FRAGMENTS > 0) {
            ((RamiMain) getActivity()).menuBackIcon(R.menu.toolbar_back, R.string.search, "", 0);
            BACK_FRAGMENTS = 0;
        }
    }

    private Call<SearchBody> callTopRatedMoviesApi(String searchText) {
        int userId = appSharedPreferences.readInteger(Constants.userid);
        Call<SearchBody> data = null;
        Log.d(Constants.Log + "search_text", "Search Text = " + searchText);
        if (getContext() != null) {
            data = apiInterface.searchTPL(getString(R.string.api_key),
                    getString(R.string.api_username),
                    getString(R.string.api_password),
                    searchText,
                    userId);
        }
        return data;
    }

    private void getSearchResult(String searchText) {
        ////////////////////////////
        indicatorView.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        ////////////////////////////
        if (!isNetworkConnected()) {
            noInternet.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(View.GONE);
        }
        callTopRatedMoviesApi(searchText).enqueue(new Callback<SearchBody>() {
            @Override
            public void onResponse(Call<SearchBody> call, Response<SearchBody> response) {

                SearchBody resource = response.body();

                int code = response.code();

                Log.d(Constants.Log + "SearchBody", " | Code = " + code);

                if (response.code() == 200) {
                    indicatorView.setVisibility(View.GONE);
                    assert resource != null;

                    teamList.clear();
                    playerList.clear();
                    leagueList.clear();
                    if (resource.getData().getTeams() != null) {
                        teamList.addAll(resource.getData().getTeams());
                    }
                    if (resource.getData().getPlayers() != null) {
                        playerList.addAll(resource.getData().getPlayers());
                    }
                    if (resource.getData().getLeagues() != null) {
                        leagueList.addAll(resource.getData().getLeagues());
                    }

                    /*teamAdapter.notifyDataSetChanged();
                    playerAdapter.notifyDataSetChanged();
                    leagueAdapter.notifyDataSetChanged();*/

                    teamRecyclerView.setAdapter(teamAdapter);
                    playerRecyclerView.setAdapter(playerAdapter);
                    leagueRecyclerView.setAdapter(leagueAdapter);

                    if (teamList.size() == 0 & playerList.size() == 0 & leagueList.size() == 0) {
                        Log.d(Constants.Log + "size", "teamList.size() == 0 = " + teamList.size() + "");
                        emptyData.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(Constants.Log + "size", "teamList.size() != 0 = " + teamList.size() + "");
                        emptyData.setVisibility(View.GONE);
                    }

                } else if (response.code() == 404) {
                    Log.d(Constants.Log + "res", "if (response.code() == 404)");
                    emptyData.setVisibility(View.VISIBLE);
                    indicatorView.setVisibility(View.GONE);
                } else {
                    Log.d(Constants.Log + "res", "if (!!!response.code() == 200)");
                    indicatorView.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchBody> call, Throwable t) {
                Log.d(Constants.Log + "55", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.Log + "res", "onFailure = " + t.getMessage() + "");
                if (t instanceof IOException) {
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                }
                call.cancel();
                indicatorView.setVisibility(View.GONE);
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
