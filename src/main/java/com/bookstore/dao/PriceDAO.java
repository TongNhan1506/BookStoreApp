package com.bookstore.dao;

import com.bookstore.dto.PriceDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceDAO {

    public List<PriceDTO> getAllActivePrices() {
        List<PriceDTO> list = new ArrayList<>();
        String sql = "SELECT b.book_id, b.book_name, c.category_name, " +
                "GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') as author_names, " +
                "IFNULL(bp.base_price, 0) as base_price, " +
                "IFNULL(bp.profit_rate, 0) as profit_rate, " +
                "IFNULL(bp.selling_price, 0) as selling_price " +
                "FROM book b " +
                "LEFT JOIN price bp ON b.book_id = bp.book_id AND bp.is_active = 1 " +
                "JOIN category c ON b.category_id = c.category_id " +
                "LEFT JOIN book_author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN author a ON ba.author_id = a.author_id " +
                "WHERE b.status = 1 " +
                "GROUP BY b.book_id, bp.base_price, bp.profit_rate, bp.selling_price";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PriceDTO dto = new PriceDTO(
                        rs.getInt("book_id"),
                        rs.getDouble("base_price"),
                        rs.getDouble("profit_rate"),
                        rs.getDouble("selling_price"),
                        rs.getString("book_name"),
                        rs.getString("author_names"),
                        rs.getString("category_name")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createNewPrice(int bookId, double basePrice, double profitRate, double sellingPrice) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String disableOldSql = "UPDATE price SET is_active = 0, end_date = NOW() WHERE book_id = ? AND is_active = 1";
            try (PreparedStatement psDisable = conn.prepareStatement(disableOldSql)) {
                psDisable.setInt(1, bookId);
                psDisable.executeUpdate();
            }

            String insertNewSql = "INSERT INTO price (book_id, base_price, profit_rate, selling_price, effective_date, is_active) " +
                    "VALUES (?, ?, ?, ?, NOW(), 1)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertNewSql)) {
                psInsert.setInt(1, bookId);
                psInsert.setDouble(2, basePrice);
                psInsert.setDouble(3, profitRate);
                psInsert.setDouble(4, sellingPrice);
                psInsert.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    DatabaseConnection.closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean updateBulkPrice(List<PriceDTO> listToUpdate, double newProfitRate) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String disableOldSql = "UPDATE price SET is_active = 0, end_date = NOW() WHERE book_id = ? AND is_active = 1";
            String insertNewSql = "INSERT INTO price (book_id, base_price, profit_rate, selling_price, effective_date, is_active) " +
                    "VALUES (?, ?, ?, ?, NOW(), 1)";

            try (PreparedStatement psDisable = conn.prepareStatement(disableOldSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertNewSql)) {

                for (PriceDTO p : listToUpdate) {
                    psDisable.setInt(1, p.getBookId());
                    psDisable.addBatch();

                    double predictedPrice = p.getBasePrice() * (1 + newProfitRate);
                    psInsert.setInt(1, p.getBookId());
                    psInsert.setDouble(2, p.getBasePrice());
                    psInsert.setDouble(3, newProfitRate);
                    psInsert.setDouble(4, predictedPrice);
                    psInsert.addBatch();
                }

                psDisable.executeBatch();
                psInsert.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    DatabaseConnection.closeConnection(conn);
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}