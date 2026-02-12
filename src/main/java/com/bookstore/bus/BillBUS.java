package com.bookstore.bus;

import com.bookstore.dao.BillDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CustomerDAO;
import com.bookstore.dto.BillDTO;
import com.bookstore.dto.BillDetailDTO;

import java.util.List;

public class BillBUS {
    private BillDAO billDAO = new BillDAO();
    private BookDAO bookDAO = new BookDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public boolean createBill(BillDTO bill, List<BillDetailDTO> details) {
        int billId = billDAO.insertBill(bill);
        if (billId == -1) return false;

        for (BillDetailDTO detail : details) {
            detail.setBillId(billId);
            billDAO.insertBillDetail(detail);

            bookDAO.decreaseQuantity(detail.getBookId(), detail.getQuantity());
        }

        if (bill.getCustomerId() > 0 && bill.getEarnedPoints() > 0) {
            customerDAO.addPoint(bill.getCustomerId(), bill.getEarnedPoints());
        }

        return true;
    }
}