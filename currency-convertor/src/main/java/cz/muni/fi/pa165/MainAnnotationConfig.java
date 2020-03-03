package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import cz.muni.fi.pa165.currency.ExchangeRateTable;
import cz.muni.fi.pa165.currency.ExchangeRateTableImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainAnnotationConfig {

        @Bean
        public ExchangeRateTable exchangeRateTable() {
            return new ExchangeRateTableImpl();
        }

        @Bean
        public CurrencyConvertor currencyConvertor(ExchangeRateTable exchangeRateTable){
            return new CurrencyConvertorImpl(exchangeRateTable);
        }
    }
