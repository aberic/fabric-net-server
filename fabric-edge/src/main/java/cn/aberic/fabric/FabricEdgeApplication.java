package cn.aberic.fabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FabricEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FabricEdgeApplication.class, args);
    }
}
