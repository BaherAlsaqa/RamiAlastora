package com.ramialastora.ramialastora.retrofit;

import com.ramialastora.ramialastora.classes.responses.BodyData;
import com.ramialastora.ramialastora.classes.responses.UserResp;
import com.ramialastora.ramialastora.classes.responses.leagues.LeagueBody;
import com.ramialastora.ramialastora.classes.responses.match_goals_video.MatchGoalsVideoBody;
import com.ramialastora.ramialastora.classes.responses.news.NewsBody;
import com.ramialastora.ramialastora.classes.responses.news.OneNewsBody;
import com.ramialastora.ramialastora.classes.responses.notifications.NotificationBody;
import com.ramialastora.ramialastora.classes.responses.participating_teams.ParticipatingTeamsBody;
import com.ramialastora.ramialastora.classes.responses.players_details.PlayerDetailsBody;
import com.ramialastora.ramialastora.classes.responses.scorers.MatchScorersBody;
import com.ramialastora.ramialastora.classes.responses.scorers.ScorersBody;
import com.ramialastora.ramialastora.classes.responses.search.PlayerSearchBody;
import com.ramialastora.ramialastora.classes.responses.search.SearchBody;
import com.ramialastora.ramialastora.classes.responses.team_players.TeamPlayersBody;
import com.ramialastora.ramialastora.classes.responses.teams.TeamsBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by baher on 09/01/17.
 */

public interface APIInterface {

    @GET("teams")
    Call<BodyData> getAllTeams(@Header("API-KEY") String apiKey,
                               @Header("API-USERNAME") String apiUsername,
                               @Header("API-PASSWORD") String apiPassword,
                               @Query("page") int nextPage);

    @GET("favorites/{user_id}/user")
    Call<BodyData> getFavoriteTeams(@Header("API-KEY") String apiKey,
                                    @Header("API-USERNAME") String apiUsername,
                                    @Header("API-PASSWORD") String apiPassword,
                                    @Path("user_id") int userId,
                                    @Query("page") int nextPage);

    @GET("teams/{user_id}/team")
    Call<BodyData> getAllTeamsToFavorite(@Header("API-KEY") String apiKey,
                                         @Header("API-USERNAME") String apiUsername,
                                         @Header("API-PASSWORD") String apiPassword,
                                         @Path("user_id") int userId,
                                         @Query("page") int nextPage);

    @GET("matches/{match_date}/{user_id}")
    Call<com.ramialastora.ramialastora.classes.responses.matches.BodyData> getMatches(@Header("API-KEY") String apiKey,
                                                                                      @Header("API-USERNAME") String apiUsername,
                                                                                      @Header("API-PASSWORD") String apiPassword,
                                                                                      @Path("match_date") String matchDate,
                                                                                      @Path("user_id") int userId,
                                                                                      @Query("page") int nextPage);

    @GET("notifications/{user_id}")
    Call<NotificationBody> getNotifications(@Header("API-KEY") String apiKey,
                                            @Header("API-USERNAME") String apiUsername,
                                            @Header("API-PASSWORD") String apiPassword,
                                            @Path("user_id") int userId,
                                            @Query("page") int nextPage);

    @GET("news")
    Call<NewsBody> getNews(@Header("API-KEY") String apiKey,
                           @Header("API-USERNAME") String apiUsername,
                           @Header("API-PASSWORD") String apiPassword,
                           @Query("page") int nextPage);

    @GET("news/team/{team_id}")
    Call<NewsBody> getNews(@Header("API-KEY") String apiKey,
                           @Header("API-USERNAME") String apiUsername,
                           @Header("API-PASSWORD") String apiPassword,
                           @Path("team_id") int teamId,
                           @Query("page") int nextPage);

    @GET("news/{league_id}/league")
    Call<NewsBody> getLeagueNews(@Header("API-KEY") String apiKey,
                                 @Header("API-USERNAME") String apiUsername,
                                 @Header("API-PASSWORD") String apiPassword,
                                 @Path("league_id") int leagueId,
                                 @Query("page") int nextPage);

    @GET("news/team/{team_id1}/{team_id2}")
    Call<NewsBody> getMatchNews(@Header("API-KEY") String apiKey,
                                @Header("API-USERNAME") String apiUsername,
                                @Header("API-PASSWORD") String apiPassword,
                                @Path("team_id1") int teamId1,
                                @Path("team_id2") int teamId2,
                                @Query("page") int nextPage);

    @GET("teams/{teams_id}/favorite-and-all")
    Call<TeamsBody> getTeams(@Header("API-KEY") String apiKey,
                             @Header("API-USERNAME") String apiUsername,
                             @Header("API-PASSWORD") String apiPassword,
                             @Path("teams_id") int teamsId,
                             @Query("page") int nextPage);

    @GET("news/{news_id}")
    Call<OneNewsBody> getOneNews(@Header("API-KEY") String apiKey,
                                 @Header("API-USERNAME") String apiUsername,
                                 @Header("API-PASSWORD") String apiPassword,
                                 @Path("news_id") int newsId);

    @GET("leagues-active/all")
    Call<LeagueBody> getLeagues(@Header("API-KEY") String apiKey,
                                @Header("API-USERNAME") String apiUsername,
                                @Header("API-PASSWORD") String apiPassword,
                                @Query("page") int nextPage);

