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

    public boolean addPoint(int customerId, int pointsToAdd) {
        String sql = "UPDATE customer SET point = point + ? WHERE customer_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, pointsToAdd);
            ps.setInt(2, customerId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                updateCustomerRank(customerId);
            }

            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateCustomerRank(int customerId) {
        String sql = "UPDATE customer SET rank_id = CASE " +
                "WHEN point >= 10000 THEN 4 " +
                "WHEN point >= 5000 THEN 3 " +
                "WHEN point >= 2000 THEN 2 " +
                "ELSE 1 END " +
                "WHERE customer_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
