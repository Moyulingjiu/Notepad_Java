package Notepad;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ReplaceController implements Initializable {
	@FXML
	private CheckBox Case;
	@FXML
	private CheckBox LoopFind;
	@FXML
	private TextField TextFind;
	@FXML
	private TextField TextReplace;

	private String text;
	private String handle;
	private String find;
	private int pos;
	private Controller controller;
	
	public int findNext() {
		if (!Case.isSelected()) { // 区分大小写
			handle = text.toLowerCase();
			find = TextFind.getText().toLowerCase();
		} else {
			handle = text;
			find = TextFind.getText();
		}

		int p = handle.indexOf(find, pos);

		if (p == -1 && LoopFind.isSelected()) {
			p = handle.indexOf(find);
		}

		if (p != -1)
			pos = p + getTextLength();
		

		return p;
	}


	public void exit() {
		controller.secondStageExit();
	}

	public int getTextLength() {
		return TextFind.getText().length();
	}

	public void setTextFind(String textFind) {
		TextFind.setText(textFind);
	}

	public void setText(String str, int position) {
		text = str;
		pos = position;
	}
	
	public void setController(Controller c) {
		controller = c;
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自动生成的方法存根
		
	}
	

	public void _findNext(ActionEvent event) {
		int p = findNext();
		if (p != -1)
			controller.selectText(p, getTextLength());
	}
	
	public void _replace(ActionEvent event) {
		String sel = controller.getSelected();
		if (!Case.isSelected()) { // 区分大小写
			find = TextFind.getText().toLowerCase();
			sel = sel.toLowerCase();
		} else {
			find = TextFind.getText();
		}
		
		if (sel.equals(find)) {
			int tmp = pos;
			controller.selectTextReplace(TextReplace.getText());
			pos = tmp + TextReplace.getText().length();
		}
		
		
		int p = findNext();
		if (p != -1)
			controller.selectText(p, getTextLength());
		controller.refreshLine();
	}

	public void _replaceAll(ActionEvent event) {
		pos = 0;
		int p = findNext();
		if (p != -1) {
			int tmp = pos;
			controller.selectText(p, getTextLength());
			pos = tmp + TextReplace.getText().length();
		}
		while (p != -1) {
			controller.selectTextReplace(TextReplace.getText());
			p = findNext();
			if (p != -1) {
				int tmp = pos;
				controller.selectText(p, getTextLength());
				pos = tmp + TextReplace.getText().length();
			}
		}
		controller.refreshLine();
	}

	public void _close(ActionEvent event) {
		exit();
	}

}
