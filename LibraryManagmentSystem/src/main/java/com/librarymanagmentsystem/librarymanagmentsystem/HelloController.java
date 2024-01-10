package com.librarymanagmentsystem.librarymanagmentsystem;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class HelloController {

    // Private variables for main controller
    @FXML
    private ListView<String> listView;

    private Library library;

    private Stage editBoxStage;

    @FXML
    private TextField searchTextField;

    // Method to set the library
    public void setLibrary(Library library) {
        this.library = library;
    }

    // Method to handle add button scene
    @FXML
    private void handleAddButtonScene() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-box.fxml"));
            Parent root = loader.load();

            // Access the controller
            AddBoxController addBoxController = loader.getController();

            // Pass the reference to the HelloController
            addBoxController.setHelloController(this);

            // Pass the reference to the Library
            addBoxController.setLibrary(library);

            // Create a new stage for adding books
            Stage addBoxStage = new Stage();
            addBoxStage.initModality(Modality.APPLICATION_MODAL);
            addBoxStage.setTitle("Add Books");

            // Set the scene
            Scene scene = new Scene(root);
            addBoxStage.setScene(scene);

            // Show the stage and wait for it to be closed
            addBoxStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle the edit button
    @FXML
    private void handleEditButton() {
        // Get selected item from ListView
        String selectedItem = listView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-box.fxml"));
                Parent root = loader.load();

                // Access the controller
                EditBoxController editBoxController = loader.getController();

                // Pass the reference to the HelloController
                editBoxController.setHelloController(this);

                // Pass the selected item details to populate fields
                editBoxController.populateFields(selectedItem);

                // Create a new stage for edit scene
                Stage editBoxStage = new Stage();
                editBoxStage.initModality(Modality.APPLICATION_MODAL);
                editBoxStage.setTitle("Edit Books");

                // Set the scene
                Scene scene = new Scene(root);
                editBoxStage.setScene(scene);

                // Show the stage and wait for it to be closed
                editBoxStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If item is not select prompt to select
            System.out.println("Please select a book to edit.");
        }
    }

    // Method to handle the delete button
    @FXML
    private void handleDeleteButton() {
        // Get selected item from ListView
        String selectedItem = listView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Find the book in the library and remove it
            Book bookToDelete = findBookByDetails(selectedItem);

            if (bookToDelete != null) {
                library.getBookList().remove(bookToDelete);

                // Update the ListView
                listView.getItems().remove(selectedItem);
                System.out.println("Book deleted from the library: " + selectedItem);

                // Update the text file after deleting the book
                deleteTextFile(bookToDelete.getAuthorName(), bookToDelete.getBookName(),
                        bookToDelete.getIsbn(), String.valueOf(bookToDelete.getQuantity()));

                // Print all books for debugging
                library.printAllBooks();
            } else {
                // Prompt if book does not exist
                System.out.println("Error: Book not found in the library.");
            }
        } else {
            // If book is not selected prompt selection
            System.out.println("Please select a book to delete.");
        }
    }

    // Method to handle the search button
    @FXML
    private void handleSearchButton() {
        // variable for searching text
        String query = searchTextField.getText().trim();

        // If search field is empty prompt user
        if (query.isEmpty()) {
            System.out.println("Please enter a search query.");
            return;
        }

        // Find the book in the library
        Book foundBook = findBookByTitleOrISBN(query);

        if (foundBook != null) {
            // Remove from current position
            library.getBookList().remove(foundBook);
            // Add to the top
            library.getBookList().add(0, foundBook);

            // Update the ListView
            updateListView(null);

            // Statement for Debugging
            System.out.println("Book found and moved to the top: " + foundBook);
        } else {
            // If entry is not found prompt user
            System.out.println("No matching books found.");
        }
    }

    // Get book Details for deletion
    private Book findBookByDetails(String details) {
        // Split the details string into individual components
        String[] components = details.split(", ");
        if (components.length == 4) {
            // Extract individual components
            String author = components[0].substring(components[0].indexOf(": ") + 2);
            String title = components[1].substring(components[1].indexOf(": ") + 2);
            String isbn = components[2].substring(components[2].indexOf(": ") + 2);
            String quantity = components[3].substring(components[3].indexOf(": ") + 2);

            // Find the book based on the extracted components
            for (Book book : library.getBookList()) {
                if (book.getAuthorName().equals(author)
                        && book.getBookName().equals(title)
                        && book.getIsbn().equals(isbn)
                        && String.valueOf(book.getQuantity()).equals(quantity)) {
                    return book;
                }
            }
        }
        return null;
    }

    // Get book title or ISBN for searching
    private Book findBookByTitleOrISBN(String query) {
        // Check if the query is an ISBN
        if (query.matches("\\d+")) {
            for (Book book : library.getBookList()) {
                if (book.getIsbn().equals(query)) {
                    return book;
                }
            }
        } else {
            // If not an ISBN, search by title
            for (Book book : library.getBookList()) {
                if (book.getBookName().equalsIgnoreCase(query)) {
                    return book;
                }
            }
        }
        return null;
    }

    // Method to update the ListView
    public void updateListView(String newItem) {
        // Debugging statement to check the added item details
        if (newItem != null) {
            System.out.println("Updating ListView with book: " + newItem);
        }

        // Clear the ListView
        listView.getItems().clear();

        // Update the ListView with the new item and all other items
        for (Book book : library.getBookList()) {
            listView.getItems().add(book.getDetails());
        }

        // Print all books for debugging purposes
        library.printAllBooks();
    }

    // New method to handle file operations
    private void deleteTextFile(String author, String title, String isbn, String quantity) {
        String filePath = "libraryData.txt";

        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                // Read all lines from the file
                List<String> lines = Files.readAllLines(path);

                // Find the line that corresponds to the book and remove it
                lines.removeIf(line -> line.contains(author)
                        && line.contains(title)
                        && line.contains(isbn)
                        && line.contains(quantity));

                // Write the updated information back to the file
                Files.write(path, lines);
            } else {
                System.err.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New method to initialize the ListView with existing data
    public void initializeListView() {
        // Clear the ListView
        listView.getItems().clear();

        // Update the ListView with the existing book data
        for (Book book : library.getBookList()) {
            listView.getItems().add(book.getDetails());
        }
    }

    // Setters and getters
    public void setEditBoxStage(Stage editBoxStage) {
        this.editBoxStage = editBoxStage;
    }

    public Stage getEditBoxStage() {
        return editBoxStage;
    }

    public Library getLibrary() {
        return library;
    }

}




