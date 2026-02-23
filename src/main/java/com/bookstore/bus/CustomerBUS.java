package com.bookstore.bus;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.dto.CustomerDTO;
import com.bookstore.dto.MembershipRankDTO;

public class CustomerBUS {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final MembershipRankBUS rankBUS = new MembershipRankBUS();

    public CustomerDTO selectByPhone(String phone) {
        return customerDAO.selectByPhone(phone);
    }

    public boolean updateCustomerRank(int customerId, int currentPoint, int earnedPoint) {
        int newTotalPoint = currentPoint + earnedPoint;
        MembershipRankDTO newRank = rankBUS.getRankByPoint(newTotalPoint);
        return customerDAO.updatePointAndRank(customerId, newTotalPoint, newRank.getRankId());
    }
}
