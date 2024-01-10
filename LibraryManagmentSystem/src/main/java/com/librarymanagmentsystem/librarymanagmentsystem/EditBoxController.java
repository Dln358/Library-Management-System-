package com.librarymanagmentsystem.librarymanagmentsystem;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class EditBoxController {

    // Reference to Hello controller
    private HelloController helloController;

    // Private variables for edit scene
    @FXML
    private TextField authorEditSceneText;
    @FXML
    private TextField titleEditSceneText;
    @FXML
    private TextField isbnEditSceneText;
    @FXML
    private TextField quantityEditSceneText;
    private String[] selectedItemDetails = new String[4];

    // Set reference controller
    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    // Populate text field with current data
    public void populateFields(String selectedItem) {
        System.out.println("Selected Item: " + selectedItem);

        // Remove the prefixes "Author: ", "Title: ", "ISBN: ", and "Quantity: "
        String[] itemDetails = selectedItem.split(", ");
        for (int i = 0; i < itemDetails.length; i++) {
            itemDetails[i] = itemDetails[i].substring(itemDetails[i].indexOf(":") + 2);
        }

        // Get item details for debugging
        System.out.println("Item Details Array: " + Arrays.toString(itemDetails));

        // Check whitespace and format
        if (itemDetails.length == 4) {
            authorEditSceneText.setText(itemDetails[0]);
            titleEditSceneText.setText(itemDetails[1]);
            isbnEditSceneText.setText(itemDetails[2]);
            String numericQuantity = itemDetails[3].replaceAll("[^0-9]", "");
            quantityEditSceneText.setText(numericQuantity);

            // Print details
            System.out.println("Author: " + itemDetails[0] + ", Type: " + itemDetails[0].getClass().getSimpleName());
            System.out.println("Title: " + itemDetails[1] + ", Type: " + itemDetails[1].getClass().getSimpleName());
            System.out.println("ISBN: " + itemDetails[2] + ", Type: " + itemDetails[2].getClass().getSimpleName());
            System.out.println("Quantity: " + numericQuantity + ", Type: " + numericQuantity.getClass().getSimpleName());

            // Store the details for later use
            selectedItemDetails = itemDetails;
        } else {
            // Debug for format
            System.out.println("Error: Invalid item details format");
        }
    }

    // Method to handle edit submission
    @FXML
    private void handleEditSubmit() {
        // Fill text fields with previous details
        String newAuthor = authorEditSceneText.getText();
        String newTitle = titleEditSceneText.getText();
        String newISBN = isbnEditSceneText.getText();
        String newQuantity = quantityEditSceneText.getText();

        // Print selection
        System.out.println("Details to find: Author=" + selectedItemDetails[0] +
                ", Title=" + selectedItemDetails[1] + ", ISBN=" + selectedItemDetails[2] +
                ", Quantity=" + selectedItemDetails[3]);

        // Reference book for updating
        Book bookToUpdate = findBookByDetails(
                selectedItemDetails[0].trim(),
                selectedItemDetails[1].trim(),
                selectedItemDetails[2].trim(),
                selectedItemDetails[3].trim()
        );

        // Confirm book exist
        if (bookToUpdate != null) {
            // Debug for book selected
            System.out.println("Books before update:");
            helloController.getLibrary().printAllBooks();

            // Debug to confirm book is updating
            System.out.println("Updating book:");
            System.out.println("Old details: " + bookToUpdate.getDetails());
            System.out.println("New details: Author=" + newAuthor + ", Title=" + newTitle +
                    ", ISBN=" + newISBN + ", Quantity=" + newQuantity);

            // Update book
            bookToUpdate.updateBook(newAuthor, newTitle, newISBN, Integer.parseInt(newQuantity));

            // Update the book details in the text file
            updateTextFile(bookToUpdate);

            // Confirm book and library is updated
            System.out.println("Books after update:");
            helloController.getLibrary().printAllBooks();
            helloController.updateListView(bookToUpdate.getDetails());

            // Close stage
            closeStage();

        } else {
            // Debug for book not found
            System.out.println("Error: Book not found in the library.");
        }
    }

    // Method to find book details
    private Book findBookByDetails(String authorName, String bookName, String isbn, String quantity) {
        // Get book and loop through details and library
        for (Book book : helloController.getLibrary().getBookList()) {
            System.out.println("Checking book: " + book.getDetails());
            if (book.getAuthorName().equals(authorName)
                    && book.getBookName().equals(bookName)
                    && book.getIsbn().equals(isbn)
                    && book.getQuantity() == Integer.parseInt(quantity)) {
                System.out.println("Found book: " + book.getDetails());
                return book;
            }
        }
        // Debug for empty library
        System.out.println("Book not found in the library.");
        return null;
    }

    // Method to close edit box
    private void closeStage() {
        helloController.setEditBoxStage(null);
        if (helloController.getEditBoxStage() != null) {
            helloController.getEditBoxStage().close();
        }
    }

    // Method to update the book details in the text file
    private void updateTextFile(Book updatedBook) {
        // Reference file path
        String filePath = "libraryData.txt";

        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Find the line that corresponds to the book and update it
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains(selectedItemDetails[0])
                        && lines.get(i).contains(selectedItemDetails[1])
                        && lines.get(i).contains(selectedItemDetails[2])
                        && lines.get(i).contains(selectedItemDetails[3])) {
                    // Update the details
                    lines.set(i, updatedBook.getDetails());
                    break;
                }
            }

            // Write the updated information back to the file
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
