package com.bookstore.gui.panel.ProductTab;

import com.bookstore.util.Refreshable;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class ProductTabbedPane extends JPanel implements Refreshable{
    JTabbedPane tabbedPane = new JTabbedPane();

    public ProductTabbedPane() {
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
        tabbedPane.addTab("Sản phẩm", new ProductPanel());
        tabbedPane.addTab("Tác giả", new AuthorPanel());
        tabbedPane.addTab("Thể loại", new CategoryPanel());
        tabbedPane.addTab("Nhà cung cấp", new SupplierPanel());

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab instanceof Refreshable r) {
                r.refresh();
            }
        });
    }
}