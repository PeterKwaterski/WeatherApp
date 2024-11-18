/*
 * Course: Direct Supply Kata
 * Weather Application
 * ForecastController Class
 * Name: Peter Kwaterski
 * Last Updated: 11/18/24
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

import java.io.IOException;
import java.io.InputStream;
import java.util.function.UnaryOperator;

/**
 * Controller Class for forecast.fxml.
 * Controls reading the JSONObjects and setting the contents accordingly.
 */
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

    /**
     * Initializing function for the controller.
     * Sets the rules for what is allowed to be entered into the two search bars.
     * For the Zip Code bar it allows numbers and letters because some countries
     * have letters in their zip codes. This initialization any letter typed
     * to be capitalized in both search bars. In addition, only letters may be typed
     * in the country code bar.
     * Immediately retrieves the data based on the Downtown Milwaukee 53202 zip
     * code so current weather is immediately displayed to the user before any
     * in put is needed.
     */
    @FXML
    public void initialize(){
        //Sets only integers and letters to be entered into the Zip Code Field
        //Makes all letters capital
        UnaryOperator<TextFormatter.Change> zipFilter = convert -> {
            String upperCase = convert.getText();
            if(upperCase.matches("[0-9a-zA-Z]*")) {
                convert.setText(upperCase.toUpperCase());
                return convert;
            }
            return null;
        };

        //Sets it so only letters can be entered in the Country Code Field
        //and automatically capitalizes them
        UnaryOperator<TextFormatter.Change> countryfilter = convert -> {
            String upperCase = convert.getText();
            if(upperCase.matches("[a-zA-Z]*")) {
                convert.setText(upperCase.toUpperCase());
                return convert;
            }
            return null;
        };

        //Apply the formats
        TextFormatter<String> zipFormatter = new TextFormatter<>(zipFilter);
        zipBar.setTextFormatter(zipFormatter);
        TextFormatter<String> countryFormatter = new TextFormatter<>(countryfilter);
        countryBar.setTextFormatter(countryFormatter);

        //Passes the MKE codes to initialize the program
        applyInformation("53202", "US");
    }

    /**
     * Takes the zip and country codes and then passes them to the APICaller
     * to get the information from the API. It then takes the JSONObjects and
     * takes the data from them to apply to the GUI.
     * @param zipCode the Zip Code of the location
     * @param countryCode country code of the location.
     */
    private void applyInformation(String zipCode, String countryCode){
        JSONObject json = APICaller.getCity(zipCode, countryCode);
        try {

            if(json != null) {
                String name = json.getString("name");
                cityName.setText(name + ", " + countryCode);
                double lat = json.getDouble("lat");
                double lon = json.getDouble("lon");
                json = APICaller.getWeather(lat, lon);

                if(json != null) {
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

                    String path = String.format("/%s.jpg", weatherCondition);

                    try(InputStream inputStream =
                                ForecastController.class.getResourceAsStream(path)){
                        if(inputStream != null) {
                            Image image = new Image(inputStream);
                            weatherImage.setImage(image);
                        } else {
                            throw new NullPointerException();
                        }
                    } catch (NullPointerException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("WARNING!");
                        alert.setContentText("The image corresponding to the\n" +
                                "current weather could not be found.");
                        alert.show();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("WARNING!");
                        alert.setContentText("The image corresponding to the\n" +
                                "current weather could not be read.");
                        alert.show();
                    }
                } else{
                    throw new NullPointerException();
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The API provided a null output
                        this is likely due to an invalid Zip or Country Code.
                        Please double check and try again""");
            alert.show();
        } catch (JSONException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The JSON provided by the API was missing information.
                        Please try again""");
            alert.show();
        }
    }

    /**
     * When the about button is hit a small window appears that
     * gives basic information about how to use the app and immediate errors.
     */
    @FXML
    private void about(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("How to Use the App");
        info.setContentText("""
                Enter a Zip/Postal Code into the Zip Code Box
                In the Country Code Box enter the country's 2-digit ISO code
                If when the program is launched many errors and
                no data shows up then the API key is likely bad.
                See ReadMe for details.""");
        info.setHeaderText("How To");
        info.show();
    }
}
