package com.adani.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class AfasFlightLegRS extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:afas-flightlegrs-route").routeId("afasFlightLegRSRoute")
                .log("RouteId ${routeId}, and Hit received at ${date:now}")
                .convertBodyTo(String.class)
                //.setExchangePattern(ExchangePattern.InOnly)
                .to("{{route.ackOutQueue}}")
                .log("RouteId ${routeId}, and message push to AFAS-OUT-QUEUE");
    }
}
