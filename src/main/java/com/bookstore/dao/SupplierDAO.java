package com.bookstore.dao;

import com.bookstore.dto.SupplierDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    public List<SupplierDTO> selectAllSuppliers() {
        List<SupplierDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new SupplierDTO(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("supplier_address"),
                        rs.getString("supplier_phone")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int add(SupplierDTO supplier){
        String sql = "INSERT INTO supplier(supplier_name, supplier_address, supplier_phone) " +
                     "VALUES (?, ?, ?)";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getPhoneNumber());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0){
                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
     
    public boolean update(SupplierDTO supplier){
        String sql = "UPDATE supplier SET supplier_name = ?, supplier_address = ?, supplier_phone = ? " +
                     "WHERE supplier_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getPhoneNumber());
            ps.setInt(4, supplier.getSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String supplierName){
        String sql = "SELECT * FROM supplier WHERE supplier_name = ?";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1, supplierName);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public List<SupplierDTO> searchByName(String suppliername){
        List<SupplierDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier WHERE supplier_name LIKE ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1, "%" + suppliername + "%");
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    list.add(new SupplierDTO(
                        rs.getInt("supplier_id"), 
                        rs.getString("supplier_name"),
                        rs.getString("supplier_address"),
                        rs.getString("supplier_phone")
                    ));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String searchByPhone(String supplierPhone){
        String sql = "SELECT * FROM supplier WHERE supplier_phone = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);){
            ps.setString(1, supplierPhone);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getString("supplier_name");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public SupplierDTO getBySupplierId(int supplierId){
        String sql = "SELECT * FROM supplier WHERE supplier_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);){
            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new SupplierDTO(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("supplier_address"),
                        rs.getString("supplier_phone")
                    );
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}


// Đây là toàn bộ DAO và DTO. Bạn giúp tôi việc sau:

// 1.  Kiểm tra giúp tôi những hàm này có sai gì ko, có thiếu hàm nào ko, có dư hàm nào ko, có gì ko hợp lý ko, có bị xung đột ko

// 2. Tôi có cần làm Book_Author DTO hay DAO ko?

// 3. Tôi có cần làm BUS của những cái này luôn ko?

// 4.  Hiện tại tôi vẫn  chưa sửa ProductPanel cho nên tôi vẫn để tự do tên các hàm bạn kêu tôii sửa như selectall hay searchbyname đúng ko?