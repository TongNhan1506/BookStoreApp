package com.bookstore.gui.panel.InventoryTab;

import com.bookstore.bus.BookBUS;
import com.bookstore.dto.BookDTO;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatSearchIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private BookBUS bookBUS = new BookBUS(); // Gọi BUS

    public InventoryPanel() {
        initUI();
        loadDataToTable(); // Hàm load data từ DB
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createFilterPanel());
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createStatsPanel());
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createTablePanel());

        add(centerPanel, BorderLayout.CENTER);
    }

    // ... (Giữ nguyên các hàm createHeaderPanel, createFilterPanel, createStatsPanel như cũ) ...
    // Để tiết kiệm không gian, tôi chỉ viết lại hàm createTablePanel và các phần thay đổi

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE")));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");

        JLabel lbTitle = new JLabel("Danh sách tồn kho");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbTitle.setBorder(new EmptyBorder(20, 20, 10, 20));
        panel.add(lbTitle, BorderLayout.NORTH);

        // Khởi tạo Model rỗng
        String[] columns = {"TÊN SÁCH", "DANH MỤC", "TỒN KHO", "TRẠNG THÁI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.GRAY);
        table.getTableHeader().setBackground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(Color.decode("#F5F5F5"));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Áp dụng Custom Renderer
        table.getColumnModel().getColumn(1).setCellRenderer(new CategoryRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new StockRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setBorder(new EmptyBorder(0, 20, 0, 0));
        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- HÀM LOAD DATA TỪ DB ---
    private void loadDataToTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<BookDTO> books = bookBUS.selectAllBooks();

        for (BookDTO book : books) {
            // Logic xác định trạng thái hiển thị dựa trên số lượng tồn
            String statusDisplay;
            if (book.getQuantity() == 0) {
                statusDisplay = "HẾT HÀNG";
            } else if (book.getQuantity() < 30) {
                statusDisplay = "SẮP HẾT HÀNG";
            } else {
                statusDisplay = "Đủ hàng";
            }

            tableModel.addRow(new Object[]{
                    book.getBookName(),
                    book.getCategoryName(), // Đã lấy được tên danh mục
                    book.getQuantity(),
                    statusDisplay
            });
        }
    }

    // --- CÁC RENDERER (Giữ nguyên hoặc tùy chỉnh thêm nếu muốn) ---
    // (Copy lại các class CategoryRenderer, StockRenderer, StatusRenderer từ câu trả lời trước)

    static class CategoryRenderer extends DefaultTableCellRenderer {
        private final JLabel label = new JLabel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value != null ? value.toString() : "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setOpaque(true);
            label.setBackground(Color.decode("#F3F4F6"));
            label.setForeground(Color.decode("#374151"));
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
            panel.setBackground(Color.WHITE);
            if(isSelected) panel.setBackground(table.getSelectionBackground());
            panel.add(label);
            return panel;
        }
    }

    static class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Integer) {
                int stock = (int) value;
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                if (stock < 30) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
            }
            return c;
        }
    }

    static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            String status = value != null ? value.toString() : "";

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
            panel.setBackground(Color.WHITE);
            if(isSelected) panel.setBackground(table.getSelectionBackground());

            if (status.equals("SẮP HẾT HÀNG") || status.equals("HẾT HÀNG")) {
                label.setText("⚠  " + status);
                label.setForeground(Color.decode("#FF5722")); // Cam/Đỏ
                label.setBackground(Color.decode("#FBE9E7"));
            } else {
                label.setText(" " + status + " ");
                label.setForeground(Color.decode("#1B5E20")); // Xanh lá
                label.setBackground(Color.decode("#E8F5E9"));
            }
            label.setOpaque(true);
            panel.add(label);
            return panel;
        }
    }

    // --- (Phần Header, Filter, Stats giữ nguyên code cũ) ---
    private JPanel createHeaderPanel() {
        // ... (Code cũ)
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbTitle = new JLabel("QUẢN LÝ TỒN KHO");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbTitle.setForeground(Color.decode("#062D1E"));
        JLabel lbSubtitle = new JLabel("Theo dõi và quản lý tồn kho sách hiệu quả");
        lbSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbSubtitle.setForeground(Color.GRAY);
        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(Color.WHITE);
        titleContainer.add(lbTitle);
        titleContainer.add(lbSubtitle);
        panel.add(titleContainer, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFilterPanel() {
        // ... (Code cũ)
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 45));
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm sách...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSearchIcon());
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 15; margin: 0,10,0,10;");
        JComboBox<String> cboStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đủ hàng", "Sắp hết hàng", "Hết hàng"});
        cboStatus.setPreferredSize(new Dimension(200, 45));
        cboStatus.putClientProperty(FlatClientProperties.STYLE, "arc: 15; padding: 0,10,0,10;");
        JButton btnDate = new JButton("Chọn khoảng thời gian");
        btnDate.setPreferredSize(new Dimension(220, 45));
        btnDate.setBackground(Color.WHITE);
        btnDate.putClientProperty(FlatClientProperties.STYLE, "arc: 15; borderColor: #cccccc; margin: 0,10,0,10;");
        panel.add(txtSearch);
        panel.add(cboStatus);
        panel.add(btnDate);
        return panel;
    }

    private JPanel createStatsPanel() {
        // ... (Code cũ, lưu ý cần sửa số liệu thành dynamic sau này nếu muốn)
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(1000, 120));
        // Tạm thời để số cứng, muốn động thì phải gọi hàm count từ DAO
        panel.add(createCard("Tổng số lượng sách", "343", "#E3F2FD", "#2196F3", "book_icon.svg"));
        panel.add(createCard("Loại sách", "8", "#E8F5E9", "#4CAF50", "inventory_icon.svg"));
        panel.add(createCard("Sắp hết hàng", "5", "#FFEBEE", "#F44336", "stats_icon.svg"));
        return panel;
    }

    private JPanel createCard(String title, String value, String bgIconColor, String iconColor, String iconName) {
        // ... (Code cũ)
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#EEEEEE"), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setBackground(Color.WHITE);
        JLabel lbTitle = new JLabel(title);
        lbTitle.setForeground(Color.GRAY);
        lbTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lbValue = new JLabel(value);
        lbValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        if (title.equals("Sắp hết hàng")) lbValue.setForeground(Color.decode(iconColor));
        leftPanel.add(lbTitle);
        leftPanel.add(lbValue);
        JLabel lbIcon = new JLabel();
        lbIcon.setPreferredSize(new Dimension(50, 50));
        lbIcon.setOpaque(true);
        lbIcon.setBackground(Color.decode(bgIconColor));
        lbIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lbIcon.putClientProperty(FlatClientProperties.STYLE, "arc: 50;");
        try {
            FlatSVGIcon svgIcon = new FlatSVGIcon("icon/" + iconName);
            FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(c -> Color.decode(iconColor));
            svgIcon.setColorFilter(filter);
            lbIcon.setIcon(svgIcon.derive(24, 24));
        } catch (Exception e) {}
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(lbIcon, BorderLayout.EAST);
        return card;
    }
}