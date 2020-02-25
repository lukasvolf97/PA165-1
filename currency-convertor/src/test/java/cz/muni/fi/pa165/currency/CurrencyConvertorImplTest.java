package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    @Mock
    private ExchangeRateTable exchangeRateTable;

    private CurrencyConvertor currencyConvertor;

    private static Currency CZK = Currency.getInstance("CZK");
    private static Currency EUR = Currency.getInstance("EUR");

    @Before
    public void init(){
        currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenReturn(new BigDecimal("25.5"));
        when(exchangeRateTable.getExchangeRate(CZK, EUR))
                .thenReturn(new BigDecimal("0.039215686"));

        assertEquals(new BigDecimal("881.02"), currencyConvertor.convert(EUR, CZK, new BigDecimal("34.55")));
        assertEquals(new BigDecimal("34.55"), currencyConvertor.convert(CZK, EUR, new BigDecimal("881.02")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullSourceCurrency() {
        currencyConvertor.convert(null, CZK, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullTargetCurrency() {
        currencyConvertor.convert(EUR, null, BigDecimal.TEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullSourceAmount() {
        currencyConvertor.convert(EUR, CZK, null);
    }

    @Test(expected = UnknownExchangeRateException.class)
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenReturn(null);
        currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);

    }

    @Test(expected = UnknownExchangeRateException.class)
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenThrow(UnknownExchangeRateException.class);
        currencyConvertor.convert(EUR, CZK, BigDecimal.TEN);
    }

}
