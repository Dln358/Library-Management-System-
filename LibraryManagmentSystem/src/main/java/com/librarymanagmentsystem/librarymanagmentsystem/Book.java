package com.librarymanagmentsystem.librarymanagmentsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Book {
    // Private variables for book
    private String authorName;
    private String bookName;
    private String isbn;
    private int quantity;
    

    // Constructor for Book
    public Book(String authorName, String bookName, String isbn, int quantity){
        this.authorName = authorName;
        this.bookName = bookName;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    // Setters and Getters for book
    public String getAuthorName(){
        return authorName;
    }

    public String getBookName(){
        return bookName;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

    // Method for updating book variables
    public void updateBook(String newAuthor, String newTitle, String newISBN, int newQuantity) {
        this.authorName = newAuthor;
        this.bookName = newTitle;
        this.isbn = newISBN;
        this.quantity = newQuantity;
    }

    // Method to get all book details
    public String getDetails() {
        return "Author: " + authorName + ", Book: " + bookName + ", ISBN: " + isbn + ", Quantity: " + quantity;
    }

    // Method to save book details to txt file
    public void saveToTextFile() {
        // Reference file path to save to
        String filePath = "libraryData.txt";

        // Write to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            // Append the book details to the file
            writer.println(getDetails());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
