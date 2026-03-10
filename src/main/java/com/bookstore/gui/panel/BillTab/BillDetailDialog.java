package com.bookstore.gui.panel.BillTab;

import com.bookstore.util.AppConstant;
import com.bookstore.util.DatabaseConnection;
import com.bookstore.util.MoneyFormatter;
import com.bookstore.bus.BillDetailDialogBUS;
import com.bookstore.dto.BillDetailDialogDTO;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class BillDetailDialog extends JDialog{

    private JTable table;
    private DefaultTableModel model;

    private JLabel lbBillId;
    private JLabel lbEmployee;
    private JLabel lbCustomer;
    private JLabel lbTotal;

    private BillDetailDialogBUS bus = new BillDetailDialogBUS();

    public BillDetailDialog(int billId){
        setTitle("Chi tiết hóa đơn #" + billId);
        setSize(700,450);
        setLayout(new BorderLayout(10,10));
        setModal(true);
        setLocationRelativeTo(null);

        add(createInfoPanel(billId), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadBillInfo(billId);
        loadTable(billId);
    }

    private JPanel createInfoPanel(int billId){
        JPanel panel = new JPanel(new GridLayout(2,2,10,10));
        panel.setBorder(new EmptyBorder(15,15,15,15));

        Font font = new Font(AppConstant.FONT_NAME, Font.BOLD, 16);

        lbBillId = new JLabel("Mã hóa đơn: " + billId);
        lbEmployee = new JLabel("Nhân Viên: --");
        lbCustomer = new JLabel("Khách hàng: --");
        lbTotal = new JLabel("Tổng tiền: " + MoneyFormatter.toVND(0));

        lbBillId.setFont(font);
        lbEmployee.setFont(font);
        lbCustomer.setFont(font);
        lbTotal.setFont(font);

        panel.add(lbBillId);
        panel.add(lbEmployee);
        panel.add(lbCustomer);
        panel.add(lbTotal);

        return panel;
    }


    private JScrollPane createTablePanel(){
        String[] column = {"Tên sách", "Số lượng", "Đơn giá", "Thành tiền"};

        model = new DefaultTableModel(column, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);


        return new JScrollPane(table);
    }

    private JPanel createBottomPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN,14));

        btnClose.addActionListener(e-> dispose());

        panel.add(btnClose);

        return panel;
    }

    private void loadTable(int billId){
        model.setRowCount(0);

        List<BillDetailDialogDTO> list = bus.getBillDetailByBillId(billId);

        double total = 0;

        for(BillDetailDialogDTO dto : list){
            double thanhTien = dto.getPrice() * dto.getQuantity();
            total += thanhTien;

            model.addRow(new Object[]{
                    dto.getBookName(),
                    dto.getQuantity(),
                    MoneyFormatter.toVND(dto.getPrice()),
                    MoneyFormatter.toVND(thanhTien)
            });
        }
    }

    private void loadBillInfo(int billId){

        String sql = """
        SELECT b.bill_id,
               e.employee_name,
               c.customer_name,
               b.total_bill_price
        FROM bill b
        JOIN employee e ON b.employee_id = e.employee_id
        LEFT JOIN customer c ON b.customer_id = c.customer_id
        WHERE b.bill_id = ?
    """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, billId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                lbEmployee.setText("Nhân viên: " + rs.getString("employee_name"));

                String customer = rs.getString("customer_name");
                lbCustomer.setText("Khách hàng: " + (customer !=null ? customer : "khách lẻ"));

                lbTotal.setText("Tổng tiền: " + MoneyFormatter.toVND(rs.getDouble("total_bill_price")));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}