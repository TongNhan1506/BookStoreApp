package com.bookstore.gui.StatisticTab;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class StatisticsTabbedPane extends JPanel{
    JTabbedPane tabbedPane = new JTabbedPane();

    public StatisticsTabbedPane(){
        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
        tabbedPane.addTab("Doanh Thu", new RevenueStatisticPanel());
        tabbedPane.addTab("Sản Phẩm", new JPanel());
        tabbedPane.addTab("Nhập Hàng", new ImportStatisticPanel());
        tabbedPane.addTab("Chi phí", new JPanel());

        add(tabbedPane, BorderLayout.CENTER);

    }
}