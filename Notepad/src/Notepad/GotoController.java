package Notepad;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class GotoController implements Initializable {
	@FXML
	private TextField text;
	
	private int lineNumber = 1;
	private String init = "1";
	
	public int getLine() {
		return lineNumber;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 输入监听
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String nowstr = text.getText(); 
				Pattern pattern = Pattern.compile("[0-9]*"); 
				Matcher matcher = pattern.matcher((CharSequence) nowstr); 
				boolean result = matcher.matches(); 
				if (result) { 
				    if (nowstr.length() == 0) {
				    	lineNumber = 1;
				    	init = "1";
				    } else {
				    	lineNumber = Integer.parseInt(nowstr);
				    	init = nowstr;
				    }
				} else { 
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("转到指定行");
					alert.setHeaderText("非法字符");
					alert.setContentText("只能输入数字！");
					alert.showAndWait();
				    text.setText(init);
				}
			}
		});
	}

}
