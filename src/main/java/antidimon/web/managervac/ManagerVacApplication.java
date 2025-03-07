package antidimon.web.managervac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:.env")
public class ManagerVacApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerVacApplication.class, args);
    }

}
