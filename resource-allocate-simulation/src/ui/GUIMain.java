package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMain extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AppController appc = new AppController(primaryStage);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Window.fxml"));
		loader.setController(appc);
		Parent root = loader.load();
				
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Asignación de Salas");
		primaryStage.show();
	}

}
