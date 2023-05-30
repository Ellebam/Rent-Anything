package io.bootify.backend.service;

import io.bootify.backend.domain.Rental;
import io.bootify.backend.model.RentalDTO;
import io.bootify.backend.repos.RentalRepository;
import io.bootify.backend.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(final RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<RentalDTO> findAll() {
        final List<Rental> rentals = rentalRepository.findAll(Sort.by("id"));
        return rentals.stream()
                .map((rental) -> mapToDTO(rental, new RentalDTO()))
                .toList();
    }

    public RentalDTO get(final Long id) {
        return rentalRepository.findById(id)
                .map((rental) -> mapToDTO(rental, new RentalDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RentalDTO rentalDTO) {
        final Rental rental = new Rental();
        mapToEntity(rentalDTO, rental);
        return rentalRepository.save(rental).getId();
    }

    public void update(final Long id, final RentalDTO rentalDTO) {
        final Rental rental = rentalRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rentalDTO, rental);
        rentalRepository.save(rental);
    }

    public void delete(final Long id) {
        rentalRepository.deleteById(id);
    }

    private RentalDTO mapToDTO(final Rental rental, final RentalDTO rentalDTO) {
        rentalDTO.setId(rental.getId());
        rentalDTO.setUserId(rental.getUserId());
        rentalDTO.setOfferId(rental.getOfferId());
        rentalDTO.setStartDate(rental.getStartDate());
        rentalDTO.setEndDate(rental.getEndDate());
        return rentalDTO;
    }

    private Rental mapToEntity(final RentalDTO rentalDTO, final Rental rental) {
        rental.setUserId(rentalDTO.getUserId());
        rental.setOfferId(rentalDTO.getOfferId());
        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        return rental;
    }

}
