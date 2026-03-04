package com.bookstore.dto;

import java.sql.Timestamp;

public class PriceDTO {
    private int priceId;
    private int bookId;
    private double basePrice;
    private double profitRate;
    private double sellingPrice;
    private Timestamp effectiveDate;
    private Timestamp end_date;
    private int is_active;
    private String bookName;
    private String authorName;
    private String categoryName;

    public PriceDTO() {}

    public PriceDTO(int priceId, int bookId, double basePrice, double profitRate, double sellingPrice, Timestamp effectiveDate, Timestamp end_date, int is_active) {
        this.priceId = priceId;
        this.bookId = bookId;
        this.basePrice = basePrice;
        this.profitRate = profitRate;
        this.sellingPrice = sellingPrice;
        this.effectiveDate = effectiveDate;
        this.end_date = end_date;
        this.is_active = is_active;
    }

    public PriceDTO(int bookId, double basePrice, double profitRate, double sellingPrice, String bookName, String authorName, String categoryName) {
        this.bookId = bookId;
        this.basePrice = basePrice;
        this.profitRate = profitRate;
        this.sellingPrice = sellingPrice;
        this.bookName = bookName;
        this.authorName = authorName;
        this.categoryName = categoryName;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
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

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
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