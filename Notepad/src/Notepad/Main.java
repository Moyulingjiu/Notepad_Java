package Notepad;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
			
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("�ޱ��� - ���±�");
			primaryStage.show();
			Controller.stage = primaryStage;
			// �رռ���
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	                System.out.print("�ر��¼�");
	            }
	        });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		launch(args);
	}

}
