package com.bookstore.gui.StatisticTab;

import com.bookstore.bus.RevenueStatisticBUS;
import com.bookstore.util.AppConstant;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.Date;
import java.util.Calendar;

public class RevenueStatisticPanel extends JPanel {

    private JLabel lblHighestMonth;
    private JLabel lblHighestValue;
    private JLabel lblLowestMonth;
    private JLabel lblLowestValue;
    private RevenueStatisticBUS revenueBUS = new RevenueStatisticBUS();

    public RevenueStatisticPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createTopMenu(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        loadRevenueData(Calendar.getInstance().get(Calendar.YEAR));

    }

    private JPanel createTopMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode(AppConstant.GREEN_COLOR_CODE));
        panel.setBorder(new EmptyBorder(10,20,10,20));

        JLabel title = new JLabel("Doanh Thu");
        title.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    private JPanel createContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(230, 230, 230));
        main.setBorder(new EmptyBorder(30, 30, 30, 30));

        main.add(createTopCards(), BorderLayout.NORTH);
        main.add(createBottomStats(), BorderLayout.CENTER);

        return main;
    }

    private JPanel createTopCards() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 0));
        panel.setOpaque(false);

        panel.add(createBigCard(
                "Doanh Thu Cao Nhất",
                Color.decode(AppConstant.GREEN_COLOR_CODE),
                true
        ));

        panel.add(createBigCard(
                "Doanh Thu Thấp Nhất",
                new Color(200, 30, 25),
                false
        ));

        return panel;
    }

    private JPanel createBigCard(String title, Color bg, boolean highest) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 36));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(bg);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblMonth = new JLabel("Tháng 3");
        lblMonth.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 50));
        lblMonth.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel("12.000.000.000");
        lblValue.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 50));
        lblValue.setForeground(Color.WHITE);

        card.add(lblMonth);
        card.add(lblValue);

        wrapper.add(titleLabel, BorderLayout.NORTH);
        wrapper.add(card, BorderLayout.CENTER);

        if (highest) {
            lblHighestMonth = lblMonth;
            lblHighestValue = lblValue;
        } else {
            lblLowestMonth = lblMonth;
            lblLowestValue = lblValue;
        }

        return wrapper;
    }

    private JPanel createBottomStats() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 30, 30));
        panel.setBorder(new EmptyBorder(40, 0, 0, 0));
        panel.setOpaque(false);

        panel.add(createStatCard("Theo ngày",1));
        panel.add(createStatCard("Theo tháng",2));
        panel.add(createStatCard("Theo quý",3));
        panel.add(createStatCard("Theo năm",4));

        return panel;
    }

    private JPanel createStatCard(String header, int type) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(5, 7, 5, 7));

        JLabel headerLabel = new JLabel(header);
        headerLabel.setFont(new Font(AppConstant.FONT_NAME, Font.BOLD, 28));

        SingleDateFilter dateFilter = new SingleDateFilter();
        dateFilter.setPreferredSize(new Dimension(140,32));
        dateFilter.setMaximumSize(new Dimension(140, 32));
        dateFilter.setMinimumSize(new Dimension(140, 32));

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(dateFilter, BorderLayout.EAST);

        JPanel body = new JPanel(new GridLayout(3, 1));
        body.setBorder(new EmptyBorder(5, 20, 5, 20));
        body.setBackground(Color.WHITE);

        JLabel percentLabel = new JLabel("0%");
        percentLabel.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 15));
        percentLabel.setForeground(new Color(0,150,0));

        JLabel valueLabel = new JLabel("0 VND");
        valueLabel.setFont(new Font(AppConstant.FONT_NAME, Font.PLAIN, 20));

        body.add(percentLabel);
        body.add(valueLabel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);

        dateFilter.addChangeListener(()->{
            Date selectedDate = dateFilter.getSelectedDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDate);

            double currentRevenue = 0;
            double previousRevenue = 0;

            switch(type){
                case 1:
                    currentRevenue = revenueBUS.getRevenueByDate(selectedDate);
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    previousRevenue = revenueBUS.getRevenueByDate(cal.getTime());
                    break;

                case 2:
                    int month = cal.get(Calendar.MONTH) + 1;
                    int year = cal.get(Calendar.YEAR);

                    currentRevenue = revenueBUS.getRevenueByMonth(month, year);
                    cal.add(Calendar.MONTH, -1);

                    previousRevenue = revenueBUS.getRevenueByMonth(
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.YEAR));
                    break;

                case 3:
                    int quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                    int qYear = cal.get(Calendar.YEAR);

                    currentRevenue = revenueBUS.getRevenueByQuarter(quarter, qYear);
                    cal.add(Calendar.MONTH, -3);
                    int prevQuarter = (cal.get(Calendar.MONTH) / 3 ) + 1;

                    previousRevenue = revenueBUS.getRevenueByQuarter(
                            prevQuarter,
                            cal.get(Calendar.YEAR));
                    break;

                case 4:
                    int y = cal.get(Calendar.YEAR);

                    currentRevenue = revenueBUS.getRevenueByYear(y);
                    previousRevenue = revenueBUS.getRevenueByYear(y-1);
                    break;
            }
            double percent = revenueBUS.calculateGrowthPercent(currentRevenue, previousRevenue);

            valueLabel.setText(com.bookstore.util.MoneyFormatter.toVND(currentRevenue));


            percentLabel.setText(
                    (percent >= 0 ? "↑ " : "↓ ") + String.format("%.1f", Math.abs(percent)) + "%"
            );

            percentLabel.setForeground(
                    percent >= 0 ? new Color(0,150,0) : Color.RED);
        });

        return panel;
    }
    private void loadRevenueData(int year) {
        var list = revenueBUS.getRevenueByMonthOfYear(year);
        if (list == null || list.isEmpty()) return;

        var highest = list.stream()
                .max(java.util.Comparator.comparingDouble(
                        dto -> dto.getTotalRevenue()))
                .orElse(null);

        var lowest = list.stream()
                .min(java.util.Comparator.comparingDouble(
                        dto -> dto.getTotalRevenue()))
                .orElse(null);

        if (highest != null) {
            lblHighestMonth.setText("Tháng " + highest.getTimeLabel());
            lblHighestValue.setText(
                    com.bookstore.util.MoneyFormatter
                            .toVND(highest.getTotalRevenue()));
        }

        if (lowest != null) {
            lblLowestMonth.setText("Tháng " + lowest.getTimeLabel());
            lblLowestValue.setText(
                    com.bookstore.util.MoneyFormatter
                            .toVND(lowest.getTotalRevenue()));
        }
    }

}