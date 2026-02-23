package com.bookstore.dao;

import com.bookstore.dto.RoleDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM role";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new RoleDTO(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}