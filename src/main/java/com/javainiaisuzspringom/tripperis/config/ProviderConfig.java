package com.javainiaisuzspringom.tripperis.config;

import com.javainiaisuzspringom.tripperis.services.calendar.AccountCalendarProvider;
import com.javainiaisuzspringom.tripperis.services.calendar.AccountTripCalendarProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean
    public AccountCalendarProvider accountTripCalendarProvider() {
        return new AccountTripCalendarProvider();
    }
}
