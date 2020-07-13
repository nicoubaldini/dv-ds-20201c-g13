package ar.edu.davinci.dvds20201cg13.controlador.rest.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenItemUpdateRequest {

	private Long id;

	private Long productoId;
	
	private BigDecimal quantity;

}
