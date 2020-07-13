package ar.edu.davinci.dvds20201cg13.controlador.rest.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenProductoResponse {
	
	private String name;
	
	private BigDecimal price;


}
