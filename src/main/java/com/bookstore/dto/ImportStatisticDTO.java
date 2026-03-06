package com.bookstore.dto;

public class ImportStatisticDTO {

    private String productName;
    private int totalQuantity;
    private double totalImportPrice;

    public ImportStatisticDTO(){}

    public ImportStatisticDTO(String productName, int totalQuantity, double totalImportPrice){
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalImportPrice = totalImportPrice;
    }

    public String getProductName(){return productName;}
    public int getTotalQuantity(){return totalQuantity;}
    public double getTotalImportPrice(){return totalImportPrice;}
}