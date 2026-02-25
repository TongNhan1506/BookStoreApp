package com.bookstore.gui.panel.BillTab;
import com.bookstore.util.AppConstant;
import com.bookstore.util.MoneyFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.bookstore.bus.BillBUS;
import com.bookstore.dto.BillDTO;
import com.bookstore.util.Refreshable;
import java.util.List;


public class BillPanel extends JPanel implements Refreshable {

    private JLabel lbTotalQuantity, lbRevenue;
    private JTable table;
    private DefaultTableModel model;

    private final Color MAIN_GREEN = Color.decode(AppConstant.GREEN_COLOR_CODE);

    public BillPanel(){
        setLayout(new BorderLayout(20,20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20,20,20,20));

        add(createStatisticPanel(),BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);

        loadBillTable();
    }

    @Override
    public void refresh(){
        loadBillTable();
    }


    private JPanel createStatisticPanel(){
        JPanel panel = new JPanel(new GridLayout(1,2,20,0));
        panel.setBackground(Color.WHITE);

        JPanel quantityBox = new JPanel(new BorderLayout());
        quantityBox.setBackground(MAIN_GREEN);
        quantityBox.setBorder(new EmptyBorder(15,20,15,20));

        JLabel lbTitle1 = new JLabel("SỐ LƯỢNG");
        lbTitle1.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD,22));
        lbTitle1.setForeground(Color.WHITE);

        lbTotalQuantity = new JLabel("5");
        lbTotalQuantity.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 40));
        lbTotalQuantity.setForeground(Color.WHITE);

        quantityBox.add(lbTitle1,BorderLayout.NORTH);
        quantityBox.add(lbTotalQuantity, BorderLayout.CENTER);


        JPanel revenueBox = new JPanel(new BorderLayout());
        revenueBox.setBackground(MAIN_GREEN);
        revenueBox.setBorder(new EmptyBorder(15,20,15,20));

        JLabel lbTitle2 = new JLabel("DOANH THU");
        lbTitle2.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 22));
        lbTitle2.setForeground(Color.WHITE);

        lbRevenue = new JLabel(MoneyFormatter.toVND(0));
        lbRevenue.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 40));
        lbRevenue.setForeground(Color.WHITE);

        revenueBox.add(lbTitle2, BorderLayout.NORTH);
        revenueBox.add(lbRevenue, BorderLayout.CENTER);

        panel.add(quantityBox);
        panel.add(revenueBox);

        return panel;
    }


    private JPanel createCenterPanel(){
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,15,5));
        filterPanel.setBackground(Color.WHITE);

        JLabel lbFrom = new JLabel("Từ:");
        lbFrom.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 16));
        SpinnerDateModel fromModel = new SpinnerDateModel(new Date(), null, null,java.util.Calendar.DAY_OF_MONTH);
        JSpinner spFrom = new JSpinner(fromModel);
        JSpinner.DateEditor fromEditor = new JSpinner.DateEditor(spFrom, "d/M/yyyy");
        spFrom.setEditor(fromEditor);
        spFrom.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 16));


        JLabel lbTo = new JLabel("Đến:");
        lbTo.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 16));
        SpinnerDateModel toModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spTo = new JSpinner(toModel);
        JSpinner.DateEditor toEditor = new JSpinner.DateEditor(spTo,"d/M/yyyy");
        spTo.setEditor(toEditor);
        spTo.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 16));


        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 16));
        txtSearch.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm theo mã hóa đơn, tên khách hàng, tên nhân viên"));


        JButton btnFilter = new JButton("Lọc");
        btnFilter.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD,16));
        btnFilter.setBackground(MAIN_GREEN);
        btnFilter.setForeground(MAIN_GREEN);



        filterPanel.add(lbFrom);
        filterPanel.add(spFrom);
        filterPanel.add(lbTo);
        filterPanel.add(spTo);
        filterPanel.add(txtSearch);
        filterPanel.add(btnFilter);


        panel.add(filterPanel, BorderLayout.NORTH);




        String[]columns = {"Mã Hóa Đơn", "Ngày Giờ Lập", "Nhân Viên Lập", "Khách Hàng", "Tổng Tiền"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        table.setRowHeight(35);
        table.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 16));
        table.getTableHeader().setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 16));
        table.getTableHeader().setBackground(MAIN_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);

        table.getTableHeader().setDefaultRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {

                        super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

                        setBackground(MAIN_GREEN);
                        setForeground(Color.WHITE);
                        setHorizontalAlignment(CENTER);
                        setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 16));

                        return this;
                    }
                }
        );

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            protected void setValue(Object value){
                if(value instanceof Number){
                    setText(MoneyFormatter.toVND(((Number)value).doubleValue()));
                }else{
                    super.setValue(value);
                }
            }
        });

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );


        panel.add(scrollPane, BorderLayout.CENTER);

        btnFilter.addActionListener(e->{
            Date fromDate = (Date) spFrom.getValue();
            Date toDate = (Date) spTo.getValue();
            String keyword = txtSearch.getText().trim();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - d/M/yyyy");

            sorter.setRowFilter(new RowFilter<>(){
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry){
                    try{
                        String dateStr = entry.getStringValue(1);
                        Date billDate = sdf.parse(dateStr);

                        boolean matchDate = !billDate.before(fromDate) && !billDate.after(toDate);

                        String keyword = txtSearch.getText().toLowerCase();
                        boolean matchSearch = keyword.isEmpty() || entry.getStringValue(0).toLowerCase().contains(keyword) || entry.getStringValue(3).toLowerCase().contains(keyword);

                        return matchDate && matchSearch;
                    }catch(ParseException ex){
                        return true;
                    }
                }
            });
            updateStatistics();
        });

        return panel;

    }

    private void updateStatistics(){
        int count = table.getRowCount();
        double total = 0;

        for (int i = 0; i < count; i++){
            int modelRow = table.convertRowIndexToModel(i);
            total += (double) model.getValueAt(modelRow, 4);
        }

        lbTotalQuantity.setText(String.valueOf(count));
        lbRevenue.setText(MoneyFormatter.toVND(total));
    }

private void loadBillTable(){
    if(model == null)
        return;
    model.setRowCount(0);

    BillBUS billBUS = new BillBUS();
    List<BillDTO> list = billBUS.getAllBills();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - d/M/yyyy");

    for (BillDTO bill : list){
        String formattedDate = "";
        if(bill.getCreatedDate() != null){
            formattedDate = sdf.format(bill.getCreatedDate());
        }
        model.addRow(new Object[]{
            bill.getBillId(), formattedDate, bill.getEmployeeId(), bill.getCustomerId(), bill.getTotalBillPrice()
        });
    }
     updateStatistics();
}

}