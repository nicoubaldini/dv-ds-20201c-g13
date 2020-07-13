package ar.edu.davinci.dvds20201cg13.controlador.rest.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenUpdateRequest {
			
	private Long id;

	private Long clienteId;
	
	private List<OrdenItemUpdateRequest> items;
}
