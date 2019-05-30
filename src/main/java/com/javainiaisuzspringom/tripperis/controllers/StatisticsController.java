package com.javainiaisuzspringom.tripperis.controllers;


import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsResult;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsResultMapper;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsTripPrice;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsTripPriceMapper;
import com.javainiaisuzspringom.tripperis.repositories.StatisticsRepository;
import com.javainiaisuzspringom.tripperis.repositories.TripRepository;
import com.javainiaisuzspringom.tripperis.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
public class StatisticsController {

    @Autowired
    TripRepository tripRepository;

    @Autowired
    StatisticsRepository statisticsRepository;

    @GetMapping("/api/statistics/orderByDuration/{orderBy}")
    public List<StatisticsResult> getTripsByDuration(@PathVariable String orderBy) {
        if (orderBy.equals("ASC")) {
            return statisticsRepository.getTripsByDurationOrderASC();
        } else {
            return statisticsRepository.getTripsByDurationOrderDESC();
        }
    }

    @GetMapping("/api/statistics/orderByPrice/{orderBy}")
    public List<StatisticsTripPrice> getTripsByPrice(@PathVariable String orderBy) {
        if (orderBy.equals("ASC")) {
            return statisticsRepository.getTripsByPriceASC();
        } else {
            return statisticsRepository.getTripsByPriceDESC();
        }
    }

    @GetMapping("/api/statistics/tripCountByEmployee/{orderBy}")
    public List<StatisticsTripPrice> getTripCountByEmployee(@PathVariable String orderBy) {
        if (orderBy.equals("ASC")) {
            return statisticsRepository.getTripCountByEmployeeASC();
        } else {
            return statisticsRepository.getTripCountByEmployeeDESC();
        }
    }

    @GetMapping("/api/statistics/tripCountByPeriod")
    public Integer getTripCountByPeriod(@RequestParam(name = "dateFrom") String dateFrom,
                                                                          @RequestParam(name = "dateTo") String dateTo)
                                                                                                throws ParseException {
        Timestamp from = DateUtils.timestamp(dateFrom);
        Timestamp to = DateUtils.timestamp(dateTo);

        return statisticsRepository.getTripCountByPeriod(from, to);

//        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @GetMapping("/api/statistics/tripCountByApartments/{orderBy}")
    public ResponseEntity<List<StatisticsTripPrice>> getTripsByApartments(@PathVariable String orderBy) {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

}


