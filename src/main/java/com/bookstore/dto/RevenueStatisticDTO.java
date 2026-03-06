package com.bookstore.dto;

public class RevenueStatisticDTO {
    private String timeLabel;
    private double totalRevenue;
    private double growthRate;

    public RevenueStatisticDTO(){}

    public RevenueStatisticDTO(String timeLabel, double totalRevenue){
        this.timeLabel = timeLabel;
        this.totalRevenue = totalRevenue;
    }

    public String getTimeLabel(){return timeLabel;}
    public double getTotalRevenue(){return totalRevenue;}
    public double getGrowthRate(){return growthRate;}

    public void setTimeLabel(String timeLabel){this.timeLabel = timeLabel;}
    public void setTotalRevenue(double totalRevenue){this.totalRevenue = totalRevenue;}
    public void setGrowthRate(double growthRate){this.growthRate = growthRate;}
}