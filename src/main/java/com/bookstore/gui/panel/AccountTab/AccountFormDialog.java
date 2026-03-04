package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;

import com.bookstore.bus.AccountBUS;
import com.bookstore.dto.AccountDTO;
import com.bookstore.dto.EmployeeDTO;

import java.awt.*;

public class AccountFormDialog extends JDialog {
    private AccountBUS accountBUS = new AccountBUS();
    private AccountPanel parentPanel;
    private JTextField txtName, txtUsername, txtPassword, txtRole;
    private JRadioButton radActive, radInactive;
    private JButton btnSave;

    public AccountFormDialog(Frame parent, String title, AccountPanel panel) {
        super(parent, title, true);
        this.parentPanel = panel;
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        txtName = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtRole = new JTextField();

        radActive = new JRadioButton("Đang làm việc");
        radInactive = new JRadioButton("Ngưng làm việc");
        ButtonGroup group = new ButtonGroup();
        group.add(radActive);
        group.add(radInactive);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        statusPanel.add(radActive);
        statusPanel.add(radInactive);

        mainPanel.add(new JLabel("Tên nhân viên:"));
        mainPanel.add(txtName);
        mainPanel.add(new JLabel("Username:"));
        mainPanel.add(txtUsername);
        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(txtPassword);
        mainPanel.add(new JLabel("Chức vụ:"));
        mainPanel.add(txtRole);
        mainPanel.add(new JLabel("Trạng thái:"));
        mainPanel.add(statusPanel);

        btnSave = new JButton("Lưu");
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave.setPreferredSize(new Dimension(100, 35));
        bottomPanel.add(btnSave);

        btnSave.addActionListener(e -> {
            AccountDTO newAcc = getDataFromForm();

            String message = accountBUS.addAccount(newAcc);

            if (message.equals("Thêm tài khoản thành công!")) {
                JOptionPane.showMessageDialog(this, message);
                this.dispose();

                if (parentPanel != null)
                    parentPanel.loadAccountData();
            } else {
                JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public AccountDTO getDataFromForm() {
        AccountDTO acc = new AccountDTO();
        acc.setUsername(txtUsername.getText().trim());
        acc.setPassword(txtPassword.getText().trim());

        acc.setStatus(radActive.isSelected() ? 1 : 0);
        return acc;
    }

    public void setDataToForm(AccountDTO acc, EmployeeDTO emp) {
        if (acc != null) {
            txtUsername.setText(acc.getUsername());
            txtPassword.setText(acc.getPassword());

            if (acc.getStatus() == 1) {
                radActive.setSelected(true);
            } else {
                radInactive.setSelected(true);
            }
        }

        if (emp != null) {
            txtName.setText(emp.getEmployeeName());
            txtRole.setText(emp.getEmployeeName());
        }
    }
}