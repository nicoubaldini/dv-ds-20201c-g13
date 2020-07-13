package ar.edu.davinci.dvds20201cg13;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.davinci.dvds20201cg13.controlador.rest.request.ClienteInsertRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.ClienteUpdateRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.OrdenInsertRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.OrdenItemInsertRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.OrdenItemUpdateRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.OrdenUpdateRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.ProductoInsertRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.request.ProductoUpdateRequest;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.ClienteResponse;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.OrdenClienteResponse;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.OrdenItemResponse;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.OrdenProductoResponse;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.OrdenResponse;
import ar.edu.davinci.dvds20201cg13.controlador.rest.response.ProductoResponse;
import ar.edu.davinci.dvds20201cg13.modelo.Cliente;
import ar.edu.davinci.dvds20201cg13.modelo.Orden;
import ar.edu.davinci.dvds20201cg13.modelo.OrdenItem;
import ar.edu.davinci.dvds20201cg13.modelo.Producto;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
public class OrikaConfiguration {
	
	private final Logger LOGGER = LoggerFactory.getLogger(OrikaConfiguration.class);

	private final ObjectMapper objectMapper;
	
	public OrikaConfiguration() {
		objectMapper = new ObjectMapper();
	}
	
	@Bean
	public MapperFacade mapper() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		
		mapperFactory.classMap(Cliente.class, ClienteResponse.class).byDefault().register();
		mapperFactory.classMap(Cliente.class, ClienteInsertRequest.class).byDefault().register();
		mapperFactory.classMap(Cliente.class, ClienteUpdateRequest.class).byDefault().register();
		mapperFactory.classMap(Cliente.class, OrdenClienteResponse.class).byDefault().register();

		mapperFactory.classMap(Producto.class, ProductoResponse.class).byDefault().register();
		mapperFactory.classMap(Producto.class, ProductoInsertRequest.class).byDefault().register();
		mapperFactory.classMap(Producto.class, ProductoUpdateRequest.class).byDefault().register();
		mapperFactory.classMap(Producto.class, OrdenProductoResponse.class).byDefault().register();

		mapperFactory.classMap(Orden.class, OrdenResponse.class)
		.customize(new CustomMapper<Orden, OrdenResponse>() {
			public void mapAtoB(final Orden orden, final OrdenResponse ordenResponse, final MappingContext context) {
				LOGGER.info(" #### Custom mapping to Orden and OrdenResponse #### ");
				OrdenClienteResponse cliente = new OrdenClienteResponse();
				cliente.setName(orden.getClient().getName());
				cliente.setLastName(orden.getClient().getLastName());
				ordenResponse.setCliente(cliente);
				ordenResponse.setId(orden.getId());
				ordenResponse.setDate(orden.getDate());
				List<OrdenItemResponse> items = new ArrayList<>();
				
				for (OrdenItem ordenItem : orden.getItems()) {
					OrdenProductoResponse producto = new OrdenProductoResponse();
					producto.setName(ordenItem.getProduct().getName());
					producto.setPrice(ordenItem.getProduct().getPrice());
					
					OrdenItemResponse ordenItemResponse = new OrdenItemResponse();
					ordenItemResponse.setId(ordenItem.getId());
					ordenItemResponse.setProducto(producto);
					ordenItemResponse.setQuantity(ordenItem.getQuantity());
					items.add(ordenItemResponse);
				}
				ordenResponse.setItems(items);
				
			}
		}).register();

		mapperFactory.classMap(OrdenItem.class, OrdenItemResponse.class).byDefault().register();

		mapperFactory.classMap(OrdenInsertRequest.class, Orden.class)
		.customize(new CustomMapper<OrdenInsertRequest, Orden>() {
			public void mapAtoB(final OrdenInsertRequest ordenInsertRequest, final Orden orden, final MappingContext context) {
				LOGGER.info(" #### Custom mapping to OrdenInsertRequest and Orden #### ");
				Cliente cliente = new Cliente();
				cliente.setId(ordenInsertRequest.getClienteId());
				orden.setClient(cliente);
				
				for (OrdenItemInsertRequest ordenItemInserRequest : ordenInsertRequest.getItems()) {
					Producto producto = new Producto();
					producto.setId(ordenItemInserRequest.getProductoId());
					
					OrdenItem ordenItem = new OrdenItem();
					ordenItem.setQuantity(ordenItemInserRequest.getQuantity());
					ordenItem.setProduct(producto);
					orden.agregarOrdenItem(ordenItem);
				}
			}
		}).register();
		
		mapperFactory.classMap(OrdenItemInsertRequest.class, OrdenItem.class).byDefault().register();
		mapperFactory.classMap(Orden.class, OrdenUpdateRequest.class).byDefault().register();
		mapperFactory.classMap(OrdenItem.class, OrdenItemUpdateRequest.class).byDefault().register();
		
		return mapperFactory.getMapperFacade();
	}
}
