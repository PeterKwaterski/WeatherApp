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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.UnaryOperator;

public class ForecastController {

    @FXML
    private Label cityName;

    @FXML
    private TextField zipBar;

    @FXML
    private TextField countryBar;

    @FXML
    private void search(){
        int zipCode = Integer.parseInt(zipBar.getText());
        String countryCode = countryBar.getText();

        JSONObject json = APICaller.getCity(zipCode, countryCode);
        try {
            if(json != null) {
                String name = json.getString("name");
                cityName.setText(name + ", " + countryCode);
                double lat = Double.parseDouble(json.getString("lat"));
                double lon = Double.parseDouble(json.getString("lon"));
                APICaller.getWeather(lat, lon);
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
    private void textSearch(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            search();
        }
    }

    @FXML
    public void initialize(){
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
    }
}
