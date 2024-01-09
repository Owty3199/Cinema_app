package org.sid.cinema;

import org.sid.cinema.entities.Film;
import org.sid.cinema.service.ICenimaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner{
	@Autowired
	private ICenimaInitService  cenimaInitService;
	
	@Autowired
	private RepositoryRestConfiguration restConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restConfiguration.exposeIdsFor(Film.class);
		cenimaInitService.initCategories();
		cenimaInitService.initCinema();
		cenimaInitService.initFilms();
		cenimaInitService.initPlaces();
		cenimaInitService.initProjections();
		cenimaInitService.initSalles();
		cenimaInitService.initSeance();
		cenimaInitService.initTicket();
		cenimaInitService.initVilles();
		
	}

}
