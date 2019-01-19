package com.ego.test;

import com.ego.FXLauncherCustomUI;
import fxlauncher.UIProvider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

/**
 * This class test the custom ui.
 * It shows the 'check for updates' phase for 3 seconds and than change to the 'updating' phase.
 * In the update phase this class call the {@link UIProvider#updateProgress(double)} 100 times (0.0 to 1.0).
 * After that this class kills the running program itself
 */
public class TestUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root));

        UIProvider ui = new FXLauncherCustomUI();
        ui.init(primaryStage);

        Parent updater = ui.createLoader();
        root.getChildren().addAll(updater);


        primaryStage.show();

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                ui.createUpdater(null);
            });

            for (double d = 0d; d < 100d; d++) {
                double finalD = d;
                Platform.runLater(() -> {
                    ui.updateProgress((finalD +1d)/100);
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            System.exit(0);
            return true;
        });
    }
}
