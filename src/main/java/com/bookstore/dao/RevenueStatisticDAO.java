package com.bookstore.dao;

import com.bookstore.dto.RevenueStatisticDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevenueStatisticDAO {
    public List<RevenueStatisticDTO> getRevenueByMonth(int year){

        List<RevenueStatisticDTO> list = new ArrayList<>();

        String sql = """
            SELECT MONTH(created_date) AS montn,
                   SUM(total_bill_price) AS total
                FROM bill
                WHERE YEAR(created_date) = ?
                GROUP BY MONTH(created_date)
                ORDER BY month
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,year);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String label = "Tháng" + rs.getInt("month") + "/" + year;
                double total = rs.getDouble("total");

                list.add(new RevenueStatisticDTO(label, total));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }

    public List<RevenueStatisticDTO> getRevenueByQuarter(int year){
        List <RevenueStatisticDTO> list = new ArrayList<>();

        String sql = """
            SELECT QUARTER(created_date) AS quarter,
                     SUM(total_bill_price) AS total
                FROM bill
                WHERE YEAR(created_date) = ?
                GROUP BY QUARTER(created_date)
                ORDER BY quarter
            """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String label = "Quý" + rs.getInt("quarter")+ "/" + year;
                double total = rs.getDouble("total");

                list.add(new RevenueStatisticDTO(label,total));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}