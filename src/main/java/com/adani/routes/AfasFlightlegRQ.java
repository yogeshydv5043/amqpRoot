package com.adani.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
public class AfasFlightlegRQ extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:afas-flightlegrq-processor").routeId("afasFlightLegRQProcessor")
                .log("Route id ${routeId}, and Hit Received at ${date:now}")
                //.setExchangePattern(ExchangePattern.InOnly)
                .to("{{route.flightLegRQEndpoint}}")
                .convertBodyTo(String.class)
                .choice()
                    .when(header("validationStatus").isEqualToIgnoreCase("Failure"))
                    .setProperty("final_response", simple("${body}", String.class))
                    .to("{{route.ackOutQueue}}")
                    .log("Route id ${routeId}, Error Ack pushed into {{route.ack_destination_subsystem}}")
                    .setProperty("ib_process_status", simple("{{route.error_ib_process_status}}"))
                    .setProperty("error_desc", simple("{{route.error_desc}}"))
                    .setProperty("destination_subsystem", simple("{{route.ack_destination_subsystem}}"))
                .otherwise()
                    .setProperty("ib_process_status", simple("{{route.success_ib_process_status}}"))
                    .setProperty("error_desc", simple(""))
                    .setProperty("destination_subsystem", simple("{{route.success_destination_subsystem}}"))
                .end()
                .to("seda:save-log-into-db")
                .log("Route id ${routeId}, Message processed successfully...")
        ;
    }
}
