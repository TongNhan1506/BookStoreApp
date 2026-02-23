package com.bookstore.bus;

import com.bookstore.dao.EmployeeDAO;
import com.bookstore.dto.EmployeeDTO;

import java.util.List;

public class EmployeeBUS {
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public List<EmployeeDTO> getAllEmployees() {
        return employeeDAO.selectAllEmployees();
    }
}
