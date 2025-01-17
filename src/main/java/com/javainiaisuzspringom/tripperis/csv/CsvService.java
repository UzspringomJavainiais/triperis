package com.javainiaisuzspringom.tripperis.csv;

import com.javainiaisuzspringom.tripperis.domain.Trip;
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

        String[] headers = new String[]{
                "name", "description",
//                "status",
                "dateFrom", "dateTo", "accounts", "organizers",
                "checklistItems"};
        List<String[]> csvLines = new ArrayList<>();

        List<Trip> trips = tripService.getAllTrips();
        trips.forEach(trip -> csvLines.add(new String[]{
                trip.getName(),
                trip.getDescription(),
//                trip.getStatus().toString(),
                trip.getDateFrom().toString(),
                trip.getDateTo().toString(),
                trip.getAccounts().toString(),
                trip.getOrganizers().toString(),
                trip.getChecklistItems().toString(),
        }));

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
