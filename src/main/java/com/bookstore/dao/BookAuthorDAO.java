package com.bookstore.dao;

import com.bookstore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookAuthorDAO {
    
    // Thêm tác giả cho sách
    public boolean addAuthorsToBook(int bookId, List<Integer> authorIds) {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            for (int authorId : authorIds) {
                ps.setInt(1, bookId);
                ps.setInt(2, authorId);
                ps.addBatch();
            }
            
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tất cả tác giả của sách
    public boolean removeAllAuthorsFromBook(int bookId) {
        String sql = "DELETE FROM book_author WHERE book_id = ?";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, bookId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy danh sách author_id của 1 sách
    public List<Integer> getAuthorIdsByBookId(int bookId) {
        List<Integer> authorIds = new ArrayList<>();
        String sql = "SELECT author_id FROM book_author WHERE book_id = ?";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                authorIds.add(rs.getInt("author_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return authorIds;
    }
}