package com.bookstore.bus;

import com.bookstore.dao.BillDetailDialogDAO;
import com.bookstore.dto.BillDetailDialogDTO;

import java.util.List;

public class BillDetailDialogBUS {

    private BillDetailDialogDAO billDetailDialogDAO;

    public BillDetailDialogBUS(){
        billDetailDialogDAO = new BillDetailDialogDAO();
    }

    public List<BillDetailDialogDTO> getBillDetailByBillId(int billId){
        return billDetailDialogDAO.getByBillId(billId);
    }

}