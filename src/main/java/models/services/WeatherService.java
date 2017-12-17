package models.services;

import javafx.application.Platform;
import models.Config;
import models.WeatherInfo;
import org.json.JSONObject;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherService {
 private static WeatherService ourService = new WeatherService(new HttpService());

 public static WeatherService getOurService(){
     return ourService;
 }
    private List<IWeatherObserver> observerList = new ArrayList<>();
    private HttpService httpService;
    private double temp;
    private int humidity;
    private int pressure;
    private int clouds;

    private ExecutorService executorService;

    private WeatherService(HttpService httpService){
        this.httpService = httpService;
        executorService = Executors.newFixedThreadPool(1);
    }

    public void makeCall(final String city) throws IOException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    parseJson(httpService.connectAndResponse(Config.APP_URL + "?q=" + city + "&appid=" + Config.APP_ID));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        executorService.execute(runnable);
    }

    private void parseJson(String json){
        JSONObject rootObject = new JSONObject(json);
        if(rootObject.getInt("cod") != 200){
            System.out.println("Error");
            temp =0;
            humidity=0;
            clouds=0;
            pressure=0;
        }
        else {
            JSONObject mainObject = rootObject.getJSONObject("main");
            DecimalFormat df = new DecimalFormat("#.##");
            temp = mainObject.getDouble("temp");
            temp = temp -273;
            temp = Math.round(temp);
          //  temp = temp/10;

            System.out.println(temp);
            humidity = mainObject.getInt("humidity")*10;
            pressure = mainObject.getInt("pressure")/10;
            JSONObject cloudsObject = rootObject.getJSONObject("clouds");
            clouds = cloudsObject.getInt("all");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    notifyObservers();
                }
            });

        }
    }


    public void registerObserver(IWeatherObserver observer){
        observerList.add(observer);
    }

    private void notifyObservers() {
        WeatherInfo weatherInfo = new WeatherInfo(temp, pressure, humidity, clouds);
        for (IWeatherObserver iWeatherObserver : observerList){
            iWeatherObserver.onWeatherUpdate(weatherInfo);
    }
    }

}