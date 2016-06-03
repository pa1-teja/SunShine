package com.example.pavan.sunshine.fetchForecastDataFromAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pavan on 6/1/2016.
 */
public interface RetrofitForWeatherForecast {

    @GET("data/2.5/forecast/daily")
    Call<FetchWeatherData> WEATHER_FORECAST_RESPONSE_CALL(@Query("q") String locationPostalCodeOrCityName,
                                                                 @Query("units") String units,
                                                                 @Query("cnt") int forecastForNoOfDays,
                                                                 @Query("APPID") String API_KEY);
}
