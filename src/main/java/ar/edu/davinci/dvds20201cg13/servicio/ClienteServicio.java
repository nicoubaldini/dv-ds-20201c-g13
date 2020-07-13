package ar.edu.davinci.dvds20201cg13.servicio;


import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.dvds20201cg13.modelo.Cliente;
import ar.edu.davinci.dvds20201cg13.repositorio.ClienteRepositorio;

@Service
public class ClienteServicio {

	private final Logger LOGGER = LoggerFactory.getLogger(ClienteServicio.class);
	 
	
	private final ClienteRepositorio clienteRepositorio;

	@Autowired
	public ClienteServicio(final ClienteRepositorio clienteRepositorio) {
		this.clienteRepositorio = clienteRepositorio;
	}

	public List<Cliente> listarClientes() {
		return clienteRepositorio.findAll();
	}

	public Page<Cliente> listar(Pageable pageable) {
		LOGGER.info("Pagegable: offset: " + pageable.getOffset() + " - pageSize:" + pageable.getPageSize());
		return clienteRepositorio.findAll(pageable);
	}
	
	public Cliente buscarClientePorId(Long clienteId) {

		Optional<Cliente> cliente = clienteRepositorio.findById(clienteId);
		if (cliente.isPresent()) {
			return cliente.get();
		}
		return null;
	}
	
	public List<Cliente> buscarClienterPorApellido(String apellido) {
		LOGGER.info("buscarClienterPorApellido: apellido: " + apellido);
		List<Cliente> clientes = clienteRepositorio.searchByLastName(apellido);
		if (CollectionUtils.isNotEmpty(clientes)) {
			LOGGER.info("clientes.size: " + clientes.size());
			return clientes;
		} else {
			LOGGER.info("clientes empty");
			return (List<Cliente>) CollectionUtils.EMPTY_COLLECTION;
		}
	}

	public Cliente grabarCliente(Cliente cliente) {
		return clienteRepositorio.save(cliente);
	}


	
	public void borrarCliente(Long clienteId) {
		clienteRepositorio.deleteById(clienteId);
	}

}
