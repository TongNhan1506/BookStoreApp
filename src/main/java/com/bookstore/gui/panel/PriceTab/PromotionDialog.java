package com.bookstore.gui.panel.PriceTab;

import com.bookstore.bus.*;
import com.bookstore.dto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PromotionDialog extends JDialog {
    private JTextField txtID, txtTen, txtPercent, txtSearchBook;
    private JSpinner spStart, spEnd;
    private JTable bookTable;
    private DefaultTableModel bookModel;
    private PromotionBUS bus = new PromotionBUS();
    private PromotionDTO data;
    private boolean isEdit;

    public PromotionDialog(JFrame parent, String title, PromotionDTO dto) {
        super(parent, title, true);
        this.data = dto;
        this.isEdit = (dto != null);
        initComponents();
        
        // Logic lọc sách khi gõ vào ô tìm kiếm
        txtSearchBook.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadBookList(bus.suggestBooksByPromotionName(txtSearchBook.getText()));
            }
        });

        if (isEdit) {
            fillData();
        } else {
            // Tự động cấp ID không trùng
            int nextId = bus.getAll().stream().mapToInt(PromotionDTO::getPromotionId).max().orElse(0) + 1;
            txtID.setText("KM" + nextId);
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(850, 700);
        setLayout(new BorderLayout(15, 15));

        // Panel thông tin dùng GridBagLayout
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các thành phần
        txtID = new JTextField(); txtID.setEditable(false);
        txtTen = new JTextField();
        txtPercent = new JTextField();
        txtSearchBook = new JTextField();
        spStart = new JSpinner(new SpinnerDateModel());
        spStart.setEditor(new JSpinner.DateEditor(spStart, "yyyy-MM-dd HH:mm:ss"));
        spEnd = new JSpinner(new SpinnerDateModel());
        spEnd.setEditor(new JSpinner.DateEditor(spEnd, "yyyy-MM-dd HH:mm:ss"));

        // Dòng 1: ID và Tên
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; infoPanel.add(new JLabel("Mã KM:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5; infoPanel.add(txtID, gbc);
        gbc.gridx = 2; gbc.weightx = 0; infoPanel.add(new JLabel("Tên chương trình:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5; infoPanel.add(txtTen, gbc);

        // Dòng 2: Phần trăm và Tìm sách
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; infoPanel.add(new JLabel("Phần trăm (%):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5; infoPanel.add(txtPercent, gbc);
        gbc.gridx = 2; gbc.weightx = 0; infoPanel.add(new JLabel("Tìm sách:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5; infoPanel.add(txtSearchBook, gbc);

        // Dòng 3: Ngày bắt đầu và Kết thúc
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; infoPanel.add(new JLabel("Ngày bắt đầu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5; infoPanel.add(spStart, gbc);
        gbc.gridx = 2; gbc.weightx = 0; infoPanel.add(new JLabel("Ngày kết thúc:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5; infoPanel.add(spEnd, gbc);

        // Bảng chọn sách
        String[] cols = {"Chọn", "Mã sách", "Tên sách", "Thể loại"};
        bookModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) { return col == 0; }
        };
        bookTable = new JTable(bookModel);
        
        TableColumnModel columnModel = bookTable.getColumnModel();
    // 1. Ép cột Checkbox "Chọn" thật nhỏ
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(0).setMaxWidth(50); 
        columnModel.getColumn(0).setMinWidth(50);

    // 2. Ép cột "Mã sách" nhỏ lại vừa đủ hiện số
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(1).setMaxWidth(100);

    // 3. Cho "Tên sách" và "Thể loại" tự động giãn ra chiếm hết phần còn lại
        columnModel.getColumn(2).setPreferredWidth(450); 
        columnModel.getColumn(3).setPreferredWidth(200);

        bookTable.setShowGrid(true); 
        bookTable.setGridColor(new Color(200, 200, 200));
        bookTable.setIntercellSpacing(new Dimension(1, 1));

        loadBookList(bus.suggestBooksByPromotionName(""));

        // Nút bấm
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnSave = new JButton("Lưu dữ liệu");
        btnSave.setBackground(new Color(35, 90, 180)); btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(120, 35));
        btnSave.addActionListener(e -> saveAction());
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.addActionListener(e -> dispose());
        btnPanel.add(btnCancel); btnPanel.add(btnSave);

        add(infoPanel, BorderLayout.NORTH);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadBookList(List<BookDTO> books) {
        bookModel.setRowCount(0);
        for (BookDTO b : books) {
            bookModel.addRow(new Object[]{false, b.getBookId(), b.getBookName(), b.getCategoryName()});
        }
    }

    private void fillData() {
        txtID.setText("KM" + data.getPromotionId());
        txtTen.setText(data.getPromotionName());
        txtPercent.setText(String.valueOf(data.getPercent()));
        spStart.setValue(data.getStartDate());
        spEnd.setValue(data.getEndDate());
        
        // Tick chọn lại sách đã lưu trong database
        List<Integer> selectedIds = bus.getSelectedBookIds(data.getPromotionId());
        for (int i = 0; i < bookModel.getRowCount(); i++) {
            int bookId = (Integer) bookModel.getValueAt(i, 1);
            if (selectedIds.contains(bookId)) bookModel.setValueAt(true, i, 0);
        }
    }

    private void saveAction() {
        try {
            String name = txtTen.getText().trim();
            String percentStr = txtPercent.getText().trim();
            if (name.isEmpty() || percentStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ tên và phần trăm!");
                return;
            }

            double percent = Double.parseDouble(percentStr);
            Timestamp start = new Timestamp(((java.util.Date) spStart.getValue()).getTime());
            Timestamp end = new Timestamp(((java.util.Date) spEnd.getValue()).getTime());

            if (start.after(end)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!");
                return;
            }

            List<Integer> selectedBookIds = new ArrayList<>();
            for (int i = 0; i < bookModel.getRowCount(); i++) {
                if ((Boolean) bookModel.getValueAt(i, 0)) {
                    selectedBookIds.add((Integer) bookModel.getValueAt(i, 1));
                }
            }

            if (selectedBookIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một cuốn sách!");
                return;
            }

            if (bus.savePromotion(isEdit, data, name, percent, start, end, selectedBookIds)) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Phần trăm phải là số!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}