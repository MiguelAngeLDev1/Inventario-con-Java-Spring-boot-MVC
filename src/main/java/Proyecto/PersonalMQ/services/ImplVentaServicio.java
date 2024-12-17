package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.dtos.VentaDTO;
import Proyecto.PersonalMQ.models.Venta;

import java.util.List;

public interface ImplVentaServicio {

    public Venta realizarVenta(List<Long> productosIds);
    List<Venta>listarVentas();
    Venta obtenerVentaPorId(Long id);
    public Venta guardarVenta(VentaDTO ventaDTO);
}
