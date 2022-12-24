package com.example.foursquare.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.dataclass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var loginUserLiveData: MutableLiveData<LoginResponse?> = MutableLiveData()
    var nearYouLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var topPickLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var popularLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var lunchLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var coffeeLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var particularPlaceLiveData: MutableLiveData<GetParticularPlaceResponse?> = MutableLiveData()
    var reviewLiveData: MutableLiveData<GetAllReviewResponse?> = MutableLiveData()
    var nearByPlacesLiveData: MutableLiveData<GetNearByPlacesResponse?> = MutableLiveData()
    var searchPlacesLiveData: MutableLiveData<GetSearchPlacesResponse?> = MutableLiveData()
    var reviewImageLiveData: MutableLiveData<GetReviewImageResponse?> = MutableLiveData()

    fun getLoginUserObserver(): MutableLiveData<LoginResponse?> {
        return loginUserLiveData
    }

    fun loginUser(user: User) {
        RetrofitClient.instance.login(user)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginUserLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginUserLiveData.postValue(null)
                }

            })
    }

    fun getNearYouObserver(): MutableLiveData<NearYouResponse?> {
        return nearYouLiveData
    }

    fun nearYou(latLangg: LatLangg) {
        RetrofitClient.instance.nearYou(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    nearYouLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    nearYouLiveData.postValue(null)
                }

            })
    }

    fun getTopPickObserver(): MutableLiveData<NearYouResponse?> {
        return topPickLiveData
    }

    fun topPick(latLangg: LatLangg) {
        RetrofitClient.instance.topPick(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    topPickLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    topPickLiveData.postValue(null)
                }

            })
    }

    fun getPopularObserver(): MutableLiveData<NearYouResponse?> {
        return popularLiveData
    }

    fun popular(latLangg: LatLangg) {
        RetrofitClient.instance.popular(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    popularLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    popularLiveData.postValue(null)
                }

            })
    }

    fun getLunchObserver(): MutableLiveData<NearYouResponse?> {
        return lunchLiveData
    }

    fun lunch(latLangg: LatLangg) {
        RetrofitClient.instance.lunch(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    lunchLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    lunchLiveData.postValue(null)
                }

            })
    }

    fun getCoffeeObserver(): MutableLiveData<NearYouResponse?> {
        return coffeeLiveData
    }

    fun coffee(latLangg: LatLangg) {
        RetrofitClient.instance.coffee(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    coffeeLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    coffeeLiveData.postValue(null)
                }

            })
    }

    fun getParticularPlaceObserver(): MutableLiveData<GetParticularPlaceResponse?> {
        return particularPlaceLiveData
    }

    fun getParticularPlace(id: Id) {
        RetrofitClient.instance.getParticularPlace(id)
            .enqueue(object : Callback<GetParticularPlaceResponse> {
                override fun onResponse(
                    call: Call<GetParticularPlaceResponse>,
                    response: Response<GetParticularPlaceResponse>
                ) {
                    particularPlaceLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetParticularPlaceResponse>, t: Throwable) {
                    particularPlaceLiveData.postValue(null)
                }

            })
    }

    fun getReviewObserver(): MutableLiveData<GetAllReviewResponse?> {
        return reviewLiveData
    }

    fun getAllReview(id: Id) {
        RetrofitClient.instance.getAllReview(id)
            .enqueue(object : Callback<GetAllReviewResponse> {
                override fun onResponse(
                    call: Call<GetAllReviewResponse>,
                    response: Response<GetAllReviewResponse>
                ) {
                    reviewLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetAllReviewResponse>, t: Throwable) {
                    reviewLiveData.postValue(null)
                }

            })
    }

    fun getNearByPlacesObserver(): MutableLiveData<GetNearByPlacesResponse?> {
        return nearByPlacesLiveData
    }

    fun getNearByPlaces(latLangg: LatLangg) {
        RetrofitClient.instance.getNearByPlaces(latLangg)
            .enqueue(object : Callback<GetNearByPlacesResponse> {
                override fun onResponse(
                    call: Call<GetNearByPlacesResponse>,
                    response: Response<GetNearByPlacesResponse>
                ) {
                    nearByPlacesLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetNearByPlacesResponse>, t: Throwable) {
                    nearByPlacesLiveData.postValue(null)
                }

            })
    }

    fun getSearchPlacesObserver(): MutableLiveData<GetSearchPlacesResponse?> {
        return searchPlacesLiveData
    }

    fun getSearchPlaces(data: GetSearchPlacesRequest) {
        RetrofitClient.instance.getSearchPlaces(data)
            .enqueue(object : Callback<GetSearchPlacesResponse> {
                override fun onResponse(
                    call: Call<GetSearchPlacesResponse>,
                    response: Response<GetSearchPlacesResponse>
                ) {
                    searchPlacesLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetSearchPlacesResponse>, t: Throwable) {
                    searchPlacesLiveData.postValue(null)
                }

            })
    }

    fun getReviewImageObserver(): MutableLiveData<GetReviewImageResponse?> {
        return reviewImageLiveData
    }

    fun getReviewImage(id: Id) {
        RetrofitClient.instance.getReviewImage(id)
            .enqueue(object : Callback<GetReviewImageResponse> {
                override fun onResponse(
                    call: Call<GetReviewImageResponse>,
                    response: Response<GetReviewImageResponse>
                ) {
                    reviewImageLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetReviewImageResponse>, t: Throwable) {
                    reviewImageLiveData.postValue(null)
                }

            })

    }

}