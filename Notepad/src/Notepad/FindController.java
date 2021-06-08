package Notepad;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class FindController implements Initializable {
	@FXML
	private RadioButton Up;
	@FXML
	private RadioButton Down;

	private ToggleGroup group = new ToggleGroup();

	@FXML
	private CheckBox Case;
	@FXML
	private CheckBox LoopFind;
	@FXML
	private TextField TextFind;

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

		int p = -1;
		if (Up.isSelected()) {
			p = handle.lastIndexOf(find, pos);

			if (p == -1 && LoopFind.isSelected()) {
				p = handle.lastIndexOf(find);
			}

			if (p != -1)
				pos = p - 1;
		} else if (Down.isSelected()) {
			p = handle.indexOf(find, pos);

			if (p == -1 && LoopFind.isSelected()) {
				p = handle.indexOf(find);
			}

			if (p != -1)
				pos = p + getTextLength();
		}
		

		return p;
	}

	public int findLast() {
		if (!Case.isSelected()) { // 区分大小写
			handle = text.toLowerCase();
			find = TextFind.getText().toLowerCase();
		} else {
			handle = text;
			find = TextFind.getText();
		}

		int p = -1;
		if (Up.isSelected()) {
			p = handle.indexOf(find, pos);

			if (p == -1 && LoopFind.isSelected()) {
				p = handle.indexOf(find);
			}

			if (p != -1)
				pos = p + getTextLength();
		} else if (Down.isSelected()) {
			p = handle.lastIndexOf(find, pos);

			if (p == -1 && LoopFind.isSelected()) {
				p = handle.lastIndexOf(find);
			}

			if (p != -1)
				pos = p - 1;
		}

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
		Up.setToggleGroup(group);
		Down.setToggleGroup(group);
		Down.setSelected(true);
	}

	public void _findNext(ActionEvent event) {
		int p = findNext();
		if (p != -1)
			controller.selectText(p, getTextLength());
	}

	public void _close(ActionEvent event) {
		exit();
	}

}
