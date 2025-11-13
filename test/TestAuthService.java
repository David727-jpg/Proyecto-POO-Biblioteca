import service.AuthService;
import Model.Usuario;

public class TestAuthService {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        
        System.out.println("=================== PRUEBA 1: LOGIN EXITOSO ADMIN ===================");
        Usuario admin = authService.login("admin@udb.edu", "1234");
        
        System.out.println("\n=================== PRUEBA 2: LOGIN CON CONTRASEÑA INCORRECTA ===================");
        Usuario errorPassword = authService.login("admin@udb.edu", "wrong");
        
        System.out.println("\n=================== PRUEBA 3: LOGIN CON EMAIL VACÍO ===================");
        Usuario emailVacio = authService.login("", "1234");
        
        System.out.println("\n=================== PRUEBA 4: LOGIN PROFESOR ===================");
        Usuario profesor = authService.login("profesor@udb.edu", "1234");
        
        System.out.println("\n=================== PRUEBA 5: LOGOUT ===================");
        if (profesor != null) {
            authService.logout(profesor);
        }
    }
}