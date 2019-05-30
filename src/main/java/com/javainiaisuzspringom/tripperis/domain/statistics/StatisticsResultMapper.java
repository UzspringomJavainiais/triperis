package com.javainiaisuzspringom.tripperis.domain.statistics;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsResultMapper implements RowMapper<StatisticsResult> {
    public StatisticsResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        StatisticsResult x = new StatisticsResult();

        String name = rs.getString("name");
        if (name != null && !name.isEmpty()) {
            x.setName(name);
        }

        String dateDiff = rs.getString("dateDiff");
        if (dateDiff != null && !dateDiff.isEmpty()) {
            x.setDateDiff(Integer.valueOf(dateDiff));
        }

        return x;
    }
}