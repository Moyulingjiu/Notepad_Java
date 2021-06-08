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
	    setTitle("转到指定行"); 
	    
	    FXMLLoader fxmlLoader = new FXMLLoader();
	    fxmlLoader.setLocation(getClass().getResource("Goto.fxml"));
	    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory()); 
	    
	    try {
	        Parent root = fxmlLoader.load(); //如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象 
	        getDialogPane().setContent(root);
	        c = fxmlLoader.getController();
	    } catch (IOException ex) {
	        Logger.getLogger(FontDialog.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
	    ButtonType loginButtonType1 = new ButtonType("转到", ButtonData.OK_DONE);
	    ButtonType loginButtonType2 = new ButtonType("取消", ButtonData.CANCEL_CLOSE);
	    getDialogPane().getButtonTypes().addAll(loginButtonType1, loginButtonType2); 
	    
	    setResultConverter(dialogButton -> {
	        if (dialogButton == loginButtonType1) {
	            return c.getLine();
	        }
	        return null;
	    }); 
	}
}
