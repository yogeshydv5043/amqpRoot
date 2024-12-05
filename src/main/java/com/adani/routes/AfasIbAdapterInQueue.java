package com.adani.routes;


import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.builder.Namespaces;

import javax.xml.namespace.NamespaceContext;

@ApplicationScoped
public class AfasIbAdapterInQueue extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("ns", "http://www.iata.org/IATA/2007/00");

        from("{{route.ibInQueue}}").routeId("AfasAdapterInQueue")
                .log("AfasAdapterInQueue_001 :: Hit recieved at ${date:now} and messageType : ${header.messageType}")
                .setHeader("subsystem", simple("{{route.source_subsystem}}"))
                .setProperty("originalBody", simple("${body}"))
                .setHeader("log_id", simple("${date:now:yyyyMMddHHmmss}"))
                .setProperty("log_id", simple("${header.log_id}"))
                .setProperty("source_subsystem", simple("{{route.source_subsystem}}"))
                .setProperty("destination_subsystem", simple(""))
                .setProperty("message_type", simple(""))
                .setProperty("original_request", simple("${body}", String.class))
                .setProperty("final_response", simple(""))
                .setProperty("transaction_time", simple("${date:now:yyyy-MM-dd HH:mm:ss.SSS}"))
                .setProperty("ib_process_status", simple("Pending"))
                .setProperty("queue_name", simple("{{route.source_subsystem}}"))
                .setProperty("process_duration_ms", simple(""))
                .setProperty("error_desc", simple(""))
                .setProperty("created_by", simple("{{route.createBy}}"))
                .setProperty("createdDate", simple("${date:now:yyyy-MM-dd HH:mm:ss.SSS}"))
                // Validate the xml request and extract value of TransactionIdentifier
                .process("XMLValidationProcessor")
                // Validate message type and send to their respective validate handler service
                .to("direct:afas-request-processor")
                .log("AfasAdapterInQueue_002 :: Message processed");


        from("direct:afas-request-processor").routeId("AfasRequestProcessor")
                .log(LoggingLevel.INFO, "RouteId ${routeId}, and Message type : ${header.messageType}")
                .choice()
                    .when(bodyAs(String.class).contains("HealthCheckRequest"))
                        .setProperty("message_type", simple("HealthCheck"))
                        .to("seda:save-log-into-db")
                        .to("direct:afas-healthcheck-processor")  // Route to the HealthCheck service
                        .log("RouteId ${routeId}, and HealthCheck processed successfully")
                    .when(bodyAs(String.class).contains("IATA_AIDX_FlightLegRQ"))
                        .setProperty("message_type", simple("FlightLegRQ"))
                        .to("seda:save-log-into-db")
                        .to("direct:afas-flightlegrq-processor")
                        .log("RouteId ${routeId}, and Message processed to with messageType: ${header.message_type}")
                    .otherwise()
                        .to("seda:save-log-into-db")
                        .to("direct:afas-flightlegrq-processor")
                        .log("RouteId ${routeId}, and Something went wrong with messageType: ${header.message_type}")
                .end();

    }
}
