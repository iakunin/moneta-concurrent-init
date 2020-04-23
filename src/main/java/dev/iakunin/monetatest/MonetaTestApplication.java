package dev.iakunin.monetatest;

import java.math.BigDecimal;
import org.javamoney.moneta.Money;
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
            final Money money = Money.of(BigDecimal.valueOf(100), "USD");
            System.out.println("money = " + money);
        }
    }
}
