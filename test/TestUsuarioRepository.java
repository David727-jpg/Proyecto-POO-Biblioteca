import repository.UsuarioRepository;
import repository.UsuarioRepositoryImpl;
import Model.Usuario;
import java.util.List;

public class TestUsuarioRepository {
    public static void main(String[] args) {
        System.out.println("🧪 Probando UsuarioRepository...");
        
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        
        // Test 1: Buscar todos los usuarios
        System.out.println("\n📋 Test 1: Listar todos los usuarios");
        List<Usuario> usuarios = usuarioRepo.findAll();
        System.out.println("✅ Usuarios encontrados: " + usuarios.size());
        for (Usuario u : usuarios) {
            System.out.println("   - " + u.getNombre() + " (" + u.getEmail() + ")");
        }
        
        // Test 2: Buscar usuario por ID
        System.out.println("\n📋 Test 2: Buscar usuario por ID");
        Usuario usuario = usuarioRepo.findById(1);
        if (usuario != null) {
            System.out.println("✅ Usuario encontrado: " + usuario.getNombre());
        } else {
            System.out.println("❌ Usuario no encontrado");
        }
        
        // Test 3: Buscar usuario por email
        System.out.println("\n📋 Test 3: Buscar usuario por email");
        Usuario usuarioEmail = usuarioRepo.findByEmail("profesor@udb.edu");
        if (usuarioEmail != null) {
            System.out.println("✅ Usuario encontrado: " + usuarioEmail.getNombre());
        } else {
            System.out.println("❌ Usuario no encontrado");
        }
        
        System.out.println("\n🎉 Pruebas de UsuarioRepository completadas");
    }
}