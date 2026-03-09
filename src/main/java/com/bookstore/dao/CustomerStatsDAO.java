package com.bookstore.dao;

import com.bookstore.dto.CustomerStatsDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerStatsDAO {
    public List<CustomerStatsDTO> getTopCustomersByQuantity(java.sql.Timestamp fromDate, java.sql.Timestamp toDate) {
        List<CustomerStatsDTO> list = new ArrayList<>();
        String sql = "SELECT c.customer_id, c.customer_name, r.rank_name, SUM(bd.quantity) as total_qty " +
                "FROM bill b " +
                "JOIN bill_detail bd ON b.bill_id = bd.bill_id " +
                "JOIN customer c ON b.customer_id = c.customer_id " +
                "JOIN membership_rank r ON c.rank_id = r.rank_id " +
                "WHERE b.created_date >= ? AND b.created_date <= ? " +
                "GROUP BY c.customer_id, c.customer_name, r.rank_name " +
                "ORDER BY total_qty DESC LIMIT 3";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CustomerStatsDTO dto = new CustomerStatsDTO();
                dto.setCustomerId(rs.getInt("customer_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setRankName(rs.getString("rank_name"));
                dto.setTotalQuantity(rs.getInt("total_qty"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<CustomerStatsDTO> getTopCustomersBySpending(java.sql.Timestamp fromDate, java.sql.Timestamp toDate) {
        List<CustomerStatsDTO> list = new ArrayList<>();
        String sql = "SELECT c.customer_id, c.customer_name, r.rank_name, SUM(b.total_bill_price) as total_spent " +
                "FROM bill b " +
                "JOIN customer c ON b.customer_id = c.customer_id " +
                "JOIN membership_rank r ON c.rank_id = r.rank_id " +
                "WHERE b.created_date >= ? AND b.created_date <= ? " +
                "GROUP BY c.customer_id, c.customer_name, r.rank_name " +
                "ORDER BY total_spent DESC LIMIT 3";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CustomerStatsDTO dto = new CustomerStatsDTO();
                dto.setCustomerId(rs.getInt("customer_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setRankName(rs.getString("rank_name"));
                dto.setTotalSpent(rs.getDouble("total_spent"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
