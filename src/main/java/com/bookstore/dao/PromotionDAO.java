package com.bookstore.dao;

import com.bookstore.dto.PromotionDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    public double getPromotionPercentByBook(int bookId) {
        double percent = 0;
        String sql = "SELECT p.percent FROM promotion p " +
                "JOIN promotion_detail pd ON p.promotion_id = pd.promotion_id " +
                "WHERE pd.book_id = ? " +
                "AND p.status = 1 " +
                "AND NOW() BETWEEN p.start_date AND p.end_date " +
                "ORDER BY p.percent DESC LIMIT 1";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    percent = rs.getDouble("percent");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return percent;
    }
}