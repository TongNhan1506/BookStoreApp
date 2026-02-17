package com.bookstore.gui.panel.ImportTab;

import com.bookstore.bus.ImportBUS;
import com.bookstore.dto.ImportTicketDTO;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImportPanel extends JPanel {
    private ImportBUS importBUS = new ImportBUS();
    private JTable table;
    private DefaultTableModel tableModel;

    public ImportPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- TOP AREA ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Header (Title + Button)
        topPanel.add(createHeader(), BorderLayout.NORTH);

        // Stats Cards
        JPanel statsContainer = new JPanel();
        statsContainer.setLayout(new BoxLayout(statsContainer, BoxLayout.Y_AXIS));
        statsContainer.setBackground(Color.WHITE);
        statsContainer.add(Box.createVerticalStrut(20));
        statsContainer.add(createStatsPanel());
        statsContainer.add(Box.createVerticalStrut(20));

        topPanel.add(statsContainer, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER AREA (TABLE) ---
        add(createTablePanel(), BorderLayout.CENTER);
    }

    // 1. HEADER: Title & Button "Tạo Phiếu Nhập Mới"
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Left: Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        JLabel lbTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbTitle.setForeground(Color.decode("#062D1E"));
        JLabel lbSubtitle = new JLabel("Quản lý các phiếu nhập hàng từ nhà cung cấp");
        lbSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbSubtitle.setForeground(Color.GRAY);
        titlePanel.add(lbTitle);
        titlePanel.add(lbSubtitle);

        // Right: Button Green
        JButton btnAdd = new JButton("Tạo Phiếu Nhập Mới");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(Color.decode("#00C853")); // Màu xanh lá
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; margin: 10,20,10,20; borderWidth: 0; focusWidth: 0;");
        // Thêm icon dấu cộng (Text fallback nếu ko có icon)
        try {
            // btnAdd.setIcon(new FlatSVGIcon("icon/add.svg"));
            btnAdd.setText("+  Tạo Phiếu Nhập Mới");
        } catch(Exception e){}

        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(btnAdd, BorderLayout.EAST);
        return panel;
    }

    // 2. STATS PANELS: 4 thẻ thống kê
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(1000, 100));

        // Màu sắc viền và chữ số
        panel.add(createStatCard("Tổng số phiếu nhập", "4", "#E0E0E0", "#333333")); // Xám
        panel.add(createStatCard("Đã hoàn thành", "2", "#A5D6A7", "#2E7D32"));     // Xanh lá
        panel.add(createStatCard("Đang chờ", "1", "#FFCC80", "#EF6C00"));           // Cam
        panel.add(createStatCard("Đã hủy", "1", "#EF9A9A", "#C62828"));             // Đỏ

        return panel;
    }

    private JPanel createStatCard(String title, String value, String borderColor, String numberColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        // Border màu tùy chỉnh
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode(borderColor), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbTitle.setForeground(Color.GRAY);

        JLabel lbValue = new JLabel(value);
        lbValue.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lbValue.setForeground(Color.decode(numberColor));

        card.add(lbTitle, BorderLayout.NORTH);
        card.add(lbValue, BorderLayout.CENTER);
        return card;
    }

    // 3. TABLE AREA
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.decode("#EEEEEE")));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");

        JLabel lbTitle = new JLabel("Danh sách phiếu nhập");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbTitle.setBorder(new EmptyBorder(15, 20, 10, 20));
        panel.add(lbTitle, BorderLayout.NORTH);

        String[] columns = {"MÃ PHIẾU", "NHÀ CUNG CẤP", "NGÀY NHẬP", "NGƯỜI TẠO", "TỔNG TIỀN", "TRẠNG THÁI", "THAO TÁC"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(60); // Chiều cao hàng lớn hơn để chứa badge
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.GRAY);
        table.getTableHeader().setBackground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(Color.decode("#F5F5F5"));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Căn chỉnh cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // 1. Mã phiếu (Đậm)
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setBorder(new EmptyBorder(0, 20, 0, 0));
                return this;
            }
        });

        // 4. Tổng tiền (Đậm + Format)
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                return this;
            }
        });

        // 5. Trạng thái (Badge)
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        // 6. Thao tác (Link Chi tiết)
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());

        // Sự kiện click vào cột Thao tác
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 6) {
                    JOptionPane.showMessageDialog(null, "Xem chi tiết phiếu: " + table.getValueAt(row, 0));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ImportTicketDTO> list = importBUS.getAllImports();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        for (ImportTicketDTO dto : list) {
            String id = "PN" + String.format("%03d", dto.getImportID()); // Format PN001
            String price = nf.format(dto.getTotalPrice()) + " đ";

            tableModel.addRow(new Object[]{
                    id,
                    dto.getSupplierName(),
                    sdf.format(dto.getCreatedDate()),
                    dto.getEmployeeName(),
                    price,
                    dto.getStatus(), // Int (0, 1, 2)
                    "Chi tiết"
            });
        }
    }

    // --- RENDERERS ---

    // Badge Renderer cho Trạng thái
    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);

            int status = Integer.parseInt(value.toString());
            String text = "";
            Color bg = Color.WHITE;
            Color fg = Color.BLACK;

            // Logic màu sắc giống hình mẫu
            if (status == 2) { // Hoàn thành
                text = "Hoàn thành";
                bg = Color.decode("#E8F5E9"); // Xanh nhạt
                fg = Color.decode("#2E7D32"); // Xanh đậm
            } else if (status == 1) { // Đang chờ
                text = "Đang chờ";
                bg = Color.decode("#FFF3E0"); // Cam nhạt
                fg = Color.decode("#EF6C00"); // Cam đậm
            } else { // Đã hủy
                text = "Đã hủy";
                bg = Color.decode("#FFEBEE"); // Đỏ nhạt
                fg = Color.decode("#C62828"); // Đỏ đậm
            }

            label.setText(text);
            label.setBackground(bg);
            label.setForeground(fg);

            // Tạo bo tròn cho badge
            // Cách đơn giản nhất trong Swing thuần là dùng Border
            // Nhưng để đẹp như FlatLaf, ta bọc nó trong 1 panel
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            if (isSelected) panel.setBackground(table.getSelectionBackground());

            // Fake bo tròn bằng border rỗng + background color
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            // Lưu ý: JLabel mặc định không bo tròn background được trừ khi override paintComponent
            // Nhưng ta có thể dùng mẹo nhỏ: không bo tròn cũng vẫn đẹp, hoặc dùng FlatLaf styling nếu là JButton

            panel.add(label);
            return panel;
        }
    }

    // Link Renderer cho Thao tác
    class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setText("◉ " + value.toString()); // Giả lập icon con mắt
            label.setForeground(Color.decode("#2196F3")); // Màu xanh dương link
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
            panel.setBackground(Color.WHITE);
            if(isSelected) panel.setBackground(table.getSelectionBackground());
            panel.add(label);
            return panel;
        }
    }
}