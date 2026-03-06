package com.bookstore.bus;

import com.bookstore.dto.RevenueStatisticDTO;
import com.bookstore.dto.BillDTO;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueStatisticBUS {
    private BillBUS billBUS = new BillBUS();

    private List<BillDTO> getAllBills() {
        return billBUS.getAllBills();
    }

    public double getRevenueByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getAllBills().stream()
                .filter(bill -> isSameDay(bill.getCreatedDate(), cal))
                .mapToDouble(BillDTO::getTotalBillPrice)
                .sum();
    }

    public double getRevenueByMonth(int month, int year) {
        return getAllBills().stream()
                .filter(bill -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bill.getCreatedDate());
                    return cal.get(Calendar.MONTH) + 1 == month
                            && cal.get(Calendar.YEAR) == year;
                })
                .mapToDouble(BillDTO::getTotalBillPrice)
                .sum();
    }

    public double getRevenueByQuarter(int quarter, int year) {
        return getAllBills().stream()
                .filter(bill -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bill.getCreatedDate());

                    int billQuarter =
                            (cal.get(Calendar.MONTH) / 3) + 1;

                    return billQuarter == quarter
                            && cal.get(Calendar.YEAR) == year;
                })
                .mapToDouble(BillDTO::getTotalBillPrice)
                .sum();
    }

    public double getRevenueByYear(int year) {
        return getAllBills().stream()
                .filter(bill -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bill.getCreatedDate());
                    return cal.get(Calendar.YEAR) == year;
                })
                .mapToDouble(BillDTO::getTotalBillPrice)
                .sum();
    }

    private boolean isSameDay(Date date, Calendar target) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR)
                == target.get(Calendar.DAY_OF_YEAR)
                && cal.get(Calendar.YEAR)
                == target.get(Calendar.YEAR);
    }

    public List<RevenueStatisticDTO> getRevenueByMonthOfYear(int year) {
        List<RevenueStatisticDTO> result = new java.util.ArrayList<>();
        for (int month = 1; month <= 12; month++) {

            double revenue = getRevenueByMonth(month, year);

            result.add(new RevenueStatisticDTO(
                    String.valueOf(month),
                    revenue
            ));
        }

        return result;
    }

    public double calculateGrowthPercent(double current, double previous) {
        if (previous == 0) {
            return current == 0 ? 0 : 100;
        }
        return ((current - previous) / previous) * 100;
    }
}
