package com.bookstore.bus;
import com.bookstore.dao.BillDAO;
import com.bookstore.dto.BillDTO;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BillBUS {

    private BillDAO dao = new BillDAO();
    public List<BillDTO> getAllBills(){
        return dao.getAllBills();
    }

    public List<BillDTO> filterBills(Date from, Date to, String keyword){
        return dao.getAllBills().stream().filter(b->
                                                 !b.getCreatedDate().before(from)
                                                 && !b.getCreatedDate().after(to)
                                                 && (keyword.isEmpty()
                                                 || b.getBillId().toLowerCase().contains(keyword.toLowerCase())
                                                 || b.getCustomerName().toLowerCase().contains(keyword.toLowerCase()))
        ).collect(Collectors.toList());
    }

    public double calculateRevenue(List<BillDTO> list){
        return list .stream()
               .mapToDouble(BillDTO::getTotalAmount)
               .sum();
    }
    
}
