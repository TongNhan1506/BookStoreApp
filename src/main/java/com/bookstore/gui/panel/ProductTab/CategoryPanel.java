package com.bookstore.gui.panel.ProductTab;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class CategoryPanel extends JPanel {
    public CategoryPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10)); // Khoảng cách giữa các phần là 10px
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding cho toàn bộ Panel

        // --- PHẦN BÊN TRÁI: BẢNG VÀ TAGS ---
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 15)); // Chia 2 hàng: trên Table, dưới Tags

        // 1. Panel Bảng Thể loại
        JPanel pnlCategoryTable = new JPanel(new BorderLayout(5, 5));
        pnlCategoryTable.add(new JLabel("DANH SÁCH THỂ LOẠI"), BorderLayout.NORTH);

        String[] columns = {"ID", "Tên Thể Loại", "Số lượng sách"};
        Object[][] data = {{1, "Văn học", 10}, {2, "Kỹ thuật", 5}}; // Data giả lập
        JTable table = new JTable(data, columns);
        pnlCategoryTable.add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Panel Quản lý Tags
        JPanel pnlTags = new JPanel(new BorderLayout(5, 5));
        pnlTags.add(new JLabel("QUẢN LÝ TAGS (NHÃN)"), BorderLayout.NORTH);

        JPanel tagCloud = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        tagCloud.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        // Sau này bạn sẽ dùng vòng lặp để addTagChip(name) vào đây
        tagCloud.add(createTagChip("Bán chạy"));
        tagCloud.add(createTagChip("Mới về"));

        pnlTags.add(new JScrollPane(tagCloud), BorderLayout.CENTER);

        JTextField txtQuickAddTag = new JTextField();
        txtQuickAddTag.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tag mới rồi Enter...");
        pnlTags.add(txtQuickAddTag, BorderLayout.SOUTH);

        leftPanel.add(pnlCategoryTable);
        leftPanel.add(pnlTags);

        // --- PHẦN BÊN PHẢI: FORM NHẬP LIỆU ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(new TitledBorder("THÔNG TIN CHI TIẾT"));
        rightPanel.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Các thành phần của Form
        rightPanel.add(new JLabel("Tên thể loại:"), gbc);
        gbc.gridy = 1;
        rightPanel.add(new JTextField(20), gbc);

        gbc.gridy = 2;
        rightPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridy = 3;
        JTextArea txtDesc = new JTextArea(5, 20);
        txtDesc.setLineWrap(true);
        rightPanel.add(new JScrollPane(txtDesc), gbc);

        // Nút bấm
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton btnSave = new JButton("LƯU");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background: #007bff; foreground: #ffffff"); // Màu xanh pro
        pnlButtons.add(btnSave);
        pnlButtons.add(new JButton("LÀM MỚI"));

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        rightPanel.add(pnlButtons, gbc);

        // --- SỬ DỤNG SPLITPANE ĐỂ GỘP LẠI ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600); // Tỷ lệ chia màn hình

        add(splitPane, BorderLayout.CENTER);
    }

    // Hàm phụ để tạo các Chip Tag đẹp (Bo góc)
    private JPanel createTagChip(String name) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        chip.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #edeff2");
        chip.add(new JLabel(name));
        JButton btnX = new JButton("x");
        btnX.setBorderPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chip.add(btnX);
        return chip;
    }
}