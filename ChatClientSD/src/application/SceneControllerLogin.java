package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SceneControllerLogin {

	@FXML
	TextField txtIP; 
	@FXML
	TextField txtPort;
	@FXML
	Label errorLabel; 
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void declareError(String error) {
		errorLabel.setText(error);
	}
	
	public void startLocalNetwork(ActionEvent event) throws IOException {
		ChatClient.run();
		if (ChatClient.connected == true) {
		switchToChat(event);	
		}
		else {
		declareError("Failed to find server!");
		}
	}

	public void startExternalNetwork(ActionEvent event) throws IOException {
		int port = 0;
		
		String ip = txtIP.getText();
		String portString = txtPort.getText();
		
		try {
		port = Integer.parseInt(portString);
		}
		catch (NumberFormatException e) {
		declareError("Port must be a valid integer!");
		return;
		}
		if (port < 0 || port > 65536) {
		declareError("Port must be between 65536 and 0!");	
		return;
		}
		
		ChatClient.run(ip, port);
		
		if (ChatClient.connected == true) {
		switchToChat(event);	
		}
		else {
		declareError("Failed to find server!");
		}
	}
	
	public void switchToChat(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		
		SceneControllerMain sceneControllerMain = loader.getController();
		sceneControllerMain.initializeReader();
		
		//root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setMinHeight(430);
		stage.setMinWidth(630);
		stage.setResizable(true);
		stage.show();
		
	}
}
