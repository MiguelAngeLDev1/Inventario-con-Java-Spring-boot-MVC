package Proyecto.PersonalMQ.services;

import Proyecto.PersonalMQ.models.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Proyecto.PersonalMQ.respository.CategoriaRepositorio;

import java.util.List;

@Service
public class CategoriaServicio implements ImplCategoriaServicio{

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    @Override
    public List<Categoria> listarCategorias() {
        List<Categoria>categorias = categoriaRepositorio.findAll();
        if (categorias.isEmpty()){
            System.out.println("No se encontraron categorias en la base de datos");
        }
        return categorias;
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    @Override
    public Categoria actualizarCategoria(Long id, Categoria categoria) {
        Categoria categoriaExistente = obtenerCategoriaPorId(id);
        categoriaExistente.setNombreCategorial(categoria.getNombreCategorial());
        return categoriaRepositorio.save(categoriaExistente);
    }

    @Override
    public void eliminarCategoria(Long id) {
        categoriaRepositorio.deleteById(id);
    }
}
