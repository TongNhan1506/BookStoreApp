package com.bookstore.bus;

import com.bookstore.dao.CustomerStatsDAO;
import com.bookstore.dto.CustomerStatsDTO;

import java.sql.Timestamp;
import java.util.List;

public class CustomerStatsBUS {
    private CustomerStatsDAO customerStatsDAO = new CustomerStatsDAO();

    public List<CustomerStatsDTO> getTopCustomersByQuantity(Timestamp fromDate, Timestamp toDate) {
        return customerStatsDAO.getTopCustomersByQuantity(fromDate, toDate);
    }

    public List<CustomerStatsDTO> getTopCustomersBySpending(Timestamp fromDate, Timestamp toDate) {
        return customerStatsDAO.getTopCustomersBySpending(fromDate, toDate);
    }
}
