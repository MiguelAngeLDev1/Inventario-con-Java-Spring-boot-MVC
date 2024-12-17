package Proyecto.PersonalMQ.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categorias")
@ToString(exclude = "productos")//Excluye la lista de productos para evitar ciclos infinitos
@EqualsAndHashCode(of = "idCategoria")//Solo usa el id para evitar ciclos
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nombre_categorial", nullable = false, unique = true)// Evita duplicados y valores nulos
    private String nombreCategorial;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)//Relacion con la clase producto
    private List<Producto> productos;

}
