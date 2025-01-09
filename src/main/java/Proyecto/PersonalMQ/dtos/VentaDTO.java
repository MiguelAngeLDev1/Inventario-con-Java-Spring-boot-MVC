package Proyecto.PersonalMQ.dtos;

import Proyecto.PersonalMQ.models.Producto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

import java.util.ArrayList;

import java.util.List;


@Data
public class VentaDTO {


    private Long id;
    private List<ProductoVentaDTO> detallesVenta = new ArrayList<>();
    private double total;//Total de la venta
    private String message;
}


