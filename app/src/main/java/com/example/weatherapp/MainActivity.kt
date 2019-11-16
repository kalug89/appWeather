package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.data.ApiWeatherApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = ApiWeatherApiService()

        fun calculateKelvinCelsiusinInt(temp: Double): Int {
            val celsiusTemp = (temp - 273.15).toInt()
            celsiusTemp.toDouble()
            return celsiusTemp
        }

        button.setOnClickListener {
            val text = editText.text.toString()

            val currentWeatherResponse =
                apiService.getCurrentWeather(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { error -> Log.e("TAG", "${error.message}") }
                    .subscribe { value ->
                        textView2.text = value.name
                        textView3.text = calculateKelvinCelsiusinInt(value.main.temp).toString()
                        val temperetureValue = calculateKelvinCelsiusinInt(value.main.temp)

                        when {
                            temperetureValue < 10 -> textView3.setTextColor(getColor(R.color.blue))
                            temperetureValue in 10..20 -> textView3.setTextColor(getColor(R.color.black))
                            else -> textView3.setTextColor(getColor(R.color.red))
                        }
                    }
        }
    }
}
