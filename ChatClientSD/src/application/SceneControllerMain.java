package application;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SceneControllerMain {
	
	@FXML
	TextField txtMessage; 
	@FXML
	TextArea txtRead;
	
	public void initializeReader() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> readMessageLoop());
	}
	
	public void readMessageLoop() {
		while (ChatClient.serverChannel.isOpen()) {
		String message = ChatClient.readMessage();
		txtRead.appendText(message + "\n");
		}
	}

	public void sendMessage(ActionEvent event) {
		ChatClient.sendMessage(txtMessage.getText());
		txtMessage.setText("");
		txtMessage.requestFocus();
	}
	
	
}
