package Notepad;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	Controller c;

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("Scene.fxml"));
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

		try {
//			Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
			Parent root = fxmlLoader.load();
			c = fxmlLoader.getController();

			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("�ޱ��� - ���±�");
			primaryStage.show();
			Controller.stage = primaryStage;
			// �رռ���
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					System.out.print("�ر��¼�");
					if (!c.saveCheck()) {
						event.consume();
					}
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
