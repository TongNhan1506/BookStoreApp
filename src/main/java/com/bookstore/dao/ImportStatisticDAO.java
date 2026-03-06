package com.bookstore.dao;

import com.bookstore.dto.ImportStatisticDTO;
import com.bookstore.util.DatabaseConnection;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ImportStatisticDAO {
    public List<ImportStatisticDTO> getImportByDateRange(Date from, Date to){
        List<ImportStatisticDTO> list = new ArrayList<>();

        String sql = """
                SELECT p.product_name,
                       SUM(d.quantity) as total_quantity,
                       SUM(d.quantity * d.import_price) as total_price
                FROM import_bill b
                JOIN import_detail d ON b.id = d.import_id
                JOIN product p ON p.id = d.product_id
                WHERE b.import_date BETWEEN ? AND ?
                GROUP BY p.product_name
                """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                list.add(new ImportStatisticDTO(
                        rs.getString("product_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_price") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<ImportStatisticDTO> getStatisticByQuarter(int quarter, int year){
        List<ImportStatisticDTO> list = new ArrayList<>();

        String sql = """
                SELECT p.product_name,
                     SUM(id.quantity) AS total_quantity,
                     SUM(id.quantity) *id.import_price) AS total_price
                FROM import_detail id
                JOIN import_bill ib ON id.import_id = ib.import_id
                JOIN product p ON id.product_id = p.product_id
                WHERE QUARTER(ib.import_date) = ?
                AND YEAR(ib.import_date) = ?
                GROUP BY p.product_id, p.product_name
                ORDER BY total_quantity DESC
                """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, quarter);
            ps.setInt(2,year);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ImportStatisticDTO dto = new ImportStatisticDTO();
                rs.getString("product_name");
                rs.getInt("total_quantity");
                rs.getDouble("total_price");
                list.add(dto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<ImportStatisticDTO> getTop3Most(int quarter, int year) {
        List<ImportStatisticDTO> list = new ArrayList<>();

        String sql = """
        SELECT p.product_name,
               SUM(id.quantity) AS total_quantity,
               SUM(id.quantity * id.import_price) AS total_price
        FROM import_detail id
        JOIN import_bill ib ON id.import_id = ib.import_id
        JOIN product p ON id.product_id = p.product_id
        WHERE QUARTER(ib.import_date) = ?
          AND YEAR(ib.import_date) = ?
        GROUP BY p.product_id, p.product_name
        ORDER BY total_quantity DESC
        LIMIT 3
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quarter);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ImportStatisticDTO(
                        rs.getString("product_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<ImportStatisticDTO> getTop3Least(int quarter, int year) {
        List<ImportStatisticDTO> list = new ArrayList<>();

        String sql = """
        SELECT p.product_name,
               SUM(id.quantity) AS total_quantity,
               SUM(id.quantity * id.import_price) AS total_price
        FROM import_detail id
        JOIN import_bill ib ON id.import_id = ib.import_id
        JOIN product p ON id.product_id = p.product_id
        WHERE QUARTER(ib.import_date) = ?
          AND YEAR(ib.import_date) = ?
        GROUP BY p.product_id, p.product_name
        ORDER BY total_quantity ASC
        LIMIT 3
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quarter);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ImportStatisticDTO(
                        rs.getString("product_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}