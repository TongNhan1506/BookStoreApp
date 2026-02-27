package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;


public class AccountPanel extends JPanel {
    private WhiteBoxPanel whiteBox;
    private JLabel lblAdminName, lblAdminPass;

    public AccountPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Box trắng bao trùm toàn bộ
        whiteBox = new WhiteBoxPanel();
        whiteBox.setLayout(new BorderLayout(0, 20));

        initAdminSection();
        initTableSection();

        add(whiteBox, BorderLayout.CENTER);
    }

    private void initAdminSection() {
    // 1. Panel bao ngoài cho phần Admin
    JPanel adminPanel = new JPanel(new BorderLayout(0, 10));

// Kết hợp LineBorder (đường viền màu) với EmptyBorder (khoảng cách bên trong)
adminPanel.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(17, 71, 50)), 
        "Tài khoản Admin", 0, 0, 
        new Font("Segoe UI", Font.BOLD, 14), 
        new Color(17, 71, 50)
    ),
    BorderFactory.createEmptyBorder(10, 10, 10, 10) // Đây là phần Padding bị lỗi ở trên
));

adminPanel.setOpaque(false);

    // 2. Tạo bảng 2 cột cho Username và Password
    String[] cols = {"Username", "Password"};
    Object[][] data = {{"admin_user", "*****"}};
    DefaultTableModel model = new DefaultTableModel(data, cols);
    JTable adminTable = new JTable(model);
    
    // Tùy chỉnh giao diện bảng Admin
    adminTable.setRowHeight(40);
    adminTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
    // Tạo Header màu đen
    JTableHeader header = adminTable.getTableHeader();
    header.setBackground(Color.BLACK);
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setPreferredSize(new Dimension(adminTable.getWidth(), 30));

    // Tô màu nền cho ô nội dung (xám nhạt)
    adminTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(new Color(245, 245, 245)); // Màu xám nhạt như ảnh
            c.setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    });

    // 3. Nút Sửa tài khoản (Xanh đậm, bo góc)
    JButton btnEdit = new JButton("Sửa tài khoản");
    btnEdit.setBackground(new Color(17, 71, 50));
    btnEdit.setForeground(Color.WHITE);
    btnEdit.setFocusPainted(false);
    btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
    btnEdit.addActionListener(e -> showEditAdminDialog());

    JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnP.setOpaque(false);
    btnP.add(btnEdit);

    adminPanel.add(new JScrollPane(adminTable) {{ setPreferredSize(new Dimension(0, 75)); }}, BorderLayout.CENTER);
    adminPanel.add(btnP, BorderLayout.SOUTH);
    
    whiteBox.add(adminPanel, BorderLayout.NORTH);
}

    private void initTableSection() {
        String[] cols = {"Email", "Password", "Name", "SĐT", "Địa chỉ", "Trạng thái", "Hành động"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        table.getColumn("Hành động").setCellRenderer(new ActionRenderer());
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Tài khoản Nhân viên"));
        tablePanel.setOpaque(false);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        whiteBox.add(tablePanel, BorderLayout.CENTER);
    }

    class WhiteBoxPanel extends JPanel {
        public WhiteBoxPanel() {
            setOpaque(false);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
        }
    }

    class ActionRenderer extends DefaultTableCellRenderer {
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

    private void showEditAdminDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Admin", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.add(new JLabel("Username:")); JTextField txtUser = new JTextField("admin_user"); dialog.add(txtUser);
        dialog.add(new JLabel("Password:")); JTextField txtPass = new JPasswordField("123456"); dialog.add(txtPass);
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnSave.addActionListener(e -> dialog.dispose());
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.add(btnCancel); dialog.add(btnSave);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
