package Proyecto.PersonalMQ.controlers;

import Proyecto.PersonalMQ.models.Categoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import Proyecto.PersonalMQ.services.CategoriaServicio;

@Controller
@RequestMapping("/categorias")
public class CategoiraControlador {

    private static final Logger log = LoggerFactory.getLogger(CategoiraControlador.class);
    @Autowired
    private CategoriaServicio categoriaServicio;

    //Mostrar el formulario para agregar una nueva categoria
    @GetMapping("/nuevo")
    public String mostrarFormuarioCategoria(Model model){
        model.addAttribute("categoria", new Categoria());
        return "categoria_form";
    }

    //Manejar la solicitud POST para guardar la categoria
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("categoria")Categoria categoria){
        categoriaServicio.guardarCategoria(categoria);
        return "redirect:/categorias/listar";
    }

    //Listar categorias
    @GetMapping("/listar")
    public String listarCategorias(Model model){
        model.addAttribute("categorias", categoriaServicio.listarCategorias());
        return "listadoCategorias";
    }

    @GetMapping("/editarCategoria/{id}")
    public String editarCategoria(@PathVariable("id")Long id,Model model){
        //Buscar categoria por id
        Categoria categoria = categoriaServicio.obtenerCategoriaPorId(id);
        model.addAttribute("categoria", categoria);
        return "editarCategoria";
    }

    //Editar categoria
    @PostMapping("/editarCategoria")
    public String editarCategoria(@ModelAttribute("categoria")Categoria categoria){
        categoriaServicio.guardarCategoria(categoria);
        return "redirect:/listadoCategorias";
    }

    //Eliminar categoria
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id){
        log.info("Eliminando categoria con id " + id);
        categoriaServicio.eliminarCategoria(id);
        return "redirect:/categorias/listar";
    }
}
