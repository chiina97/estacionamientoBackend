package sem.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "Debe ingresar una clave")
	@Size(min = 4, message = "La clave debe tener al menos 4 digitos")
	private String password;
	@NotNull(message = "Debe ingresar un correo")
	@NotBlank(message = "el correo NO puede ser vacio")
	@Email(message = "el correo debe tener el formato texto@texto.texto")
	private String mail;
	@NotNull(message = "Debe ingresar un telefono")
	@NotBlank(message = "El telefono NO puede ser vacio")
	@Size(min = 10, max = 10, message = "El telefono debe tener 10 digitos")
	@Pattern(regexp = "\\d{10}", message = "El telefono debe contener solo numeros")
	private String phone;
	// roles:
	private Set<String> roles = new HashSet<>();

	@JsonIgnore
	private List<PatentDTO> patents;

	private CurrentAccountDTO currentAccount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public List<PatentDTO> getPatents() {
		return patents;
	}

	public void setPatents(List<PatentDTO> patents) {
		this.patents = patents;
	}

	public CurrentAccountDTO getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(CurrentAccountDTO currentAccounts) {
		this.currentAccount = currentAccounts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
