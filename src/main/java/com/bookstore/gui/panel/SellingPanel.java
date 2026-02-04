package com.bookstore.gui.panel;

import com.bookstore.util.SearchableComboBox;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SellingPanel extends JPanel {
    private JComboBox<String> cboCategory;
    private SearchableComboBox cboAuthor;
    private JTextField txtPriceFrom, txtPriceTo, txtSearch;
    private JTable tblProduct;
    private DefaultTableModel productModel;
    private JLabel lbProductImage;
    private JButton btnAddToCart;

    private JTable tblCart;
    private DefaultTableModel cartModel;
    private JTextField txtEmployee, txtCustomer, txtRank, txtPromo;
    private JLabel lbSubTotal, lbDiscountPromo, lbDiscountMember, lbFinalTotal;
    private JButton btnRefresh, btnDelete, btnEdit, btnPay;

    public SellingPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBackground(Color.WHITE);

        JPanel leftPanel = createLeftPanel();
        add(leftPanel);

        JPanel rightPanel = createRightPanel();
        add(rightPanel);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel pFilter = new JPanel(new GridLayout(3, 1, 10, 10));
        pFilter.setOpaque(false);

        JPanel pRow1 = new JPanel(new GridLayout(1,2,10,10));
        pRow1.setOpaque(false);
        cboCategory = new JComboBox<>(new String[]{"Tất cả thể loại", "Truyện tranh", "Tiểu thuyết"});

        String[] authorData = {
                "Tất cả tác giả",
                "Nguyễn Nhật Ánh",
                "Nguyễn Du",
                "Nguyễn Ngọc Tư",
                "Fujiko F. Fujio",
                "Nam Cao",
                "Vũ Trọng Phụng",
                "Tô Hoài",
                "Lê Lợi (Tác giả giả định)"
        };
        cboAuthor = new SearchableComboBox(authorData);
        pRow1.add(cboCategory);
        pRow1.add(cboAuthor);
        pFilter.add(pRow1);

        JPanel pRow2 = new JPanel(new GridLayout(1,4,10,10));
        pRow2.setOpaque(false);
        pRow2.add(new JLabel("Giá từ:", SwingConstants.RIGHT));
        txtPriceFrom = new JTextField();
        pRow2.add(txtPriceFrom);
        pRow2.add(new JLabel("Đến:", SwingConstants.RIGHT));
        txtPriceTo = new JTextField();
        pRow2.add(txtPriceTo);
        pFilter.add(pRow2);

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên hoặc mã sách...");
        FlatSVGIcon searchIcon = new FlatSVGIcon("icon/search_icon.svg").derive(20,20);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, searchIcon);
        pFilter.add(txtSearch);

        String[] headers = {"Tên sản phẩm", "Thể loại", "Đơn giá", "SL"};
        productModel = new DefaultTableModel(headers, 0);
        tblProduct = new JTable(productModel);
        tblProduct.setRowHeight(30);
        styleTable(tblProduct);
        setColumnWidth(tblProduct, 0, 200);
        setColumnWidth(tblProduct, 2, 100);
        setColumnWidth(tblProduct, 3, 50);

        productModel.addRow(new Object[]{"Doraemon Tập 1", "Truyện tranh", "25.000", 142});
        productModel.addRow(new Object[]{"Mắt Biếc", "Tiểu thuyết", "110.000", 50});
        productModel.addRow(new Object[]{"Conan Tập 100", "Truyện tranh", "25.000", 20});

        JScrollPane scrollTable = new JScrollPane(tblProduct);
        scrollTable.getViewport().setBackground(Color.WHITE);

        JPanel pDetail = new JPanel(new BorderLayout(10, 0));
        pDetail.setBackground(Color.WHITE);
        pDetail.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        lbProductImage = new JLabel("Ảnh", SwingConstants.CENTER);
        lbProductImage.setPreferredSize(new Dimension(100, 120));
        lbProductImage.setOpaque(true);
        lbProductImage.setBackground(Color.decode("#4DD0E1"));
        lbProductImage.setForeground(Color.WHITE);
        lbProductImage.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel pInfo = new JPanel(new GridLayout(2, 2, 10, 5));
        pInfo.setBackground(Color.WHITE);
        pInfo.add(createDetailLabel("Tên sản phẩm:", "Doraemon"));
        pInfo.add(createDetailLabel("Thể loại:", "Truyện tranh"));
        pInfo.add(createDetailLabel("Đơn giá:", "25.000đ"));
        pInfo.add(createDetailLabel("Số lượng tồn:", "142"));

        btnAddToCart = new JButton("Thêm vào hóa đơn");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setBackground(Color.decode("#114732"));
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.putClientProperty(FlatClientProperties.STYLE, "arc: 10; hoverBackground: #00A364;");

        JPanel pBottomRight = new JPanel(new BorderLayout(0, 10));
        pBottomRight.setOpaque(false);
        pBottomRight.add(pInfo, BorderLayout.CENTER);
        pBottomRight.add(btnAddToCart, BorderLayout.SOUTH);

        pDetail.add(lbProductImage, BorderLayout.WEST);
        pDetail.add(pBottomRight, BorderLayout.CENTER);

        panel.add(pFilter, BorderLayout.NORTH);
        panel.add(scrollTable, BorderLayout.CENTER);
        panel.add(pDetail, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel pInfo = new JPanel(new GridLayout(2, 2, 10, 10));
        pInfo.setOpaque(false);

        txtEmployee = new JTextField("Admin");
        txtEmployee.setEditable(false);
        txtEmployee.setBorder(BorderFactory.createTitledBorder("Nhân viên"));

        txtCustomer = new JTextField("Khách lẻ");
        txtCustomer.setEditable(false);
        txtCustomer.setBorder(BorderFactory.createTitledBorder("Khách hàng"));

        txtPromo = new JTextField("Không có");
        txtPromo.setEditable(false);
        txtPromo.setBorder(BorderFactory.createTitledBorder("Chương trình KM"));

        txtRank = new JTextField("Thành viên");
        txtRank.setEditable(false);
        txtRank.setBorder(BorderFactory.createTitledBorder("Hạng thành viên"));

        pInfo.add(txtEmployee);
        pInfo.add(txtCustomer);
        pInfo.add(txtPromo);
        pInfo.add(txtRank);

        JPanel pActions = new JPanel(new GridLayout(1, 3, 10, 0));
        pActions.setOpaque(false);

        btnRefresh = createActionButton("Làm mới", "#114732");
        btnRefresh.putClientProperty(FlatClientProperties.STYLE, "hoverBackground: #00A364;");
        btnDelete = createActionButton("Xóa", "#E53935");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "hoverBackground: #ff5a5a;");
        btnEdit = createActionButton("Sửa", "#FBC02D");
        btnEdit.putClientProperty(FlatClientProperties.STYLE, "hoverBackground: #ffdd53;");

        pActions.add(btnRefresh);
        pActions.add(btnDelete);
        pActions.add(btnEdit);

        JPanel pTopRight = new JPanel(new BorderLayout(0, 10));
        pTopRight.setOpaque(false);
        pTopRight.add(pInfo, BorderLayout.CENTER);
        pTopRight.add(pActions, BorderLayout.SOUTH);

        String[] cartHeaders = {"Tên sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        cartModel = new DefaultTableModel(cartHeaders, 0);
        tblCart = new JTable(cartModel);
        tblCart.setRowHeight(30);
        styleTable(tblCart);
        setColumnWidth(tblCart, 1, 40);
        setColumnWidth(tblCart, 2,100);
        setColumnWidth(tblCart, 3, 100);

        cartModel.addRow(new Object[]{"Doraemon", 2, "25.000", "50.000"});
        cartModel.addRow(new Object[]{"Mắt Biếc", 1, "110.000", "110.000"});

        JScrollPane scrollCart = new JScrollPane(tblCart);
        scrollCart.getViewport().setBackground(Color.WHITE);

        JPanel pSummary = new JPanel(new GridLayout(4, 1, 0, 5));
        pSummary.setBackground(Color.WHITE);

        pSummary.add(createSummaryRow("Tổng hóa đơn:", "160.000đ", false));
        pSummary.add(createSummaryRow("Giảm giá CTKM:", "0đ", false));
        pSummary.add(createSummaryRow("Giảm giá thành viên:", "0đ", false));
        pSummary.add(createSummaryRow("CÒN LẠI:", "160.000đ", true));
        btnPay = new JButton("Hoàn thành");
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPay.setForeground(Color.WHITE);
        btnPay.setBackground(Color.decode("#114732"));
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPay.putClientProperty(FlatClientProperties.STYLE, "arc: 10; hoverBackground: #00A364;");
        btnPay.setPreferredSize(new Dimension(0, 50));

        JPanel pBottomRight = new JPanel(new BorderLayout(0, 10));
        pBottomRight.setOpaque(false);
        pBottomRight.add(pSummary, BorderLayout.CENTER);
        pBottomRight.add(btnPay, BorderLayout.SOUTH);

        panel.add(pTopRight, BorderLayout.NORTH);
        panel.add(scrollCart, BorderLayout.CENTER);
        panel.add(pBottomRight, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetailLabel(String title, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbTitle = new JLabel(title);
        lbTitle.setForeground(Color.GRAY);
        JLabel lbValue = new JLabel(value);
        lbValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lbTitle, BorderLayout.NORTH);
        p.add(lbValue, BorderLayout.CENTER);
        return p;
    }

    private JButton createActionButton(String text, String colorHex) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.decode(colorHex));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        return btn;
    }

    private JPanel createSummaryRow(String title, String value, boolean isRed) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lbValue = new JLabel(value);
        lbValue.setFont(new Font("Segoe UI", Font.BOLD, 16));

        if (isRed) {
            lbTitle.setForeground(Color.RED);
            lbValue.setForeground(Color.RED);
        }

        p.add(lbTitle, BorderLayout.WEST);
        p.add(lbValue, BorderLayout.EAST);
        return p;
    }

    private void styleTable(JTable table) {
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(Color.decode("#114732"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setSelectionBackground(Color.decode("#d4ffee"));
        table.setSelectionForeground(Color.BLACK);
    }

    private void setColumnWidth(JTable table, int columnIndex, int width) {
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(width);
        table.getColumnModel().getColumn(columnIndex).setMinWidth(width);
         table.getColumnModel().getColumn(columnIndex).setMaxWidth(width);
    }
}