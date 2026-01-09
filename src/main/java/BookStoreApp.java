import com.bookstore.util.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class BookStoreApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Không thể khởi tạo giao diện");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Bán Sách");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Căn giữa màn hình

            // Thêm thử một nút để thấy nó đẹp hơn nút mặc định
            frame.add(new JButton("Xin chào! Đây là giao diện FlatLaf"));

            frame.setVisible(true);
        });

        System.out.println("Đang kiểm tra kết nối...");
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("Kết nối thành công!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Kết nối thất bại. Vui lòng kiểm tra lại User/Pass.");
        }
    }
}