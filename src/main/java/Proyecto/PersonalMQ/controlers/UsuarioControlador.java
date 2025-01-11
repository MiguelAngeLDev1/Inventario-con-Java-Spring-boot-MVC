package Proyecto.PersonalMQ.controlers;

import Proyecto.PersonalMQ.dtos.RegistroUsuarioDTO;
import Proyecto.PersonalMQ.models.Usuario;
import Proyecto.PersonalMQ.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;


    @GetMapping("/registro")
    public String mostrarFormularioRegistro(){
        return "registroUsuario";
    }


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Validated RegistroUsuarioDTO registroDTO){
        try {
            Usuario usuarioRegistrado = usuarioServicio.registrarUsuario(registroDTO);
            return ResponseEntity.ok(Map.of("message","Usuario registrado con exito"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Error Inesperado"));
        }
    }
}
