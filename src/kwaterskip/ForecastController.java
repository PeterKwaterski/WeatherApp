/*
 * Course: Direct Supply Kata
 * Weather Application
 * ForecastController Class
 * Name: Peter Kwaterski
 * Last Updated: 11/17/24
 */


package kwaterskip;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

import java.io.File;
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
        String zipCode = zipBar.getText();
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
        //Ensures that only integer values can be entered into the Zip Code Search Bar
        UnaryOperator<TextFormatter.Change> zipFilter = convert -> {
            String upperCase = convert.getText();
            convert.setText(upperCase.toUpperCase());
            return convert;
        };

        UnaryOperator<TextFormatter.Change> countryfilter = convert -> {
            String upperCase = convert.getText();
            if(upperCase.matches("[a-zA-Z]*")) {
                convert.setText(upperCase.toUpperCase());
                return convert;
            }
            return null;
        };

        TextFormatter<String> zipFormatter = new TextFormatter<>(zipFilter);
        zipBar.setTextFormatter(zipFormatter);
        TextFormatter<String> countryFormatter = new TextFormatter<>(countryfilter);
        countryBar.setTextFormatter(countryFormatter);

        applyInformation("53202", "US");
    }

    private void applyInformation(String zipCode, String countryCode){
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

                String path = String.format("data/%s.jpg", weatherCondition);
                try {
                    File file = new File(path);
                    Image image = new Image(file.toURI().toString());
                    weatherImage.setImage(image);
                } catch (NullPointerException e){
                    System.out.println("uh oh");
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            System.out.println("No City Name from the API");
        } catch (JSONException e) {
            System.out.println("JSON could not be read");
        }
    }

    @FXML
    private void about(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("How to Use the App");
        info.setContentText("Enter a Zip/Postal Code into the Zip Code Box\n" +
                "In the Country Code Box enter the country's 2-digit ISO code\n " +
                "then hit either enter in a box or press search");
    }
}
