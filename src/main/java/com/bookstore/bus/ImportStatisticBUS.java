package com.bookstore.bus;

import com.bookstore.dao.ImportStatisticDAO;
import com.bookstore.dto.ImportStatisticDTO;
import java.util.Date;
import java.util.List;

public class ImportStatisticBUS {
    private ImportStatisticDAO dao = new ImportStatisticDAO();

    public List<ImportStatisticDTO> getStatistic(Date from, Date to){
        return dao.getImportByDateRange(from,to);
    }
    public List<ImportStatisticDTO> getStatisticByQuarter(int quarter, int year){
        return dao.getStatisticByQuarter(quarter, year);
    }
    public List<ImportStatisticDTO> getTop3Most(int quarter, int year){
        return dao.getTop3Most(quarter, year);
    }
    public List<ImportStatisticDTO> getTop3Least(int quarter, int year){
        return dao.getTop3Least(quarter, year);
    }
}