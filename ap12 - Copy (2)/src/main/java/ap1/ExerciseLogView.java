package ap1;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.*;

import java.util.function.BiConsumer;

public class ExerciseLogView {
    private VBox view;
    private ObservableList<Exercise> data;
    private TableView<Exercise> table;
    private TextField searchField;

    private BiConsumer<Exercise, String> onExerciseAdded;

    // Declare the columns as class-level variables
    private TableColumn<Exercise, String> nameColumn;
    private TableColumn<Exercise, Integer> durationColumn;
    private TableColumn<Exercise, Integer> caloriesColumn;
    private TableColumn<Exercise, String> intensityColumn;
    private TableColumn<Exercise, String> notesColumn;

    public ExerciseLogView(ObservableList<Exercise> data) {
        this.data = data;
        view = new VBox(10);

        // Style for the VBox
        view.setStyle("-fx-background-color: #F0EBFF");

        HBox controlBox = new HBox(10);
        controlBox.setStyle("-fx-background-color: #F0EBFF; -fx-border-color: transparent;");
        controlBox.setPadding(new Insets(10));

        searchField = new TextField();
        searchField.setPromptText("Search ...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTable(newVal));
        searchField.setStyle(
                "-fx-text-fill: #808080; -fx-background-color: #FFFFFF; -fx-border-color: #808080; -fx-border-radius: 05;");

        Button filterButton = new Button();
        Image filterImage = new Image(getClass().getResource("filter.png").toExternalForm());
        ImageView filterIcon = new ImageView(filterImage);
        filterIcon.setFitWidth(20);
        filterIcon.setFitHeight(18);
        filterButton.setGraphic(filterIcon);
        filterButton.setStyle(
                "-fx-text-fill: #808080; -fx-background-color: #FFFFFF; -fx-border-color: #808080; -fx-border-radius: 05;");
        filterButton.setOnAction(e -> showFilterPopup());

        Button addNewButton = new Button("NEW");
        addNewButton.setOnAction(e -> popup());
        addNewButton.setStyle(
                "-fx-text-fill: #FFFFFF; -fx-background-color: #311E54; -fx-border-color: #311E54;-fx-border-radius: 05;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(searchField, new Insets(35, 00, 10, 50));
        HBox.setMargin(filterButton, new Insets(35, 00, 00, 00));
        HBox.setMargin(addNewButton, new Insets(35, 50, 10, 50));
        controlBox.getChildren().addAll(searchField, filterButton, spacer, addNewButton);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: linear-gradient(to bottom, #F0EBFF, #DDD5FF); "
                + "-fx-border-color: transparent; "
                + "-fx-table-header-border-color: transparent;");
        table.setPadding(new Insets(0, 50, 0, 50));

        // Initialize columns
        nameColumn = new TableColumn<>("Exercise");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().exerciseNameProperty());

        durationColumn = new TableColumn<>("Duration (mins)");
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());

        caloriesColumn = new TableColumn<>("Calories Burned");
        caloriesColumn.setCellValueFactory(cellData -> cellData.getValue().caloriesProperty().asObject());

        intensityColumn = new TableColumn<>("Intensity");
        intensityColumn.setCellValueFactory(cellData -> cellData.getValue().intensityProperty());

        notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        table.getColumns().addAll(nameColumn, durationColumn, caloriesColumn, intensityColumn, notesColumn);
        table.setItems(data);

        applyColumnStyles();

        VBox.setVgrow(table, Priority.ALWAYS);
        view.getChildren().addAll(controlBox, table);
    }

    private void applyColumnStyles() {
        nameColumn.setCellFactory(col -> createStyledCell());
        durationColumn.setCellFactory(col -> createStyledCell());
        caloriesColumn.setCellFactory(col -> createStyledCell());
        intensityColumn.setCellFactory(col -> createStyledCell());
        notesColumn.setCellFactory(col -> createStyledCell());
    }

