package Notepad;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class GotoDialog extends Dialog<Integer>{
	GotoController c;
	
	public GotoDialog(){
	    setTitle("ת��ָ����"); 
	    
	    FXMLLoader fxmlLoader = new FXMLLoader();
	    fxmlLoader.setLocation(getClass().getResource("Goto.fxml"));
	    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory()); 
	    
	    try {
	        Parent root = fxmlLoader.load(); //���ʹ�� Parent root = FXMLLoader.load(...) ��̬��ȡ�������޷���ȡ��Controller��ʵ������ 
	        getDialogPane().setContent(root);
	        c = fxmlLoader.getController();
	    } catch (IOException ex) {
	        Logger.getLogger(FontDialog.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
	    ButtonType loginButtonType1 = new ButtonType("ת��", ButtonData.OK_DONE);
	    ButtonType loginButtonType2 = new ButtonType("ȡ��", ButtonData.CANCEL_CLOSE);
	    getDialogPane().getButtonTypes().addAll(loginButtonType1, loginButtonType2); 
	    
	    setResultConverter(dialogButton -> {
	        if (dialogButton == loginButtonType1) {
	            return c.getLine();
	        }
	        return null;
	    }); 
	}
}
