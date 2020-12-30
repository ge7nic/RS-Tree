package de.getto.nicolas;

import java.io.IOException;
import java.net.URL;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.*;
import javafx.stage.*;
import javafx.stage.StageStyle;

@SuppressWarnings("unused")
public class App extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		URL mainURL = getClass().getResource("/MainWindow.fxml");
		loader.setLocation(mainURL);
		Parent root = loader.load();
		
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void sampleProgram(Stage stage) {
		String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
	}
	
	private void tryoutProgram(Stage stage) {
	}

}
