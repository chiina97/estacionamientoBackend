package sem.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.OneToOne;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import sem.model.City;
import sem.model.Holiday;

@Component
public class ParkingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String patent;
	private String startTime;
	private double amount;
	private boolean startedParking;
	
	

	@OneToOne()
	private UserDTO user;
	
	private MessageSource msg;
	
	
	public Message validations(City city, Iterable<Holiday> holidays) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		String date = sdf.format(new Date());
		Date fullDate = new Date();

		@SuppressWarnings("deprecation")
		int startTime = fullDate.getHours();

		String startTimeCity = city.getStartTime().split(":")[0];
		String endTimeCity = city.getEndTime().split(":")[0];

		if ((startTime >= Integer.valueOf(startTimeCity)) && (startTime < Integer.valueOf(endTimeCity))) {
			if (!isNonWorkingDate(date, holidays)) {
				if (!isWeekend(fullDate.toString())) {
					return null;
				} else
					return (new Message(msg.getMessage("parking.notValid.theWeekend", null, LocaleContextHolder.getLocale())));
			} else {
				return (new Message(msg.getMessage("parking.notValid.workingDays", null, LocaleContextHolder.getLocale())));
			}
		} else {
			return (new Message(msg.getMessage("city.notValid.operatingHours", new String[]{city.getStartTime(),city.getEndTime()}, LocaleContextHolder.getLocale())));
		}

	}

	public static boolean isWeekend(String date) {
		String today = date.split(" ")[0];
		return (today.equals("Sun") || (today.equals("Sat")));

	}

	public static boolean isNonWorkingDate(String date, Iterable<Holiday> holidays) {
		for (Holiday h : holidays) {
			if (h.getDate().equals(date)) {
				return true;
			}
		}
		return false;

	}
	
	public TimePriceDTO getCurrentPaymentDetails(City city) {
	@SuppressWarnings("deprecation")
	Date startDate = new Date(this.getStartTime());
	Date currentDate = new Date();

	// tiempo trasncurrido (hora actual - hora inicio):
	Long timeElapsed = currentDate.getTime() - startDate.getTime();

	// lo paso a segundos:
	double seconds = timeElapsed / 1000;
	double hour = Math.floor(seconds / 3600);

	double rest = Math.floor(((seconds % 3600) / 60) % 15);
	double minutes = Math.floor(((seconds % 3600) / 60) / 15);
	double valueByFraction = city.getValueByHour() / 4;


	if ((rest == 0) && (minutes != 0)) {

		// si pasaron extactamente de a 15 minutos
		return new TimePriceDTO(this.getPatent(),hour,Math.floor((seconds % 3600) /60),(minutes * valueByFraction) + (hour * city.getValueByHour()));

	} else {

		// si pasa de a 15 minutos y pico ,te cobra los minutos que pasaron como si
		// fueran 15
		double account = (minutes * valueByFraction) + (hour * city.getValueByHour()) + valueByFraction;
		return new TimePriceDTO(this.getPatent(),hour,Math.floor((seconds % 3600) /60),account);

	}

}

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isStartedParking() {
		return startedParking;
	}

	public void setStartedParking(boolean startedParking) {
		this.startedParking = startedParking;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setMsg(MessageSource msg) {
		this.msg = msg;
	}
	
	public MessageSource getMsg() {
		return msg;
	}

	

}
