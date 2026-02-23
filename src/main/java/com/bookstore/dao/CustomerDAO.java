package com.bookstore.dao;

import com.bookstore.dto.CustomerDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAO {
    public CustomerDTO selectByPhone(String phone) {
        CustomerDTO customer = null;
        String sql = "SELECT * FROM customer WHERE customer_phone = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new CustomerDTO(
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getString("customer_phone"),
                            rs.getInt("point"),
                            rs.getInt("rank_id")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public boolean updatePointAndRank(int customerId, int newTotalPoint, int newRankId) {
        String sql = "UPDATE customer SET point = ?, rank_id = ? WHERE customer_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, newTotalPoint);
            ps.setInt(2, newRankId);
            ps.setInt(3, customerId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
