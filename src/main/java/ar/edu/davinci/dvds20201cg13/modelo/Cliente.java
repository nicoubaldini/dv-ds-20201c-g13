package ar.edu.davinci.dvds20201cg13.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="clientes", uniqueConstraints={@UniqueConstraint(columnNames={"cli_email"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cli_id")
	private Long id;
	
	@Column(name = "cli_nombre")
	@NotEmpty(message = "*Por favor ingrese su nombre")
	private String name;
	
	@Column(name = "cli_apellido")
	@NotEmpty(message = "*Por favor ingrese su apellido")
	private String lastName;
	
	@Column(name = "cli_email")
	@NotEmpty(message = "* Por favor ingrese su email")
	@Email(message = "Por favor ingrese un email válido")
	private String email;
	
	@Column(name = "cli_password")
	private String password;

}