    private <T> TableCell<Exercise, T> createStyledCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.toString());
                    setStyle("-fx-border-color: white white black white; "
                            + "-fx-border-width: 1 0 1 0; "
                            + "-fx-background-color: white; "
                            + "-fx-text-fill: black;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        };
    }

    public VBox getView() {
        return view;
    }

    public void setOnExerciseAdded(BiConsumer<Exercise, String> onExerciseAdded) {
        this.onExerciseAdded = onExerciseAdded;
    }

    private void popup() {
        Stage stage = new Stage();

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        Label headingLabel = new Label("Log Your Exercise");
        headingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #311E54;");
        GridPane.setHalignment(headingLabel, HPos.CENTER);
        grid.add(headingLabel, 0, 0, 2, 1);

        Label subheadingLabel = new Label("Fill out the details of your workout to track your progress.");
        subheadingLabel.setStyle("-fx-text-fill: #B19CD7;");
        GridPane.setHalignment(subheadingLabel, HPos.CENTER);
        grid.add(subheadingLabel, 0, 1, 2, 1);

        Label nameLabel = new Label("Exercise Name");
        nameLabel.setStyle("-fx-text-fill: #311E54;");
        TextField nameField = new TextField();
        nameField.setStyle(
                "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(nameLabel, 0, 2, 2, 1);
        grid.add(nameField, 0, 3, 2, 1);

        Label durationLabel = new Label("Duration (minutes)");
        durationLabel.setStyle("-fx-text-fill: #311E54;");
        TextField durationField = new TextField();
        durationField.setStyle(
                "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(durationLabel, 0, 4, 2, 1);
        grid.add(durationField, 0, 5, 2, 1);

        Label caloriesLabel = new Label("Calories Burned");
        caloriesLabel.setStyle("-fx-text-fill: #311E54;");
        TextField caloriesField = new TextField();
        caloriesField.setStyle(
                "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(caloriesLabel, 0, 6, 2, 1);
        grid.add(caloriesField, 0, 7, 2, 1);

        Label dayLabel = new Label("Day of the Week");
        dayLabel.setStyle("-fx-text-fill: #311E54;");
        TextField dayField = new TextField();
        dayField.setStyle(
                "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(dayLabel, 0, 8, 2, 1);
        grid.add(dayField, 0, 9, 2, 1);

        Label intensityLabel = new Label("Intensity");
        intensityLabel.setStyle("-fx-text-fill: #311E54;");
        TextField intensityField = new TextField();
        intensityField
                .setStyle(
                        "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(intensityLabel, 0, 10, 2, 1);
        grid.add(intensityField, 0, 11, 2, 1);

        Label notesLabel = new Label("Notes");
        notesLabel.setStyle("-fx-text-fill: #311E54;");
        TextField notesField = new TextField();
        notesField.setStyle(
                "-fx-text-fill: #311E54; -fx-background-color: transparent; -fx-border-color: #B19CD7;-fx-border-radius: 05;");
        grid.add(notesLabel, 0, 12, 2, 1);
        grid.add(notesField, 0, 14, 2, 1);

        Button saveButton = new Button("SAVE");
        saveButton.setStyle("-fx-background-color: #311E54; -fx-text-fill: #FFFFFF;-fx-border-radius: 05;");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int duration = Integer.parseInt(durationField.getText());
                int calories = Integer.parseInt(caloriesField.getText());
                String day = dayField.getText();
                String intensity = intensityField.getText();
                String notes = notesField.getText();

                Exercise newExercise = new Exercise(name, duration, calories, intensity, notes);
                data.add(newExercise);

                if (onExerciseAdded != null) {
                    onExerciseAdded.accept(newExercise, day);
                }

                stage.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter valid numbers for duration and calories.");
                alert.showAndWait();
            }
        });

        GridPane.setHalignment(saveButton, HPos.CENTER);
        grid.add(saveButton, 0, 15, 2, 1);

        Scene scene = new Scene(grid, 360, 530);
        stage.setScene(scene);
        stage.show();
    }

    private void filterTable(String query) {
        if (query == null || query.isEmpty()) {
            table.setItems(data);
            return;
        }

        ObservableList<Exercise> filteredList = FXCollections.observableArrayList();
        for (Exercise exercise : data) {
            if (exercise.exerciseNameProperty().get().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(exercise);
            }
        }
        table.setItems(filteredList);
    }

    private void showFilterPopup() {
        Stage filterStage = new Stage();
        filterStage.initModality(Modality.APPLICATION_MODAL);

        VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(10));
        filterBox.setStyle("-fx-background-color: #F0EBFF;");

        Label filterLabel = new Label("Filter Options");
        filterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #311E54;");

        // Example filters (checkboxes, dropdowns, etc.)
        CheckBox intensityHigh = new CheckBox("High Intensity");
        CheckBox intensityMedium = new CheckBox("Medium Intensity");
        CheckBox intensityLow = new CheckBox("Low Intensity");

        Button applyButton = new Button("Apply");
        applyButton.setStyle("-fx-background-color: #311E54; -fx-text-fill: white;");
        applyButton.setOnAction(e -> {
            // Implement filter logic here
            filterStage.close();
        });

        filterBox.getChildren().addAll(filterLabel, intensityHigh, intensityMedium, intensityLow, applyButton);

        Scene scene = new Scene(filterBox, 300, 200);
        filterStage.setScene(scene);
        filterStage.show();
    }

    public static class Exercise {
        private final StringProperty exerciseName;
        private final IntegerProperty duration;
        private final IntegerProperty calories;
        private final StringProperty intensity;
        private final StringProperty notes;

        public Exercise(String exerciseName, int duration, int calories, String intensity, String notes) {
            this.exerciseName = new SimpleStringProperty(exerciseName);
            this.duration = new SimpleIntegerProperty(duration);
            this.calories = new SimpleIntegerProperty(calories);
            this.intensity = new SimpleStringProperty(intensity);
            this.notes = new SimpleStringProperty(notes);
        }

        public StringProperty exerciseNameProperty() {
            return exerciseName;
        }

        public IntegerProperty durationProperty() {
            return duration;
        }

        public IntegerProperty caloriesProperty() {
            return calories;
        }

        public StringProperty intensityProperty() {
            return intensity;
        }

        public StringProperty notesProperty() {
            return notes;
        }
    }
}
