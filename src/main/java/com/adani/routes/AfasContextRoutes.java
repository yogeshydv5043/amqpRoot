package com.adani.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class AfasContextRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        rest("/api/v1/afasibadapter")
                .post("/afas-flightlegrs")
                .to("direct:afas-flightlegrs-route")
                .post("/afas-flightLegNotifRQ")
                .to("direct:afas-flightlegnotifrq-route");

        from("direct:afas-flightlegnotifrq-route")
                .routeId("afasFlightLegNotifRQHttpRoute")
                .log("RouteId ${routeId} and, Hit received at ${date:now}")
                .convertBodyTo(String.class)
                .setExchangePattern(ExchangePattern.InOnly)
                .to("{{route.ackOutQueue}}")
                .log("Route id ${routeId}, Message pushed into AFAS-OUT-QUEUE");

    }
}
