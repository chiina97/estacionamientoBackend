package sem.service;

import sem.model.Holiday;

public interface IHoliday {
	public Iterable<Holiday> findAll();

	public Holiday save(Holiday f);

}
