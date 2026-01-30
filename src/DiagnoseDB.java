import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DiagnoseDB {
  public static void main(String[] args) {
    System.out.println("--- DB Diagnostic Tool ---");

    // 1. Check Driver
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("[OK] MySQL Driver found.");
    } catch (Exception e) {
      System.out.println("[FAIL] MySQL Driver missing!");
      return;
    }

    // 1.5. Raw Socket Test
    try {
      System.out.println("--- Testing Raw Socket (127.0.0.1:3306) ---");
      java.net.Socket s = new java.net.Socket("127.0.0.1", 3306);
      s.setSoTimeout(2000); // 2 seconds timeout
      java.io.InputStream in = s.getInputStream();
      System.out.println("[OK] Socket connected.");
      System.out.println("Waiting for server greeting...");
      int b = in.read();
      if (b != -1) {
        System.out.println("[OK] Server sent data. First byte: " + b + " (0x" + Integer.toHexString(b) + ")");
      } else {
        System.out.println("[FAIL] Server closed connection immediately (EOS).");
      }
      s.close();
    } catch (Exception e) {
      System.out.println("[FAIL] Raw Socket Error: " + e.getMessage());
    }

    // 2. Check Server Reachability (No DB)
    String serverUrl = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    try (Connection conn = DriverManager.getConnection(serverUrl, "root", "")) {
      System.out.println("[OK] Connected to MySQL Server (127.0.0.1:3306)");

      // 3. Check Database Existence
      boolean dbExists = false;
      try (ResultSet rs = conn.getMetaData().getCatalogs()) {
        while (rs.next()) {
          if ("cinema_db".equalsIgnoreCase(rs.getString(1))) {
            dbExists = true;
            break;
          }
        }
      }
      if (dbExists) {
        System.out.println("[OK] Database 'cinema_db' found.");
      } else {
        System.out.println("[FAIL] Database 'cinema_db' DOES NOT EXIST!");
      }

    } catch (Exception e) {
      System.out.println("[FAIL] Cannot connect to MySQL Server.");
      System.out.println("Error: " + e.getMessage());
    }
  }
}
