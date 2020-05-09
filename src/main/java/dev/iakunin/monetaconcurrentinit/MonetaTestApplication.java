package dev.iakunin.monetaconcurrentinit;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.IntStream;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.zalando.jackson.datatype.money.MoneyModule;

@SpringBootApplication
public class MonetaTestApplication {

    static {
        // This is a workaround for proper currency initialization.
        // For more details see:
        // https://github.com/JavaMoney/jsr354-ri/issues/158#issuecomment-313672702
        Monetary.getCurrency("EUR");
    }

    public static void main(String[] args) {
        SpringApplication.run(MonetaTestApplication.class, args);
    }

    @Component
    @Profile("race-condition")
    @RequiredArgsConstructor
    @Slf4j
    public static class RaceCondition implements CommandLineRunner {

        private final ObjectMapper objectMapper;

        @Override
        public void run(String... args) {
            IntStream.range(0, 100_000).parallel().forEach(i -> {
                var monetaryAmount = Monetary.getDefaultAmountFactory()
                    .setCurrency("EUR")
                    .setNumber(1)
                    .create();

                try {
                    objectMapper.writeValueAsString(new TempDto(monetaryAmount));
                } catch (Exception e) {
                    log.error("Exception during writing value as string", e);
                }
            });

            log.error("You should never be here! Try to start the app again.");
        }
    }

    @Component
    @Profile("!race-condition")
    @RequiredArgsConstructor
    @Slf4j
    public static class NoRaceCondition implements CommandLineRunner {

        private final ObjectMapper objectMapper;
        private final MonetaryAmountFactory<?> amountFactory;

        @Override
        public void run(String... args) {
            IntStream.range(0, 100_000).parallel().forEach(i -> {
                var monetaryAmount = amountFactory
                    .setCurrency("EUR")
                    .setNumber(1)
                    .create();

                try {
                    objectMapper.writeValueAsString(new TempDto(monetaryAmount));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            log.info("Everything is fine.");
            log.info("There is no `No MonetaryAmountsSingletonSpi loaded` error.");
        }
    }

    @Bean
    @Profile("!race-condition")
    public MonetaryAmountFactory<?> money() {
        return Monetary.getDefaultAmountFactory();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new MoneyModule());
    }

    @Value
    public static class TempDto {
        MonetaryAmount value;
    }
}
