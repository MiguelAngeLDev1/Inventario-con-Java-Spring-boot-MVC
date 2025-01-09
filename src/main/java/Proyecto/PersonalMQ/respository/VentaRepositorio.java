package Proyecto.PersonalMQ.respository;

import Proyecto.PersonalMQ.models.Usuario;
import Proyecto.PersonalMQ.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuario(Usuario usuario);
}
