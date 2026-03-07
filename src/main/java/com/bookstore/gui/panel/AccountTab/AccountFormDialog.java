package com.bookstore.gui.panel.AccountTab;

import javax.swing.*;

import com.bookstore.bus.AccountBUS;
import com.bookstore.bus.EmployeeBUS;
import com.bookstore.dto.AccountDTO;
import com.bookstore.dto.EmployeeDTO;
import com.bookstore.util.AppConstant;

import java.awt.*;
import java.util.List;

public class AccountFormDialog extends JDialog {
    private AccountBUS accountBUS = new AccountBUS();
    private EmployeeBUS employeeBUS = new EmployeeBUS();

    private AccountPanel parentPanel;
    private JComboBox<EmployeeDTO> cboEmployee;
    private JTextField txtUsername, txtPassword, txtRole;
    private JRadioButton radActive, radInactive;
    private JButton btnCreate, btnSave;

    private AccountDTO account;

    public AccountFormDialog(Frame parent, AccountPanel panel, AccountDTO account) {
        super(parent, account == null ? "Thêm Tài Khoản Mới" : "Chỉnh Sửa Tài Khoản", true);
        this.parentPanel = panel;
        this.account = account;

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        cboEmployee = new JComboBox<>();
        cboEmployee.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof EmployeeDTO) {
                    EmployeeDTO emp = (EmployeeDTO) value;
                    if (emp.getEmployeeId() == -1) {
                        setText(emp.getEmployeeName());
                        setForeground(Color.GRAY);
                        setFont(getFont().deriveFont(Font.ITALIC));
                    } else {
                        setText(emp.getEmployeeName() + " (" + emp.getRoleName() + ")");
                        setForeground(Color.BLACK);
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                }
                return this;
            }
        });
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtRole = new JTextField();
        txtRole.setEditable(false);
        txtRole.setBackground(Color.decode("#EEEEEE"));

        cboEmployee.addActionListener(e -> {
            EmployeeDTO selectedEmp = (EmployeeDTO) cboEmployee.getSelectedItem();
            if (selectedEmp != null) {
                txtRole.setText(selectedEmp.getRoleName());
            } else {
                txtRole.setText("");
            }
        });

        radActive = new JRadioButton("Đang làm việc");
        radInactive = new JRadioButton("Ngưng làm việc");
        ButtonGroup group = new ButtonGroup();
        group.add(radActive);
        group.add(radInactive);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        statusPanel.add(radActive);
        statusPanel.add(radInactive);

        mainPanel.add(new JLabel("Chọn nhân viên:"));
        mainPanel.add(cboEmployee);
        mainPanel.add(new JLabel("Username:"));
        mainPanel.add(txtUsername);
        mainPanel.add(new JLabel("Password:"));
        mainPanel.add(txtPassword);
        mainPanel.add(new JLabel("Chức vụ:"));
        mainPanel.add(txtRole);
        mainPanel.add(new JLabel("Trạng thái:"));
        mainPanel.add(statusPanel);

        btnCreate = new JButton("Tạo mới");
        btnCreate.setBackground(Color.BLACK);
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setPreferredSize(new Dimension(100, 35));

        btnSave = new JButton("Lưu");
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(100, 35));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (account == null) {
            bottomPanel.add(btnCreate);
        } else {
            bottomPanel.add(btnSave);
            txtUsername.setEditable(false);
            txtUsername.setBackground(Color.decode("#EEEEEE"));
        }

        btnCreate.addActionListener(e -> {
            AccountDTO newAcc = getDataFromForm();
            if (newAcc == null) return;

            String message = accountBUS.addAccount(newAcc);
            if (message.equals("Thêm tài khoản thành công!")) {
                JOptionPane.showMessageDialog(this, message);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSave.addActionListener(e -> {
            AccountDTO updatedAcc = getDataFromForm();
            if (updatedAcc == null) return;

            String newPassword = txtPassword.getText().trim();
            boolean isChangePassword = !newPassword.isEmpty();

            String message = accountBUS.updateAccount(updatedAcc, isChangePassword);
            if (message.equals("Cập nhật tài khoản thành công!")) {
                JOptionPane.showMessageDialog(this, message);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public void setDataToForm(AccountDTO acc, EmployeeDTO currentEmp) {
        List<EmployeeDTO> availableEmps = employeeBUS.getEmployeesWithoutAccount();
        cboEmployee.removeAllItems();

        if (currentEmp != null) {
            cboEmployee.addItem(currentEmp);
        }

        for (EmployeeDTO emp : availableEmps) {
            cboEmployee.addItem(emp);
        }

        if (cboEmployee.getItemCount() == 0) {
            EmployeeDTO dummyEmp = new EmployeeDTO();
            dummyEmp.setEmployeeId(-1);
            dummyEmp.setEmployeeName("Tất cả NV đều đã có tài khoản");

            cboEmployee.addItem(dummyEmp);
            cboEmployee.setEnabled(false);

            if (btnCreate != null) {
                btnCreate.setEnabled(false);
                btnCreate.setBackground(Color.GRAY);
            }
        } else {
            cboEmployee.setEnabled(true);
            if (btnCreate != null) {
                btnCreate.setEnabled(true);
                btnCreate.setBackground(Color.BLACK);
            }
        }

        if (acc != null) {
            txtUsername.setText(acc.getUsername());
            txtPassword.setText("");
            if (acc.getStatus() == 1) {
                radActive.setSelected(true);
            } else {
                radInactive.setSelected(true);
            }
        } else {
            radActive.setSelected(true);
        }

        if (currentEmp != null) {
            for (int i = 0; i < cboEmployee.getItemCount(); i++) {
                if (cboEmployee.getItemAt(i).getEmployeeId() == currentEmp.getEmployeeId()) {
                    cboEmployee.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public AccountDTO getDataFromForm() {
        EmployeeDTO selectedEmp = (EmployeeDTO) cboEmployee.getSelectedItem();
        if (selectedEmp == null || selectedEmp.getEmployeeId() == -1) {
            JOptionPane.showMessageDialog(this, "Không có nhân viên hợp lệ để cấp tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }


        AccountDTO acc = new AccountDTO();
        acc.setEmployeeId(selectedEmp.getEmployeeId());
        acc.setUsername(txtUsername.getText().trim());
        acc.setPassword(txtPassword.getText().trim());
        acc.setStatus(radActive.isSelected() ? 1 : 0);
        return acc;
    }
}