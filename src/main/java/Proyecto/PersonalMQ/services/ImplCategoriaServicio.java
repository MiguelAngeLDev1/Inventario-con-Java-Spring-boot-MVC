package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.models.Categoria;

import java.util.List;

public interface ImplCategoriaServicio {

    List<Categoria> listarCategorias();
    Categoria obtenerCategoriaPorId(Long id);
    Categoria guardarCategoria(Categoria categoria);
    Categoria actualizarCategoria(Long id, Categoria categoria);
    void eliminarCategoria(Long id);
}
