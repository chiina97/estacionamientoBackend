package sem.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.model.Patent;
import sem.repository.PatentRepository;
import sem.service.IPatent;

@Service
public class PatentService implements IPatent {

	@Autowired
	private PatentRepository patentRepo; // inyectamos

	@Override
	@Transactional(readOnly = true)
	public Iterable<Patent> findAll() {
		return patentRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Iterable<Patent> findAllByUser(Long id) {
		return patentRepo.findAllPatentsByUser(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Patent> findAll(Pageable pageable) {
		return patentRepo.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Patent existsByPatentAndUser(String patent, Long idUser) {
		return patentRepo.existsByPatentAndUser(patent, idUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Patent> findById(Long id) {
		return patentRepo.findById(id);
	}

	@Override
	@Transactional
	public Patent save(Patent patent) {
		return patentRepo.save(patent);
	}

	@Override
	@Transactional
	public Patent update(Patent patentRequest, Long id) {
		Optional<Patent> patent = patentRepo.findById(id);
		if (patent.isPresent()) {
			patent.get().setPatent(patentRequest.getPatent());
			patent.get().setUser(patentRequest.getUser());
			return patentRepo.save(patent.get());
		} else
			return null;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		patentRepo.deleteById(id);

	}

}
