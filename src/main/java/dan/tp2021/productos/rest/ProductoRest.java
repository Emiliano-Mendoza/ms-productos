package dan.tp2021.productos.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.service.ProductoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/producto")
public class ProductoRest {

	@Autowired
	ProductoService productoService;
	
	@PostMapping
	public ResponseEntity<Producto> crearProducto(@RequestBody Producto productoNuevo) throws Exception{
		try {
			return productoService.crearProducto(productoNuevo);
		}
		catch (Exception e) {
			throw new Exception("No se ha podido crear el producto");
		}
	}
	
	@PutMapping(path="{id}")
	public ResponseEntity<String> actualizarProducto(@PathVariable Integer id) throws Exception{
		try {
			Producto producto = productoService.getById(id);
			if(producto.getId() != null) {
				return productoService.actualizarProducto(producto);
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
		catch(Exception e) {
			throw new Exception("No se ha podido actualiar el producto");
		}
	}
	
	@GetMapping(path="{id}")
	public ResponseEntity<Producto> consultarInformacion(@PathVariable Integer id) throws Exception{
		try {
			Producto producto = productoService.getById(id);
			return ResponseEntity.ok(producto);
		}
		catch (Exception e) {
			throw new Exception("No se ha podido actualiar el producto");
		}
	}
	
	@GetMapping(path="consultarPorNombre/{nombre}")
	public ResponseEntity<Producto> consultarProductoPorNombre(@PathVariable String nombre) throws Exception{
		try {
			Producto producto = productoService.getByNombre(nombre);
			return ResponseEntity.ok(producto);
		}
		catch (Exception e) {
			throw new Exception("No se ha podido actualiar el producto");
		}
	}
	
	@GetMapping(path="consultarPorStock/{rango1}/{rango2}")
	public ResponseEntity<Producto> consultarProductoPorStock(@PathVariable Integer rango1,@PathVariable Integer rango2) throws Exception{
		try {
			Producto producto = productoService.getByRango(rango1, rango2);
			return ResponseEntity.ok(producto);
		}
		catch (Exception e) {
			throw new Exception("No se ha podido actualiar el producto");
		}
	}
	
	@GetMapping(path="consultarPorPrecio/{precio}")
	public ResponseEntity<Producto> consultarProductoPorPrecio(@PathVariable Double precio) throws Exception{
		try {
			Producto producto = productoService.getByPrecio(precio);
			return ResponseEntity.ok(producto);
		}
		catch (Exception e) {
			throw new Exception("No se ha podido actualiar el producto");
		}
	}
	
	
	@GetMapping
	public ResponseEntity<List<Producto>> listarProductos(){
		return ResponseEntity.ok( productoService.getAllProductos());
	}
	
	@DeleteMapping(path = "/id/{id}")
    public ResponseEntity<Producto> borrar(@PathVariable Integer id){
		productoService.eliminarProducto(id);
		return ResponseEntity.ok().build();
    }
}
