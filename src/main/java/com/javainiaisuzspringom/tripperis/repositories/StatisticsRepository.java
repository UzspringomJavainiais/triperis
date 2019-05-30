package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.TripRequest;
import com.javainiaisuzspringom.tripperis.domain.TripRequestStatus;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsResult;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsResultMapper;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsTripPrice;
import com.javainiaisuzspringom.tripperis.domain.statistics.StatisticsTripPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class StatisticsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<StatisticsResult> getTripsByDurationOrderASC() {
        return jdbcTemplate.query("select name , EXTRACT(DAY FROM date_to - date_from) AS dateDiff from trip ORDER BY dateDiff ASC;",
                new StatisticsResultMapper());
    }

    public List<StatisticsResult> getTripsByDurationOrderDESC() {
        return jdbcTemplate.query("select name , EXTRACT(DAY FROM date_to - date_from) AS dateDiff from trip ORDER BY dateDiff DESC;",
                new StatisticsResultMapper());

    }

    public List<StatisticsTripPrice> getTripsByPriceASC() {
        return jdbcTemplate.query(
                "select t.id, t.name as name, SUM(ci.price) as sum from trip t INNER JOIN checklist_item ci ON t.id = ci.trip_id GROUP BY (t.id, t.name, ci.price) ORDER BY sum ASC;", new StatisticsTripPriceMapper());
    }

    public List<StatisticsTripPrice> getTripsByPriceDESC() {
        return jdbcTemplate.query(
                "select t.id, t.name as name, SUM(ci.price) as sum from trip t INNER JOIN checklist_item ci ON t.id = ci.trip_id GROUP BY (t.id, t.name, ci.price) ORDER BY sum ASC;", new StatisticsTripPriceMapper());
    }

    public List<StatisticsTripPrice> getTripCountByEmployeeASC() {
        return jdbcTemplate.query(
                "select first_name || ' ' || last_name as name, count(*) as sum from trip_account ta inner join account a on a.id = ta.account_id GROUP BY (a.id, ta.account_id, a.first_name, a.last_name) ORDER BY sum ASC;", new StatisticsTripPriceMapper());
    }

    public List<StatisticsTripPrice> getTripCountByEmployeeDESC() {
        return jdbcTemplate.query(
                "select first_name || ' ' || last_name as name, count(*) as sum from trip_account ta inner join account a on a.id = ta.account_id GROUP BY (a.id, ta.account_id, a.first_name, a.last_name) ORDER BY sum DESC;", new StatisticsTripPriceMapper());
    }

    public Integer getTripCountByPeriod(Timestamp dateFrom, Timestamp dateTo) {
        return jdbcTemplate.queryForObject("select count(*) as sum  from trip t where date_from >= ? AND date_to <= ?;", Integer.class, dateFrom, dateTo);
    }
}