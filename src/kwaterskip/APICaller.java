/*
 * Course: Direct Supply Kata
 * Weather Application
 * APICaller Class
 * Name: Peter Kwaterski
 * Last Updated: 11/18/24
 */

package kwaterskip;

import javafx.scene.control.Alert;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/**
 * APICaller Class. Contains two methods for calling two different APIs from OpenWeather.
 */
public class APICaller {

    static String KEY = System.getenv("OPEN_WEATHER_KEY");
    private static final int TIMEOUT = 10000;

    /**
     * Establishes a connection to the OpenWeather Geocoding API. The uses the
     * zipCode and CountryCode provided to retrieve and read the output from the
     * API. Converts and returns the output as a JSONObject.
     * @param zipCode the ZipCode of the location to retrieve the weather from
     * @param countryCode the two-letter country code of the country the zip code is from
     * @return JSONObject The JSON Retrieved read and constructed from the OpenWeather API
     */
    public static JSONObject getCity(String zipCode, String countryCode){
        if(KEY == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("No API Key was found.");
            alert.show();
        }
        try{
            String geoCodeCall = String.format(
                    "http://api.openweathermap.org/geo/1.0/zip?zip=%s,%s&appid=%s",
                    zipCode, countryCode, KEY);
            URL url = URI.create(geoCodeCall).toURL();
            HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.setConnectTimeout(TIMEOUT);
            apiConnection.setReadTimeout(TIMEOUT);
            apiConnection.connect();
            StringBuilder cityInformation = new StringBuilder();
            try (Scanner reader = new Scanner(new
                    InputStreamReader(apiConnection.getInputStream()))){
                while(reader.hasNext()){
                    cityInformation.append(reader.nextLine());
                }
                return new JSONObject(cityInformation.toString());
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setContentText("""
                        The API failed to provide an output.
                        this is likely due to an invalid Zip code, Country Code,
                        or a bad API key.
                        Please double check and try again""");
                alert.show();
            }
            apiConnection.disconnect();
        } catch (MalformedURLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The URL formed to connect to the API was bad.
                        Please double check everything and try again""");
            alert.show();
        } catch (ProtocolException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The protocol used to connect to the API was bad.
                        Please double check everything and try again""");
            alert.show();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The API Could not be connected to or read from
                        This could be caused by the API being offline
                        or another connection error.
                        Please wait and try again later.""");
            alert.show();
        }
        return null;
    }

    /**
     * Using the latitude and longitude received from the getCity() JSONObject
     * a call is made to the Current Weather Data API where it asks for
     * the data in imperial units at that location. Reads the output from the API
     * and converts into a JSONObject to return.
     * @param lat the latitude coordinate of the location
     * @param lon the longitude coordinate of the location
     * @return JSONObject containing all the current weather information
     * at the coordinates passed as parameters.
     */
    public static JSONObject getWeather(double lat, double lon){
        String currentWeatherCall = String.format(
                "https://api.openweathermap.org/data/2.5/weather?" +
                        "lat=%f&lon=%f&appid=%s&units=imperial",
                lat, lon, KEY);
        try {
            URL url = URI.create(currentWeatherCall).toURL();
            HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.setConnectTimeout(TIMEOUT);
            apiConnection.setReadTimeout(TIMEOUT);
            apiConnection.connect();
            StringBuilder weatherInformation = new StringBuilder();
            try (Scanner reader = new Scanner(new
                    InputStreamReader(apiConnection.getInputStream()))){
                while(reader.hasNext()){
                    weatherInformation.append(reader.nextLine());
                }
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setContentText("""
                        The API failed to provide an output.
                        Please try again""");
                alert.show();
            }
            apiConnection.disconnect();
            return new JSONObject(weatherInformation.toString());
        } catch (MalformedURLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The URL formed to connect to the API was bad.
                        Please double check everything and try again""");
            alert.show();
        } catch (ProtocolException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The protocol used to connect to the API was bad.
                        Please double check everything and try again""");
            alert.show();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setContentText("""
                        The API Could not be connected to or read from
                        This could be caused by the API being offline
                        or another connection error.
                        Please wait and try again later.""");
            alert.show();
        }
        return null;
    }
}
