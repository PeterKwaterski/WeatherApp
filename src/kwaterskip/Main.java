/*
 * Course: Direct Supply Kata
 * Weather Application
 * Main Class
 * Name: Peter Kwaterski
 * Last Updated: 11/17/24
 */

package kwaterskip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forecast.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Weather App");
        stage.show();
    }
}
