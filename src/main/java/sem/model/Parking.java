package sem.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import sem.dto.Message;
import sem.dto.TimePriceDTO;

@Entity
@Table(name = "parking")
public class Parking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String patent;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "amount")
	private double amount;
	@Column(name = "startedParking")
	private boolean startedParking;

	@OneToOne()
	private User user;

	public Parking() {
	};

	public Parking(String patent, String startTime, double amount, boolean startedParking, User user) {
		super();
		this.patent = patent;
		this.startTime = startTime;
		this.amount = amount;
		this.startedParking = startedParking;
		this.user = user;
	}

	public static Message validations(City city, Iterable<Holiday> holidays) {
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
					return (new Message("No puede operar los fines de semana"));
			} else {
				return (new Message("No puede estacionar fuera de las fechas habiles"));
			}
		} else {
			return (new Message("No puede estacionar fuera del horario operable de " + city.getStartTime() + " a "
					+ city.getEndTime() + "hs "));
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
