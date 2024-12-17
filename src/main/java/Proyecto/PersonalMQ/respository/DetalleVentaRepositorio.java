package Proyecto.PersonalMQ.respository;

import Proyecto.PersonalMQ.models.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepositorio extends JpaRepository<DetalleVenta, Long> {
}
