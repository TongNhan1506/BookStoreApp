package com.bookstore.bus;

import com.bookstore.dao.PromotionDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.dto.BookDTO;
import com.bookstore.dto.PromotionDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;

public class PromotionBUS {
    private PromotionDAO promotionDAO = new PromotionDAO();
    private BookDAO bookDAO = new BookDAO();
    private List<PromotionDTO> list;

    public double getPromotionPercentByBook(int bookId) {
        return promotionDAO.getPromotionPercentByBook(bookId);
    }

    public List<PromotionDTO> getAll() {
        this.list = promotionDAO.readAll();
        return list;
    }

    public List<PromotionDTO> search(String keyword, String type) {
    List<PromotionDTO> all = getAll();
    if (keyword == null || keyword.isEmpty()) return all;
    String lowerKeyword = keyword.toLowerCase();

    return all.stream().filter(p -> {
        switch (type) {
            case "Tên":
                return p.getPromotionName().toLowerCase().contains(lowerKeyword);
            case "Phần trăm":
                try {
                    double percent = Double.parseDouble(lowerKeyword);
                    return p.getPercent() == percent;
                } catch (Exception e) { return false; }
            case "Thời gian":
                try {
                    String[] dates = lowerKeyword.split("\\|");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    long startLimit = (dates[0].isEmpty()) ? 0 : sdf.parse(dates[0]).getTime();
                    long endLimit = (dates.length < 2 || dates[1].isEmpty()) ? Long.MAX_VALUE : sdf.parse(dates[1]).getTime();
                    return p.getStartDate().getTime() >= startLimit && p.getEndDate().getTime() <= endLimit;
                } catch (Exception e) { return false; }
            default:
                return true;
        }
    }).collect(Collectors.toList());
}

    public void updateAutoStatus() {
        List<PromotionDTO> all = promotionDAO.readAll();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (PromotionDTO p : all) {
            int newStatus = p.getStatus();
            if (now.after(p.getEndDate())) {
                newStatus = 0;
            } else if (now.after(p.getStartDate()) && now.before(p.getEndDate())) {
                newStatus = 1;
            }
            if (newStatus != p.getStatus()) {
                p.setStatus(newStatus);
                promotionDAO.updateStatus(p);
            }
        }
    }

// Lọc sách theo chủ đề tên chương trình
    public List<BookDTO> suggestBooksByPromotionName(String promoName) {
        String name = promoName.toLowerCase();
        int categoryId = -1;
        if (name.contains("văn học")) categoryId = 1;
        else if (name.contains("truyện tranh")) categoryId = 3;
        else if (name.contains("kỹ năng")) categoryId = 4;
    
        if (categoryId != -1) {
            return bookDAO.getByCategory(categoryId); 
        }
        return bookDAO.selectAllBooks(); 
    }

    public void updateStatus(PromotionDTO p) {
        promotionDAO.updateStatus(p);
    }

    public boolean savePromotion(boolean isEdit, PromotionDTO dto, String name, double percent, Timestamp start, Timestamp end, List<Integer> bookIds) {
        if (!isEdit) {
        // Tạo DTO mới để Thêm
            PromotionDTO newPromo = new PromotionDTO(0, name, percent, start, end, 1);
            int newId = promotionDAO.add(newPromo); // Hàm add này trả về ID vừa chèn
            if (newId > 0) {
                return promotionDAO.savePromotionDetails(newId, bookIds);
            }
            } else {
        // Cập nhật DTO cũ
                dto.setPromotionName(name);
                dto.setPercent(percent);
                dto.setStartDate(start);
                dto.setEndDate(end);
                if (promotionDAO.update(dto)) {
            // Xóa chi tiết cũ và lưu lại chi tiết mới cho đồng bộ
                    promotionDAO.deletePromotionDetails(dto.getPromotionId());
                    return promotionDAO.savePromotionDetails(dto.getPromotionId(), bookIds);
                }
            }
        return false;
    }

    public List<Integer> getSelectedBookIds(int promoId) {
        return promotionDAO.getSelectedBookIds(promoId);
    }   
}