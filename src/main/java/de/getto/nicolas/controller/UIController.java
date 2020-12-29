package de.getto.nicolas.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.DoubleBinding;
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
	private TreeDrawerLayout drawerLayout;
	
	@Override
	public void initialize(URL location, ResourceBundle  resources) {
		drawer = new TreeDrawer();
		drawerLayout = new TreeDrawerLayout();
		
		rootContainer.setCenter(drawerLayout);
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