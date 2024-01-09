package org.sid.cinema.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
//import jakarta.transaction.Transactional;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICenimaInitService{
	@Autowired
	private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
	private SalleRepository salleRepository;
    @Autowired
	private PlaceRepository placeRepository;
    @Autowired
	private SeanceRepository seanceRepository;
    @Autowired
	private FilmRepository filmRepository;
    @Autowired
	private ProjectionRepository projectionRepository;
    @Autowired
	private CategoryRepository categorieRepository;
    @Autowired
	private TicketRepository ticketRepository;
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(nameVille->{
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinema() {
		villeRepository.findAll().forEach(v->{
			Stream.of("Megarama","Sahra","IMAX","FONON","CHAHRAZAD","DAOULIZ")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)Math.random()*7);
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0; i<cinema.getNombreSalles();i++) {
				Salle salle = new Salle();
				salle.setName("Salle "+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(20 +(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}
	
	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0; i<salle.getNombrePlace();i++) {
				Place place = new Place();
				place.setNumeroPlace(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
				}
		});
			
	}
	@Override
	public void initSeance() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		Stream.of("12:00","14:00","17:00","19:00","21:00").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(df.parse(s));
				seanceRepository.save(seance);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
		
	}

	

	@Override
	public void initCategories() {
		Stream.of("Action","Drama","Ramantic","Histoire").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
			
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("Prison break","Spider Man", "Aeron Man","The Exandables","Breaking bad","The walking dead","Banshee1","Banshee2","Banshee3").forEach(titreFilm->{
			Film film=new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", ""+".jpg"));
			film.setCategories(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
	
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] {30,50,60,70,100,120};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinema().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index = new Random().nextInt(films.size());
					Film film=films.get(index);
					
						seanceRepository.findAll().forEach(seance->{
	Projection projection = new Projection();
	projection.setDateProjection(new Date());
	projection.setFilm(film);
	projection.setPrix(prices[new Random().nextInt(prices.length)]);
	projection.setSalle(salle);
	projection.setSeance(seance);
	projectionRepository.save(projection);
	
});					
				});
			});
			
		});
		
	}

	@Override
	public void initTicket() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
		
	}
	
}
