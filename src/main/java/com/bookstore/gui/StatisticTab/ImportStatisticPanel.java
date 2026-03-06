package com.bookstore.gui.StatisticTab;

import com.bookstore.bus.ImportStatisticBUS;
import com.bookstore.dto.ImportStatisticDTO;
import com.bookstore.util.MoneyFormatter;
import com.bookstore.util.AppConstant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;


public class ImportStatisticPanel extends JPanel{

    private JTable table;
    private DefaultTableModel model;
    private JSpinner toDateSpinner;
    private JSpinner fromDateSpinner;
    private JComboBox<String> cboQuarter;
    private JComboBox<Integer> cboYear;
    private ImportStatisticBUS bus = new ImportStatisticBUS();
    private JPanel topPanel;
    private JTextArea topArea;
    private JTextArea bottomArea;

    public ImportStatisticPanel(){
        setLayout(new BorderLayout());
        initUI();
        loadDataByDate();
    }

    private void initUI(){
        JPanel header = new JPanel();
        header.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        header.setPreferredSize(new Dimension(0,50));
        header.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel title = new JLabel("NHẬP HÀNG");
        title.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        header.add(title);
        add(header, BorderLayout.NORTH);



        JPanel mainContainer = new JPanel(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel leftFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "dd/MM/yyyy"));

        toDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "dd/MM/yyyy"));

        JButton btnFilterDate = new JButton("Lọc");
        btnFilterDate.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        btnFilterDate.setForeground(Color.WHITE);
        btnFilterDate.addActionListener(e-> loadDataByDate());

        leftFilter.add(new JLabel("Từ"));
        leftFilter.add(fromDateSpinner);
        leftFilter.add(new JLabel("Đến"));
        leftFilter.add(toDateSpinner);
        leftFilter.add(btnFilterDate);


        JPanel rightFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cboQuarter = new JComboBox<>(new String[]{"Quý 1", "Quý 2", "Quý 3", "Quý 4"});

        cboYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 1; i++){
            cboYear.addItem(i);
        }
        cboYear.setSelectedItem(currentYear);

        JButton btnFilterQuarter = new JButton("Lọc");
        btnFilterQuarter.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        btnFilterQuarter.setForeground(Color.WHITE);
        btnFilterQuarter.addActionListener(e-> loadTop3ByQuarter());

        rightFilter.add(cboQuarter);
        rightFilter.add(cboYear);
        rightFilter.add(btnFilterQuarter);


        filterPanel.add(leftFilter, BorderLayout.WEST);
        filterPanel.add(rightFilter, BorderLayout.EAST);

        mainContainer.add(filterPanel, BorderLayout.NORTH);


        JPanel contentPanel = new JPanel(new GridLayout(1,2,20,0));
        contentPanel.setBorder(new EmptyBorder(0,10,10,10));
        mainContainer.add(contentPanel, BorderLayout.CENTER);


        model = new DefaultTableModel(
                new String[]{"Tên Sản Phẩm", "Số Lượng", "Tổng Tiền"},0);

        table = new JTable(model);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(true);
        tableHeader.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        contentPanel.add(scrollPane);


        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2,1,0,10));

        JPanel top3Panel = createStatBox("3 sản phẩm nhập nhiều nhất trong quý", Color.decode(AppConstant.GREEN_COLOR_CODE));
        JPanel bottom3Panel = createStatBox("3 sản phẩm nhập ít nhất trong quý", new Color(220,80,70));

        rightPanel.add(top3Panel);
        rightPanel.add(bottom3Panel);

        contentPanel.add(rightPanel);
    }





    private JPanel createStatBox(String title, Color color){
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(color);
        header.setForeground(Color.WHITE);
        header.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD,14));
        header.setPreferredSize(new Dimension(0,40));

        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        return panel;
    }


    private void loadDataByDate(){
        model.setRowCount(0);

        Date fromDate = (Date) fromDateSpinner.getValue();
        Date toDate = (Date) toDateSpinner.getValue();

        List<ImportStatisticDTO> list = bus.getStatistic(fromDate, toDate);

        for(ImportStatisticDTO dto : list){
            model.addRow(new Object[]{
                    dto.getProductName(),
                    dto.getTotalQuantity(),
                    MoneyFormatter.toVND(dto.getTotalImportPrice())
            });
        }
    }


    private void loadDataByQuarter(){
        int quarter = cboQuarter.getSelectedIndex() + 1;
        int year = (int) cboYear.getSelectedItem();

        model.setRowCount(0);

        List<ImportStatisticDTO> list = bus.getStatisticByQuarter(quarter, year);

        for(ImportStatisticDTO dto : list){
            model.addRow(new Object[]{
                    dto.getProductName(),
                    dto.getTotalQuantity(),
                    MoneyFormatter.toVND(dto.getTotalImportPrice())
            });
        }
    }


    private void loadTop3ByQuarter(){
        int quarter = cboQuarter.getSelectedIndex() + 1;
        int year = (int) cboYear.getSelectedItem();

        List<ImportStatisticDTO> topMost = bus.getTop3Most(quarter, year);

        topArea.setText("");
        for(ImportStatisticDTO dto : topMost){
            topArea.append(dto.getProductName() + "- SL: " + dto.getTotalQuantity() + "\n");

        }

        List<ImportStatisticDTO> topLeast = bus.getTop3Least(quarter, year);

        bottomArea.setText("");
        for(ImportStatisticDTO dto : topLeast){
            bottomArea.append(dto.getProductName() + "- SL: " + dto.getTotalQuantity() + "\n");
        }
    }
}