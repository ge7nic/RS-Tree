package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class UIController {
	
	@FXML
	private BorderPane rootContainer;
	
	private TreeCanvas canvas;

	public void initialize() {
		canvas = new TreeCanvas();
		
		rootContainer.setCenter(canvas);
		canvas.widthProperty().bind(rootContainer.widthProperty());
		canvas.heightProperty().bind(rootContainer.heightProperty().subtract(50));
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
		canvas.test();
	}
}
