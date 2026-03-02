package com.bookstore.dao;

import com.bookstore.dto.RoleDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public List<RoleDTO> selectAllRoles() {
        List<RoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM role ORDER BY role_id ASC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RoleDTO role = new RoleDTO(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                );
                list.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertRole(com.bookstore.dto.RoleDTO role) {
        String sql = "INSERT INTO role (role_name) VALUES (?)";
        try (java.sql.Connection conn = com.bookstore.util.DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, role.getRoleName());
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateRole(RoleDTO role) {
        String sql = "UPDATE role SET role_name = ? WHERE role_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setInt(2, role.getRoleId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}