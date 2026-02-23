package com.bookstore.dao;

import com.bookstore.dto.EmployeeDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public EmployeeDTO selectById(int id) {
        EmployeeDTO employee = null;

        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM employee WHERE employee_id = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employee = new EmployeeDTO();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setEmployeeName(rs.getString("employee_name"));
                employee.setRoleId(rs.getInt("role_id"));
            }
            DatabaseConnection.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    public List<EmployeeDTO> selectAllEmployees() {
        List<EmployeeDTO> list = new ArrayList<>();
        String sql = "SELECT e.*, r.role_name " +
                "FROM employee e " +
                "JOIN role r ON e.role_id = r.role_id " +
                "ORDER BY e.employee_id DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EmployeeDTO emp = new EmployeeDTO(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("employee_phone"),
                        rs.getDate("birthday"),
                        rs.getDouble("base_salary"),
                        rs.getDouble("salary_factor"),
                        rs.getDate("day_in"),
                        rs.getInt("status"),
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                );
                list.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
