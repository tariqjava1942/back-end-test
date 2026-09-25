package com.virginholidays.backend.test.service;


import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The FlightInfoServiceImpl unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoServiceImplTest {
	
	@Mock
    private FlightInfoRepository flightInfoRepository;

    private FlightInfoServiceImpl flightInfoService;

    @BeforeEach
    void setUp() {
        flightInfoService = new FlightInfoServiceImpl(flightInfoRepository);
    }
    
    
    @Test
    void shouldReturnFlightsForGivenDaySortedByDepartureTime() {

        Flight morning = new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.MONDAY));
        Flight afternoon = new Flight(LocalTime.of(15, 35), "Las Vegas", "LAS", "VS044", List.of(DayOfWeek.MONDAY));
        Flight wrongDay = new Flight(LocalTime.of(12, 0), "Cancun", "CUN", "VS093", List.of(DayOfWeek.TUESDAY));

        when(flightInfoRepository.findAll())
                .thenReturn(CompletableFuture.completedFuture(Optional.of(List.of(afternoon, morning, wrongDay))));

        Optional<List<Flight>> result = flightInfoService
                .findFlightByDate(LocalDate.of(2024, 1, 1)) 
                .toCompletableFuture()
                .join();

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), contains(morning, afternoon));
    }

    @Test
    void shouldReturnEmptyOptionalWhenRepositoryReturnsEmpty() {

        when(flightInfoRepository.findAll())
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        Optional<List<Flight>> result = flightInfoService
                .findFlightByDate(LocalDate.of(2024, 1, 1))
                .toCompletableFuture()
                .join();

        assertThat(result.isEmpty(), is(true));
    }
}