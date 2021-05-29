package dan.tp2021.productos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.productos.domain.Pedido;
import dan.tp2021.productos.service.PedidoService;
import dan.tp2021.productos.service.dao.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Override
	public Pedido getById(Integer id) {
		return pedidoRepository.findById(id).get();
	}
	
}
