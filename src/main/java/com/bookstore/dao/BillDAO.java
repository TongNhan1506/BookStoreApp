package com.bookstore.dao;
import com.bookstore.dto.BillDTO;
import com.bookstore.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public List<BillDTO> getAllBills(){
        List<BillDTO> list = new ArrayList<>();

        String sql = """
        SELECT b.bill_id, b.created_date,
               e.employee_name,
               c.customer_name,
               b.total_amount
        FROM bill b
        JOIN employee e ON b.employee_id = e.employee_id
        JOIN customer c ON b.customer_id = c.customer_id
                
                """;
       try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql);
           ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                BillDTO bill = new BillDTO(
                    rs.getString("bill_id"),
                    rs.getTimestamp("created_date"),
                    rs.getString("employee_name"),
                    rs.getString("customer_name"),
                    rs.getDouble("total_amount")
                );
                list.add(bill);
            }
           }catch(Exception e){
            e.printStackTrace();
           }
           return list;
    }
    
}
