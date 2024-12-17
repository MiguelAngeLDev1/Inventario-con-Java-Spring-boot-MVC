package Proyecto.PersonalMQ.dtos;

import lombok.Data;

@Data
public class DetalleVentaDTO {

    private Long idProducto;
    private String nombre;
    private Integer precio;
    private Integer cantidad;//La cantidad a vender de cada producto
}
