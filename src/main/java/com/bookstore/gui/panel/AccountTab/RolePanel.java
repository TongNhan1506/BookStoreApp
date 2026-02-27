package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class RolePanel extends JPanel {
    public RolePanel() {
        setLayout(new GridLayout(2, 1, 10, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 1. Bảng Vai trò
        JTable tableRole = new JTable(new DefaultTableModel(new String[]{"Tên quyền", "TAB"}, 0));
        tableRole.setRowHeight(35);
        tableRole.setShowGrid(true);
        tableRole.setGridColor(Color.GRAY);
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("Vai trò hệ thống"));
        p1.add(new JScrollPane(tableRole));
        add(p1);
        
        // 2. Bảng Quyền
        String[] cols = {"Mã quyền", "Tên quyền", "Số quyền", "Trạng thái", "Hành động"};
        JTable tablePerm = new JTable(new DefaultTableModel(cols, 0));
        tablePerm.setRowHeight(35);
        tablePerm.setShowGrid(true);
        tablePerm.setGridColor(Color.GRAY);
        tablePerm.getColumn("Hành động").setCellRenderer(new RoleActionRenderer());
        
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("Quyền truy cập hệ thống"));
        p2.add(new JScrollPane(tablePerm));
        add(p2);
    }

    // --- Renderer riêng cho RolePanel ---
    class RoleActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            p.setBackground(isSelected ? new Color(17, 71, 50) : Color.WHITE);
            JButton btnEdit = new JButton("Sửa");
            JButton btnDelete = new JButton("Xóa");
            btnEdit.setBackground(new Color(240, 173, 78)); btnEdit.setForeground(Color.WHITE);
            btnDelete.setBackground(new Color(217, 83, 79)); btnDelete.setForeground(Color.WHITE);
            btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 9));
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 9));
            p.add(btnEdit); p.add(btnDelete);
            return p;
        }
    }
}