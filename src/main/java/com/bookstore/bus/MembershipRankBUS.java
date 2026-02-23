package com.bookstore.bus;

import com.bookstore.dao.MembershipRankDAO;
import com.bookstore.dto.MembershipRankDTO;

public class MembershipRankBUS {
    private final MembershipRankDAO rankDAO = new MembershipRankDAO();

    public MembershipRankDTO getRankByPoint(int point) {
        MembershipRankDTO rank = rankDAO.getRankByPoint(point);
        if (rank == null) {
            return new MembershipRankDTO(1, "Thành viên", 0, 0.0);
        }
        return rank;
    }
}