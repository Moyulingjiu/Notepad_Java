package Notepad;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {
	public static Stage stage;
	@FXML
	private BorderPane root;
	@FXML
	private TextArea text;

	@FXML
	private Label pos;

	@FXML
	private MenuItem menu_search;

	private String last = "";
	private String initText = "";
	private String filename = "";
	private String title = "�ޱ��� - ���±�";

	private void recordText() {
		last = text.getText();
	}

	private boolean saveCheck() {
		if (!initText.equals(text.getText())) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("���±�");
			alert.setHeaderText("������ı��浽 " + title.substring(1, title.length() - 5) + "��");
			// alert.setContentText("Choose your option.");

			ButtonType buttonTypeConfirm = new ButtonType("��");
			ButtonType buttonTypeRefuse = new ButtonType("��");
			ButtonType buttonTypeCancel = new ButtonType("ȡ��", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeConfirm, buttonTypeRefuse, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeConfirm) {
				System.out.println("ȷ�ϱ���");
				if (_saveFile())
					return true;
				else
					return false;
			} else if (result.get() == buttonTypeRefuse) {
				System.out.println("ȡ������");
				return true;
			} else {
				System.out.println("ȡ��");
				return false;
			}
		} else
			return true;
	}

	private boolean _saveFile() {
		if (filename.equals("")) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.setTitle("����");
			fileChooser.setInitialFileName("C:/*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showOpenDialog(stage);
			if (file == null)
				return false;
			filename = file.toString();
		}
		_saveFile(filename);
		return true;
	}

	private void _saveFile(String dir) {
		File file = new File(dir);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(text.getText());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menu_search.setDisable(false);
		
		// �������
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//		        System.out.println("observable = " + observable + ", oldValue = " + oldValue + ", newValue = " + newValue);
				if (initText.equals(text.getText())) {
					if (title.substring(0, 1).equals("*"))
						title = title.substring(1);
					stage.setTitle(title);
				} else {
					if (!title.substring(0, 1).equals("*"))
						title = "*" + title;
					stage.setTitle(title);
				}
			}
		});
		// ѡ�м���
		text.selectedTextProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//		        System.out.println("observable = " + observable + ", oldValue = " + oldValue + ", newValue = " + newValue);
				String select = text.getSelectedText();
				if (select.length() == 0) {
					menu_search.setDisable(true);
				} else {
					menu_search.setDisable(false);
				}
			}
		});
	}

	// ==============================================
	// �ļ��˵���
	public void newFile(ActionEvent event) {
		if (saveCheck()) {
			last = "";
			initText = "";
			filename = "";
			text.setText(last);
			stage.setTitle("�ޱ��� - ���±�");
		}
	}

	public void openFile(ActionEvent event) {
		if (saveCheck()) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.setTitle("��");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showOpenDialog(stage);
			if (file == null) {
				return;
			}
			filename = file.toString();

			BufferedReader bf;
			try {
				bf = new BufferedReader(new FileReader(file));
				String content = "";
				String tmp = "";
				while (content != null) {
					try {
						content = bf.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (content == null) {
						break;
					}

					tmp += content + "\n";
				}
				text.setText(tmp); // �����ı�
				initText = tmp;
				last = tmp;
				String[] filename_tmp = filename.split("\\\\");
				System.out.println(filename);
				System.out.println(filename_tmp.length);
				title = filename_tmp[filename_tmp.length - 1] + " - ���±�";
				stage.setTitle(title);
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveFile(ActionEvent event) {
		_saveFile();
	}

	public void saveAsFile(ActionEvent event) {
		String tmp = filename;
		filename = "";
		if (!_saveFile())
			filename = tmp;
	}

	public void pageSet(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("���±�");
		alert.setHeaderText("ҳ������");

		alert.showAndWait();
	}

	public void pagePrint(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("���±�");
		alert.setHeaderText("��ӡ");

		alert.showAndWait();
	}

	public void exit(ActionEvent event) {
		if (saveCheck()) {
			Platform.exit();
		}
	}

	// ==============================================
	// �༭�˵���
	public void revoke(ActionEvent event) {
		String tmp = last;
		recordText();
		text.setText(tmp);
		text.selectAll();
	}

	public void cut(ActionEvent event) {
		String select = text.getSelectedText();
		recordText();
		text.replaceSelection("");
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(select);
		clip.setContents(tText, null);
		System.out.println("�ѽ����ݸ��Ƶ�ճ���� - " + select);
	}

	public void copy(ActionEvent event) {
		String select = text.getSelectedText();
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(select);
		clip.setContents(tText, null);
		System.out.println("�ѽ����ݸ��Ƶ�ճ���� - " + select);
	}

	public void paste(ActionEvent event) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// ��ȡ���а��е�����
		Transferable clipT = clip.getContents(null);
		if (clipT != null) {
			// ��������Ƿ����ı�����
			if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
					recordText();
					text.insertText(text.getCaretPosition(), context);
					System.out.println("�ѽ�����ճ�����ı� - " + context);
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void delete(ActionEvent event) {
		recordText();
		text.replaceSelection("");
	}

	public void bingSearch(ActionEvent event) {
		String url = String.format("https://cn.bing.com/search?q=%s&form=NPCTXT", text.getSelectedText());
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri;
			try {
				uri = new URI(url);
				desktop.browse(uri);
			} catch (URISyntaxException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
        }
	}

	public void selectAll(ActionEvent event) {
		text.selectAll();
	}

	public void insertDate(ActionEvent event) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm yyyy/MM/dd");
		recordText();
		text.insertText(text.getCaretPosition(), formatter.format(date));
	}

	// ==============================================
	// ��ʽ�˵���

	// ==============================================
	// �鿴�˵���

	// ==============================================
	// �����˵���
	
	public void viewHelp(ActionEvent event) {
		String url = String.format("https://cn.bing.com/search?q=%s&form=NPCTXT", "��ȡ�й� windows 10 �еļ��±��İ���");
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri;
			try {
				uri = new URI(url);
				desktop.browse(uri);
			} catch (URISyntaxException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
        }
	}
}
