package com.se.astro;


import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.ZodiacSign;
import com.se.astro.user.model.enums.*;
import com.se.astro.user.repository.AstroUserRepository;
import com.se.astro.user.repository.ZodiacRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class AstroApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstroApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(AstroUserRepository userRepository, ZodiacRepository zodiacRepository){
		return  args -> {
			if (zodiacRepository.count() == 0) {
				zodiacRepository.save(new ZodiacSign("Aries", LocalDateTime.of(2024, 3, 21, 0, 0), LocalDateTime.of(2024, 4, 19, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Taurus", LocalDateTime.of(2024, 4, 20, 0, 0), LocalDateTime.of(2024, 5, 20, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Gemini", LocalDateTime.of(2024, 5, 21, 0, 0), LocalDateTime.of(2024, 6, 20, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Cancer", LocalDateTime.of(2024, 6, 21, 0, 0), LocalDateTime.of(2024, 7, 22, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Leo", LocalDateTime.of(2024, 7, 23, 0, 0), LocalDateTime.of(2024, 8, 22, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Virgo", LocalDateTime.of(2024, 8, 23, 0, 0), LocalDateTime.of(2024, 9, 22, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Libra", LocalDateTime.of(2024, 9, 23, 0, 0), LocalDateTime.of(2024, 10, 22, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Scorpio", LocalDateTime.of(2024, 10, 23, 0, 0), LocalDateTime.of(2024, 11, 21, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Sagittarius", LocalDateTime.of(2024, 11, 22, 0, 0), LocalDateTime.of(2024, 12, 21, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Capricorn", LocalDateTime.of(2024, 12, 22, 0, 0), LocalDateTime.of(2025, 1, 19, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Aquarius", LocalDateTime.of(2025, 1, 20, 0, 0), LocalDateTime.of(2025, 2, 18, 23, 59)));
				zodiacRepository.save(new ZodiacSign("Pisces", LocalDateTime.of(2025, 2, 19, 0, 0), LocalDateTime.of(2025, 3, 20, 23, 59)));
			}

			if (!userRepository.existsByEmail("radu.stefanescu@gmail.com")) {
				AstroUser radu = new AstroUser(
						"radus",
						"test123",
						"radu.stefanescu@gmail.com",
						LocalDateTime.of(2002, 2, 7, 0, 0),
						"Aquarius",
						Gender.MALE,
						new ArrayList<Gender>(Arrays.asList(Gender.FEMALE)),
						1.79,
						Nationality.ROMANIA,
						new ArrayList<Language>(Arrays.asList(Language.ROMANIAN, Language.ENGLISH, Language.SPANISH)),
						".",
						".",
						18,
						24,
						new ArrayList<>(Arrays.asList(Tag.GYMBRO)),
						AccountType.ADMIN);

				userRepository.save(radu);
			}
		};
	}
}
