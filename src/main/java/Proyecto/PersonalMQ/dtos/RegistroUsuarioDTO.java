package Proyecto.PersonalMQ.dtos;

import lombok.Data;

@Data
public class RegistroUsuarioDTO {

    private String nombreUsuario;

    private String contrasenia;

    private String rol;
}
