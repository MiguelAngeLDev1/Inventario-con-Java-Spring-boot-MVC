package Proyecto.PersonalMQ.controlers;

import Proyecto.PersonalMQ.dtos.ProductoVentaDTO;
import Proyecto.PersonalMQ.dtos.VentaDTO;
import Proyecto.PersonalMQ.models.DetalleVenta;
import Proyecto.PersonalMQ.models.Producto;
import Proyecto.PersonalMQ.models.Venta;
import Proyecto.PersonalMQ.respository.DetalleVentaRepositorio;
import Proyecto.PersonalMQ.respository.ProductoRepositorio;
import Proyecto.PersonalMQ.respository.VentaRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import Proyecto.PersonalMQ.services.ProductoServicio;
import Proyecto.PersonalMQ.services.VentaServicio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ventas")
public class VentaControlador {

    private static final Logger log = LoggerFactory.getLogger(VentaControlador.class);
    @Autowired
    private VentaServicio ventaServicio;
    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private VentaRepositorio ventaRepositorio;
    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;

    //Mostrar formulario para realizar una venta
    @GetMapping("/realizar")
    public String mostrarFormularioVenta(Model model){
        //Obtener todos los productos disponibles
        List<Producto> productos = productoServicio.listarProductos();
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setDetallesVenta(new ArrayList<>());

        //Inicializar 'detallesVenta' como una lista de ProductoVentaDTO
        List<ProductoVentaDTO>detallesVenta = productos.stream()
                .map(producto -> {
                    ProductoVentaDTO productoVentaDTO = new ProductoVentaDTO();
                    productoVentaDTO.setProductoId(producto.getIdProducto());
                    productoVentaDTO.setNombreProducto(producto.getNombreProducto());
                    productoVentaDTO.setPrecio(producto.getPrecioProducto());
                    productoVentaDTO.setCantidad(0);//Valor inicial de cantidad
                    return productoVentaDTO;
                })
                .collect(Collectors.toList());

        //Asignar la lista inicializada al ventaDTO
        ventaDTO.setDetallesVenta(detallesVenta);

        //Añadir los atributos al modelo para la vista
        model.addAttribute("productos", productos);
        model.addAttribute("ventaDTO", ventaDTO);

        return "listarVentas";//Vista del formulario para hacer la venta

    }

    /*@PostMapping("/realizarVenta")
    @ResponseBody
    public ResponseEntity<String> realizarVenta(@RequestBody VentaDTO ventaDTO){
        try {
            //Procesar la venta y guardarla en la base de datos
            ventaServicio.realizarVenta((List<Long>)ventaDTO);
            return ResponseEntity.ok("Venta realizada con exito");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al realizar la venta");
        }
    }*/

    //Procesar la venta y desconatr inventario
    @PostMapping("/realizarVenta/form")
    public String realizarVenta(@ModelAttribute VentaDTO ventaDTO, Model model){

        System.out.println("VentaDTO recibida: "+ ventaDTO);

        //Verificar que la ventaDTO contiene productos
        if(ventaDTO.getDetallesVenta() == null || ventaDTO.getDetallesVenta().isEmpty()){
            model.addAttribute("error", " No se han agregado productos a la venta.");
            return "listarVenta";
        }

        try {
            //Logica para procesar la venta(Guardar en la base de datos,actualizar inventario)
            Venta venta = new Venta();
            venta.setFechVenta(LocalDateTime.now());
            venta.setTotalVenta(ventaDTO.getTotal());

            //Guardar la venta en la base datos
            Venta ventaGuardada = ventaRepositorio.save(venta);

            //Procesar los detalles de la venta(productos)
            for (ProductoVentaDTO productoDTO : ventaDTO.getDetallesVenta()){
                //Encuentra el prodcuto en la base de datos
                Producto producto = productoServicio.obtenerProductoPorId(productoDTO.getProductoId());

                //Crear el objeto DetalleVenta para cada producto
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(ventaGuardada); // Relaciona el detalle con la venta
                detalleVenta.setProducto(producto); // Relaciona el detalle con el producto
                detalleVenta.setCantidad(productoDTO.getCantidad());// Asigna la cantidad
                detalleVenta.setPrecio(productoDTO.getPrecio());// Asigna el precio

                //Guarda el detalle en la base de datos
                detalleVentaRepositorio.save(detalleVenta);

                //Actualiza el inventario del producto
                producto.setCantidadProducto(producto.getCantidadProducto() - productoDTO.getCantidad());
                productoRepositorio.save(producto);// Guarda el producto actualizado en la base de datos
            }

            //Redirige a la pagina de exito si la venta se procesa
            model.addAttribute("mensaje", " Venta Realizada con exito");
            return "listadoVentas";
        }catch (Exception e){
            //Si ocurre un error
            model.addAttribute("error"," Hubo un error al procesar la venta.");
            return "listarVentas";
        }
    }


    //Listar todas las ventas realizadas
    @GetMapping("/detalles")
    public String listarVentas(Model model){
        List<Venta> ventas = ventaServicio.listarVentas();

        //Formatear las fechas
        ventas.forEach(venta -> {
            if (venta.getFechVenta() != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                venta.setFechaFormateada(venta.getFechVenta().format(formatter));
            }
        });

        //Imprimo despues despues de formatear
        ventas.forEach(venta -> System.out.println("ID: "+ venta.getIdVenta() + " Fecha: "+ venta.getFechVenta()+ " Total: "+ venta.getTotalVenta()));


        model.addAttribute("ventas", ventas);
        return "listadoVentas"; //vista de listado ventas
    }

    //Guardar la venta
    @PostMapping("/guardar")
    public ResponseEntity<?> guardarVenta(@RequestBody VentaDTO ventaDTO){
        try {
            //Llamar al servicio para guardar la venta
            Venta respuesta = ventaServicio.guardarVenta(ventaDTO);

            //Retornar una respues con los datos actualizados
            Map<String, Object> response = new HashMap<>();
            response.put("message","Venta guardada con exito");
            response.put("detallesVenta", respuesta.getDetalles() != null ? respuesta.getDetalles().stream()
                    .map(detalle ->{
                        Producto producto = detalle.getProducto();
                        Map<String,Object> productoData = new HashMap<>();
                        productoData.put("productoId",producto.getIdProducto());
                        productoData.put("cantidad",producto.getCantidadProducto());
                        return productoData;
                    })
                    .toList(): Collections.emptyList());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error("Error al guardar la venta", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message","Error al guardar la venta","error",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public String verDetalleVenta(@PathVariable Long id, Model model){
        Venta venta = ventaServicio.obtenerVentaPorId(id);

        if (venta.getFechVenta() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:ss");
            String fechaFormateada = venta.getFechVenta().format(formatter);
            model.addAttribute("fechaFormateada", fechaFormateada);
        }

        model.addAttribute("venta", venta);
        return "detallesVentaId";
    }

}
