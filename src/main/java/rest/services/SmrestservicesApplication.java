package rest.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rest.model.ResponseXml;
import rest.model.SolvedXml;
import sm.SMService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SmrestservicesApplication {

    public static void main(String[] args) {
		SpringApplication.run(SmrestservicesApplication.class, args);
	}

}
