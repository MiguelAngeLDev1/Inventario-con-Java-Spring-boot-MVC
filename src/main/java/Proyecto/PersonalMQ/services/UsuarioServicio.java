package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.dtos.RegistroUsuarioDTO;
import Proyecto.PersonalMQ.models.Usuario;
import Proyecto.PersonalMQ.respository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuario obteberUsuarioPorNombre(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public Usuario registrarUsuario(RegistroUsuarioDTO registroDTO){
        //Verificar si el usuario ya existe
        if (usuarioRepositorio.findByNombreUsuario(registroDTO.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de ususario ya esta en uso");
        }

        //Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(registroDTO.getNombreUsuario());
        usuario.setContrasenia(registroDTO.getContrasenia());
        usuario.setRol(registroDTO.getRol());

        //Guardar en la base de datos
        return usuarioRepositorio.save(usuario);
    }
}
