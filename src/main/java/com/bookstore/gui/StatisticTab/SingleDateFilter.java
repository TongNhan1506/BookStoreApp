package com.bookstore.gui.StatisticTab;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;


public class SingleDateFilter extends JPanel {

    private JSpinner spinner;

    public SingleDateFilter() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(new Color(180, 180, 180), 2, true));
        setPreferredSize(new Dimension(200, 45));


        SpinnerDateModel model =
                new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);

        spinner = new JSpinner(model);
        spinner.setBorder(null);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(spinner, "d/M/yyyy");
        spinner.setEditor(editor);

        ((JSpinner.DefaultEditor) spinner.getEditor())
                .getTextField().setBorder(null);

        add(spinner, BorderLayout.CENTER);


        Component[] comps = spinner.getComponents();
        for (Component c : comps) {
            if (c instanceof JButton) {
                c.setBackground(Color.WHITE);
                ((JButton) c).setBorder(null);
            }
        }
    }

    public Date getSelectedDate() {
        return (Date) spinner.getValue();
    }

    public void addChangeListener(Runnable action) {
        spinner.addChangeListener(e -> action.run());
    }
}