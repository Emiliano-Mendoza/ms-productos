package dan.tp2021.productos.service;

import java.util.List;

import dan.tp2021.productos.domain.DetalleProvision;
import dan.tp2021.productos.domain.Provision;

public interface ProvisionService {
	public Provision crearOrdenProvision(List<DetalleProvision> detalle);
}
