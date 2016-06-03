package com.example.pavan.sunshine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pavan.sunshine.fetchForecastDataFromAPI.City;
import com.example.pavan.sunshine.fetchForecastDataFromAPI.Coord;
import com.example.pavan.sunshine.fetchForecastDataFromAPI.FetchWeatherData;
import com.example.pavan.sunshine.fetchForecastDataFromAPI.RetrofitForWeatherForecast;
import com.example.pavan.sunshine.fetchForecastDataFromAPI.Temp;
import com.example.pavan.sunshine.fetchForecastDataFromAPI.Weather;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pavan on 6/2/2016.
 */
public class ForecastFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();
    private static final String API_KEY = "fb29688b4c0181f127943ed3b473d8b0";
    private Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create()).build();
    protected RetrofitForWeatherForecast api = retrofit.create(RetrofitForWeatherForecast.class);

    private FetchWeatherData fetchWeatherData = new FetchWeatherData();

    public ForecastFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
          FetchWeatherTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        String[] forecastEntry = {"Today-Sunny-88/63","Tomorrow- Foggy-70/46","Weds-CLouds-72/63",
                "Thurs-Rainy-64/51","Fri-Foggy-70/46","Sat-Sunny-75/68"};

        java.util.List<String> weekForecast = new ArrayList(Arrays.asList(forecastEntry));

        ArrayAdapter mForecastAdapter = new ArrayAdapter(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textView,weekForecast);

        ListView mForecastListView = (ListView) rootView.findViewById(R.id.listView_forecast);

        mForecastListView.setAdapter(mForecastAdapter);



        return rootView;
    }

    public void FetchWeatherTask(){
        Call<FetchWeatherData> FetchWeatherDataCall = api.WEATHER_FORECAST_RESPONSE_CALL("94043","metric",7,API_KEY);

        FetchWeatherDataCall.enqueue(new Callback<FetchWeatherData>() {

            @Override
            public void onResponse(Call<FetchWeatherData> call, Response<FetchWeatherData> response) {


                Log.i(LOG_TAG,"retrofit response : " + response.raw());

                fetchWeatherData = response.body();
                City city = fetchWeatherData.getCity();
                Coord coord = city.getCoord();
                Log.i(LOG_TAG,"coord : " + coord.getLat() + " / " + coord.getLon());
                com.example.pavan.sunshine.fetchForecastDataFromAPI.List list = (com.example.pavan.sunshine.fetchForecastDataFromAPI.List) fetchWeatherData.getList();
                Log.i(LOG_TAG,"list : " + list);
                Temp temp = list.getTemp();
                Log.i(LOG_TAG,"temp : " + temp);
                Weather weather = (Weather) list.getWeather();
                Log.i(LOG_TAG,"weather : " + weather);



            }

            @Override
            public void onFailure(Call<FetchWeatherData> call, Throwable t) {

                Log.i(LOG_TAG,"retrofit failed");
                Log.i(LOG_TAG,call.request().toString());

                Log.i(LOG_TAG,"\n stacktrace : " + t);

            }
        });
    }
}
