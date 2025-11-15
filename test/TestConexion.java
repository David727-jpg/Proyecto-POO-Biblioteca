

import repository.DatabaseService;  // ✅ Cambiado a repository
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("🧪 Probando conexión a MySQL...");
        
        try {
            DatabaseService dbService = DatabaseService.getInstance();
            Connection conn = dbService.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ ¡Conexión exitosa a MySQL!");
                conn.close();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}