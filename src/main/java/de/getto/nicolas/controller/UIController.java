package de.getto.nicolas.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.fxml.Initializable;

public class UIController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private BorderPane rootContainer;
	
	@FXML
	private HBox hBox;
	
	private TreeDrawer drawer;
	
	@Override
	public void initialize(URL location, ResourceBundle  resources) {
		drawer = new TreeDrawer();
		
		rootContainer.setCenter(drawer);
		drawer.widthProperty().bind(rootContainer.widthProperty());
		drawer.heightProperty().bind(rootContainer.heightProperty().subtract(hBox.heightProperty()));
	}

	@FXML
	private void insertButtonClicked() {
		
	}
	
	@FXML
	private void deleteButtonClicked() {
		
	}
	
	@FXML
	private void searchButtonClicked() {
		
	}

	@FXML
	private void drawButtonClicked() {
		drawer.drawTree();
	}
}