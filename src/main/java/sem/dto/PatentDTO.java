package sem.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import sem.model.Parking;

public class PatentDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "")
	@NotBlank(message = "la patente NO puede ser vacio")
	@Pattern(regexp = "([a-zA-Z]{3}\\d{3})|([a-zA-Z]{2}\\d{3}[a-zA-Z]{2})", message = "la patente debe tener el formato AAA999 o AA000AA")
	private String patent;
	// probando:
	@NotNull(message = "La patente debe pertenecer a un usuario")
	private UserDTO user;

	private List<Parking> parkings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPatent() {
		return patent;
	}

	public void setPatent(String patent) {
		this.patent = patent;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public List<Parking> getParkings() {
		return parkings;
	}

	public void setParkings(List<Parking> parkings) {
		this.parkings = parkings;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
