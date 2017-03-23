package com.ylw.requestviewer.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fxmisc.flowless.VirtualizedScrollPane;

import com.ylw.common.utils.Res;
import com.ylw.requestviewer.MainApp;
import com.ylw.requestviewer.controller.BaseController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MainViewController extends BaseController {
	private static Log log = LogFactory.getLog(MainViewController.class);

	private MainApp mainApp;

	@FXML
	ImageView bookPic;

	@FXML
	TextField textBookName;

	@FXML
	TextField textAuthor;

	@FXML
	TextField textArticleId;

	@FXML
	TextField textUrl;

	@FXML
	TextField textDeep;

	@FXML
	StackPane filterStack;

	@FXML
	StackPane chapterStack;

	@FXML
	ProgressBar progressBar;

	// @FXML
	// TextField book_name;
	//
	// @FXML
	// TextField author;
	//
	// @FXML
	// ProgressBar progress;
	//
	// @FXML
	// TextField articleid;
	//
	// @FXML
	// Label cover_path;
	//
	// @FXML
	// StackPane url_rule;
	//
	// @FXML
	// StackPane chapter_rule;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	protected void initialize() {
		// Initialize the person table with the two columns.
		bookPic.setImage(Res.getImageFromRes("icon.jpg"));
//		filterStack.getChildren().remove(0);
//		filterStack.getChildren().add(new VirtualizedScrollPane<>(filterCode));
//		chapterStack.getChildren().remove(0);
//		chapterStack.getChildren().add(new VirtualizedScrollPane<>(chapterCode));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void load(String filePath) {
		System.out.println("load file:///" + filePath);
	}

	public void exec(String jsData) {
		log.debug("exec : " + jsData);
	}

	@FXML
	public void selectPicture(MouseEvent event) {
	}

	@FXML
	public void startPull(MouseEvent event) {
	}

	@FXML
	public void selCover() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.titleProperty().set("信息");
		alert.headerTextProperty().set("信息");
		alert.contentTextProperty().set("选择封面");
		alert.showAndWait();
	}
	
	public void stop() {
//		filterCode.stop();
//		chapterCode.stop();
	}

}
