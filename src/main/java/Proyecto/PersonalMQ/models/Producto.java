package Proyecto.PersonalMQ.models;

import Proyecto.PersonalMQ.dtos.VentaDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="productos")
@ToString(exclude = "categoria")//Excluye la relacion para evitar ciclos repetitivos
@EqualsAndHashCode(of = "idProducto")//Solo usa el id para evitar los ciclos infinitos
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idProducto;

    private String nombreProducto;

    private String descripcionProducto;

    private Double precioProducto;

    private int cantidadProducto;

    //Relacion de muchos a uno con categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)//clave foranea
    private Categoria categoria;




}
