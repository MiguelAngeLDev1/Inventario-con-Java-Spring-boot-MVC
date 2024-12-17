package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.dtos.ProductoVentaDTO;
import Proyecto.PersonalMQ.dtos.VentaDTO;
import Proyecto.PersonalMQ.respository.DetalleVentaRepositorio;
import jakarta.transaction.Transactional;
import Proyecto.PersonalMQ.models.DetalleVenta;
import Proyecto.PersonalMQ.models.Producto;
import Proyecto.PersonalMQ.models.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import Proyecto.PersonalMQ.respository.ProductoRepositorio;
import Proyecto.PersonalMQ.respository.VentaRepositorio;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServicio implements ImplVentaServicio{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;
    @Autowired
    private VentaRepositorio ventaRepositorio;
    @Autowired
    private ProductoServicio productoServicio;


    @Override
    public Venta realizarVenta(List<Long> productosIds) {
        //Obtengo todos los productos seleccionados
        List<Producto> productos = productoRepositorio.findAllById(productosIds);

        if (productos.isEmpty()){
            throw new IllegalArgumentException("No se encontraro productos con los IDs proporcionados: " );
        }

        //Calcular el total de la venta
        Double totalVenta = productos.stream()
                .mapToDouble(Producto::getPrecioProducto)
                .sum();

        //Crea la venta
        Venta venta = new Venta();
        venta.setFechVenta(LocalDateTime.now());//Agregamos la fecha de la venta
        venta.setTotalVenta(totalVenta);

        //Crear los detalles de la venta
        List<DetalleVenta> detalles = productos.stream().map(producto->{
            DetalleVenta detalle = new DetalleVenta();
            detalle.setNombre(producto.getNombreProducto());
            detalle.setCantidad(1);
            detalle.setPrecio(producto.getPrecioProducto());
            detalle.setVenta(venta);//Asociamos el detalle con la venta
            return detalle;
        }).toList();

        venta.setDetalles(detalles);//Asociamos los detalles con la venta

        //Actualizar la cantidad del inventario
        productos.forEach(producto -> producto.setCantidadProducto(producto.getCantidadProducto()-1));
        productoRepositorio.saveAll(productos);

        //Guardar la venta y sus detalles
        return ventaRepositorio.save(venta);
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepositorio.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepositorio.findById(id).orElseThrow(()->
                new IllegalArgumentException("Venta no encontrada con ID: "+ id));
    }


    @Transactional
    public Venta guardarVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setTotalVenta(ventaDTO.getTotal());
        venta.setFechVenta(LocalDateTime.now());

        //Guardar la venta para obtener su ID generado
        venta  = ventaRepositorio.save(venta);

        //Procesar los detalles de la venta
        for (ProductoVentaDTO productoVentaDTO : ventaDTO.getDetallesVenta()){
            Producto producto = productoServicio.obtenerProductoPorId(productoVentaDTO.getProductoId());

            //Validar Stock
            if (producto.getCantidadProducto() < productoVentaDTO.getCantidad()){
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }

            //Actualizar la cantidad en inventario
            producto.setCantidadProducto(producto.getCantidadProducto()- productoVentaDTO.getCantidad());
            productoServicio.actualizarProducto(producto);

            //Crear el detalle de la venta
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidad(productoVentaDTO.getCantidad());
            detalleVenta.setPrecio(productoVentaDTO.getPrecio());

            //Guardar el detalle de la venta
            detalleVentaRepositorio.save(detalleVenta);
        }

        return venta;

    }

    private Venta convertirVentaDTOaEntidad(VentaDTO ventaDTO){
        Venta venta = new Venta();
        venta.setTotalVenta(ventaDTO.getTotal());
        venta.setFechVenta(LocalDateTime.now());

        //Convertir cada detalle de venta del DTO a DetalleVenta
        List<DetalleVenta> detalleVentas = ventaDTO.getDetallesVenta().stream()
                .map(detallesVentaDTO ->{
                    //Busca el producto asociado
                    Producto producto = productoServicio.obtenerProductoPorId(detallesVentaDTO.getProductoId());

                //Validar stock
                if (producto.getCantidadProducto() < detallesVentaDTO.getCantidad()){
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
                }

                //Actualizar  inventario del producto
                    producto.setCantidadProducto(producto.getCantidadProducto() -detallesVentaDTO.getCantidad());
                //Llamar a un servicio que actualice el inventario de productos
                    productoServicio.agregarProducto(producto);

                //Crear DetalleVenta
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setNombre(producto.getNombreProducto());
                detalleVenta.setCantidad(detallesVentaDTO.getCantidad());
                detalleVenta.setPrecio(detallesVentaDTO.getPrecio());
                detalleVenta.setVenta(venta);
                return detalleVenta;
                })
                .collect(Collectors.toList());

        //Asignar detalles a la venta
        venta.setDetalles(detalleVentas);
        return venta;
    }


}
