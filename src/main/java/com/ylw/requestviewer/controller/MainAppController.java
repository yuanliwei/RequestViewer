package com.ylw.requestviewer.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.common.utils.PropUtils;
import com.ylw.common.utils.Res;
import com.ylw.requestviewer.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainAppController extends BaseController {
	private static Log log = LogFactory.getLog(MainAppController.class);

	@FXML
	MenuBar menuBar;

	ProgressBar progressBar;
	ProgressIndicator progressIndicator;

	private MainApp mainApp;

	@FXML
	public BorderPane borderPane;

	@FXML
	TabPane tabPane;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		openTab("RequestView");
//		openTab("NetCaptureView");
	}

	@Override
	protected void initialize() {
	}

	@FXML
	public void onClose(ActionEvent event) {
		System.out.println(((MenuItem) event.getSource()).getUserData());
	}

	@FXML
	public void onOpenUrl(ActionEvent event) {
		System.out.println((String) ((MenuItem) event.getSource()).getUserData());
	}

	@FXML
	public void onRefresh() {
		System.out.println("onRefresh()");
		// mainApp.webPageController.load("http://www.baidu.com");
	}

	@FXML
	public void onAlert() {
		System.out.println("onAlert()");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.alertTypeProperty().addListener(listener -> {
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		});
	}

	@FXML
	public void onOpenFile() {
		log.debug("click open file");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("打开一个html文档");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("html", "*.htm", "*.html"),
				new ExtensionFilter("All Files", "*.*"));

		String lastPath = PropUtils.get("sel_html_path");
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(lastPath)) {
			File file = new File(lastPath);
			if (file.exists() && file.isFile()) {
				lastPath = file.getParent();
			}
			fileChooser.setInitialDirectory(new File(lastPath));
		}

		File selectedFile = fileChooser.showOpenDialog(mainApp.primaryStage);
		if (selectedFile != null) {
			String openFilePath = selectedFile.getAbsolutePath();
			log.debug("selectFile : " + openFilePath);
			// mainApp.webPageController.load(openFilePath);
			PropUtils.put("sel_html_path", openFilePath);
		}
	}

	public void setCenter(WebView center2) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void onAddTab() {
		// TabPane tabPane = new TabPane();
		Tab tab = new Tab();
		tab.setText("new tab");
		tab.setContent(new Rectangle(200, 200, Color.LIGHTSTEELBLUE));
		tabPane.getTabs().add(tab);
		log.debug("add tab");
	}

	@FXML
	public void addNewTab(ActionEvent event) {
		String uData = (String) ((MenuItem) event.getSource()).getUserData();
		openTab(uData);
	}

	private void openTab(String uData) {
		try {
			FXMLLoader loader;
			Tab tab;

			switch (uData) {
			case "RequestView":
				loader = Res.getFXMLLoader("RequestView.fxml");
				tab = new Tab();
				tab.setContent(loader.load());
				tab.setText(uData);
				tabPane.getTabs().add(tab);
				break;
			case "NetCaptureView":
				loader = Res.getFXMLLoader("NetCaptureView.fxml");
				tab = new Tab();
				tab.setContent(loader.load());
				tab.setText(uData);
				tabPane.getTabs().add(tab);
				break;

			default:
				log.warn("not found view " + uData);
				break;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
