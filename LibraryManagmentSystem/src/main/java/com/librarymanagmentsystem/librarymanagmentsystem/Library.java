// Library.java
package com.librarymanagmentsystem.librarymanagmentsystem;

import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class Library {
    // Array list for books
    private List<Book> books;

    // Reference to the ListView
    private static ListView<String> listView;

    // Library class
    public Library() {
        this.books = new ArrayList<>();
    }

    // Method for adding books
    public void addBook(Book book) {
        // Check for duplicates before adding or update the quantity
        for (Book existingBook : books) {
            if (existingBook.getAuthorName().equals(book.getAuthorName())
                    && existingBook.getBookName().equals(book.getBookName())
                    && existingBook.getIsbn().equals(book.getIsbn())) {
                // Book with the same details exists, update the quantity
                existingBook.setQuantity(existingBook.getQuantity() + book.getQuantity());
                System.out.println("Book quantity updated: " + existingBook.getDetails());
                return;
            }
        }

        // If no duplicate is found, add the new book
        books.add(book);
        System.out.println("Book added to the library: " + book.getDetails());
    }


    // New method to provide access to the private book list
    public List<Book> getBookList() {
        return books;
    }

    public void printAllBooks() {
        System.out.println("Printing all books in the library:");
        for (Book book : books) {
            System.out.println(book.getDetails());
        }
        System.out.println("End of book list");
    }
}


