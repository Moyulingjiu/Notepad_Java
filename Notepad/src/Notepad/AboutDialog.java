package Notepad;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.text.Font;
import javafx.scene.control.ButtonBar.ButtonData;

public class AboutDialog extends Dialog<Font>{
	AboutController c;
	
	public AboutDialog(){
	    setTitle("����\"���±�\""); 
	    
	    FXMLLoader fxmlLoader = new FXMLLoader();
	    fxmlLoader.setLocation(getClass().getResource("About.fxml"));
	    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory()); 
	    
	    try {
	        Parent root = fxmlLoader.load(); //���ʹ�� Parent root = FXMLLoader.load(...) ��̬��ȡ�������޷���ȡ��Controller��ʵ������ 
	        getDialogPane().setContent(root);
	        c = fxmlLoader.getController();
	    } catch (IOException ex) {
	        Logger.getLogger(FontDialog.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
	    ButtonType loginButtonType1 = new ButtonType("ȷ��", ButtonData.OK_DONE);
	    getDialogPane().getButtonTypes().addAll(loginButtonType1); 
	}
}
