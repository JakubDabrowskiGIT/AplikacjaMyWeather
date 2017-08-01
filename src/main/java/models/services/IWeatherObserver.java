package models.services;

import models.WeatherInfo;

public interface IWeatherObserver {
    void onWeatherUpdate(WeatherInfo info);
}
