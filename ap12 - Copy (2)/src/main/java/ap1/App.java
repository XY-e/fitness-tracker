package ap1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class App extends Application {
    private final ObservableList<ExerciseLogView.Exercise> exerciseLogData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #EBE8FC, #DDD5F3);");

            // Button styles
            String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: #311E54; -fx-font-size: 15px; -fx-font-family: 'Inter'; -fx-font-weight: bold;";

            // Exercise Log Button
            Button exerciseLogButton = new Button("EXERCISE LOG");
            exerciseLogButton.setStyle(buttonStyle);

            // Diet Tracker Button
            Button dietTrackerButton = new Button("DIET TRACKER");
            dietTrackerButton.setStyle(buttonStyle);

            // My Progress Button
            Button myProgressButton = new Button("MY PROGRESS");
            myProgressButton.setStyle(buttonStyle);

            // Home Button with Image
            Button homeButton = new Button();
            homeButton.setGraphic(new ImageView(new Image(getClass().getResource("home.png").toExternalForm())));
            homeButton.setStyle("-fx-background-color: transparent;");

            // Profile Button with Image
            Button profileButton = new Button();
            profileButton.setGraphic(new ImageView(new Image(getClass().getResource("profile.png").toExternalForm())));
            profileButton.setStyle("-fx-background-color: transparent;");

            // Menu bar
            MenuBar menuBar = new MenuBar();
            menuBar.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            Menu moreMenu = new Menu();
            moreMenu.setGraphic(new ImageView(new Image(getClass().getResource("moreoption.png").toExternalForm())));
            MenuItem dashboardMenuItem = new MenuItem("Dashboard");
            dashboardMenuItem.setStyle(
                    "-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #311E54;");
            moreMenu.getItems().add(dashboardMenuItem);
            menuBar.getMenus().add(moreMenu);

            DashboardView dashboardView = new DashboardView();
            ExerciseLogView exerciseLogView = new ExerciseLogView(exerciseLogData);
            exerciseLogView.setOnExerciseAdded((exercise, day) -> dashboardView.updateDashboard(exercise, day));

            exerciseLogButton.setOnAction(e -> root.setCenter(exerciseLogView.getView()));
            dietTrackerButton.setOnAction(e -> System.out.println("Diet Tracker Button Clicked"));
            myProgressButton.setOnAction(e -> System.out.println("My Progress Button Clicked"));
            homeButton.setOnAction(e -> System.out.println("Home Button Clicked"));
            profileButton.setOnAction(e -> System.out.println("Profile Button Clicked"));
            dashboardMenuItem.setOnAction(e -> root.setCenter(dashboardView.getView()));

            HBox navigationBar = new HBox(10, exerciseLogButton, dietTrackerButton, myProgressButton, homeButton,
                    profileButton, menuBar);
            navigationBar.setStyle("-fx-background-color: linear-gradient(to bottom, #EBE8FC, #DDD5F3);");

            navigationBar.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

            root.setTop(navigationBar);
            root.setCenter(dashboardView.getView());

            Scene scene = new Scene(root, 800, 600);
            scene.setFill(Paint.valueOf("#FFFFFF"));

            primaryStage.setTitle("Fitness Tracker App");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
