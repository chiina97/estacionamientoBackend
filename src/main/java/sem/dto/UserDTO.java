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
	@NotNull(message = "{user.password.notNull}")
	@Size(min = 4, message = "{user.password.notSize}")
	private String password;
	@NotNull(message = "{user.email.notNull}")
	@NotBlank(message = "{user.email.notBlank}")
	@Email(message = "{user.email.notValid}")
	private String mail;
	@NotNull(message = "{user.phone.notNull}")
	@NotBlank(message = "{user.phone.notBlank}")
	@Size(min = 10, max = 10, message = "{user.phone.notSize}")
	@Pattern(regexp = "[0-9]+", message = "{user.phone.notNumber}")
	private String phone;
	@NotNull(message = "{user.username.notNull}")
	@NotBlank(message = "{user.username.notBlank}")
	private String username;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
