package com.bookstore.dao;

import com.bookstore.dto.ImportDetailDTO;
import com.bookstore.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ImportDetailDAO {
    public void add(ImportDetailDTO detail) {
        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "INSERT INTO import_ticket_detail (import_ticket_id, book_id, import_quantity, import_price) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, detail.getImportID());
            ps.setInt(2, detail.getBookID());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getPrice());

            ps.executeUpdate();
            DatabaseConnection.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}