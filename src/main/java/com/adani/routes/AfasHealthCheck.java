package com.adani.routes;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
public class AfasHealthCheck extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:afas-healthcheck-processor").routeId("afasHealthCheckProcessor")
                .log("Route id ${routeId}, and Hit Received at ${date:now}")
                .to("{{route.healthCheckEndpoint}}")
                .log("Received request: ${body}")
                .convertBodyTo(String.class)
                .choice()
                    .when(header("validationStatus").isEqualToIgnoreCase("Failure"))
                        .setProperty("ib_process_status", simple("{{route.error_ib_process_status}}"))
                        .setProperty("error_desc", simple("{{route.error_desc}}"))
                        .log("Route id ${routeId}, Error Ack pushed into {{route.ack_destination_subsystem}}")
                    .when(header("validationStatus").isEqualToIgnoreCase("SERVICE_INACTIVE"))
                        .setProperty("ib_process_status", constant("Failure"))// Dynamically include the inactive services in the error description
                        .setProperty("error_desc", simple("Inactive service(s): ${header.inactiveServices}"))
                        .log("Route id ${routeId}, Error Ack pushed into BHS-OUT-QUEUE. Inactive service(s): ${header.inactiveServices}")
                    .when(header("validationStatus").isEqualToIgnoreCase("Success"))
                        .setProperty("ib_process_status", simple("{{route.success_ib_process_status}}"))
                        .setProperty("error_desc", simple(""))
                        .log("Route id ${routeId}, Success Ack pushed into {{route.ack_destination_subsystem}}")
                .end()
                .setProperty("final_response", simple("${body}", String.class))
                .to("{{route.ackOutQueue}}")
                .setProperty("destination_subsystem", simple("{{route.ack_destination_subsystem}}"))
                .to("seda:save-log-into-db")
        ;
    }
}
