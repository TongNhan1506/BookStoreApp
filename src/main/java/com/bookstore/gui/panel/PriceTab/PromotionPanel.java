package com.bookstore.gui.panel.PriceTab;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import com.bookstore.bus.*;

public class PromotionPanel extends JPanel {
    private PromotionBUS bus = new PromotionBUS();
    private JTable table;
    private JPanel header;
    private WhiteBoxPanel centerBox;
    private DefaultTableModel model;
    private JTextField txtTenKM, txtPhanTram;
    private JComboBox<String> cbTrangThai, cbChonSach;

    public PromotionPanel() {

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(235, 235, 235));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        initHeader();
        //initTable();
        //initForm();
    }

    class WhiteBoxPanel extends JPanel {
        public WhiteBoxPanel() {
            setOpaque(false);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            // Vẽ hình chữ nhật bo góc 30 pixel
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
        }
    }

    private void initHeader() {
        // 1. Tiêu đề Khuyến mãi
        JLabel lblTitle = new JLabel("Khuyến mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(17, 71, 50));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. WhiteBoxPanel làm nền trắng bo góc cho nội dung
        WhiteBoxPanel whiteBox = new WhiteBoxPanel();
        whiteBox.setLayout(new BorderLayout(0, 15));

        // --- THANH ĐIỀU KHIỂN (NORTH của WhiteBox) ---
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);

        // Cụm lọc và tìm kiếm (Căn trái)
        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftGroup.setOpaque(false);

        // Radio Buttons
        JPanel filterBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBox.setOpaque(false);
        JRadioButton rbTen = new JRadioButton("Tên", true);
        JRadioButton rbPhanTram = new JRadioButton("Phần trăm");
        JRadioButton rbThoiGian = new JRadioButton("Thời gian");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbTen); bg.add(rbPhanTram); bg.add(rbThoiGian);
        filterBox.add(new JLabel("Tìm theo:"));
        filterBox.add(rbTen); filterBox.add(rbPhanTram); filterBox.add(rbThoiGian);

        // Thanh Tìm Kiếm (Tự vẽ để không bị nuốt)
        JPanel searchWrapper = createSearchWrapper();

        leftGroup.add(filterBox);
        leftGroup.add(searchWrapper);

        // Nút thêm (Căn phải)
        JButton btnAdd = new JButton("+ Thêm Khuyến Mãi") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAdd.setPreferredSize(new Dimension(180, 42));
        btnAdd.setBackground(new Color(35, 90, 180));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setContentAreaFilled(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);

        controlPanel.add(leftGroup, BorderLayout.WEST);
        controlPanel.add(btnAdd, BorderLayout.EAST);

        whiteBox.add(controlPanel, BorderLayout.NORTH);

        // 3. Bảng dữ liệu
        initTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        whiteBox.add(scroll, BorderLayout.CENTER);

        add(whiteBox, BorderLayout.CENTER);
    }

    private JPanel createSearchWrapper() {
        // Dùng -2 để ép 2 khối dính liền khít rịt
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, -2, 0));
        wrapper.setOpaque(false);

        // Ô Icon Kính lúp (Swing vẽ tay)
        JPanel iconBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 195, 180));
                g2.fillRoundRect(0, 0, getWidth() + 20, getHeight(), 25, 25);
                g2.dispose();
            }
        };
        iconBox.setPreferredSize(new Dimension(50, 42));
        iconBox.setOpaque(false);

        java.net.URL imgURL = getClass().getResource("/icon/Thêm văn bản-Photoroom.png");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            iconBox.add(new JLabel(icon));
        }

        // Ô Nhập liệu (Swing vẽ tay để chống lỗi nuốt textfield)
        JTextField txtSearch = new JTextField("Tìm kiếm chương trình...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(235, 235, 235));
                g2.fillRoundRect(-20, 0, getWidth() + 20, getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        txtSearch.setOpaque(false);
        txtSearch.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        txtSearch.setPreferredSize(new Dimension(300, 42));
        txtSearch.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        wrapper.add(iconBox);
        wrapper.add(txtSearch);
        return wrapper;
    }

    private void initTable() {
        String[] cols = {"ID", "Tên chương trình", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Thao tác"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 210, 200));

        // Header
        table.getTableHeader().setBackground(new Color(17, 71, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Render hàng
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected) {
                    c.setBackground(new Color(17, 71, 50));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 != 0 ? new Color(180, 200, 180) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Dữ liệu mẫu sếp xem
        model.addRow(new Object[]{"KM001", "Mừng Xuân 2026", "01/02/2026", "28/02/2026", "Đang chạy", ""});
        model.addRow(new Object[]{"KM002", "Xả kho Truyện Tranh", "10/02/2026", "15/02/2026", "Đang chạy", ""});
    }
    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(43, 45, 49));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 63, 65)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        form.putClientProperty(FlatClientProperties.STYLE, "arc: 25");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 15, 15);

        // --- Cột trái ---
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(createLabel("Chương trình khuyến mãi:"), gbc);
        txtTenKM = new JTextField();
        gbc.gridy = 1;
        form.add(txtTenKM, gbc);

        gbc.gridy = 2;
        form.add(createLabel("Ngày bắt đầu:"), gbc);
        JTextField txtNgayBD = new JTextField("31/12/2023");
        gbc.gridy = 3;
        form.add(txtNgayBD, gbc);

        // --- Cột giữa ---
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(createLabel("Trạng thái:"), gbc);
        cbTrangThai = new JComboBox<>(new String[]{"Đang hoạt động", "Ngừng hoạt động"});
        gbc.gridy = 1;
        form.add(cbTrangThai, gbc);

        // --- Nút Lưu/Hủy ---
        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBox.setOpaque(false);
        JButton btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(35, 90, 180));
        JButton btnCancel = new JButton("Hủy");
        btnBox.add(btnCancel); btnBox.add(btnSave);

        gbc.gridx = 2; gbc.gridy = 5;
        form.add(btnBox, gbc);

        add(form, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 200, 200));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    /*private void loadData() {
        model.setRowCount(0);
        List<Promotion> list = bus.getList();
        for (Promotion p : list) {
            model.addRow(new Object[]{p.id, p.name, p.startDate, p.endDate, p.status, "Sửa | Xóa"});
        }
    }*/
}

