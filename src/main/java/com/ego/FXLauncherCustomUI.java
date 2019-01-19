package com.ego;

import fxlauncher.FXManifest;
import fxlauncher.UIProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FXLauncherCustomUI implements UIProvider {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label label_text;
    private Stage stage;
    private Parent parent;
    private ResourceBundle bundle;


    @Override
    public void init(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Parent createLoader() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fxlauncher_splashScreen.fxml"));
        bundle = ResourceBundle.getBundle("fxlauncher_stringResource", Locale.getDefault(), getClass().getClassLoader());
        loader.setResources(bundle);

        Parent root;
        try {
            root = loader.load();

            //workaround: if we use the custom ui from the fxLauncher the @FXML injection don't work.
            //To instantiate the UI Nodes we use the css id instead
            progressBar = (ProgressBar) root.lookup("#minimal-boarder");
            label_text = (Label) root.lookup("#label_text");

            stage.setTitle(bundle.getString("general_applicationName"));
            stage.getIcons().addAll(new Image(getClass().getResourceAsStream("/FXLauncher_QA-App-logo.png")));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.getScene().setFill(Color.WHITE);
            label_text.setText(bundle.getString("splash_checkingForUpdate"));

            parent = root;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Parent createUpdater(FXManifest fxManifest) {
        progressBar.setProgress(0d);
        return parent;
    }

    @Override
    public void updateProgress(double v) {
        label_text.setText(bundle.getString("splash_updating"));
        progressBar.setProgress(v);
    }

    public ResourceBundle getBundle(){
        return bundle;
    }
}
