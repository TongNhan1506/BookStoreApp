package com.bookstore.dto;

public class ImportDetailDTO {
    private int importID;
    private int bookID;
    private int quantity;
    private double price; // Giá nhập

    public ImportDetailDTO() {}

    public ImportDetailDTO(int importID, int bookID, int quantity, double price) {
        this.importID = importID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getImportID() { return importID; }
    public void setImportID(int importID) { this.importID = importID; }

    public int getBookID() { return bookID; }
    public void setBookID(int bookID) { this.bookID = bookID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}