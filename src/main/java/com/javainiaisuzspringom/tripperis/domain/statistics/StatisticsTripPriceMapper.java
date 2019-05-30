package com.javainiaisuzspringom.tripperis.domain.statistics;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsTripPriceMapper implements RowMapper<StatisticsTripPrice> {
    public StatisticsTripPrice mapRow(ResultSet rs, int rowNum) throws SQLException {
        StatisticsTripPrice x = new StatisticsTripPrice();

        String name = rs.getString("name");
        if (name != null && !name.isEmpty()) {
            x.setName(name);
        }

        BigDecimal sum = rs.getBigDecimal("sum");
        if (sum != null) {
            x.setSum(sum);
        }

        return x;
    }
}