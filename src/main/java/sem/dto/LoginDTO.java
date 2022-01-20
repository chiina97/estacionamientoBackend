package sem.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotNull(message = "Debe ingresar una clave")
	private String password;
	@NotNull(message = "Debe ingresar un telefono")
	@NotBlank(message = "El telefono NO puede ser vacio")
	@Size(min = 10, max = 10, message = "El telefono debe tener 10 digitos")
	@Pattern(regexp = "\\d{10}", message = "El telefono debe contener solo numeros")
	private String phone;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
