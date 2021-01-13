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
			treePane.insert(newVal, 2);
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
			if (!treePane.delete(newVal)) {
				Alert newAlert = new Alert(Alert.AlertType.WARNING, input + " is not a Node in this Tree.");
				newAlert.showAndWait();
			}
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
			treePane.search(newVal, 2);
		} catch (NumberFormatException e) {
			Alert newAlert = new Alert(Alert.AlertType.ERROR, input + " is not a valid number.");
			newAlert.showAndWait();
		}
	}

	@FXML
	private void drawButtonClicked() {
		treePane.test();
	}
}