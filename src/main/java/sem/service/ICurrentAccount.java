package sem.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sem.model.CurrentAccount;

public interface ICurrentAccount {

	public Iterable<CurrentAccount> findAll();

	public Page<CurrentAccount> findAll(Pageable pageable);

	public Optional<CurrentAccount> findById(Long id);

	public CurrentAccount save(CurrentAccount cc);

	public CurrentAccount update(CurrentAccount cc, Long id);

}
