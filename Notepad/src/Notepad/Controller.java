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

import javax.swing.filechooser.FileSystemView;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

public class Controller implements Initializable {
	public static Stage stage;
	@FXML
	private BorderPane root;
	@FXML
	private TextArea text;

	@FXML
	private HBox state;
	@FXML
	private Label position;
	@FXML
	private Label zoom;

	@FXML
	private MenuItem menu_newWindow;
	
	// =================================
	// 编辑
	@FXML
	private MenuItem menu_revoke;
	@FXML
	private MenuItem menu_cut;
	@FXML
	private MenuItem menu_copy;
	@FXML
	private MenuItem menu_paste;
	@FXML
	private MenuItem menu_delete;

	@FXML
	private MenuItem menu_search_text;
	@FXML
	private MenuItem menu_search_next;
	@FXML
	private MenuItem menu_search_last;
	@FXML
	private MenuItem menu_replace;

	@FXML
	private MenuItem menu_goto;

	@FXML
	private MenuItem menu_search;

	

	@FXML
	private MenuItem menu_revoke2;
	@FXML
	private MenuItem menu_cut2;
	@FXML
	private MenuItem menu_copy2;
	@FXML
	private MenuItem menu_paste2;
	@FXML
	private MenuItem menu_delete2;
	@FXML
	private MenuItem menu_search2;
	@FXML
	private MenuItem menu_formRight;
	@FXML
	private MenuItem menu_showUnicode;
	@FXML
	private MenuItem menu_closeInput;
	@FXML
	private MenuItem menu_Rechoose;
	// =================================
	// 格式
	@FXML
	private MenuItem menu_autoChangeLine;

	// =================================
	// 查看
	@FXML
	private MenuItem menu_stateBar;

	// =================================
	// 系统变量
	private String last = "";
	private String initText = "";
	private String filename = "";
	private String title = "无标题 - 记事本";
	private int fontSize = 14;
	private int nowFontSize = 14;
	public String findString = ""; // 查找的字符串
	public String replaceString = ""; // 要替换的字符串
	
	Stage secondStage;
	private boolean findDialog; // 查找对话框有没有打开
	private FindController findController;
	private boolean replaceDialog; // 替换对话框有没有打开
	private ReplaceController replaceController;
	
	private boolean insertDate = false;

	public void secondStageExit() {
		if (findDialog || replaceDialog) {
			secondStage.close();
			findDialog = false;
			replaceDialog = false;
		}
	}
	
	public void selectText(int startIndex, int length) {
		int len = text.getText().length();
		if (startIndex > len)
			return;
		else if (startIndex + length > len)
			length = len - startIndex;
		text.selectRange(startIndex, startIndex + length);
	}
	
	public void selectTextReplace(String str) {
		text.replaceSelection(str);
		if (findDialog) {
			findController.setText(text.getText(), text.getCaretPosition());
		}
		if (replaceDialog) {
			replaceController.setText(text.getText(), text.getCaretPosition());
		}
	}
	
	public String getSelected() {
		return text.getSelectedText();
	}

	private void setTitle() {
		String[] filename_tmp = filename.split("\\\\");
		title = filename_tmp[filename_tmp.length - 1] + " - 记事本";
		stage.setTitle(title);
	}

