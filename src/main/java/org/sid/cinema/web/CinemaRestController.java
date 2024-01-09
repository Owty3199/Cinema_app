package org.sid.cinema.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private FilmRepository filmRepository;
	
	@GetMapping(path="/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
	public byte [] image (@PathVariable (name = "id")Long id) throws Exception{
		Film f = filmRepository.findById(id).get();
		String photoName= f.getPhoto();
		File file = new File(System.getProperty("user.home")+"/cinemas/images"+photoName+".jpg");
		Path path = Paths.get(file.toURI());
		return Files.readAllBytes(path);
		
		}
	
	
	@PostMapping("/payerTickets")
	@Transactional
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
		List<Ticket> listTickets = new ArrayList<>();
		ticketForm.getTickets().forEach(idTicket->{
			Ticket ticket = ticketRepository.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomClient());
			ticket.setReserve(true);
			ticketRepository.save(ticket);
		});
		return listTickets;
	}
}

@Data
class TicketForm{
	public List<Long> getTickets() {
		return tickets;
	}
	public void setTickets(List<Long> tickets) {
		this.tickets = tickets;
	}
	public String getNomClient() {
		return nomClient;
	}
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}
	private List<Long> tickets = new ArrayList<>();
	private String nomClient;
}
	
