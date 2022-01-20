package sem.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.model.History;
import sem.repository.HistoryRepository;
import sem.service.IHistory;

@Service
public class HistoryService implements IHistory {

	@Autowired
	HistoryRepository historyRepo;

	@Override
	@Transactional(readOnly = true)
	public Iterable<History> findAll() {
		return historyRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<History> findById(Long id) {
		return historyRepo.existsByCurrentAccount(id);
	}

	@Override
	public History save(History h) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
		String dateString = sdf.format(new Date());

		h.setDate(dateString);
		return historyRepo.save(h);
	}

}
