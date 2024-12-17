package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.models.Producto;

import java.util.List;

public interface ImplProductoServicio {

    List<Producto>listarProductos();
    Producto obtenerProductoPorId(Long id);
    Producto agregarProducto(Producto producto);
    Producto actualizarProducto(Producto producto);
    void eliminarProducto(Long id);
    List<Producto>obtenerProductosPorCategoria(Long categoriaId);
}
