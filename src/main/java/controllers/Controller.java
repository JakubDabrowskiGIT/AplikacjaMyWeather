package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.WeatherInfo;
import models.services.IWeatherObserver;
import models.services.WeatherService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable,IWeatherObserver {


    @FXML
    Button buttonShowWeather;

    @FXML
    TextField textFieldTypeCity;

    @FXML
    Label labelTemp;

    @FXML
    Label labelPressure;

    @FXML
    Label labelHumidity;

    @FXML
    Label labelClouds;

    @FXML
    ProgressIndicator progress;

    private WeatherService weatherService = WeatherService.getOurService();


    private void weatherInformation() {
        try {
            weatherService.makeCall(textFieldTypeCity.getText());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL location, ResourceBundle resources) {

        weatherService.registerObserver(this);

        textFieldTypeCity.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                textFieldTypeCity.clear();
            }
        });

        textFieldTypeCity.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                    weatherInformation();
            }
        });
        buttonShowWeather.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                weatherInformation();
                progress.setVisible(true);
            }
        });
    }

    public void onWeatherUpdate(WeatherInfo info) {
        labelTemp.setText("Temperatura: " + info.getTemp() + " \u00b0C");
        labelHumidity.setText("Wilgotność: " + info.getHumidity() + "%");
        labelPressure.setText("Ciśnienie: " + info.getPressure() + " hPa");
        labelClouds.setText("Zachmurzenie: " + info.getClouds() + "%");
        progress.setVisible(false);
    }

}
