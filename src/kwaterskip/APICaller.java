/*
 * Course: Direct Supply Kata
 * Weather Application
 * APICaller Class
 * Name: Peter Kwaterski
 * Last Updated: 11/17/24
 */

package kwaterskip;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class APICaller {

    private static final String KEY = "REDACTED";
    private static final int TIMEOUT = 10000;

    public static JSONObject getCity(String zipCode, String countryCode){
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
            try (Scanner reader = new Scanner(new InputStreamReader(apiConnection.getInputStream()))){
                while(reader.hasNext()){
                    cityInformation.append(reader.nextLine());
                }
            } catch (IOException e){
                System.out.println("Reader IO.");
            }
            apiConnection.disconnect();
            System.out.println(cityInformation);
            return new JSONObject(cityInformation.toString());
        } catch (MalformedURLException e){
            System.out.println("Bad URL");
        } catch (ProtocolException e){
            System.out.println("Bad Protocol");
        } catch (IOException e){
            System.out.println("Cannot Connect to API");
        }
        return null;
    }

    public static JSONObject getWeather(double lat, double lon){
        System.out.println("getting weather");
        String currentWeatherCall = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=imperial",
                lat, lon, KEY);
        try {
            URL url = URI.create(currentWeatherCall).toURL();
            HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.setConnectTimeout(TIMEOUT);
            apiConnection.setReadTimeout(TIMEOUT);
            apiConnection.connect();
            StringBuilder weatherInformation = new StringBuilder();
            System.out.println("creating sb");
            try (Scanner reader = new Scanner(new InputStreamReader(apiConnection.getInputStream()))){
                while(reader.hasNext()){
                    weatherInformation.append(reader.nextLine());
                }
                System.out.println("read json");
            } catch (IOException e){
                System.out.println("Reader IO.");
            }
            System.out.println("passing json");
            apiConnection.disconnect();
            System.out.println(weatherInformation);
            return new JSONObject(weatherInformation.toString());
        } catch (MalformedURLException e){
            System.out.println("Bad URL");
        } catch (ProtocolException e){
            System.out.println("Bad protocol");
        } catch (IOException e){
            System.out.println("Cannot read");
        }
        return null;
    }
}
