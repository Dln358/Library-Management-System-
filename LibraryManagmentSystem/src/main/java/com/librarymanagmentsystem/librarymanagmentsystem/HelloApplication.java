package com.librarymanagmentsystem.librarymanagmentsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create an instance of LibraryDataLoader
        LibraryDataLoader libraryDataLoader = new LibraryDataLoader();

        // Create an instance of Library
        Library library = new Library();

        // Load data from the text file and pass it to the Library instance
        libraryDataLoader.loadDataFromFile("libraryData.txt", library);

        // Print library loader
        System.out.println(libraryDataLoader);

        // Load the UI components after loading data
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        HelloController controller = fxmlLoader.getController();

        // Set the library with loaded data to the HelloController
        controller.setLibrary(library);

        // Initialize the ListView with existing data
        controller.initializeListView();

        // Set scene
        Scene scene = new Scene(root);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}




