
import Model.Usuario;  // "model" minúscula, no "Model"
import service.UsuarioService;
import java.util.List;

public class TestUsuarioService {
    public static void main(String[] args) {
        try {
            UsuarioService usuarioService = new UsuarioService();
            
            System.out.println("\n=== PRUEBA LISTAR USUARIOS ===");
            usuarioService.listarTodosUsuarios().forEach(usuario -> {
                System.out.println("- " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
            });
            
            System.out.println("\n=== PRUEBA CREAR USUARIO ===");
            Usuario nuevo = new Usuario("Maria Lopez", "maria@udb.edu", "5678", "ALUMNO");
            usuarioService.crearUsuario(nuevo);
            
            System.out.println("\n=== PRUEBA RESTABLECER CONTRASEÑA ===");
            usuarioService.restablecerContraseña("profesor@udb.edu");
            
            System.out.println("\n=== PRUEBA VERIFICAR MORA ===");
            usuarioService.tieneMora(2);
            
        } catch (Exception e) {
            System.out.println("❌ Error en test: " + e.getMessage());
        }
    }
}