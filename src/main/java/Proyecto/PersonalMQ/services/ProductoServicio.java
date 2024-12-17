package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.models.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import Proyecto.PersonalMQ.respository.ProductoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServicio implements ImplProductoServicio{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public List<Producto> listarProductos() {
        return productoRepositorio.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    @Override
    public Producto agregarProducto(Producto producto) {
        return productoRepositorio.save(producto);
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        Producto productoExistente = productoRepositorio.findById(producto.getIdProducto())
                .orElseThrow(()->new IllegalArgumentException("No Existe el producto con id: "+ producto.getIdProducto()));

        //Se actualizan los campos necesarios
        productoExistente.setNombreProducto(producto.getNombreProducto());
        productoExistente.setDescripcionProducto(producto.getDescripcionProducto());
        productoExistente.setPrecioProducto(producto.getPrecioProducto());
        productoExistente.setCategoria(producto.getCategoria());
        return productoRepositorio.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepositorio.deleteById(id);
    }

    @Override
    public List<Producto> obtenerProductosPorCategoria(Long categoriaId) {
        return productoRepositorio.findByCategoria_IdCategoria(categoriaId);
    }
}
