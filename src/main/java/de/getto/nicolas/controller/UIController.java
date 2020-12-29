package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class UIController {

	@FXML
	private Button clickButton;
	
	@FXML
	private Label label;
	
	@FXML
	private AnchorPane centerAnchorPane;
	
	@FXML
	private void buttonClicked() {
		System.out.println("Clicked on " + clickButton.getText());
	}
}
