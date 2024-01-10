package com.librarymanagmentsystem.librarymanagmentsystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Superclass
class DataLoader {
    // Final method to load data into program
    public final void loadDataFromFile(String filePath, Library library) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            for (String line : lines) {
                // Process each line as needed
                processLine(line, library);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Subclass method to override
    protected void processLine(String line, Library library) {
        // Will override
    }
}

// Subclass
class LibraryDataLoader extends DataLoader {
    @Override
    protected void processLine(String line, Library library) {
        try {
            // Debug for processing each line in txt file
            System.out.println("Processing line: " + line);

            // Parse the line and add the book to the library
            String[] bookDetails = line.split(", ");
            if (bookDetails.length == 4) {
                String authorName = bookDetails[0].substring(bookDetails[0].indexOf(":") + 2);
                String bookName = bookDetails[1].substring(bookDetails[1].indexOf(":") + 2);
                String isbn = bookDetails[2].substring(bookDetails[2].indexOf(":") + 2);
                int quantity = Integer.parseInt(bookDetails[3].substring(bookDetails[3].indexOf(":") + 2));

                // Create a Book object
                Book loadedBook = new Book(authorName, bookName, isbn, quantity);

                // Add the loaded book to the library
                library.addBook(loadedBook);
            } else {
                // Debug for format
                System.err.println("Invalid book details format: " + line);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error processing line: " + line + ". Skipping.");
        }
    }

    // Method to prompt that data is uploaded
    @Override
    public String toString() {
        return "Library data is uploaded.";
    }
}





