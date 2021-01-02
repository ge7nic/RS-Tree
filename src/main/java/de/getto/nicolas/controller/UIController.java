package de.getto.nicolas.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.*;

public class UIController {
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private BorderPane rootContainer;
	@FXML
	private HBox hBox;
	
	private TreePane treePane;
	
	public void initialize() {
		treePane = new TreePane();
		
		rootContainer.setCenter(treePane);
	}

	@FXML
	private void insertButtonClicked() {
		
	}
	
	@FXML
	private void deleteButtonClicked() {
		
	}
	
	@FXML
	private void searchButtonClicked() {
		treePane.animateSearchNodeTest();
	}

	@FXML
	private void drawButtonClicked() {
		treePane.drawTree();
	}
}