package com.ylw.requestviewer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.common.utils.PropUtils;
import com.ylw.common.utils.Res;
import com.ylw.common.utils.TaskUtils;
import com.ylw.common.utils.event.EventUtil;
import com.ylw.requestviewer.controller.MainAppController;
import com.ylw.requestviewer.model.StopAppEvent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Log log = LogFactory.getLog(MainApp.class);

    private BorderPane root;
    public MainAppController mainAppController;
    public Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("RequestViewer");

        primaryStage.getIcons().add(Res.getImageFromRes("icon.jpg"));
        PropUtils.load();
        FXMLLoader loader = Res.getFXMLLoader("MainApp.fxml");
        try {
            root = loader.load();
            mainAppController = loader.getController();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        // scene.getStylesheets().add(FileUtil.getResUrl("css/java-keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        initCenter();

        initController();
    }

    private void initCenter() {
        // FXMLLoader loader = Res.getFXMLLoader("MainView.fxml");
        // FXMLLoader loader = Res.getFXMLLoader("WebPageView.fxml");
        // FXMLLoader loader = Res.getFXMLLoader("MainTabPane.fxml");
        // try {
        // TabPane center = loader.load();
        // webPageController = loader.getController();
        // mainAppTabController = loader.getController();
        // mainAppController.borderPane.setCenter(center);
        // mainAppController.stackPane.getChildren().add(center);
        // } catch (IOException e) {
        // log.error(e.getMessage(), e);
        // }
    }

    private void initController() {
        mainAppController.setMainApp(this);
        // webPageController.setMainApp(this);
        // webPageController.load(PropUtils.get("sel_html_path"));
    }

    @Override
    public void stop() throws Exception {
        PropUtils.store();
        EventUtil.post(new StopAppEvent());
        TaskUtils.shutdown();
        // webPageController.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
