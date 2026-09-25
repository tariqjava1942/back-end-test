package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The FlightInfoResource unit tests
 *
 * @author Geoff Perks
 */

@ExtendWith(MockitoExtension.class)
class FlightInfoResourceTest {

	@Mock
    private FlightInfoService flightInfoService;

    private FlightInfoResource flightInfoResource;

    @BeforeEach
    void setUp() {
        flightInfoResource = new FlightInfoResource(flightInfoService);
    }

    @Test
    void shouldReturnOkWithFlightsWhenServiceReturnsData() {

        Flight flight = new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.MONDAY));

        when(flightInfoService.findFlightByDate(LocalDate.of(2024, 1, 1)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(List.of(flight))));

        ResponseEntity<?> response = flightInfoResource
                .getResults("2024-01-01")
                .toCompletableFuture()
                .join();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(List.of(flight)));
    }

    @Test
    void shouldReturnNoContentWhenServiceReturnsEmpty() {

        when(flightInfoService.findFlightByDate(LocalDate.of(2024, 1, 1)))
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        ResponseEntity<?> response = flightInfoResource
                .getResults("2024-01-01")
                .toCompletableFuture()
                .join();

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }
}

