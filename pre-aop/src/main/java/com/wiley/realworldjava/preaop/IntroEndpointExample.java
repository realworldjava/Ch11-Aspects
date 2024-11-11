package com.wiley.realworldjava.preaop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class IntroEndpointExample {

    private Logger log = LoggerFactory.getLogger(IntroEndpointExample.class);

    public void someEndpoint(){
        long start = System.currentTimeMillis();
        log.info("Starting someEndpoint");

        // do stuff

        long duration = System.currentTimeMillis() - start;
        log.info("Completed someEndpoint. Execution duration:{}ms", duration);
    }


    public static void main(String[] args) {
      SpringApplication.run(IntroEndpointExample.class, args);
   }

}
