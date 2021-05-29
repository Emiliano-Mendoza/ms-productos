package dan.tp2021.productos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.DetallePedido;
import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.Pedido;
import dan.tp2021.productos.service.PedidoService;
import dan.tp2021.productos.service.ProductoService;
import dan.tp2021.productos.service.ProvisionService;
import dan.tp2021.productos.service.dao.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);
	
	@Autowired
	ProductoRepository productoRepository;
	
	@Autowired
	PedidoService pedidoService;
	
	@Autowired
	ProvisionService provisionService;
	
	@Autowired
	JmsTemplate jms;
	
	@Override
	public ResponseEntity<Producto> crearProducto(Producto productoNuevo) {
		if(verificarProducto(productoNuevo)) {
			productoRepository.save(productoNuevo);
			return ResponseEntity.ok(productoNuevo);
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@Override
	public Producto getById(Integer id) {
		Optional<Producto> producto = productoRepository.findById(id);
		if (producto.isPresent()) {
			return producto.get();
		}
		else {
			return null;
		}
	}
	
	@Override
	public Producto getByNombre(String nombre) {
		return productoRepository.findAll()
					.stream()
					.filter(prod -> prod.getNombre().equals(nombre))
					.findFirst()
					.get();
	}

	@Override
	public Producto getByRango(Integer rango1, Integer rango2) {
		return productoRepository.findAll()
				.stream()
				.filter(prod -> prod.getStockActual() >= rango1 && prod.getStockActual() < rango2)
				.findFirst()
				.get();
	}

	@Override
	public Producto getByPrecio(Double precio) {
		return productoRepository.findAll()
				.stream()
				.filter(prod -> prod.getPrecio() == precio)
				.findFirst()
				.get();
	}

	@Override
	public ResponseEntity<String> actualizarProducto(Producto producto) {
		productoRepository.save(producto);
		return ResponseEntity.ok("producto: " + producto.getNombre() + " actualizado");
	}
	
	@Override
	public ResponseEntity<String> actualizarStockProducto(Producto producto) {
		Integer stockActual = producto.getStockActual();
		producto.setStockActual(stockActual-1);
		
		productoRepository.save(producto);
		return ResponseEntity.ok("producto: " + producto.getNombre() + " actualizado");
	}
	
	@Override
	public void actualizarStockProductos(List<DetallePedido> detalles) {
		List<DetalleProvision> listaProvision = new ArrayList<DetalleProvision>();
		
		for (DetallePedido detalle : detalles) {
	    	this.actualizarStockProducto(detalle.getProducto());
	    
	    	Integer stockActual = detalle.getProducto().getStockActual();
	    	
	    	if (stockActual <= detalle.getProducto().getStockMinimo()) {
				DetalleProvision detalleProvision = new DetalleProvision();
				detalleProvision.setCantidad(detalle.getProducto().getStockMinimo() - stockActual);
				detalleProvision.setProducto(detalle.getProducto());
				listaProvision.add(detalleProvision);
			}
	    }
		
		if(!listaProvision.isEmpty()) {
    		provisionService.crearOrdenProvision(listaProvision);
    	}
	}
	
	@Transactional
	@JmsListener(destination = "COLA_PEDIDOS")
	public void handle(Message msg) throws JmsException{
		logger.info("LLEGO MENSAJE {}",msg);
		try {
		    TextMessage textMessage = (TextMessage) msg;
		    String mensaje = textMessage.getText();
		    System.out.println("Received: " + mensaje);
		    
		    if (mensaje.contains("Pedido confirmado")) {
		    	Integer index = textMessage.getText().toString().indexOf("id:", 0);
		    	String idPedido = textMessage.getText().toString().substring(index+3);
		    	Pedido pedido = pedidoService.getById(Integer.parseInt(idPedido));
		    	
		    	this.actualizarStockProductos(pedido.getDetalle());
		    }
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private boolean verificarProducto(Producto productoNuevo) {	
		if(productoNuevo.getNombre() == null || productoNuevo.getNombre().isEmpty()) {
			return false;
		}
		if(productoNuevo.getPrecio() == null) {
			return false;
		}
		if(productoNuevo.getStockActual() == null) {
			return false;
		}
		if(productoNuevo.getStockMinimo() == null) {
			return false;
		}
		if(productoNuevo.getDescripcion() == null || productoNuevo.getDescripcion().isEmpty()) {
			return false;
		}
		
		return true;
	}
}
