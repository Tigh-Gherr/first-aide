package com.firstadie.csftcarroll.b00641329.firstaide.ui.WeatherActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.api.API;
import com.firstadie.csftcarroll.b00641329.firstaide.api.Weather;
import com.firstadie.csftcarroll.b00641329.firstaide.api.WeatherAPI;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;

import org.json.JSONException;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherActivityFragment extends Fragment {

    private RecyclerView mClothingRecyclerView;
    private ClothingRecyclerViewAdapter mClothingRecyclerViewAdapter;

    private Weather mWeather;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private AppCompatTextView mTemperatureTextView;
    private AppCompatImageView mWeatherIconImageView;
    private AppCompatTextView mDescriptionTextView;


    public WeatherActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCollapsingToolbarLayout = view.findViewById(R.id.collapsingtoolbarlayout);
        mCollapsingToolbarLayout.setTitle("Weather Aide");
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.tranparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));

        mTemperatureTextView = view.findViewById(R.id.textview_temperature);
        mWeatherIconImageView = view.findViewById(R.id.imageview_weatherIcon);
        mDescriptionTextView = view.findViewById(R.id.textview_weatherDescription);

        String lat = String.valueOf(LocationSingleton.get().getLatitude());
        String lon = String.valueOf(LocationSingleton.get().getLongitude());
        final API api = new WeatherAPI();

        api.setOnEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                Log.d(WeatherActivityFragment.this.getClass().getSimpleName(), result);
                mWeather = (Weather) api.parse(result);
                displayWeather(mWeather);
            }
        });

        api.addParam("lat", lat);
        api.addParam("lon", lon);
        api.query();

        mClothingRecyclerView = view.findViewById(R.id.recyclerview_clothes);
        mClothingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void displayWeather(Weather weather) {
        mTemperatureTextView.setText(TextFormatUtils.formatTemperature(weather.getTemperature()));
        mWeatherIconImageView.setImageResource(weather.getWeatherIcon());
        mDescriptionTextView.setText(weather.getDescription());

        mClothingRecyclerViewAdapter = new ClothingRecyclerViewAdapter(
                Clothing.forCode(weather.getWeatherCode())
        );

        mClothingRecyclerView.setAdapter(mClothingRecyclerViewAdapter);
        mClothingRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
