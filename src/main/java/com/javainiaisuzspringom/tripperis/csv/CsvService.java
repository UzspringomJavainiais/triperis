package com.javainiaisuzspringom.tripperis.csv;

import com.javainiaisuzspringom.tripperis.domain.Trip;
import com.javainiaisuzspringom.tripperis.domain.TripStep;
import com.javainiaisuzspringom.tripperis.dto.entity.TripDTO;
import com.javainiaisuzspringom.tripperis.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private TripService tripService;

    @Autowired
    private CsvWriter csvWriter;

    public void createTripsCsv(HttpServletResponse response) {
        List<Trip> trips = tripService.getAllTrips();

        String[] headers = new String[]{"name", "description"};

        List<String[]> csvLines = new ArrayList<>();
        trips.forEach(trip -> csvLines.add(new String[]{trip.getName(), trip.getDescription()}));

        createCsv(headers, csvLines, response);
    }

    private void createCsv(String[] headers, List<String[]> csvLines, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            csvWriter.writeDataToFile(headers, csvLines, writer);
        } catch (IOException e) {
            throw new RuntimeException("err.csv.creation", e);
        }
    }
}
