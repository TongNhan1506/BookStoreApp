package com.bookstore.dto;

public class PriceDTO {
    private int bookId;
    private double basePrice;
    private double profitRate;
    private double sellingPrice;
    private String bookName;
    private String authorName;
    private String categoryName;

    public PriceDTO() {}

    public PriceDTO(int bookId, double basePrice, double profitRate, double sellingPrice, String bookName, String authorName, String categoryName) {
        this.bookId = bookId;
        this.basePrice = basePrice;
        this.profitRate = profitRate;
        this.sellingPrice = sellingPrice;
        this.bookName = bookName;
        this.authorName = authorName;
        this.categoryName = categoryName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(double profitRate) {
        this.profitRate = profitRate;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}