package dan.tp2021.productos.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.Producto;

public interface ProductoService {

	public ResponseEntity<Producto> crearProducto(Producto productoNuevo);

	public Producto getById(Integer id);

	public ResponseEntity<String> actualizarProducto(Producto producto);

	public Producto getByNombre(String nombre);

	public Producto getByRango(Integer rango1, Integer rango2);

	public Producto getByPrecio(Double precio);

	public void actualizarStockProductos(List<DetallePedido> detalle);

	public ResponseEntity<String> actualizarStockProducto(Producto producto);

}
