package com.bookstore.dao;

import com.bookstore.dto.MembershipRankDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MembershipRankDAO {

    public MembershipRankDTO getRankByPoint(int point) {
        String sql = "SELECT rank_id, rank_name, min_point, discount_percent " +
                "FROM membership_rank WHERE min_point <= ? " +
                "ORDER BY min_point DESC LIMIT 1";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, point);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MembershipRankDTO(
                            rs.getInt("rank_id"),
                            rs.getString("rank_name"),
                            rs.getInt("min_point"),
                            rs.getDouble("discount_percent")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}