package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.util.Comparator;

/**
 * The service implementation of FlightInfoService
 *
 * @author Geoff Perks
 */
@Service
public class FlightInfoServiceImpl implements FlightInfoService {

    private final FlightInfoRepository flightInfoRepository;

    /**
     * The constructor
     *
     * @param flightInfoRepository the flightInfoRepository
     */
    public FlightInfoServiceImpl(FlightInfoRepository flightInfoRepository) {
        this.flightInfoRepository = flightInfoRepository;
    }

    @Override
    public CompletionStage<Optional<List<Flight>>> findFlightByDate(LocalDate outboundDate) {

        
    	 return flightInfoRepository.findAll().thenApply(maybeFlights -> {

    	        if (maybeFlights.isEmpty()) {
    	            return Optional.<List<Flight>>empty();
    	        }

    	        DayOfWeek dayOfWeek = outboundDate.getDayOfWeek();

    	        List<Flight> flights = maybeFlights.get().stream()
    	                .filter(flight -> flight.days().contains(dayOfWeek))
    	                .sorted(Comparator.comparing(Flight::departureTime))
    	                .toList();

    	        return Optional.of(flights);
    	    });
    }
}
