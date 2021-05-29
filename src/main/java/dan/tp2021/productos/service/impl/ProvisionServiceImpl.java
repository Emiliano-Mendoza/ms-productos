package dan.tp2021.productos.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.Producto;
import dan.tp2021.productos.domain.Provision;
import dan.tp2021.productos.service.ProvisionService;
import dan.tp2021.productos.service.dao.DetalleProvisionRepository;
import dan.tp2021.productos.service.dao.ProvisionRepository;

@Service
public class ProvisionServiceImpl implements ProvisionService{
	@Autowired
	ProvisionRepository provisionRepository;
	
	@Autowired
	DetalleProvisionRepository detalleRepository;

	@Override
	public Provision crearOrdenProvision(List<DetalleProvision> ListaDetalle) {
		List<DetalleProvision> nuevoDetalle = quitarProductosConOrdenesPrevias(ListaDetalle);
		
		if(!nuevoDetalle.isEmpty()) {
			Provision provision = new Provision();
			provision.setFechaProvision(new Date());
			provision.setDetalle(nuevoDetalle);
			
			provisionRepository.save(provision);
			this.guardarDetalle(nuevoDetalle, provision);
			return provision;
		}
		else {
			return null;
		}
	}

	private List<DetalleProvision> quitarProductosConOrdenesPrevias(List<DetalleProvision> listaDetalle) {
		List<Provision> provisiones = provisionRepository.findAll();
		
		List<Producto> productosNuevos = new ArrayList<Producto>();
		for (DetalleProvision detalle : listaDetalle) {
			productosNuevos.add(detalle.getProducto());
		}
		
		for (Provision provision : provisiones) {
			for(DetalleProvision detalle: provision.getDetalle())
			{
				if(productosNuevos.contains(detalle.getProducto())) {
					OptionalInt indexOpt = IntStream.range(0, listaDetalle.size())
												.filter(det -> listaDetalle.get(det).getProducto().getId().equals(detalle.getProducto().getId()))
												.findFirst(); 
					if (indexOpt.isPresent()) { 
						listaDetalle.remove(indexOpt.getAsInt());  
					}
				}
			}
		}
		return listaDetalle;
	}

	private void guardarDetalle(List<DetalleProvision> listaDetalle, Provision provision) {
		for (DetalleProvision detalle : listaDetalle) {
			detalle.setProvision(provision);
			detalleRepository.save(detalle);
		}
	}
}
