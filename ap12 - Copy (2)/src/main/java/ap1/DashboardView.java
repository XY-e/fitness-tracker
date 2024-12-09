package ap1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DashboardView {
    private VBox view;
    private LineChart<String, Number> lineChart;
    private ProgressBar cyclingProgressBar;
    private ProgressBar runningProgressBar;
    private ProgressBar walkingProgressBar;
    private Label cyclingLabel;
    private Label runningLabel;
    private Label walkingLabel;
    private int cyclingProgress = 0;
    private int runningProgress = 0;
    private int walkingProgress = 0;

    private static final int CYCLING_TARGET = 180;
    private static final int RUNNING_TARGET = 120;
    private static final int WALKING_TARGET = 260;

    private final ObservableList<XYChart.Data<String, Number>> weeklyData;

    public DashboardView() {
        view = new VBox(20);
        view.setPadding(new Insets(35, 50, 35, 50));
        view.setStyle("-fx-background-color: linear-gradient(to bottom, #F0EBFF, #DDD5F3);");

        VBox contentBox = new VBox(20);
        contentBox.setStyle("-fx-background-color: white; -fx-padding: 45px; -fx-background-radius: 10px;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day of the Week");
        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);

        Label xAxisLabel = (Label) xAxis.lookup(".axis-label");
        if (xAxisLabel != null) {
            xAxisLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        }

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Calories Burned");
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);

        Label yAxisLabel = (Label) yAxis.lookup(".axis-label");
        if (yAxisLabel != null) {
            yAxisLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        }

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weekly Calories Burned");
        lineChart.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #C0AFE2, #8174D3);" +
                        "-fx-font-fill: white;");

        lineChart.setTitle("Weekly Calories Burned");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        lineChart.setLegendVisible(false);

        XYChart.Series<String, Number> caloriesSeries = new XYChart.Series<>();
        weeklyData = FXCollections.observableArrayList(
                new XYChart.Data<>("Monday", 0),
                new XYChart.Data<>("Tuesday", 0),
                new XYChart.Data<>("Wednesday", 0),
                new XYChart.Data<>("Thursday", 0),
                new XYChart.Data<>("Friday", 0),
                new XYChart.Data<>("Saturday", 0),
                new XYChart.Data<>("Sunday", 0));
        caloriesSeries.setData(weeklyData);
        lineChart.getData().add(caloriesSeries);

        cyclingProgressBar = new ProgressBar(0);
        runningProgressBar = new ProgressBar(0);
        walkingProgressBar = new ProgressBar(0);

        HBox progressBars = new HBox(50);
        progressBars.setAlignment(Pos.CENTER);
        progressBars.setMargin(progressBars, new Insets(20, 0, 0, 0)); // Add margin top of 20
        progressBars.setStyle("-fx-pref-height: 300px;");
        progressBars.getChildren().addAll(
                createProgressBox("cycling.png", "Cycling Hero", cyclingProgressBar, CYCLING_TARGET, cyclingProgress),
                createProgressBox("running.png", "Daily Running", runningProgressBar, RUNNING_TARGET, runningProgress),
                createProgressBox("walking.png", "Daily Step", walkingProgressBar, WALKING_TARGET, walkingProgress));

        contentBox.getChildren().addAll(lineChart, progressBars);

        view.getChildren().add(contentBox);
    }

    public VBox getView() {
        return view;
    }

    public void updateDashboard(ExerciseLogView.Exercise newExercise, String day) {
        updateProgress(newExercise);
        updateLineGraph(newExercise, day);
    }

    public void updateLineGraph(ExerciseLogView.Exercise newExercise, String day) {
        for (XYChart.Data<String, Number> data : weeklyData) {
            if (data.getXValue().equalsIgnoreCase(day)) {
                data.setYValue(data.getYValue().intValue() + newExercise.caloriesProperty().get());
            }
        }
    }

    public void updateProgress(ExerciseLogView.Exercise newExercise) {
        String exerciseName = newExercise.exerciseNameProperty().get();
        int duration = newExercise.durationProperty().get();
        ProgressBar progressBar = null;
        Label progressLabel = null;
        int target = 0;
        int progress = 0;

        switch (exerciseName.toLowerCase()) {
            case "cycling":
                cyclingProgress += duration;
                progressBar = cyclingProgressBar;
                progressLabel = cyclingLabel;
                target = CYCLING_TARGET;
                progress = cyclingProgress;
                break;
            case "running":
                runningProgress += duration;
                progressBar = runningProgressBar;
                progressLabel = runningLabel;
                target = RUNNING_TARGET;
                progress = runningProgress;
                break;
            case "walking":
                walkingProgress += duration;
                progressBar = walkingProgressBar;
                progressLabel = walkingLabel;
                target = WALKING_TARGET;
                progress = walkingProgress;
                break;
        }

        if (progressBar != null && progressLabel != null) {
            double progressPercent = (double) progress / target;
            progressBar.setProgress(progressPercent);
            progressLabel.setText(String.format("Progress: %.2f%%", progressPercent * 100));
        }
    }

    private VBox createProgressBox(String imageFileName, String title, ProgressBar progressBar, int target,
            int progress) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        view.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() / 3;
            box.setPrefWidth(width);

            progressBar.setPrefWidth(width * 0.6);
        });

        Image image = new Image(getClass().getResourceAsStream(imageFileName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(65);
        imageView.setFitHeight(65);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 17px; -fx-font-weight:bold; -fx-text-fill: #4138D0;");

        progressBar.setStyle("-fx-accent: #5B50C8; -fx-background-color: #D2D2D2;");

        Label progressLabel = new Label();
        progressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4138D0;");
        updateProgressLabel(progressLabel, progress, target);

        progressBar.setProgress((double) progress / target);

        Label targetLabel = new Label("Target: " + target + " mins/week");
        targetLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #787878;");

        box.getChildren().addAll(imageView, titleLabel, progressLabel, progressBar, targetLabel);
        if (title.equals("Cycling Hero"))
            cyclingLabel = progressLabel;
        if (title.equals("Daily Running"))
            runningLabel = progressLabel;
        if (title.equals("Daily Step"))
            walkingLabel = progressLabel;
        return box;
    }

    private void updateProgressLabel(Label progressLabel, int progress, int target) {
        double progressPercent = (double) progress / target * 100;
        progressLabel.setText(String.format("Progress: %.2f%%", progressPercent));
    }

    public ProgressBar getProgressBar(String exerciseName) {
        switch (exerciseName.toLowerCase()) {
            case "cycling":
                return cyclingProgressBar;
            case "running":
                return runningProgressBar;
            case "walking":
                return walkingProgressBar;
            default:
                return null;
        }
    }
}
