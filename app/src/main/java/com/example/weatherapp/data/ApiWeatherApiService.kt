package com.example.weatherapp.data

import com.example.weatherapp.data.response.CurrentWeatherResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "4c9631cfd6a269ccdf68cd157ac6a318"

//http://api.openweathermap.org/data/2.5/weather?q=Warsaw,PL&appid=4c9631cfd6a269ccdf68cd157ac6a318


interface ApiWeatherApiService {

    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String = "Warsaw"
    ): Single<CurrentWeatherResponse>

    companion object {
        operator fun invoke(): ApiWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiWeatherApiService::class.java)


        }
    }
}
