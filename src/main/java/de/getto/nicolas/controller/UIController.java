package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class UIController {
	
	private final static String ERROR_STYLE = "-fx-text-fill: red";
	private final static String WARNING_STYLE = "-fx-text-fill: #CCCC00";
	private final static String SUCCESS_STYLE = "-fx-text-fill: green";
	
	@FXML
	private BorderPane rootContainer;
	@FXML
	private TextField inputField;
	@FXML
	private TextField console;
	
	private TreeCanvas canvas;

	public void initialize() {
		canvas = new TreeCanvas();
		
		rootContainer.setCenter(canvas);
		canvas.widthProperty().bind(rootContainer.widthProperty());
		canvas.heightProperty().bind(rootContainer.heightProperty().subtract(70));
	}
	
	@FXML
	private void insertButtonClicked() {
		String input = inputField.getText();
		try {
			int newInput = Integer.valueOf(input);
			canvas.insertNewNode(newInput);
			canvas.writeOnConsole(console, input + " added.", SUCCESS_STYLE);
		} catch (NumberFormatException e) {
			canvas.writeOnConsole(console, input + " is not a valid number.", ERROR_STYLE);
		}
	}
	
	@FXML
	private void deleteButtonClicked() {
		String input = inputField.getText();
		try {
			int newInput = Integer.valueOf(input);
			canvas.removeNode(newInput);
			canvas.writeOnConsole(console, input + " removed.", SUCCESS_STYLE);
		} catch (NumberFormatException e) {
			canvas.writeOnConsole(console, input + " is not a valid number.", ERROR_STYLE);
		}
	}
	
	@FXML
	private void searchButtonClicked() {
		
	}
	
	@FXML
	private void drawButtonClicked() {
		canvas.test();
		canvas.writeOnConsole(console, "Test Warning", WARNING_STYLE);
	}
}
