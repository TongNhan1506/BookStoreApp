package com.bookstore.dao;

import com.bookstore.dto.FinancialStatsDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancialStatsDAO {

    public List<FinancialStatsDTO> getThongKeTheoNgay(Date tuNgay, Date denNgay) {
        List<FinancialStatsDTO> list = new ArrayList<>();
        String sql = "SELECT x.thoi_gian, SUM(x.doanh_thu) AS doanh_thu, SUM(x.chi_phi) AS chi_phi " +
                "FROM (" +
                "  SELECT DATE(b.created_date) AS thoi_gian, SUM(b.total_bill_price) AS doanh_thu, 0 AS chi_phi " +
                "  FROM bill b " +
                "  WHERE DATE(b.created_date) BETWEEN ? AND ? " +
                "  GROUP BY DATE(b.created_date) " +
                "  UNION ALL " +
                "  SELECT DATE(i.approved_date) AS thoi_gian, 0 AS doanh_thu, SUM(d.import_quantity * d.import_price) AS chi_phi " +
                "  FROM import_ticket i " +
                "  JOIN import_ticket_detail d ON d.import_ticket_id = i.import_ticket_id " +
                "  WHERE i.status = 2 AND i.approved_date IS NOT NULL AND DATE(i.approved_date) BETWEEN ? AND ? " +
                "  GROUP BY DATE(i.approved_date) " +
                ") x " +
                "GROUP BY x.thoi_gian " +
                "ORDER BY x.thoi_gian";

        SimpleDateFormat viewFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            java.sql.Date from = new java.sql.Date(tuNgay.getTime());
            java.sql.Date to = new java.sql.Date(denNgay.getTime());
            ps.setDate(1, from);
            ps.setDate(2, to);
            ps.setDate(3, from);
            ps.setDate(4, to);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date thoiGian = rs.getDate("thoi_gian");
                    double doanhThu = rs.getDouble("doanh_thu");
                    double chiPhi = rs.getDouble("chi_phi");
                    list.add(new FinancialStatsDTO(viewFormat.format(thoiGian), doanhThu, chiPhi));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<FinancialStatsDTO> getThongKeTheoThang(int nam) {
        List<FinancialStatsDTO> list = new ArrayList<>();
        String sql = "SELECT x.thang, SUM(x.doanh_thu) AS doanh_thu, SUM(x.chi_phi) AS chi_phi " +
                "FROM (" +
                "  SELECT MONTH(b.created_date) AS thang, SUM(b.total_bill_price) AS doanh_thu, 0 AS chi_phi " +
                "  FROM bill b " +
                "  WHERE YEAR(b.created_date) = ? " +
                "  GROUP BY MONTH(b.created_date) " +
                "  UNION ALL " +
                "  SELECT MONTH(i.approved_date) AS thang, 0 AS doanh_thu, SUM(d.import_quantity * d.import_price) AS chi_phi " +
                "  FROM import_ticket i " +
                "  JOIN import_ticket_detail d ON d.import_ticket_id = i.import_ticket_id " +
                "  WHERE i.status = 2 AND i.approved_date IS NOT NULL AND YEAR(i.approved_date) = ? " +
                "  GROUP BY MONTH(i.approved_date) " +
                ") x " +
                "GROUP BY x.thang " +
                "ORDER BY x.thang";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int thang = rs.getInt("thang");
                    double doanhThu = rs.getDouble("doanh_thu");
                    double chiPhi = rs.getDouble("chi_phi");
                    list.add(new FinancialStatsDTO("Tháng " + thang + "/" + nam, doanhThu, chiPhi));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}