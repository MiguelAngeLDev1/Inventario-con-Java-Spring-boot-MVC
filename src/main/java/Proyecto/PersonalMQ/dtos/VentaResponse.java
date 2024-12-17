package Proyecto.PersonalMQ.dtos;

import Proyecto.PersonalMQ.models.Producto;
import lombok.Data;


import java.util.List;

@Data
public class VentaResponse {

    private String message;
    private List<Producto>productos;//Lista de los productos actualizados

}
