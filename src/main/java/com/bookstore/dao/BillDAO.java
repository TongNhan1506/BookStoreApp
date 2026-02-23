package com.bookstore.dao;

import com.bookstore.dto.BillDTO;
import com.bookstore.dto.BillDetailDTO;
import com.bookstore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private BookDAO bookDAO;

    public boolean createBillTransaction(BillDTO bill, List<BillDetailDTO> details) {
        String sqlBill = "INSERT INTO bill(total_bill_price, tax, employee_id, customer_id, payment_method_id, earned_points) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO bill_detail (bill_id, book_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        Connection c = null;
        PreparedStatement psBill = null;
        PreparedStatement psDetail = null;
        ResultSet rs = null;

        try {
            c = DatabaseConnection.getConnection();
            c.setAutoCommit(false);

            psBill = c.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS);
            psBill.setDouble(1, bill.getTotalBillPrice());
            psBill.setDouble(2, bill.getTax());
            psBill.setInt(3, bill.getEmployeeId());
            if (bill.getCustomerId() > 0) {
                psBill.setInt(4, bill.getCustomerId());
            } else {
                psBill.setNull(4, java.sql.Types.INTEGER);
            }
            psBill.setInt(5, bill.getPaymentMethodId());
            psBill.setInt(6, bill.getEarnedPoints());
            psBill.executeUpdate();

            int generatedBillId = -1;
            rs = psBill.getGeneratedKeys();
            if (rs.next()) {
                generatedBillId = rs.getInt(1);
            }

            if (generatedBillId > 0) {
                psDetail = c.prepareStatement(sqlDetail);
                BookDAO bookDAO = new BookDAO();

                for (BillDetailDTO detail : details) {
                    // A. Lưu chi tiết hóa đơn
                    psDetail.setInt(1, generatedBillId);
                    psDetail.setInt(2, detail.getBookId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setDouble(4, detail.getUnitPrice());
                    psDetail.executeUpdate();

                    bookDAO.decreaseQuantity(c, detail.getBookId(), detail.getQuantity());
                }
            }
            c.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psDetail != null) psDetail.close(); } catch (Exception e) {}
            try { if (psBill != null) psBill.close(); } catch (Exception e) {}
            try { if (c != null) { c.setAutoCommit(true); c.close(); } } catch (Exception e) {}
        }
    }

    public List<BillDTO> getAllBills() {
        List<BillDTO> list = new ArrayList<>();
        String sql = "SELECT bill_id, created_date, total_bill_price, tax, " +
                "employee_id, customer_id, payment_method_id, earned_points " +
                "FROM bill";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BillDTO bill = new BillDTO(
                        rs.getInt("bill_id"),
                        rs.getTimestamp("created_date"),
                        rs.getDouble("total_bill_price"),
                        rs.getDouble("tax"),
                        rs.getInt("employee_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("payment_method_id"),
                        rs.getInt("earned_points")
                );
                list.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}