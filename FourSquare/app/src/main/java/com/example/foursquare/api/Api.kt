package com.example.foursquare.api

import com.example.foursquare.dataclass.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @Headers("Content-Type:application/json")
    @POST("login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("getNearPlace")
    fun nearYou(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getTopPlace")
    fun topPick(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getPopularPlace")
    fun popular(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getRestaurants")
    fun lunch(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getCafe")
    fun coffee(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getParticularPlace")
    fun getParticularPlace(@Body id: Map<String, String>): Call<GetParticularPlaceResponse>

    @Headers("Content-Type:application/json")
    @POST("getReview")
    fun getAllReview(@Body id: Id): Call<GetAllReviewResponse>

    @Headers("Content-Type:application/json")
    @POST("getNearCity")
    fun getNearByPlaces(@Body latLng: LatLangg): Call<GetNearByPlacesResponse>

    @Headers("Content-Type:application/json")
    @POST("searchPlace")
    fun getSearchPlaces(@Body data: GetSearchPlacesRequest): Call<GetSearchPlacesResponse>

    @Headers("Content-Type:application/json")
    @POST("getReviewImage")
    fun getReviewImage(@Body id: Id): Call<GetReviewImageResponse>

    @Headers("Content-Type:application/json")
    @POST("filterSearch")
    fun getFilterSearch(@Body preferences: MutableMap<String, Any>): Call<GetFilterSearchResponse>

    @Headers("Content-Type:application/json")
    @POST("getProfile")
    fun getProfile(@Header("Authorization") token: String): Call<GetProfileResponse>

    @Headers("Content-Type:application/json")
    @POST("addFavourite")
    fun addFavourite(
        @Header("Authorization") token: String,
        @Body id: Id
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("searchFavourite")
    fun getFavourites(
        @Header("Authorization") token: String,
        @Body getFavouritesRequest: GetFavouritesRequest
    ): Call<GetFavouritesResponse>

    @Headers("Content-Type:application/json")
    @POST("addRating")
    fun addRating(
        @Header("Authorization") token: String,
        @Body addRatingRequest: MutableMap<String, Any>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("addFeedback")
    fun getFeedback(
        @Header("Authorization") token: String,
        @Body getFeedbackRequest: GetFeedbackRequest
    ): Call<AddFavouriteResponse>

    @GET("getAboutUs")
    fun getAboutUs(): Call<GetAboutUsResponse>

    @Headers("Content-Type:application/json")
    @POST("addReview")
    fun addReviewText(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Multipart
    @POST("addReviewImage")
    fun addReviewImages(
        @Header("Authorization") token: String,
        @Part("_id") id: RequestBody,
        @Part("image") imagesList: MutableList<ImageList>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("register")
    fun register(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("sendOtp")
    fun getOtp(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("emailExists")
    fun emailExists(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("verifyOtp")
    fun verifyOtp(
        @Body request: Map<String, String>
    ): Call<Map<String, Boolean>>
//
//
//    //@Headers("Content-Type:multipart/form-data")
//    //@Multipart
//    //@FormUrlEncoded
//    @Multipart
//    @POST("api/user")
//    fun userRegister(
//        @Part body: MultipartBody.Part,
//        @Part("name") name: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("phone") phone: RequestBody,
//        @Part("city") city: RequestBody,
//        @Part("gender") gender: RequestBody,
//        @Part("password") password: RequestBody
//    ): Call<LoginResponse>


//    @GET("api/tournament")
//    fun getAllTournaments(@Header("Authorization") token: String): Call<GetAllTournamentsResponse>
//
//    @GET("api/tournament/{code}")
//    fun getTournamentByCode(@Path("code") code: String): Call<GetTournamentByCodeResponse>
//
//    @GET("api/user")
//    fun getParticularUser(@Header("Authorization") token: String): Call<GetParticularUserResponse>
//    @Headers("Content-Type:application/json")
//    @POST("api/otp?")
//    fun forgotpassword(
//        @Query("email") email: String
//    ):Call<ForgotPasswordResponse>
//
//    @GET("api/match?")
//    fun getMatches(@Query("tournamentId") tournamentId: String): Call<GetMatchesResponse>
//
//    @GET("api/team?")
//    fun getTeams(@Query("tournamentId") tournamentId: String): Call<GetTeamsResponse>
//
//    @GET("api/tournament/standings?")
//    fun getStandings(@Query("tournamentId") tournamentId: String): Call<GetStandingsResponse>
//
//    @GET("api/tournament/stats/63737b20476c70dbafbba373?")
//    fun getStats(@Query("query") query: String): Call<GetStatsResponse>
//
//    @GET("api/tournament/ground-details/all?")
//    fun getGrounds(@Query("tournamentId") tournamentId: String): Call<GetGroundsResponse>
//
//    @GET("api/participant/umpire/all?")
//    fun getUmpires(@Query("tournamentId") tournamentId: String): Call<GetUmpiresResponse>
//
//    @GET("api/participant/player/all?")
//    fun getTeamPlayers(
//        @Query("tournamentId") tournamentId: String,
//        @Query("teamId") teamId: String
//    ): Call<TaamPlayerResponse>
//
//    @GET("api/team/team-stats?")
//    fun getTeamStats(
//        @Query("tournamentId") tournamentId: String,
//        @Query("teamId") teamId: String
//    ): Call<GetTeamStatsResponse>
//
//
//
//    @GET("api/tournament/user-created-tournament")
//    fun getUserCreatedTournament(@Header("Authorization") token: String): Call<GetTournamentByCodeResponse>
//
//    @GET("api/match/get-scoreboard?")
//    fun getScoreboard(@QueryMap scoreboard: HashMap<String, String>) : Call<SecondScoreResponse>
//
//    @GET("api/match/live-score?tournamentId")
//    fun getLiveScore(@QueryMap scoreboard: HashMap<String, String>) : Call<LiveScore>
//
//    @Multipart
//    @POST("api/tournament")
//    fun createTournament(
//        @Header("Authorization") token: String,
//        @Part body: MultipartBody.Part,
//        @Part("name") name: RequestBody,
//        @Part("tournamentType") tournamentType: RequestBody,
//        @Part("email") email: RequestBody,
//    ): Call<CreateTournamentResponse>
}