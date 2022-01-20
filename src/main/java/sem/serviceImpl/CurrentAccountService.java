package sem.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.model.CurrentAccount;
import sem.repository.CurrentAccountRepository;
import sem.service.ICurrentAccount;

@Service
public class CurrentAccountService implements ICurrentAccount {

	@Autowired
	private CurrentAccountRepository currentAccountRepo; // inyectamos

	@Override
	@Transactional(readOnly = true)
	public Iterable<CurrentAccount> findAll() {
		return currentAccountRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CurrentAccount> findAll(Pageable pageable) {
		return currentAccountRepo.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CurrentAccount> findById(Long id) {
		return currentAccountRepo.findById(id);
	}

	@Override
	@Transactional
	public CurrentAccount save(CurrentAccount account) {
		return currentAccountRepo.save(account);
	}

	@Override
	@Transactional
	public CurrentAccount update(CurrentAccount accountRequest, Long id) {
		Optional<CurrentAccount> account = currentAccountRepo.findById(id);
		if (account.isPresent()) {

			account.get().setBalance(accountRequest.getBalance());
			account.get().setPhone(accountRequest.getPhone());

			return currentAccountRepo.save(account.get());

		} else
			return null;
	}

	@Transactional(readOnly = true)
	public Optional<CurrentAccount> findByUser(Long id) {
		return currentAccountRepo.findByUser(id);
	}

}
