package com.bookstore.bus;

import com.bookstore.dao.AccountDAO;
import com.bookstore.dao.EmployeeDAO;
import com.bookstore.dto.AccountDTO;
import com.bookstore.dto.EmployeeDTO;

public class AccountBUS {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    public EmployeeDTO login(String username, String password) throws Exception {
        AccountDTO acc = accountDAO.selectByUsername(username);

        if (acc == null) {
            throw new Exception("Tài khoản không tồn tại!");
        }

        if (acc.getStatus() == 0) {
            throw new Exception("Tài khoản này đã bị khóa. Vui lòng liên hệ Quản Lý!");
        }

        if (!acc.getPassword().equals(password)) {
            throw new Exception("Mật khẩu không chính xác!");
        }

        return employeeDAO.selectById(acc.getEmployeeId());
    }

    public AccountDTO selectByUsername(String username) {
        return accountDAO.selectByUsername(username);
    }

    public String updateAccount(AccountDTO acc, boolean isChangePassword) {
        if (acc.getUsername() == null || acc.getUsername().trim().isEmpty()) {
            return "Tên đăng nhập không hợp lệ!";
        }

        boolean success = accountDAO.updateAccount(acc, isChangePassword);

        if (success) {
            return "Cập nhật tài khoản thành công!";
        } else {
            return "Cập nhật thất bại. Vui lòng kiểm tra lại hệ thống!";
        }
    }

    public String addAccount(AccountDTO acc) {
        if (acc.getUsername().trim().isEmpty()) return "Username không được để trống!";

        if (accountDAO.selectByUsername(acc.getUsername()) != null) {
            return "Username này đã tồn tại!";
        }

        return accountDAO.insert(acc) ? "Thêm tài khoản thành công!" : "Thêm thất bại!";
    }
}