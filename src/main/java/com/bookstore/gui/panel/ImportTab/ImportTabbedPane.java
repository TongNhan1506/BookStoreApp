package com.bookstore.gui.panel.ImportTab;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class ImportTabbedPane extends JPanel {
    public ImportTabbedPane() { initUI();}

    private void initUI() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
        tabbedPane.addTab("Phiếu Nhập", new ImportPanel());
        tabbedPane.addTab("Nhập Hàng", new JPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }
}
