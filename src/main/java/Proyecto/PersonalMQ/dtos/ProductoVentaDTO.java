package Proyecto.PersonalMQ.dtos;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Data
public class ProductoVentaDTO {
    @NotNull
    private Long productoId;
    @NotNull
    private String nombreProducto;
    @NotNull
    private Integer cantidad;
    @NotNull
    private Double precio;
    @NotNull
    private Long ventaId;
}
