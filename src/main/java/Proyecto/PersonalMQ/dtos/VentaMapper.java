package Proyecto.PersonalMQ.dtos;

import Proyecto.PersonalMQ.models.DetalleVenta;
import Proyecto.PersonalMQ.models.Producto;
import Proyecto.PersonalMQ.models.Usuario;
import Proyecto.PersonalMQ.models.Venta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaMapper {
    public static Venta toEntity(VentaDTO ventaDTO, Usuario usuario) {
        Venta venta = new Venta();
        venta.setIdVenta(ventaDTO.getId());
        venta.setTotalVenta(ventaDTO.getTotal());
        venta.setUsuario(usuario);

        if (ventaDTO.getDetallesVenta() !=null && !ventaDTO.getDetallesVenta().isEmpty()){
            List<DetalleVenta>detalles = ventaDTO.getDetallesVenta().stream().map(detalleDTO ->{
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(new Producto(detalleDTO.getProductoId()));
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setVenta(venta);
                return detalle;
            }).toList();

            venta.setDetalles(detalles);
        }

        return venta;
    }

}
