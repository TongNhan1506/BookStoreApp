package com.bookstore.dto;
import java.util.Date;

public class BillDTO {

    private String billId;
    private Date createdDate;
    private String employeeName;
    private String customerName;
    private double totalAmount;

    public BillDTO(){}

    public BillDTO(String billId, Date createdDate, String employeeName, String customerName, double totalAmount){
        this.billId = billId;
        this.createdDate = createdDate;
        this.employeeName = employeeName;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
    }

    public String getBillId(){return billId;}
    public Date getCreatedDate(){return createdDate;}
    public String getEmployeeName(){return employeeName;}
    public String getCustomerName(){return customerName;}
    public double getTotalAmount(){return totalAmount;}

    public void setBillId(String billId){this.billId = billId;}
    public void setCreatedDate( Date createdDate){this.createdDate = createdDate;}
    public void setEmployeeName(String employeeDate){this.employeeName = employeeName;}
    public void setCustomerName(String customerName){this.customerName = customerName;}
    public void setTotalAmount(double totalAmount){this.totalAmount = totalAmount;}
    
    
}
