package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class UIController {
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private BorderPane rootContainer;
	@FXML
	private HBox hBox;
	@FXML
	private TextField inputField;
	@FXML
	private TextField console;
	@FXML
	private CheckBox animButton;
	
	private TreePane treePane;
	
	private static final int ANIMATION_LENGTH = 2;
	
	public void initialize() {
		treePane = new TreePane(hBox, console);
		
		rootContainer.setCenter(treePane);
	}

	@FXML
	private void insertButtonClicked() {
		String input = inputField.getText();
		
		try {
			int newVal = Integer.valueOf(input);
			if (newVal > 9999 || newVal < -9999) {
				Alert newAlert = new Alert(Alert.AlertType.WARNING, "Value can not be over 9999 or under -9999.");
				newAlert.showAndWait();
				return;
			}
			treePane.insert(newVal, ANIMATION_LENGTH);
		} catch (NumberFormatException e) {
			Alert newAlert = new Alert(Alert.AlertType.ERROR, input + " is not a valid number.");
			newAlert.showAndWait();
		}
	}
	
	@FXML
	private void deleteButtonClicked() {
		String input = inputField.getText();
		
		try {
			int newVal = Integer.valueOf(input);
			treePane.delete(newVal, ANIMATION_LENGTH);
		} catch (NumberFormatException e) {
			Alert newAlert = new Alert(Alert.AlertType.ERROR, input + " is not a valid number.");
			newAlert.showAndWait();
		}
	}
	
	@FXML
	private void searchButtonClicked() {
		String input = inputField.getText();
		
		try {
			int newVal = Integer.valueOf(input);
			treePane.search(newVal, ANIMATION_LENGTH);
		} catch (NumberFormatException e) {
			Alert newAlert = new Alert(Alert.AlertType.ERROR, input + " is not a valid number.");
			newAlert.showAndWait();
		}
	}

	@FXML
	private void drawButtonClicked() {
		
	}
}