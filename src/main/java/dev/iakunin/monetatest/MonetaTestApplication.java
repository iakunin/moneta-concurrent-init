package dev.iakunin.monetatest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zalando.jackson.datatype.money.MoneyModule;

@SpringBootApplication
@EnableScheduling
public class MonetaTestApplication {

    private static final List<String> CURRENCIES = List.of("EUR", "CHF", "PLN", "DKK", "GBP");
    private final static Logger LOGGER = Logger.getLogger(MonetaTestApplication.class.getName());

    /*static {
        Monetary.getCurrency("EUR");
        Monetary.getCurrency("CHF");
        Monetary.getCurrency("PLN");
        Monetary.getCurrency("DKK");
        Monetary.getCurrency("GBP");
    }*/

    public static void main(String[] args) {
        SpringApplication.run(MonetaTestApplication.class, args);
    }


   /* @Component
    @RequiredArgsConstructor
    public static class MoneyFactory {

        private final MonetaryAmountFactory<?> monetaryAmountFactory;

        @Synchronized
        public MonetaryAmount create(int count) {
            return monetaryAmountFactory
                    .setCurrency("EUR")
                    .setNumber(count)
                    .create();
        }
    }

    @Bean
    public MonetaryAmountFactory<?> money() {
        return Monetary.getDefaultAmountFactory();
    }*/

    @Bean
    public ObjectMapper runnerTest() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY);
        objectMapper.registerModule(new MoneyModule().withQuotedDecimalNumbers());

        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());


        IntStream.range(0, 10_000).parallel().forEach(i -> {
            Random r = new Random();
            var currency1 = CURRENCIES.stream()
                .skip(r.nextInt(CURRENCIES.size() - 1)).findFirst().get();

            var currency = Monetary.getDefaultAmountFactory()
                .setCurrency("EUR")
                .setNumber(1)
                .create();

            //var currency = moneyFactory.create(1);

            try {
                final Body body = new Body();
                body.setValue(currency);
                final String value = objectMapper.writeValueAsString(body);
//                System.out.println(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* try {
                Path path = Paths.get(MoneyUtils.class.getClassLoader()
                        .getResource("body.json").toURI());

                Stream<String> lines = Files.lines(path);
                String data = lines.collect(Collectors.joining("\n"));
                lines.close();
                var body = objectMapper.readValue(data, Body.class);
                System.out.println(body);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        });

        return objectMapper;
    }

    @Data
    public static class Body {
        private MonetaryAmount value;
    }

}
