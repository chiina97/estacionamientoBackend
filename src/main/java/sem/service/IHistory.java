package sem.service;

import java.util.List;

import sem.model.History;

public interface IHistory {

	public Iterable<History> findAll();

	public List<History> findById(Long id);

	public History save(History h);

}
