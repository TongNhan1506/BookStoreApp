package com.bookstore.bus;

import com.bookstore.dao.FinancialStatsDAO;
import com.bookstore.dto.FinancialStatsDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinancialStatsBUS {
    private final FinancialStatsDAO financialStatsDAO = new FinancialStatsDAO();

    public List<FinancialStatsDTO> getThongKeTheoNgay(Date tuNgay, Date denNgay) {
        return financialStatsDAO.getThongKeTheoNgay(tuNgay, denNgay);
    }

    public List<FinancialStatsDTO> getThongKeTheoThang(int nam) {
        return financialStatsDAO.getThongKeTheoThang(nam);
    }

    public QuyDoanhThuSummary getQuyDoanhThuSummary(int nam) {
        List<FinancialStatsDTO> dataTheoThang = getThongKeTheoThang(nam);
        if (dataTheoThang.isEmpty()) {
            return null;
        }

        Map<Integer, Double> doanhThuTheoQuy = new HashMap<>();
        for (FinancialStatsDTO item : dataTheoThang) {
            int thang = extractMonth(item.getThoiGian());
            if (thang <= 0) {
                continue;
            }
            int quy = ((thang - 1) / 3) + 1;
            doanhThuTheoQuy.merge(quy, item.getDoanhThu(), Double::sum);
        }

        if (doanhThuTheoQuy.isEmpty()) {
            return null;
        }

        int quyMax = -1;
        int quyMin = -1;
        double doanhThuMax = Double.NEGATIVE_INFINITY;
        double doanhThuMin = Double.POSITIVE_INFINITY;

        for (Map.Entry<Integer, Double> entry : doanhThuTheoQuy.entrySet()) {
            int quy = entry.getKey();
            double doanhThu = entry.getValue();

            if (doanhThu > doanhThuMax) {
                doanhThuMax = doanhThu;
                quyMax = quy;
            }

            if (doanhThu < doanhThuMin) {
                doanhThuMin = doanhThu;
                quyMin = quy;
            }
        }

        return new QuyDoanhThuSummary(nam, quyMax, doanhThuMax, quyMin, doanhThuMin);
    }

    private int extractMonth(String thoiGian) {
        if (thoiGian == null) {
            return -1;
        }

        String normalized = thoiGian.trim().toLowerCase();
        if (!normalized.startsWith("tháng ")) {
            return -1;
        }

        String[] parts = normalized.replace("tháng ", "").split("/");
        if (parts.length == 0) {
            return -1;
        }

        try {
            return Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static class QuyDoanhThuSummary {
        private final int nam;
        private final int quyCaoNhat;
        private final double doanhThuQuyCaoNhat;
        private final int quyThapNhat;
        private final double doanhThuQuyThapNhat;

        public QuyDoanhThuSummary(int nam, int quyCaoNhat, double doanhThuQuyCaoNhat, int quyThapNhat, double doanhThuQuyThapNhat) {
            this.nam = nam;
            this.quyCaoNhat = quyCaoNhat;
            this.doanhThuQuyCaoNhat = doanhThuQuyCaoNhat;
            this.quyThapNhat = quyThapNhat;
            this.doanhThuQuyThapNhat = doanhThuQuyThapNhat;
        }

        public int getNam() {
            return nam;
        }

        public int getQuyCaoNhat() {
            return quyCaoNhat;
        }

        public double getDoanhThuQuyCaoNhat() {
            return doanhThuQuyCaoNhat;
        }

        public int getQuyThapNhat() {
            return quyThapNhat;
        }

        public double getDoanhThuQuyThapNhat() {
            return doanhThuQuyThapNhat;
        }
    }
}