    @GET("leagues/{league_id}/scorers")
    Call<ScorersBody> getScorers(@Header("API-KEY") String apiKey,
                                 @Header("API-USERNAME") String apiUsername,
                                 @Header("API-PASSWORD") String apiPassword,
                                 @Path("league_id") int leagueId,
                                 @Query("page") int nextPage);

    @GET("leagues/{league_id}/teams-participating/{user_id}")
    Call<ParticipatingTeamsBody> getPaticipatingTeams(@Header("API-KEY") String apiKey,
                                                      @Header("API-USERNAME") String apiUsername,
                                                      @Header("API-PASSWORD") String apiPassword,
                                                      @Path("league_id") int leagueId,
                                                      @Path("user_id") int userId,
                                                      @Query("page") int nextPage);

    @GET("teams/{team_id}/players")
    Call<TeamPlayersBody> getTeamPlayers(@Header("API-KEY") String apiKey,
                                         @Header("API-USERNAME") String apiUsername,
                                         @Header("API-PASSWORD") String apiPassword,
                                         @Path("team_id") int teamId);

    @GET("matches/{match_id}/goal/match")
    Call<MatchScorersBody> getPlayersGoalsMatch(@Header("API-KEY") String apiKey,
                                                @Header("API-USERNAME") String apiUsername,
                                                @Header("API-PASSWORD") String apiPassword,
                                                @Path("match_id") int matchId);

    @GET("matches/{league_id}/league/all")
    Call<com.ramialastora.ramialastora.classes.responses.matches
            .BodyData> getMatchesInLeague(@Header("API-KEY") String apiKey,
                                          @Header("API-USERNAME") String apiUsername,
                                          @Header("API-PASSWORD") String apiPassword,
                                          @Path("league_id") int leagueId,
                                          @Query("page") int nextPage);

    @GET("matches-team/{team_id}")
    Call<com.ramialastora.ramialastora.classes.responses.matches
            .BodyData> getTeamMatches(@Header("API-KEY") String apiKey,
                                      @Header("API-USERNAME") String apiUsername,
                                      @Header("API-PASSWORD") String apiPassword,
                                      @Path("team_id") int teamId,
                                      @Query("page") int nextPage);

    @GET("matches/{match_id}/video/goal")
    Call<MatchGoalsVideoBody> getMatchGoalsVideo(@Header("API-KEY") String apiKey,
                                                 @Header("API-USERNAME") String apiUsername,
                                                 @Header("API-PASSWORD") String apiPassword,
                                                 @Path("match_id") int matchId);

    @GET("player/{player_id}/{league_id}")
    Call<PlayerDetailsBody> getPlayerDetailsInLeague(@Header("API-KEY") String apiKey,
                                                     @Header("API-USERNAME") String apiUsername,
                                                     @Header("API-PASSWORD") String apiPassword,
                                                     @Path("player_id") int playerId,
                                                     @Path("league_id") int leagueId);


    //TODO //////////////POST///////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("users/user")
    Call<UserResp> createUser(@Header("API-KEY") String apiKey,
                              @Header("API-USERNAME") String apiUsername,
                              @Header("API-PASSWORD") String apiPassword,
                              @Header("Content-Type") String contentType,
                              @Field("token") String token);

    @FormUrlEncoded
    @POST("users/user")
    Call<UserResp> updateToken(@Header("API-KEY") String apiKey,
                               @Header("API-USERNAME") String apiUsername,
                               @Header("API-PASSWORD") String apiPassword,
                               @Header("Content-Type") String contentType,
                               @Field("user_id") int userId,
                               @Field("token") String token);

    @FormUrlEncoded
    @POST("favorites/favorite/user")
    Call<UserResp> addFavorite(@Header("API-KEY") String apiKey,
                               @Header("API-USERNAME") String apiUsername,
                               @Header("API-PASSWORD") String apiPassword,
                               @Field("user_id") int userId,
                               @Field("team_id") int teamId,
                               @Field("type") int type);

    @FormUrlEncoded
    @POST("search/all")
    Call<SearchBody> searchTPL(@Header("API-KEY") String apiKey,
                               @Header("API-USERNAME") String apiUsername,
                               @Header("API-PASSWORD") String apiPassword,
                               @Field("query") String searchText,
                               @Field("userID") int userId);

    @FormUrlEncoded
    @POST("search/players")
    Call<PlayerSearchBody> playerSearch(@Header("API-KEY") String apiKey,
                                        @Header("API-USERNAME") String apiUsername,
                                        @Header("API-PASSWORD") String apiPassword,
                                        @Field("query") String searchText,
                                        @Field("page") int nextPage);

    @FormUrlEncoded
    @POST("search/teams")
    Call<BodyData> teamSearch(@Header("API-KEY") String apiKey,
                              @Header("API-USERNAME") String apiUsername,
                              @Header("API-PASSWORD") String apiPassword,
                              @Field("query") String searchText,
                              @Field("userID") int userID,
                              @Field("page") int nextPage);

    @FormUrlEncoded
    @POST("search/leagues")
    Call<LeagueBody> leagueSearch(@Header("API-KEY") String apiKey,
                                  @Header("API-USERNAME") String apiUsername,
                                  @Header("API-PASSWORD") String apiPassword,
                                  @Field("query") String searchText,
                                  @Field("page") int nextPage);
}
