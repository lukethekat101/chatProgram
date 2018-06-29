
/*
 * Create a GUI for each client that displays all
 * messages received by the server.
 */

import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application implements EventHandler<ActionEvent> {

	// Client Object
	private Client client;

	// Pop up window
	private String username;

	// Main window
	private Button exit;
	private TextField input;
	private TextArea display;
	private Stage window;
	private Scene scene1;

	// Connected users
	private TextArea connectedUsers;
	public ArrayList<String> allUsers = new ArrayList<>();

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;
		window.setTitle("Chat");

		// Main text area
		display = new TextArea();
		display.setPrefColumnCount(50);
		display.setPrefRowCount(50);
		display.setEditable(false);
		display.setWrapText(true);

		// Connected users text area
		connectedUsers = new TextArea("Online:\n");
		connectedUsers.setPrefColumnCount(10);
		connectedUsers.setPrefRowCount(10);
		connectedUsers.setEditable(false);
		connectedUsers.setWrapText(true);

		// Text Field
		input = new TextField();
		input.setPromptText("...");
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String fullData = username + "> " + input.getText();
					display.setText(display.getText() + fullData + "\n");
					client.send(input.getText());
					input.clear();
				}
			}
		});

		// Buttons
		exit = new Button("EXIT");
		exit.setOnAction(this);
		exit.setMinWidth(170);

		// GridPane configuration
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(3);
		grid.setVgap(3);
		grid.getChildren().addAll(exit, connectedUsers, input, display);

		// GridPane constraints
		GridPane.setConstraints(display, 0, 0);
		GridPane.setConstraints(input, 0, 1);
		GridPane.setConstraints(exit, 1, 1);
		GridPane.setConstraints(connectedUsers, 1, 0);

		// Scenes
		scene1 = new Scene(grid, 900, 600);
		grid.setStyle("-fx-background: black;");
		window.setScene(scene1);
		window.setResizable(false);
		window.show();
		window.setOnCloseRequest((WindowEvent event1) -> {
			System.exit(0);
		});

		// Background colors
		Region region = (Region) display.lookup(".content");
		region.setStyle("-fx-background-color: #FFFFFF;");
		display.setStyle("-fx-text-fill: #00e500;");
		display.setStyle("-fx-background: #00e500;");

		// Client object
		client = new Client(this);
		client.start();

		// Setting focus to text field
		input.requestFocus();

	}

	public void promptUsername() {
		do {
			username = JOptionPane.showInputDialog("Username: ");
			if (username == null) {
				System.exit(0);
			}
		} while (username.trim().equals(""));

	}

	// Updates Text Field screen
	public void displayMessage(String message) {
		display.setText(display.getText() + message + "\n");
	}

	public String getUsername() {
		return username;
	}

	public void updateUsers() {
		for (int i = 0; i < allUsers.size(); i++) {
			connectedUsers.setText("Online:\n" + allUsers.get(i));
		}
	}

	// Events
	public void handle(ActionEvent event) {
		if (event.getSource() == exit) {
			System.exit(0);
		}
	}

}
