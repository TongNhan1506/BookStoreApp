package com.bookstore.gui.panel.StatisticTab;

import com.bookstore.bus.FinancialStatsBUS;
import com.bookstore.dto.FinancialStatsDTO;
import com.bookstore.util.AppConstant;
import com.bookstore.util.Refreshable;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FinancialStatsPanel extends JPanel implements Refreshable {
    private static final int SUMMARY_CARD_HEIGHT = 130;
    private static final int QUARTER_CARD_HEIGHT = SUMMARY_CARD_HEIGHT / 2;
    private final JComboBox<String> cboThongKeTheo = new JComboBox<>(new String[]{"Ngày", "Tháng", "Quý", "Năm"});
    private final JDateChooser dchTuNgay = new JDateChooser(new Date());
    private final JDateChooser dchDenNgay = new JDateChooser(new Date());
    private final JButton btnThongKe = new JButton("Thống kê");

    private final JLabel lblDoanhThuValue = new JLabel();
    private final JLabel lblChiPhiValue = new JLabel();
    private final JLabel lblLoiNhuanValue = new JLabel();
    private final JLabel lblVonNhapHangValue = new JLabel();
    private final JLabel lblQuyCaoNhat = new JLabel("Quý doanh thu cao nhất: --");
    private final JLabel lblQuyThapNhat = new JLabel("Quý doanh thu thấp nhất: --");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Thời gian", "Doanh thu", "Lợi nhuận", "Chi phí", "Vốn nhập hàng"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblChiTiet = new JTable(tableModel);
    private final FinancialStatsBUS thongKeBus = new FinancialStatsBUS();
    private List<FinancialStatsDTO> currentData = new ArrayList<>();

    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);

    public FinancialStatsPanel() {
        initUI();
        bindEvents();
        loadThongKe();
    }

    @Override
    public void refresh() {
        loadThongKe();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFilterPanel(), BorderLayout.NORTH);
        add(createDashboardPanel(), BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        pnlFilter.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));
        dchTuNgay.setDateFormatString("dd/MM/yyyy");
        dchDenNgay.setDateFormatString("dd/MM/yyyy");

        Font filterFont = new Font(AppConstant.FONT_NAME, Font.PLAIN, 14);
        cboThongKeTheo.setFont(filterFont);
        cboThongKeTheo.setPreferredSize(new Dimension(140, 36));
        dchTuNgay.setFont(filterFont);
        dchTuNgay.setPreferredSize(new Dimension(150, 36));
        dchDenNgay.setFont(filterFont);
        dchDenNgay.setPreferredSize(new Dimension(150, 36));
        btnThongKe.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        btnThongKe.setPreferredSize(new Dimension(120, 38));

        pnlFilter.add(new JLabel("Thống kê theo:"));
        pnlFilter.add(cboThongKeTheo);
        pnlFilter.add(new JLabel("Từ ngày:"));
        pnlFilter.add(dchTuNgay);
        pnlFilter.add(new JLabel("Đến ngày:"));
        pnlFilter.add(dchDenNgay);
        pnlFilter.add(btnThongKe);

        return pnlFilter;
    }

    private JPanel createDashboardPanel() {
        JPanel pnlDashboard = new JPanel(new BorderLayout(10, 10));

        JPanel pnlTop = new JPanel(new BorderLayout(0, 10));

        JPanel pnlCards = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlCards.add(createSummaryCard("Doanh thu", lblDoanhThuValue, new Color(37, 99, 235)));
        pnlCards.add(createSummaryCard("Chi phí", lblChiPhiValue, new Color(220, 38, 38)));
        pnlCards.add(createSummaryCard("Lợi nhuận", lblLoiNhuanValue, new Color(22, 163, 74)));
        pnlCards.add(createSummaryCard("Vốn nhập hàng", lblVonNhapHangValue, new Color(234, 88, 12)));
        JPanel pnlQuarterInfo = new JPanel(new GridLayout(2, 1, 12, 10));
        pnlQuarterInfo.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnlQuarterInfo.setPreferredSize(new Dimension(0, QUARTER_CARD_HEIGHT + 18));

        Font quarterFont = new Font(AppConstant.FONT_NAME, Font.BOLD, 16);

        lblQuyCaoNhat.setOpaque(true);
        lblQuyCaoNhat.setBackground(new Color(236, 253, 245));
        lblQuyCaoNhat.setForeground(new Color(21, 128, 61));
        lblQuyCaoNhat.setFont(quarterFont);
        lblQuyCaoNhat.setPreferredSize(new Dimension(0, QUARTER_CARD_HEIGHT));
        lblQuyCaoNhat.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        lblQuyThapNhat.setOpaque(true);
        lblQuyThapNhat.setBackground(new Color(254, 242, 242));
        lblQuyThapNhat.setForeground(new Color(185, 28, 28));
        lblQuyThapNhat.setFont(quarterFont);
        lblQuyThapNhat.setPreferredSize(new Dimension(0, QUARTER_CARD_HEIGHT));
        lblQuyThapNhat.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        pnlQuarterInfo.add(lblQuyCaoNhat);
        pnlQuarterInfo.add(lblQuyThapNhat);

        pnlTop.add(pnlCards, BorderLayout.NORTH);
        pnlTop.add(pnlQuarterInfo, BorderLayout.CENTER);

        JPanel tablePanel = createTablePanel();

        pnlDashboard.add(pnlTop, BorderLayout.NORTH);
        pnlDashboard.add(tablePanel, BorderLayout.CENTER);

        return pnlDashboard;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(0, SUMMARY_CARD_HEIGHT));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));

        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 26f));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        tblChiTiet.setRowHeight(42);
        tblChiTiet.setShowGrid(false);
        tblChiTiet.setIntercellSpacing(new Dimension(0, 0));
        tblChiTiet.setSelectionBackground(Color.decode("#d4ffee"));
        tblChiTiet.setSelectionForeground(Color.BLACK);

        JTableHeader header = tblChiTiet.getTableHeader();
        header.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        header.setForeground(Color.WHITE);
        header.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 13));
        header.setReorderingAllowed(false);

        tblChiTiet.getColumnModel().getColumn(1).setCellRenderer(new CurrencyCellRenderer());
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(new ProfitCellRenderer());
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(new CurrencyCellRenderer());
        tblChiTiet.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void bindEvents() {
        btnThongKe.addActionListener(e -> loadThongKe());
    }

    private void loadThongKe() {
        Date tuNgay = dchTuNgay.getDate();
        Date denNgay = dchDenNgay.getDate();
        String kieuThongKe = (String) cboThongKeTheo.getSelectedItem();

         try {
            if ("Ngày".equals(kieuThongKe)) {
                thongKeBus.validateThongKeTheoNgay(tuNgay, denNgay);
                currentData = thongKeBus.getThongKeTheoNgay(tuNgay, denNgay);
            } else {
                thongKeBus.validateThongKeTheoNam(tuNgay);
                int year = thongKeBus.extractYear(tuNgay);
                currentData = thongKeBus.getThongKeTheoThang(year);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date ngayChon = tuNgay == null ? new Date() : tuNgay;
        int year = thongKeBus.extractYear(ngayChon);

        updateSummaryCards();
        updateQuarterSummary(year);
        updateTable();
    }

    private void updateSummaryCards() {
        double tongDoanhThu = currentData.stream().mapToDouble(FinancialStatsDTO::getDoanhThu).sum();
        double tongChiPhi = currentData.stream().mapToDouble(FinancialStatsDTO::getChiPhi).sum();
        double tongLoiNhuan = currentData.stream().mapToDouble(FinancialStatsDTO::getLoiNhuan).sum();
        double tongVonNhapHang = currentData.stream().mapToDouble(FinancialStatsDTO::getVonNhapHang).sum();

        lblDoanhThuValue.setText(formatCurrency(tongDoanhThu));
        lblChiPhiValue.setText(formatCurrency(tongChiPhi));
        lblLoiNhuanValue.setText(formatCurrency(tongLoiNhuan));
        lblVonNhapHangValue.setText(formatCurrency(tongVonNhapHang));
        lblLoiNhuanValue.setForeground(tongLoiNhuan < 0 ? new Color(198, 40, 40) : new Color(22, 163, 74));
    }

    private void updateQuarterSummary(int year) {
        FinancialStatsBUS.QuyDoanhThuSummary summary = thongKeBus.getQuyDoanhThuSummary(year);
        if (summary == null) {
            lblQuyCaoNhat.setText("Quý doanh thu cao nhất (" + year + "): Không có dữ liệu");
            lblQuyThapNhat.setText("Quý doanh thu thấp nhất (" + year + "): Không có dữ liệu");
            return;
        }

        lblQuyCaoNhat.setText("Quý doanh thu cao nhất (" + summary.getNam() + "): Q" + summary.getQuyCaoNhat()
                + " - " + formatCurrency(summary.getDoanhThuQuyCaoNhat()));
        lblQuyThapNhat.setText("Quý doanh thu thấp nhất (" + summary.getNam() + "): Q" + summary.getQuyThapNhat()
                + " - " + formatCurrency(summary.getDoanhThuQuyThapNhat()));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (FinancialStatsDTO item : currentData) {
            tableModel.addRow(new Object[]{
                    item.getThoiGian(),
                    item.getDoanhThu(),
                    item.getLoiNhuan(),
                    item.getChiPhi(),
                    item.getVonNhapHang()
            });
        }
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount) + " VNĐ";
    }

    private class CurrencyCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value instanceof Number number) {
                setText(formatCurrency(number.doubleValue()));
            } else {
                super.setValue(value);
            }
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }

    private class ProfitCellRenderer extends CurrencyCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected && value instanceof Number number) {
                c.setForeground(number.doubleValue() < 0 ? new Color(198, 40, 40) : new Color(22, 163, 74));
            }
            return c;
        }
    }
}