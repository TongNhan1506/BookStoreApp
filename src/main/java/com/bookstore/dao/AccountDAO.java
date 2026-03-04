package com.bookstore.dao;

import com.bookstore.dto.AccountDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public AccountDTO selectByUsername(String username) {
        AccountDTO acc = null;

        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                acc = new AccountDTO(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("employee_id"),
                        rs.getInt("status")
                );
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public boolean update(AccountDTO acc) {
        try {
            Connection c = DatabaseConnection.getConnection();
            String sql = "UPDATE account SET password = ? WHERE username = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, acc.getPassword());
            ps.setString(2, acc.getUsername());
            int result = ps.executeUpdate();
            c.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AccountDTO> selectAllAccounts() {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AccountDTO(rs.getString("username"), rs.getString("password"),
                        rs.getInt("employee_id"), rs.getInt("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(AccountDTO acc) {
        String sql = "INSERT INTO account (username, password, employee_id, status) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.setInt(3, acc.getEmployeeId());
            ps.setInt(4, acc.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
