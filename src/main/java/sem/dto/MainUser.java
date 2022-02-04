package sem.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import sem.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MainUser implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id; // chquear si va
	private String phone;
	private String password;
	private String mail;
	private String username;
	private Collection<? extends GrantedAuthority> authorities;

	public MainUser(Long id, String phone, String password, String mail, String username,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.phone = phone;
		this.password = password;
		this.mail = mail;
		this.username = username;
		this.authorities = authorities;
	}

	public static MainUser build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getRoleName().name())).collect(Collectors.toList());
		return new MainUser(user.getId(), user.getPhone(), user.getPassword(), user.getMail(), user.getUsername(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return phone;
	}

	public String getUser() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getMail() {
		return mail;
	}

}
