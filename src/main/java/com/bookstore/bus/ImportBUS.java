package com.bookstore.bus;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.ImportDAO;
import com.bookstore.dao.ImportDetailDAO;
import com.bookstore.dto.ImportTicketDTO;
import com.bookstore.dto.ImportDetailDTO;

import java.util.List; // <--- QUAN TRỌNG: Thêm dòng này để sửa lỗi 'cannot find symbol class List'

public class ImportBUS {

    private ImportDAO importDAO = new ImportDAO();
    private ImportDetailDAO detailDAO = new ImportDetailDAO();
    private BookDAO bookDAO = new BookDAO();

    public List<ImportTicketDTO> getAllImports() {
        return importDAO.getAll();
    }

    public boolean importBooks(ImportTicketDTO importDTO, ImportDetailDTO[] details) {
        int newImportID = importDAO.add(importDTO);
        if (newImportID != -1) {
            for (ImportDetailDTO detail : details) {
                if (detail != null) {
                    detail.setImportID(newImportID);
                    detailDAO.add(detail);

                    // Cập nhật tồn kho
                    int currentStock = bookDAO.getQuantityByID(detail.getBookID());
                    bookDAO.updateQuantity(detail.getBookID(), currentStock + detail.getQuantity());
                }
            }
            return true;
        }
        return false;
    }
}