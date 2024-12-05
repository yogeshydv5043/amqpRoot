package com.adani.routes;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AfasFlightlegRQTest extends CamelQuarkusTestSupport {


    private static final Logger log = LoggerFactory.getLogger(AfasFlightlegRQTest.class);
    private String xmlFlightLegRQ;

    @Inject
    private CamelContext camelContext;

    @BeforeEach
    public void setUp() throws Exception {
        xmlFlightLegRQ = Files.readString(Paths.get("src/main/resources/XSD/IATA_AIDX_FlightLegRQ.xml"));

        camelContext.addRoutes(new AfasFlightlegRQ());
        camelContext.start();

        // Manually create the ProducerTemplate
        if (template == null) {
            template = camelContext.createProducerTemplate();
        }

        AdviceWith.adviceWith(camelContext, "afasFlightLegRQProcessor", route -> {
            route.replaceFromWith("direct:afas-flightlegrq");

           route.interceptSendToEndpoint("{{route.flightLegRQEndpoint}}")
                    .skipSendToOriginalEndpoint() // Skip sending to actual queue
                    .to("mock:flightLegRQEndpoint");
//            route.mockEndpointsAndSkip("{{route.flightLegRQEndpoint}}");
            route.mockEndpoints("{{route.ackOutQueue}}");
            route.mockEndpoints("seda:save-log-into-db");
        });
    }


    @Test
    void SuccessScenarioTest() throws InterruptedException {

        // Mock endpoints
        MockEndpoint mockFlightLegRQEndpoint = getMockEndpoint("mock:flightLegRQEndpoint");
        MockEndpoint mockAckOutQueue = getMockEndpoint("mock:{{route.ackOutQueue}}");
        MockEndpoint mockSaveLogDb = getMockEndpoint("mock:seda:save-log-into-db");

        // Expectations
        mockFlightLegRQEndpoint.expectedMessageCount(1);
        mockAckOutQueue.expectedMessageCount(0);
        mockSaveLogDb.expectedMessageCount(1);

        // Send a test message with validationStatus as "SUCCESS"
        template.sendBodyAndHeader("direct:afas-flightlegrq", xmlFlightLegRQ, "validationStatus", "Success");

        // Assertions
        mockFlightLegRQEndpoint.assertIsSatisfied();
        mockAckOutQueue.assertIsSatisfied();
        mockSaveLogDb.assertIsSatisfied();

        // Check if the correct properties are set for success
        String errorDesc = mockSaveLogDb.getExchanges().get(0).getProperty("error_desc", String.class);

        assertEquals("", errorDesc, "The error description should be empty for success.");

    }

    @Test
    void FailureScenarioTest() throws InterruptedException {

        // Mock endpoints
        MockEndpoint mockFlightLegRQEndpoint = getMockEndpoint("mock:flightLegRQEndpoint");
        MockEndpoint mockAckOutQueue = getMockEndpoint("mock:{{route.ackOutQueue}}");
        MockEndpoint mockSaveLogDb = getMockEndpoint("mock:seda:save-log-into-db");

        // Expectations
        mockFlightLegRQEndpoint.expectedMessageCount(1);
        mockAckOutQueue.expectedMessageCount(1);
        mockSaveLogDb.expectedMessageCount(1);

        // Send a test message with validationStatus as "SUCCESS"
        template.sendBodyAndHeader("direct:afas-flightlegrq", xmlFlightLegRQ, "validationStatus", "Failure");


        // Check if the correct properties are set for success
        String finalResponse = mockAckOutQueue.getExchanges().get(0).getProperty("final_response", String.class);

        System.out.println(finalResponse);


        // Assertions
        mockFlightLegRQEndpoint.assertIsSatisfied();
        mockAckOutQueue.assertIsSatisfied();
        mockSaveLogDb.assertIsSatisfied();

    }


}