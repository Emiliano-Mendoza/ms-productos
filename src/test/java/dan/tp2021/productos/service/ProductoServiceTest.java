package dan.tp2021.productos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.service.dao.ProductoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductoServiceTest {
	@Autowired
	ProductoService productoService;
	
	@MockBean
	ProductoRepository productoRepository;

	@Test
	public void crearProductoOk() {
		Producto producto = new Producto();
		producto.setId(1);
		producto.setNombre("producto test");
		producto.setPrecio(1.0);
		producto.setStockActual(10);
		producto.setStockMinimo(5);
		producto.setDescripcion("producto para test unitario");
		
		when(productoRepository.save(any(Producto.class))).thenReturn(producto);
		
		ResponseEntity<Producto> productoResultado = productoService.crearProducto(producto);
		
		Assertions.assertEquals(productoResultado.getStatusCodeValue(), 200);
		Assertions.assertEquals(productoResultado.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void crearProductoShouldFail() {
		Producto producto = new Producto();
		producto.setId(1);
		producto.setNombre("producto test");
		producto.setStockActual(10);
		producto.setStockMinimo(5);
		
		when(productoRepository.save(any(Producto.class))).thenReturn(producto);
		
		ResponseEntity<Producto> productoResultado = productoService.crearProducto(producto);
		
		Assertions.assertEquals(productoResultado.getStatusCodeValue(), 204);
		Assertions.assertEquals(productoResultado.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void getByIdOk() {
		Producto prod = new Producto();
		prod.setId(1);
		prod.setNombre("producto test");
		prod.setPrecio(1.0);
		prod.setStockActual(10);
		prod.setStockMinimo(5);
		prod.setDescripcion("producto para test unitario");
		Optional<Producto> producto = Optional.of(prod);
		
		when(productoRepository.findById(1)).thenReturn(producto);
		
		Producto productoResultado = productoService.getById(1);
		
		Assertions.assertEquals(productoResultado.getId(), producto.get().getId());
		Assertions.assertEquals(productoResultado.getNombre(), producto.get().getNombre());
		Assertions.assertEquals(productoResultado.getPrecio(), producto.get().getPrecio());
		Assertions.assertEquals(productoResultado.getStockActual(), producto.get().getStockActual());
		Assertions.assertEquals(productoResultado.getStockMinimo(), producto.get().getStockMinimo());
		Assertions.assertEquals(productoResultado.getDescripcion(), producto.get().getDescripcion());
	}
	
	@Test
	public void getByIdShouldFail() {
		when(productoRepository.findById(1)).thenReturn(Optional.empty());
		
		Producto productoResultado = productoService.getById(1);
		
		Assertions.assertEquals(productoResultado, null);
	}
	
	@Test
	public void actualizarStockProductoOk() {
		Producto producto = new Producto();
		producto.setId(1);
		producto.setNombre("producto test");
		producto.setPrecio(1.0);
		producto.setStockActual(10);
		producto.setStockMinimo(5);
		producto.setDescripcion("producto para test unitario");
		
		Producto producto2 = new Producto();
		producto2.setId(1);
		producto2.setNombre("producto test");
		producto2.setPrecio(1.0);
		producto2.setStockActual(9);
		producto2.setStockMinimo(5);
		producto2.setDescripcion("producto para test unitario");
		
		when(productoRepository.save(any(Producto.class))).thenReturn(producto2);
		
		ResponseEntity<String> resultado = productoService.actualizarStockProducto(producto);
		
		Assertions.assertEquals(resultado.getStatusCodeValue(), 200);
		Assertions.assertEquals(resultado.getStatusCode(), HttpStatus.OK);	
	}
}
