package com.bookstore.gui.panel.PriceTab;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.bookstore.util.AppConstant;
//import org.w3c.dom.events.MouseEvent;

import com.bookstore.bus.*;

public class PromotionPanel extends JPanel {
    private PromotionBUS bus = new PromotionBUS();
    private JTable table;
    private JPanel header;
    private WhiteBoxPanel centerBox, whiteBox;;
    private DefaultTableModel model;
    private JTextField txtTenKM, txtPhanTram;
    private JComboBox<String> cbTrangThai, cbChonSach;
    private JTextField txtSearch;
    private JScrollPane scroll;

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
    // Tiêu đề
    JLabel lblTitle = new JLabel("Khuyến mãi");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblTitle.setForeground(new Color(17, 71, 50));
    lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    add(lblTitle, BorderLayout.NORTH);

    // Panel chứa nút và lọc
    JPanel controlPanel = new JPanel(new BorderLayout());
    controlPanel.setOpaque(false);
    controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

    // Cụm lọc bên trái (Copy phong cách PricePanel)
    JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    leftGroup.setOpaque(false);
    
    // 3 Label Radio mới thêm: Tên, Phần trăm, Thời gian
    JPanel filterBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    filterBox.setOpaque(false);
    JRadioButton rbTen = new JRadioButton("Tên", true);
    JRadioButton rbPhanTram = new JRadioButton("Phần trăm");
    JRadioButton rbThoiGian = new JRadioButton("Thời gian");
    ButtonGroup bg = new ButtonGroup();
    bg.add(rbTen); bg.add(rbPhanTram); bg.add(rbThoiGian);
    filterBox.add(new JLabel("Tìm theo:"));
    filterBox.add(rbTen); filterBox.add(rbPhanTram); filterBox.add(rbThoiGian);

    leftGroup.add(filterBox);
    leftGroup.add(createSearchWrapper());

    controlPanel.add(leftGroup, BorderLayout.WEST);
    
    // Nút thêm dạt sang phải
    JButton btnAdd = new JButton("+ Thêm Khuyến Mãi");
    btnAdd.setPreferredSize(new Dimension(180, 42));
    btnAdd.setBackground(new Color(35, 90, 180));
    btnAdd.setForeground(Color.WHITE);
    btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
    controlPanel.add(btnAdd, BorderLayout.EAST);

    // Lắp ráp vào Box trắng
    whiteBox = new WhiteBoxPanel();
    whiteBox.setLayout(new BorderLayout(0, 15));
    whiteBox.add(controlPanel, BorderLayout.NORTH);
    
    initTable();

    add(whiteBox, BorderLayout.CENTER);

}

    private JPanel createSearchWrapper() {
    // 1. Dùng FlowLayout.LEFT để nhích cả cụm sang trái
    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, -1, 0));
    wrapper.setOpaque(false);
    wrapper.setPreferredSize(new Dimension(450, 45));

    // 2. Ô Icon Kính lúp (Giữ nguyên phong cách của Vy)
    JPanel iconBox = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(180, 195, 180));
            // Vẽ bo góc trái, lấn sang phải 20px để nối liền với ô nhập
            g2.fillRoundRect(0, 0, getWidth() + 20, getHeight(), 25, 25);
            g2.dispose();
        }
    };
    iconBox.setPreferredSize(new Dimension(50, 42));
    iconBox.setOpaque(false);

    java.net.URL imgURL = getClass().getResource("/icon/Thêm văn bản-Photoroom.png");
    if (imgURL != null) {
        iconBox.add(new JLabel(new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH))));
    }

    // 3. Ô Nhập liệu (CHỈ VẼ NỀN, KHÔNG VẼ ĐÈ LÊN SUPER)
    JPanel inputBackground = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 235, 235));
            // Vẽ lấn sang trái 20px để khớp với iconBox
            g2.fillRoundRect(-20, 0, getWidth() + 20, getHeight(), 25, 25);
            g2.dispose();
        }
    };
    inputBackground.setOpaque(false);
    inputBackground.setPreferredSize(new Dimension(300, 42));

    // Nhét JTextField thuần vào trong cái nền vừa vẽ
    txtSearch = new JTextField("Tìm kiếm chương trình...");
    txtSearch.setOpaque(false);
    txtSearch.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
    txtSearch.setFont(new Font("Segoe UI", Font.ITALIC, 13));
    txtSearch.setBackground(new Color(0,0,0,0)); // Trong suốt tuyệt đối
    
    inputBackground.add(txtSearch, BorderLayout.CENTER);

    wrapper.add(iconBox);
    wrapper.add(inputBackground);
    return wrapper;
}

    private void initTable() {
    String[] cols = {"ID", "Tên chương trình", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Thao tác"};
    model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    table = new JTable(model);
    
    scroll = new JScrollPane(table);
    scroll.getViewport().setBackground(Color.WHITE);
    scroll.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200,200,200), 1),
            " Bảng Khuyến Mãi ", 
            TitledBorder.CENTER, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), 
            new Color(17, 71, 50)
        ),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

   table.addMouseListener(new java.awt.event.MouseAdapter() {
        private int lastSelectedRow = -1;

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            if (row != -1 && row == lastSelectedRow) {
                table.clearSelection();
                lastSelectedRow = -1;
            } else {
                lastSelectedRow = row;
            }
        }
    });
    
    table.setRowHeight(50);
    table.setShowGrid(true); 
    table.setGridColor(new Color(5,30,20));
    table.setIntercellSpacing(new Dimension(1, 1));

    // Header màu xanh đậm đồng bộ app
    table.getTableHeader().setBackground(new Color(17, 71, 50));
    table.getTableHeader().setForeground(Color.WHITE);
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    // cac cot khac
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setHorizontalAlignment(SwingConstants.CENTER);
        
            if (isSelected) {
                c.setBackground(new Color(200, 200, 200)); 
                c.setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) { 
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(new Color(17, 71, 50)); 
                c.setForeground(Color.WHITE);
            }
            }
            return c;
        }
    });

    //cot cuoi
    table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        p.setOpaque(true); // Đảm bảo hiện màu nền
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // Viền đen dưới

        if (isSelected) {
            p.setBackground(new Color(200, 200, 200)); // Màu click chọn
        } else {
            if (row % 2 == 0) { 
                p.setBackground(Color.WHITE);
            } else {
                p.setBackground(new Color(17, 71, 50));
            }
        }

        JButton btnEdit = new JButton("Sửa");
        JButton btnChange = new JButton("Đổi");

        btnEdit.setBackground(new Color(240, 173, 78));
        btnChange.setBackground(new Color(35, 90, 180));

        for (JButton b : new JButton[]{btnEdit, btnChange}) {
            b.setForeground(Color.WHITE);
            b.setPreferredSize(new Dimension(65, 30));
            b.setFont(new Font("Segoe UI", Font.BOLD, 11));
            p.add(b);
        }
        return p;
    }
});


    model.addRow(new Object[]{"KM1", "Mừng Xuân 2026 - Giảm giá Văn học", "01/02/2026", "28/02/2026", "Đang chạy", ""});
    model.addRow(new Object[]{"KM2", "Xả kho Truyện Tranh - Flash Sale", "10/02/2026", "15/02/2026", "Đang chạy", ""});

    if (whiteBox != null && scroll != null) {
        whiteBox.add(scroll, BorderLayout.CENTER);
        whiteBox.revalidate();
        whiteBox.repaint();
    }

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

