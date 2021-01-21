package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
	@FXML
	private ChoiceBox<String> choiceBoxTreeWalkType;
	
	private TreePane treePane;
	
	private static final int ANIMATION_LENGTH = 2;
	private String treeWalkType;
	
	public void initialize() {
		treePane = new TreePane(hBox, console);
		
		rootContainer.setCenter(treePane);
		treeWalkType = "Preorder";
	}

	@FXML
	private void insertButtonClicked() {
		String input = retrieveInput();
		
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
		String input = retrieveInput();
		
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
		String input = retrieveInput();
		
		try {
			int newVal = Integer.valueOf(input);
			treePane.search(newVal, ANIMATION_LENGTH);
		} catch (NumberFormatException e) {
			Alert newAlert = new Alert(Alert.AlertType.ERROR, input + " is not a valid number.");
			newAlert.showAndWait();
		}
	}
	
	@FXML
	private void treeWalkButtonClicked() {
		treePane.treeWalk(treeWalkType, 1);
	}
	
	@FXML
	private void typeChanged() {
		treeWalkType = choiceBoxTreeWalkType.getValue().toString();
		System.out.println(treeWalkType);
		
	}
	
	@FXML
	private void clearTreeButtonClicked() {
		treePane.clearTree();
	}
	
	/**============================================================================
	 * HERE ARE HELPER METHODS
	 * ============================================================================
	 */
	
	/**
	 * Retrieving the input means saving it and then clearing the Input Field.
	 * @return The User input as a String.
	 */
	private String retrieveInput() {
		String res = inputField.getText();
		inputField.clear();
		
		return res;
	}
}