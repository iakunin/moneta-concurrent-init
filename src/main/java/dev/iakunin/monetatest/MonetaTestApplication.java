package dev.iakunin.monetatest;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class MonetaTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonetaTestApplication.class, args);
    }

    @Component
    public final static class TestScheduler implements Runnable {
        @Override
        @Scheduled(cron = "* * * * * *")
        public void run() {
            final MonetaryAmount amount = Monetary.getDefaultAmountFactory()
                .setCurrency("USD")
                .setNumber(100L)
                .create();

            System.out.println("amount = " + amount);
        }
    }
}
