package com.bookstore.dao;

import com.bookstore.dto.BookDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<BookDTO> selectAllBooks() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT b.*, c.category_name, " +
                "GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') as author_names, " +
                "GROUP_CONCAT(DISTINCT ba.author_id SEPARATOR ',') as author_ids " +
                "FROM book b " +
                "JOIN category c ON b.category_id = c.category_id " +
                "LEFT JOIN book_author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN author a ON ba.author_id = a.author_id " +
                "WHERE b.status = 1 " +
                "GROUP BY b.book_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToBookDTO(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BookDTO> searchByName(String bookname) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT b.*, c.category_name, " +
                "GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') as author_names, " +
                "GROUP_CONCAT(DISTINCT ba.author_id SEPARATOR ',') as author_ids " +
                "FROM book b " +
                "JOIN category c ON b.category_id = c.category_id " +
                "LEFT JOIN book_author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN author a ON ba.author_id = a.author_id " +
                "WHERE b.status = 1 AND b.book_name LIKE ? " +
                "GROUP BY b.book_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + bookname + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBookDTO(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookDTO getBookbyId(int bookId) {
        String sql = "SELECT b.*, c.category_name, " +
                "GROUP_CONCAT(DISTINCT a.author_name SEPARATOR ', ') as author_names, " +
                "GROUP_CONCAT(DISTINCT ba.author_id SEPARATOR ',') as author_ids " +
                "FROM book b " +
                "JOIN category c ON b.category_id = c.category_id " +
                "LEFT JOIN book_author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN author a ON ba.author_id = a.author_id " +
                "WHERE b.book_id = ? " +
                "GROUP BY b.book_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookDTO(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(BookDTO book) {
        String sql = "INSERT INTO book(book_name, selling_price, quantity, translator, image, description, status, category_id, tag_detail, supplier_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getBookName());
            ps.setDouble(2, book.getSellingPrice());
            ps.setInt(3, book.getQuantity());
            ps.setString(4, book.getTranslator());
            ps.setString(5, book.getImage());
            ps.setString(6, book.getDescription());
            ps.setInt(7, book.getStatus());
            ps.setInt(8, book.getCategoryId());
            ps.setString(9, book.getTagDetail());
            ps.setInt(10, book.getSupplierId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(BookDTO book) {
        String sql = "UPDATE book SET book_name = ?, selling_price = ?, quantity = ?, translator = ?, image = ?, description = ?, status = ?, category_id = ?, tag_detail = ?, supplier_id = ? " +
                "WHERE book_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, book.getBookName());
            ps.setDouble(2, book.getSellingPrice());
            ps.setInt(3, book.getQuantity());
            ps.setString(4, book.getTranslator());
            ps.setString(5, book.getImage());
            ps.setString(6, book.getDescription());
            ps.setInt(7, book.getStatus());
            ps.setInt(8, book.getCategoryId());
            ps.setString(9, book.getTagDetail());
            ps.setInt(10, book.getSupplierId());
            ps.setInt(11, book.getBookId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean decreaseQuantity(int bookId, int quantitySold) {
        String sql = "UPDATE book SET quantity = quantity - ? WHERE book_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, quantitySold);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByName(String bookname) {
        String sql = "SELECT * FROM book WHERE book_name = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bookname);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getQuantityByID(int bookId) {
        int quantity = 0;
        String sql = "SELECT quantity FROM book WHERE book_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public void updateQuantity(int bookId, int newQuantity) {
        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "UPDATE book SET quantity = ? WHERE book_id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            DatabaseConnection.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BookDTO mapResultSetToBookDTO(ResultSet rs) throws Exception {
        BookDTO book = new BookDTO(
                rs.getInt("book_id"),
                rs.getString("book_name"),
                rs.getDouble("selling_price"),
                rs.getInt("quantity"),
                rs.getString("translator"),
                rs.getString("image"),
                rs.getString("description"),
                rs.getInt("status"),
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getString("tag_detail"),
                rs.getInt("supplier_id")
        );
        book.setAuthorIdsFromString(rs.getString("author_ids"));
        book.setAuthorsName(rs.getString("author_names"));
        return book;
    }
}