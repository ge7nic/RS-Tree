package de.getto.nicolas;

import java.io.IOException;
import java.net.URL;

import de.getto.nicolas.node.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Stop;
import javafx.scene.*;
import javafx.stage.*;
import javafx.stage.StageStyle;
import javafx.fxml.*;

@SuppressWarnings("unused")
public class App extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL mainURL = getClass().getResource("/MainScene.fxml");
		loader.setLocation(mainURL);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.setTitle("RS-Tree Viewer");
		
		stage.show();
	} 
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
