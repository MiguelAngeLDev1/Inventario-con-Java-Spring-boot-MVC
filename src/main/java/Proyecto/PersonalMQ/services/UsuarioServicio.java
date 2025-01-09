package Proyecto.PersonalMQ.services;

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
}
