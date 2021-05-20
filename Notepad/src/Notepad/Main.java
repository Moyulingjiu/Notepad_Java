package Notepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
			
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("无标题 - 记事本");
			primaryStage.show();
			Controller.stage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		launch(args);
	}

}
