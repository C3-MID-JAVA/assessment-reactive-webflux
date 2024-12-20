package org.example.financespro;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main class for the FinancesPro application. */
@SpringBootApplication
public class FinancesProApplication {
  public static void main(String[] args) {
    // Load .env file
    Dotenv dotenv = Dotenv.configure().directory("./").load();

    // Set MongoDB connection environment variables
    System.setProperty("SPRING_MONGODB_URI", dotenv.get("SPRING_MONGODB_URI"));

    SpringApplication.run(FinancesProApplication.class, args);
  }
}
