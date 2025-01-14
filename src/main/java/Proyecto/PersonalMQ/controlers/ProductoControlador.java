package Proyecto.PersonalMQ.controlers;

import Proyecto.PersonalMQ.models.Categoria;
import Proyecto.PersonalMQ.models.Producto;
import Proyecto.PersonalMQ.models.Usuario;
import Proyecto.PersonalMQ.services.UsuarioServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import Proyecto.PersonalMQ.services.CategoriaServicio;
import Proyecto.PersonalMQ.services.ProductoServicio;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductoControlador {

    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private CategoriaServicio categoriaServicio;

    @Autowired
    private ProductoServicio productoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        String nombreUsuario = principal.getName(); //Obtener el nombre del usuario autenticado

        //Traigo al usuario de la base de datos
        Usuario usuario = usuarioServicio.obteberUsuarioPorNombre(nombreUsuario);

        //Agregar el nombre del usuario al modelo para que este disponible en la vista
        model.addAttribute("nombreUsuario", usuario.getNombreUsuario());
        return "index";
    }

    @GetMapping("/agregar")
    public String agregar(Model model) {
        logger.info("Agregando producto");
        //Se crea un objeto producto para el formulario
        Producto producto = new Producto();
        model.addAttribute("producto", producto);
        //Obtenemos todas las categorias
        List<Categoria>categorias = categoriaServicio.listarCategorias();
        logger.info("Lista de categorias " + categorias.size());
        if (categorias.isEmpty()){
            //Verificar que la lista de categorías no este vacia
            System.out.println("No hay categorias disponible");
        }
        model.addAttribute("categorias", categorias);//Pasa las categorias al modelo
        return "agregar";
    }

    //Metodo para enviar datos a la base de datos
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("producto")Producto producto, Model model, BindingResult result){
        //Verifica si hay errores de validacion
        if (result.hasErrors()) {
            //si hay errores, volver a cargar el formulario con la lista de categorias
            List<Categoria>categorias = categoriaServicio.listarCategorias();
            model.addAttribute("categorias", categorias);
            return "agregar";
        }
        //Guardar el producto en la base de datos usando el servicio
        productoServicio.agregarProducto(producto);

        //Rediriguimos al listado de productos
        return "redirect:/";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", productoServicio.listarProductos());
        return "listadoProductos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id")Long id, Model model) {
        //Buscar el producto por el id
        Producto producto = productoServicio.obtenerProductoPorId(id);

        if(producto.getCategoria() == null){
            producto.setCategoria(new Categoria());//Asignar una instancia vacia si es nulo
        }
        List<Categoria>categorias = categoriaServicio.listarCategorias();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        return "editar";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("producto")Producto producto){
        productoServicio.actualizarProducto(producto);//Se llama al servicio de actualizacion
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id")Long id){
        Producto producto = productoServicio.obtenerProductoPorId(id);
        if (producto != null){
            productoServicio.eliminarProducto(id);
        }else {
            return "redirect:/";
        }
        return "redirect:/productos";
    }

    @GetMapping("/mostrar")
    public List<Producto> mostrarProductos(){
        //Mensaje en la consola para verificar que el metodo es llamado
        System.out.println("Ejecutando el metodo mostrarProductos");

        System.out.println("Iniciando la busqueda de productos...");
        //Obtiene todos los productos de la base de datos
        List<Producto> productos = productoServicio.listarProductos();
        System.out.println("Número de productos encontrados: "+ productos.size());

        //Imprimir cada producto en la consola
        productos.forEach(producto -> System.out.println(producto));

        return productos;

    }

    @GetMapping("/test")
    public String test(){
        logger.info("Metodo test ha sido llamado");
        return "Hola desde Spring";
    }

    @GetMapping("/productos/categoria/{id}")
    public String listarProductosPorCategoria(@PathVariable("id") Long categoriaId, Model model){
        //Obtener productos por categorias
        List<Producto> productos = productoServicio.obtenerProductosPorCategoria(categoriaId);

        //Agregar datos al modelo
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaId", categoriaId);
        return "productosPorCategoria";

    }
}
