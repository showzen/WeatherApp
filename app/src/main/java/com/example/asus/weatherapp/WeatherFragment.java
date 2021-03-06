package com.example.asus.weatherapp;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class WeatherFragment extends Fragment {


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private WeatherViewModel viewModel;
    // FOR DESIGN
    @BindView(R.id.weatherCityName) TextView CityName;
    @BindView(R.id.weatherMaxTemp) TextView MaxTemp;
    @BindView(R.id.weatherMinTemp) TextView MinTemp;
    @BindView(R.id.newWeather) EditText newWeather;
    @BindView(R.id.newWeatherButton) Button newWeatherButton;
    @BindView(R.id.weatherImage) ImageView weatherImage;
    @BindView(R.id.weatherDescription) TextView weatherDescription;
    public String city_name = "Lisbon";
    public WeatherFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        Log.d("onCreateFrag:",city_name);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_name = newWeather.getText().toString();
                Log.d("Here_onClick:",city_name);
                configureViewModel();
            }
        });
        Log.d("onActivityFrag:",city_name);
        this.configureDagger();
        this.configureViewModel();

    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel.class);
        viewModel.init(city_name);
        Log.d("configViewModel:",city_name);
        viewModel.getWeather().observe(this, weather -> updateUI(weather));
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUI(@Nullable Weather weather){
        if (weather != null){
            Log.d("WeatherFrag:",String.valueOf(weather.getCity_name()));
            this.CityName.setText(weather.getCity_name());
            this.MaxTemp.setText(String.valueOf(weather.getMain().getTemp_max()-272.15) + "ºC");
            this.MinTemp.setText(String.valueOf(weather.getMain().getTemp_min()-272.15) + "ºC");
            this.weatherDescription.setText(weather.getTempo().get(0).getMain());
            this.weatherImage.setBackgroundResource(setDrawable(weather.getTempo().get(0).getMain()));
        }
    }

    private int setDrawable(String weatherMain){
        switch (weatherMain){
            case "Clouds":
                return R.drawable.ic_cloudy;
            case "Clear":
                return R.drawable.ic_sunny;
            case "Rain":
                return R.drawable.ic_rainy;
        }
        return R.drawable.ic_sunny;
    }
}