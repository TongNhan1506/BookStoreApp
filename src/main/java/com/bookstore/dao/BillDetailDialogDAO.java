package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.dto.BillDetailDialogDTO;
import com.bookstore.util.DatabaseConnection;

public class BillDetailDialogDAO {
    public List<BillDetailDialogDTO> getByBillId(int billId){
        List<BillDetailDialogDTO> list = new ArrayList<>();

        String sql = """
            SELECT bd.bill_id,
                   bd.book_id,
                   b.book_name,
                   bd.quantity,
                   bd.unit_price
                FROM bill_detail bd
                JOIN book b ON bd.book_id = b.book_id
                WHERE bd.bill_id = ?
                ORDER BY bd.book_id
            """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, billId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                BillDetailDialogDTO dto = new BillDetailDialogDTO(
                        rs.getInt("bill_id"),
                        rs.getInt("book_id"),
                        rs.getString("book_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                );

                list.add(dto);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
}