	public boolean saveCheck() {
		if (!initText.equals(text.getText())) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("记事本");
			alert.setHeaderText("你想更改保存到 " + title.substring(1, title.length() - 5) + "吗？");
			// alert.setContentText("Choose your option.");

			ButtonType buttonTypeConfirm = new ButtonType("是");
			ButtonType buttonTypeRefuse = new ButtonType("否");
			ButtonType buttonTypeCancel = new ButtonType("取消", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeConfirm, buttonTypeRefuse, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeConfirm) {
				System.out.println("确认保存");
				if (_saveFile())
					return true;
				else
					return false;
			} else if (result.get() == buttonTypeRefuse) {
				System.out.println("取消保存");
				return true;
			} else {
				System.out.println("取消");
				return false;
			}
		} else
			return true;
	}

	private boolean _saveFile() {
		if (filename.equals("")) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			File com = fsv.getHomeDirectory(); // 读取桌面路径

			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.setTitle("保存");
			fileChooser.setInitialDirectory(com); // 设置文件对话框的初始目录
			fileChooser.setInitialFileName("*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(stage);
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
			initText = text.getText();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setTitle();
	}
	
	public void refreshLine() {
		if (findDialog) {
			findController.setText(text.getText(), text.getCaretPosition());
		}
		if (replaceDialog) {
			replaceController.setText(text.getText(), text.getCaretPosition());
		}
		String str = text.getText();
		int pos = text.getCaretPosition(); 
		int len = str.length();
		
		int i = 0;
		int nowLine = 1;
		int index = 0;
		while (i <= pos && i < len) {
			if (str.charAt(i) == '\n') {
				if (i != pos) {
					nowLine++;
					index = i;
				}
			}
			i++;
		}
		int column = i - index;
//		if (nowLine == 1)
//			column++;
		position.setText("第 " + nowLine + " 行，第 " + column + " 列");
		
	}
	
	private void eventRegiste() {
		// 输入监听
		text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//		        System.out.println("observable = " + observable + ", oldValue = " + oldValue + ", newValue = " + newValue);
		        if (!insertDate) {
					if (!last.equals(newValue)) {
						int len1 = last.length();
						int len2 = newValue.length();
						if (Math.abs(len1 - len2) >= 5) {
							last = newValue;
						} else {
							int len = Math.min(len1, len2);
							if (len > 5) {
								int number = 0;
								for (int i = 0; i < len; i++) {
									if (last.charAt(i) != newValue.charAt(i)) {
										number++;
										if (number > 5)
											break;
									}
								}
								if (number > 5)
									last = newValue;
							} else {
								last = newValue;
							}
						}
					}
		        } else {
		        	insertDate = false;
		        }
				
				if (findDialog) {
					findController.setText(text.getText(), text.getCaretPosition());
				}
				
				if (newValue.length() == 0) {
					menu_search_text.setDisable(true);
					menu_search_next.setDisable(true);
					menu_search_last.setDisable(true);
					menu_replace.setDisable(true);
				} else {
					menu_search_text.setDisable(false);
					menu_search_next.setDisable(false);
					menu_search_last.setDisable(false);
					menu_replace.setDisable(false);
				}
				
				if (initText.equals(text.getText())) {
					if (title.substring(0, 1).equals("*")) {
						title = title.substring(1);
						menu_revoke.setDisable(true);
					}
					stage.setTitle(title);
				} else {
					if (!title.substring(0, 1).equals("*")) {
						title = "*" + title;
						menu_revoke.setDisable(false);
					}
					stage.setTitle(title);
				}
				refreshLine();
			}
		});
		// 选中监听
		text.selectedTextProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//		        System.out.println("observable = " + observable + ", oldValue = " + oldValue + ", newValue = " + newValue);
				String select = text.getSelectedText();
				if (select.length() == 0) {
					menu_search.setDisable(true);
					menu_cut.setDisable(true);
					menu_copy.setDisable(true);
					menu_delete.setDisable(true);
					menu_search2.setDisable(true);
					menu_cut2.setDisable(true);
					menu_copy2.setDisable(true);
					menu_delete2.setDisable(true);
				} else {
					menu_search.setDisable(false);
					menu_cut.setDisable(false);
					menu_copy.setDisable(false);
					menu_delete.setDisable(false);
					menu_search2.setDisable(false);
					menu_cut2.setDisable(false);
					menu_copy2.setDisable(false);
					menu_delete2.setDisable(false);
				}
				refreshLine();
			}
		});
	}

	public void click(MouseEvent event) {
		refreshLine();
	}

	public void keydown(KeyEvent event) {
		refreshLine();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menu_search2.setDisable(true);
		menu_cut2.setDisable(true);
		menu_copy2.setDisable(true);
		menu_delete2.setDisable(true);
		menu_formRight.setDisable(true);
		menu_showUnicode.setDisable(true);
		menu_closeInput.setDisable(true);
		menu_Rechoose.setDisable(true);
		
		//menu_newWindow.setDisable(true);
		
		menu_search.setDisable(true);
		menu_revoke.setDisable(true);
		menu_cut.setDisable(true);
		menu_copy.setDisable(true);
		menu_delete.setDisable(true);
		menu_goto.setDisable(true);
		menu_search_text.setDisable(true);
		menu_search_next.setDisable(true);
		menu_search_last.setDisable(true);
		menu_replace.setDisable(true);
		eventRegiste();
		secondStage = new Stage();
		findDialog = false;
		replaceDialog = false;
	}

	// ==============================================
	// 文件菜单栏
	public void newFile(ActionEvent event) {
		if (saveCheck()) {
			last = "";
			initText = "";
			filename = "";
			text.setText(last);
			stage.setTitle("无标题 - 记事本");
		}
	}

	public void openFile(ActionEvent event) {
		if (saveCheck()) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.setTitle("打开");
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
				text.setText(tmp); // 设置文本
				initText = tmp;
				last = tmp;
				setTitle();
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
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File com = fsv.getHomeDirectory(); // 读取桌面路径

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.setTitle("另存为文件");
		fileChooser.setInitialDirectory(com); // 设置文件对话框的初始目录
		fileChooser.setInitialFileName("*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			filename = file.toString();
			_saveFile(filename);
		}
	}

	public void pageSet(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("记事本");
		alert.setHeaderText("页面设置");

		alert.showAndWait();
	}

	public void pagePrint(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("记事本");
		alert.setHeaderText("打印");

		alert.showAndWait();
	}

	public void exit(ActionEvent event) {
		if (saveCheck()) {
			Platform.exit();
		}
	}

	// ==============================================
	// 编辑菜单栏
	public void revoke(ActionEvent event) {
		insertDate = true;
		String tmp = last;
		last = text.getText();
		text.setText(tmp);
		text.selectAll();
	}

	public void cut(ActionEvent event) {
		String tmp = text.getText();
		if (!tmp.equals(last))
			last = tmp;
		insertDate = true;
		String select = text.getSelectedText();
		text.replaceSelection("");
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(select);
		clip.setContents(tText, null);
		//System.out.println("已将内容复制到粘贴板 - " + select);
		refreshLine();
	}

	public void copy(ActionEvent event) {
		String select = text.getSelectedText();
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(select);
		clip.setContents(tText, null);
		//System.out.println("已将内容复制到粘贴板 - " + select);
		refreshLine();
	}

	public void paste(ActionEvent event) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable clipT = clip.getContents(null);
		if (clipT != null) {
			// 检查内容是否是文本类型
			if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					String tmp = text.getText();
					if (!tmp.equals(last))
						last = tmp;
					insertDate = true;
					
					String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
					text.insertText(text.getCaretPosition(), context);
					refreshLine();
					//System.out.println("已将内容粘贴到文本 - " + context);
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void delete(ActionEvent event) {
		insertDate = true;
		String tmp = text.getText();
		if (!tmp.equals(last))
			last = tmp;
		text.replaceSelection("");
		refreshLine();
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public void find(ActionEvent event) {
		if (!findDialog) {
			secondStageExit();
			findDialog = true;
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("Find.fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

			try {
				Parent root = fxmlLoader.load();
				findController = fxmlLoader.getController();
				findController.setController(this);
				findController.setText(text.getText(), text.getCaretPosition());
				findController.setTextFind(text.getSelectedText());

				secondStage.setScene(new Scene(root));
				secondStage.setTitle("查找");
				secondStage.show();
				secondStage.setResizable(false);
				Controller.stage = secondStage;
				// 关闭监听
				secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						System.out.print("关闭事件");
						findDialog = false;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void findLast(ActionEvent event) {
		if (findDialog) {
			int p = findController.findLast();
			if (p != -1)
				selectText(p, findController.getTextLength());
		}
	}

	public void findNext(ActionEvent event) {
		if (findDialog) {
			int p = findController.findNext();
			if (p != -1)
				selectText(p, findController.getTextLength());
		}
	}

	public void replace(ActionEvent event) {
		if (!replaceDialog) {
			secondStageExit();
			replaceDialog = true;
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("Replace.fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

			try {
				Parent root = fxmlLoader.load();
				replaceController = fxmlLoader.getController();
				replaceController.setController(this);
				replaceController.setText(text.getText(), text.getCaretPosition());
				replaceController.setTextFind(text.getSelectedText());

				secondStage.setScene(new Scene(root));
				secondStage.setTitle("替换");
				secondStage.show();
				secondStage.setResizable(false);
				Controller.stage = secondStage;
				// 关闭监听
				secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						replaceDialog = false;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void gotoLine(ActionEvent event) {
		GotoDialog dialog = new GotoDialog();
		Optional<Integer> result = dialog.showAndWait();
		result.ifPresent(f -> {
			System.out.println("转到行：" + f);
			String str = text.getText();
			int len = str.length();
			int i = 0;
			int pos = -1;
			int nowLine = 1;
			while (i < len) {
				if (nowLine == f) {
					pos = i;
					break;
				}
				if (str.charAt(i) == '\n')
					nowLine++;
				i++;
			}
			if (pos == -1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("记事本");
				alert.setHeaderText("不存在该行");
				alert.showAndWait();
			} else {
				text.positionCaret(pos);
			}
			
		});
	}

	public void selectAll(ActionEvent event) {
		text.selectAll();
	}

	public void insertDate(ActionEvent event) {
		insertDate = true;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm yyyy/MM/dd");
		text.insertText(text.getCaretPosition(), formatter.format(date));
	}

	// ==============================================
	// 格式菜单栏

	public void autoChangeLine(ActionEvent event) {
		text.setWrapText(!text.isWrapText());
		menu_goto.setDisable(!menu_goto.isDisable());
	}

	public void setFont(ActionEvent event) {
		FontDialog dialog = new FontDialog();
		Optional<Font> result = dialog.showAndWait();
		result.ifPresent(f -> {
			System.out.println(f);
			text.setFont(f);
		});
	}

	// ==============================================
	// 查看菜单栏
	public void enlarge(ActionEvent event) {
		Font tmp = text.getFont();
		if (nowFontSize < 100)
			nowFontSize++;

		text.setFont(new Font(tmp.getStyle(), nowFontSize));
		zoom.setText(String.format("%d%%", nowFontSize * 100 / fontSize));
	}

	public void narrow(ActionEvent event) {
		Font tmp = text.getFont();
		if (nowFontSize > 8)
			nowFontSize--;

		text.setFont(new Font(tmp.getStyle(), nowFontSize));
		zoom.setText(String.format("%d%%", nowFontSize * 100 / fontSize));
	}

	public void normal(ActionEvent event) {
		Font tmp = text.getFont();
		nowFontSize = fontSize;
		text.setFont(new Font(tmp.getStyle(), fontSize));
		zoom.setText("100%");
	}

	public void stateBar(ActionEvent event) {
		state.setVisible(!state.isVisible());
	}
	// ==============================================
	// 帮助菜单栏

	public void viewHelp(ActionEvent event) {
		String url = "https://cn.bing.com/search?q=%E8%8E%B7%E5%8F%96%E6%9C%89%E5%85%B3+windows+10+%E4%B8%AD%E7%9A%84%E8%AE%B0%E4%BA%8B%E6%9C%AC%E7%9A%84%E5%B8%AE%E5%8A%A9&filters=guid:%224466414-zh-hans-dia%22%20lang:%22zh-hans%22&form=T00032&ocid=HelpPane-BingIA";
		Desktop desktop = Desktop.getDesktop();
		if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
			URI uri;
			try {
				uri = new URI(url);
				desktop.browse(uri);
			} catch (URISyntaxException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public void viewAbout(ActionEvent event) {
		AboutDialog dialog = new AboutDialog();
		dialog.showAndWait();
	}

}
