package com.zim0101.authvault;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.AuthProvider;
import com.zim0101.authvault.model.enums.Role;
import com.zim0101.authvault.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
public class AuthvaultApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthvaultApplication(AccountRepository accountRepository,
								PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthvaultApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Account account = new Account();
		account.setName("Mr. Admin");
		account.setUsername("admin");
		account.setEmail("admin@gmail.com");
		account.setPassword(passwordEncoder.encode("admin"));
		account.setRoles(Set.of(Role.ADMIN));
		account.setEmailVerified(true);
		account.setAuthProvider(AuthProvider.LOCAL);

		accountRepository.save(account);
	}

}
