package hr.fer.proinz.skilletcooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//this annotation says that this is a spring boot application
//and triggers functions specific for spring boot apllications
@SpringBootApplication

/** A class that is used to run a spring boot application */
public class SkillEtCookingApplication {

    /**
     * A main method method that rouns the spring boot application
     * 
     * @param args the given arguments that are given in the command line
     */
    public static void main(String[] args) {
        SpringApplication.run(SkillEtCookingApplication.class, args);
    }

}
