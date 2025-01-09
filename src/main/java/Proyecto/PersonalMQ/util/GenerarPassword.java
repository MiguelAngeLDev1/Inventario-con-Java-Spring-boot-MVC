package Proyecto.PersonalMQ.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = encoder.encode("1234");// Cambia por la contraseña en texto plano
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
