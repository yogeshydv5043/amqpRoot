package com.adani.routes;

import com.adani.entities.LogginEvent;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class SaveLogIntoDatabase extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .log("Inside SaveLogIntoDatabase Exception block ${exception.message}")
                .setBody(simple("<Ack><status>ERROR</status><message>${exception.message}</message></Ack>"))
                .log("Exception : ${body}");

        from("seda:save-log-into-db").routeId("SaveLogIntoDatabase")
                .log("Inside the SaveIntoDB")

                .process(ex -> {
                    String error_desc = (ex.getProperty("error_desc") == null || ((String) ex.getProperty("error_desc")).isEmpty()) ? "" : ex.getProperty("error_desc", String.class);
                    LogginEvent logginEvent = new LogginEvent();
                    logginEvent.setLog_id(ex.getProperty("log_id").toString());
                    logginEvent.setSource_subsystem(ex.getProperty("source_subsystem").toString());
                    logginEvent.setDestination_subsystem(ex.getProperty("destination_subsystem").toString());
                    logginEvent.setMessage_type(ex.getProperty("message_type").toString());
                    logginEvent.setOriginal_request(ex.getProperty("original_request").toString());
                    logginEvent.setFinal_response(ex.getProperty("final_response").toString());
                    logginEvent.setTransaction_time(ex.getProperty("transaction_time").toString());
                    logginEvent.setTransaction_identifier(ex.getProperty("transaction_identifier", String.class));
                    logginEvent.setIb_process_status(ex.getProperty("ib_process_status").toString());
                    logginEvent.setProcess_duration_ms(ex.getProperty("process_duration_ms").toString());
                    logginEvent.setError_desc(error_desc);
                    logginEvent.setCreated_by(ex.getProperty("created_by").toString());
                    logginEvent.setCreated_date(ex.getProperty("createdDate").toString());
                    logginEvent.setQueue_name(ex.getProperty("queue_name").toString());
                    ex.getIn().setBody(logginEvent);
                })

               .to("jpa://com.adani.entities.LogginEvent")
                .log(LoggingLevel.INFO, "Data insert into database")
                .setBody(simple("${exchangeProperty.originalBody}"))
        ;
    }
}
