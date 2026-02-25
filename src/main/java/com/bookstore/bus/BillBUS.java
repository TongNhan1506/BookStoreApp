package com.bookstore.bus;

import com.bookstore.dao.BillDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CustomerDAO;
import com.bookstore.dto.BillDTO;
import com.bookstore.dto.BillDetailDTO;

import java.util.Date;
import java.util.List;


public class BillBUS {
    private BillDAO billDAO = new BillDAO();

    public boolean createBill(BillDTO bill, List<BillDetailDTO> details) {
        return billDAO.createBillTransaction(bill, details);
    }

    public List<BillDTO> getAllBills(){
        return billDAO.getAllBills();
    }
    public List<BillDTO> filterBills(Date fromDate, Date toDate, String keyword){
        return billDAO.filterBills(fromDate, toDate, keyword);
    }
}
