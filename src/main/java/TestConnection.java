import com.bookstore.util.DatabaseConnection; // Nhớ import class kết nối của bạn
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println("--- Đang kiểm tra kết nối đến Database ---");
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("KẾT NỐI THÀNH CÔNG!");
            System.out.println("Tên Database: bookstore_db");

            try {
                conn.close();
                System.out.println("Đã đóng kết nối an toàn.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("KẾT NỐI THẤT BẠI!");
            System.out.println("Vui lòng kiểm tra lại:");
            System.out.println("2. Tên database 'bookstore_db' đã tạo trong MySQL chưa?");
            System.out.println("3. Password có đúng không?");
        }
    }
}