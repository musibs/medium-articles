package io.springbytes;

import io.springbytes.model.Pocket;
import io.springbytes.repository.PocketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringRestBasicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestBasicAuthApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(PocketRepository pocketRepository) {
        return args -> {
            Pocket pocket1 = new Pocket("Food", "https://digest.myhq.in/top-food-bloggers-in-india/");
            Pocket pocket2 = new Pocket("LifeStyle", "https://blog.feedspot.com/lifestyle_blogs/");
            pocketRepository.saveAll(Arrays.asList(pocket1, pocket2));
            pocketRepository.findAll().forEach(System.out::println);
        };
    }
}
