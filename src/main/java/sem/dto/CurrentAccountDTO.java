package sem.dto;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrentAccountDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "Debe ingresar un telefono")
	@Min(value = 10, message = "El telefono debe tener 10 digitos!")
	@Pattern(regexp = "\\d{10}", message = "El telefono debe contener solo numeros")
	private String phone;
	@NotNull(message = "Debe ingresar un saldo")
	@DecimalMin("100")
	private double balance;
	@JsonIgnore
	private UserDTO user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

}
