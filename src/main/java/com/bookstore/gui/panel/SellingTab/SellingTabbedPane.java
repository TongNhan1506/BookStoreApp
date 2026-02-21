package com.bookstore.gui.panel.SellingTab;

import com.bookstore.util.Refreshable;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class SellingTabbedPane extends JPanel implements Refreshable{
    JTabbedPane tabbedPane = new JTabbedPane();

    public SellingTabbedPane() {
        initUI();
    }

    @Override
    public void refresh() {
        Component selectedTab = tabbedPane.getSelectedComponent();
        if (selectedTab instanceof Refreshable r) {
            r.refresh();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
        tabbedPane.addTab("Bán Hàng", new SellingPanel());
        tabbedPane.addTab("Khách Hàng", new CustomerPanel());
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab instanceof Refreshable r) {
                r.refresh();
            }
        });
    }
}