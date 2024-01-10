package com.librarymanagmentsystem.librarymanagmentsystem;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddBoxController {

    // Reference to HelloController
    private HelloController helloController;

    // Private variables for add scene
    @FXML
    private TextField authorAddSceneText;
    @FXML
    private TextField titleAddSceneText;
    @FXML
    private TextField isbnAddSceneText;
    @FXML
    private TextField quantityAddSceneText;

    // Reference to the Library
    private Library library;

    // Setter method to set the reference
    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    // Set library
    public void setLibrary(Library library) {
        this.library = library;
    }

    // Method to handle submission in add box
    @FXML
    private void addSubmit() {
        // Ensure that the library is set
        if (library == null) {
            System.err.println("Library is not set in AddBoxController.");
            return;
        }

        // reference private variables
        String authorName = authorAddSceneText.getText();
        String bookName = titleAddSceneText.getText();
        String isbn = isbnAddSceneText.getText();
        String quantityText = quantityAddSceneText.getText();

        try {
            // Parse quantity
            int quantity = Integer.parseInt(quantityText);

            // Check if the book already exists in the library
            boolean isDuplicate = library.getBookList().stream()
                    .anyMatch(book -> book.getAuthorName().equals(authorName)
                            && book.getBookName().equals(bookName)
                            && book.getIsbn().equals(isbn)
                            && book.getQuantity() == quantity);

            if (!isDuplicate) {
                // Create a Book object
                Book newBook = new Book(authorName, bookName, isbn, quantity);

                // Add the new book to the library
                library.addBook(newBook);

                // Save the new book details to the file
                newBook.saveToTextFile();

                // Update the ListView in HelloController
                helloController.updateListView(newBook.getDetails());

                // Clear the text fields
                clearFields();
            }

        } catch (NumberFormatException e) {
            // Handle the case where the quantity is not a valid integer
            System.out.println("Please enter a valid quantity.");
        }
    }
    // Method to clear text fields
    private void clearFields() {
        authorAddSceneText.clear();
        titleAddSceneText.clear();
        isbnAddSceneText.clear();
        quantityAddSceneText.clear();
    }
}

