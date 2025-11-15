


import service.UsuarioService;

public class TestUsuarioService {
    public static void main(String[] args) {
        System.out.println("🧪 Test simple de inicialización...");
        
        try {
            UsuarioService usuarioService = new UsuarioService();
            System.out.println("✅ UsuarioService creado exitosamente");
            
            // Intentar listar usuarios
            int cantidad = usuarioService.listarTodosUsuarios().size();
            System.out.println("✅ Usuarios en BD: " + cantidad);
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}