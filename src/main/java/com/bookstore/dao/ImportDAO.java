package com.bookstore.dao;

import com.bookstore.dto.ImportTicketDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList; // <--- BẮT BUỘC CÓ
import java.util.List;      // <--- BẮT BUỘC CÓ

public class ImportDAO {

    // Hàm thêm mới (INSERT)
    public int add(ImportTicketDTO importDTO) {
        int generatedId = -1;
        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "INSERT INTO import_ticket (employee_id, supplier_id, total_import_price, status) VALUES (?, ?, ?, 1)";
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, importDTO.getEmployeeID());
            ps.setInt(2, importDTO.getSupplierID());
            ps.setDouble(3, importDTO.getTotalPrice());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            DatabaseConnection.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    // Hàm lấy danh sách (SELECT ALL)
    public List<ImportTicketDTO> getAll() {
        List<ImportTicketDTO> list = new ArrayList<>();
        // Query join 3 bảng để lấy tên thay vì ID
        String sql = "SELECT i.import_ticket_id, s.supplier_name, i.created_date, e.employee_name, i.total_import_price, i.status " +
                "FROM import_ticket i " +
                "JOIN supplier s ON i.supplier_id = s.supplier_id " +
                "JOIN employee e ON i.employee_id = e.employee_id " +
                "ORDER BY i.import_ticket_id DESC";

        try {
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ImportTicketDTO dto = new ImportTicketDTO(
                        rs.getInt("import_ticket_id"),
                        rs.getString("supplier_name"),
                        rs.getTimestamp("created_date"),
                        rs.getString("employee_name"),
                        rs.getDouble("total_import_price"),
                        rs.getInt("status")
                );
                list.add(dto);
            }
            DatabaseConnection.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}