package sem.service;

import java.util.ArrayList;

import sem.model.Holiday;

public interface IHoliday {
	public  ArrayList<Holiday> findAll();

	public Holiday save(Holiday f);

}
