package ar.edu.davinci.dvds20201cg13.servicio;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dvds20201cg13.modelo.Cliente;
import ar.edu.davinci.dvds20201cg13.modelo.Orden;
import ar.edu.davinci.dvds20201cg13.modelo.OrdenItem;
import ar.edu.davinci.dvds20201cg13.modelo.Producto;
import ar.edu.davinci.dvds20201cg13.repositorio.OrdenItemRepositorio;
import ar.edu.davinci.dvds20201cg13.repositorio.OrdenRepositorio;

@Service
public class OrdenServicio {

	private final Logger LOGGER = LoggerFactory.getLogger(OrdenServicio.class);

	private final OrdenRepositorio ordenRepositorio;

	private final OrdenItemRepositorio ordenItemRepositorio;
	
	private final ClienteServicio clienteServicio;

	private final ProductoServicio productoServicio;

	@Autowired
	public OrdenServicio(final OrdenRepositorio ordenRepositorio, 
			final OrdenItemRepositorio ordenItemRepositorio,
			final ClienteServicio clienteServicio,
			final ProductoServicio productoServicio) {
		this.ordenRepositorio = ordenRepositorio;
		this.ordenItemRepositorio = ordenItemRepositorio;
		this.clienteServicio = clienteServicio;
		this.productoServicio = productoServicio;
	}

	public Iterable<Orden> listarOrdens() {
		return ordenRepositorio.findAll();
	}
	
	public Page<Orden> listarOrden(Pageable pageable){
		return ordenRepositorio.findAll(pageable);
 	}

	public Orden buscarOrdenPorId(Long id) {
		Optional<Orden> productoOptional = ordenRepositorio.findById(id);
		return productoOptional.orElse(null);
	}

//	public List<Orden> buscarOrdenPorCliente(String nombre) {
//		LOGGER.info("buscarOrdenPorNombre: nombre: " +nombre);
//		
//		List<Orden> ordenes = ordenRepositorio.searchByCliente(nombre);
//		if (CollectionUtils.isNotEmpty(ordenes)) {
//			LOGGER.info("ordenes.size: " + ordenes.size());
//			return ordenes;
//		} else {
//			LOGGER.info("ordenes empty");
//			return (List<Orden>) CollectionUtils.EMPTY_COLLECTION;
//		}
//	}

	public Orden prepararOrdenAGrabar(Orden orden) {
		LOGGER.info("prepararOrdenAGrabar: orden: " +orden.toString());
		
		// se setea la fecha de creación de la orden antes de grabar
		orden.setDate(Calendar.getInstance().getTime());
		
		Cliente cliente = clienteServicio.buscarClientePorId(orden.getClient().getId());
		if (Objects.nonNull(cliente)) {
			orden.setClient(cliente);
		}
		
		for (OrdenItem item : orden.getItems()) {
			Producto producto = productoServicio.buscarProductoPorId(item.getProduct().getId());
			if (Objects.nonNull(producto)) {
				item.setProduct(producto);
			}	
		}
		LOGGER.info("prepararOrdenAGrabar: orden final: " +orden.toString());
		return grabarOrden(orden);
	}

	@Transactional
	public Orden grabarOrden(Orden orden) {
		LOGGER.info("grabarOrden: orden: " +orden.toString());
		Orden nuevaOrden =  null;
		try {
			nuevaOrden =  ordenRepositorio.save(orden);
//			for (OrdenItem item : orden.getItems()) {
//				item.setOrden(orden1);
//				ordenItemRepositorio.save(item);
//			}
		} catch (Exception e) {
			LOGGER.info("Mensaje: " + e.getMessage());
			LOGGER.info("Causa: " + e.getCause());
			e.printStackTrace();
		}
		return nuevaOrden;
	}

	public void borrarOrden(Long id) {
		ordenRepositorio.deleteById(id);
	}

	public Orden agregarOrdenItem(Orden orden, OrdenItem item) {
		orden.agregarOrdenItem(item);
		return grabarOrden(orden);
		
 	}

	public Orden borrarOrdenItem(Orden orden, OrdenItem item) {
		if (orden.contieneItem(item)) {
			orden.removerOrdenItem(item);
			return grabarOrden(orden);
		}
		return orden;
 	}
}

