package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;
import javax.swing.table.*;

import com.bookstore.dto.AccountDTO;
import com.bookstore.bus.*;
import java.awt.*;

public class AccountPanel extends JPanel {
    private WhiteBoxPanel whiteBox;

    private AccountBUS accountBUS = new AccountBUS();
    private DefaultTableModel adminModel;
    private String currentAdminPassword = "";

    public AccountPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        whiteBox = new WhiteBoxPanel();
        whiteBox.setLayout(new BorderLayout(0, 20));

        initAdminSection();
        initTableSection();

        add(whiteBox, BorderLayout.CENTER);
        loadAdminData();
    }

    private void initAdminSection() {
        JPanel adminPanel = new JPanel(new GridBagLayout());
        adminPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(17, 71, 50)),
                        "Tài khoản Admin", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(17, 71, 50)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        adminPanel.setOpaque(false);

        String[] cols = { "Username", "Password" };
        adminModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable adminTable = new JTable(adminModel);
        adminTable.setRowHeight(40);
        adminTable.setShowGrid(true);
        adminTable.setGridColor(Color.GRAY);
        adminTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = adminTable.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(adminTable.getWidth(), 30));

        adminTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setBackground(Color.WHITE);
                l.setForeground(Color.BLACK);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
                return l;
            }
        });

        JButton btnEdit = new JButton("Sửa tài khoản");
        btnEdit.setBackground(new Color(17, 71, 50));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.addActionListener(e -> showEditAdminDialog());

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnP.setOpaque(false);
        btnP.add(btnEdit);

        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        scrollPane.setPreferredSize(new Dimension(800, 72));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        adminPanel.add(scrollPane, gbc);

        gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        adminPanel.add(btnP, gbc);

        whiteBox.add(adminPanel, BorderLayout.NORTH);
    }

    private void initTableSection() {
        String[] cols = { "Email", "Password", "Name", "SĐT", "Địa chỉ", "Trạng thái", "Hành động" };
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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            p.setBackground(isSelected ? new Color(17, 71, 50) : Color.WHITE);
            JButton btnEdit = new JButton("Sửa");
            JButton btnDelete = new JButton("Xóa");
            btnEdit.setBackground(new Color(240, 173, 78));
            btnEdit.setForeground(Color.WHITE);
            btnDelete.setBackground(new Color(217, 83, 79));
            btnDelete.setForeground(Color.WHITE);
            btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 9));
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 9));
            p.add(btnEdit);
            p.add(btnDelete);
            return p;
        }
    }

    private void showEditAdminDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Admin", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String currentUsername = adminModel.getValueAt(0, 0).toString();

        JTextField txtUser = new JTextField(currentUsername);
        JTextField txtPass = new JTextField(currentAdminPassword);

        form.add(new JLabel("Username:"));
        form.add(txtUser);
        form.add(new JLabel("Password:"));
        form.add(txtPass);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            AccountDTO updatedAcc = new AccountDTO(
                    txtUser.getText(),
                    txtPass.getText(),
                    0,
                    1
            );

            if (accountBUS.updateAccount(updatedAcc)) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                loadAdminData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadAdminData() {
        try {
            AccountDTO admin = accountBUS.selectByUsername("admin");
            adminModel.setRowCount(0);
            if (admin != null) {
                currentAdminPassword = admin.getPassword(); // Lưu mật khẩu thật
                adminModel.addRow(new Object[] { admin.getUsername(), "*****" }); // Bảng chỉ hiện sao
            } else {
                adminModel.addRow(new Object[] { "Không tìm thấy", "N/A" });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}