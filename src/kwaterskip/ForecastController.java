/*
 * Course: Direct Supply Kata
 * Weather Application
 * ForecastController Class
 * Name: Peter Kwaterski
 * Last Updated: 11/17/24
 */


package kwaterskip;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.UnaryOperator;

public class ForecastController {

    @FXML
    private Label cityName;

    @FXML
    private Label temp;

    @FXML
    private Label feelsLike;

    @FXML
    private Label weatherType;

    @FXML
    private Label windSpeed;

    @FXML
    private Label humidity;

    @FXML
    private TextField zipBar;

    @FXML
    private TextField countryBar;

    @FXML
    private ImageView weatherImage;

    @FXML
    private void search(){
        int zipCode = Integer.parseInt(zipBar.getText());
        String countryCode = countryBar.getText();
        applyInformation(zipCode, countryCode);
    }

    @FXML
    private void textSearch(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            search();
        }
    }

    @FXML
    public void initialize(){
        final int mkeZip = 53202;
        //Ensures that only integer values can be entered into the Zip Code Search Bar
        zipBar.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if(!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        UnaryOperator<TextFormatter.Change> filter = convert -> {
            String upperCase = convert.getText();
            if(upperCase.matches("[a-zA-Z]*")) {
                convert.setText(upperCase.toUpperCase());
                return convert;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        countryBar.setTextFormatter(textFormatter);

        applyInformation(mkeZip, "US");
    }

    private void applyInformation(int zipCode, String countryCode){
        JSONObject json = APICaller.getCity(zipCode, countryCode);
        try {
            if(json != null) {
                String name = json.getString("name");
                cityName.setText(name + ", " + countryCode);
                double lat = json.getDouble("lat");
                double lon = json.getDouble("lon");
                json = APICaller.getWeather(lat, lon);

                JSONObject mainNumerics = json.getJSONObject("main");
                double currTemp = mainNumerics.getDouble("temp");
                double feelsLikeTemp = mainNumerics.getDouble("feels_like");
                double humidPercent = mainNumerics.getDouble("humidity");
                JSONObject windNum = json.getJSONObject("wind");
                double speed = windNum.getDouble("speed");
                JSONArray weatherArr = json.getJSONArray("weather");
                JSONObject weather = weatherArr.getJSONObject(0);
                String weatherCondition = weather.getString("main");

                temp.setText(String.valueOf(currTemp));
                feelsLike.setText(String.valueOf(feelsLikeTemp));
                humidity.setText(String.valueOf(humidPercent));
                windSpeed.setText(String.valueOf(speed));
                weatherType.setText(weatherCondition);


            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            System.out.println("No City Name from the API");
        } catch (JSONException e) {
            System.out.println("JSON could not be read");
        }
    }
}
