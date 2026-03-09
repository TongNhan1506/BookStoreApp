package com.bookstore.dto;

public class BillDetailDialogDTO {
    private int billId;
    private int bookId;
    private String bookName;
    private int quantity;
    private double price;
    private double unitPrice;
    private double finalPrice;

    public BillDetailDialogDTO(){}

    public BillDetailDialogDTO(int billId, int bookId, String bookName, int quantity, double price){
        this.billId = billId;
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getBillId(){return billId;}
    public void setBillId(int billId){this.billId = billId;}

    public int getBookId(){return bookId;}
    public void setBookId(int bookId){this.bookId = bookId;}

    public String getBookName(){return bookName;}
    public void setBookName(String bookName){this.bookName = bookName;}

    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity = quantity;}

    public double getPrice(){return price;}
    public void setPrice(double price){this.price = price;}

    public double getTotal(){return quantity * price;}
}