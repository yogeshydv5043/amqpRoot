package com.adani.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class LogginEvent {
    @Id
    private String log_id;
    private String transaction_identifier;
    private String source_subsystem;
    private String destination_subsystem;
    private String message_type;
    @Lob
    private String original_request;
    @Lob
    private String final_response;
    private String transaction_time;
    private String ib_process_status;
    private String process_duration_ms;
    private String error_desc;
    private String created_by;
    private String created_date;
    private String queue_name;

    public LogginEvent() {
    }

    public LogginEvent(String transaction_identifier, String log_id, String source_subsystem, String destination_subsystem, String message_type, String original_request, String final_response, String transaction_time, String ib_process_status, String process_duration_ms, String error_desc, String created_by, String created_date, String queue_name) {
        this.transaction_identifier = transaction_identifier;
        this.log_id = log_id;
        this.source_subsystem = source_subsystem;
        this.destination_subsystem = destination_subsystem;
        this.message_type = message_type;
        this.original_request = original_request;
        this.final_response = final_response;
        this.transaction_time = transaction_time;
        this.ib_process_status = ib_process_status;
        this.process_duration_ms = process_duration_ms;
        this.error_desc = error_desc;
        this.created_by = created_by;
        this.created_date = created_date;
        this.queue_name = queue_name;
    }

    public String getTransaction_identifier() {
        return transaction_identifier;
    }

    public void setTransaction_identifier(String transaction_identifier) {
        this.transaction_identifier = transaction_identifier;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getSource_subsystem() {
        return source_subsystem;
    }

    public void setSource_subsystem(String source_subsystem) {
        this.source_subsystem = source_subsystem;
    }

    public String getDestination_subsystem() {
        return destination_subsystem;
    }

    public void setDestination_subsystem(String destination_subsystem) {
        this.destination_subsystem = destination_subsystem;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getOriginal_request() {
        return original_request;
    }

    public void setOriginal_request(String original_request) {
        this.original_request = original_request;
    }

    public String getFinal_response() {
        return final_response;
    }

    public void setFinal_response(String final_response) {
        this.final_response = final_response;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getIb_process_status() {
        return ib_process_status;
    }

    public void setIb_process_status(String ib_process_status) {
        this.ib_process_status = ib_process_status;
    }

    public String getProcess_duration_ms() {
        return process_duration_ms;
    }

    public void setProcess_duration_ms(String process_duration_ms) {
        this.process_duration_ms = process_duration_ms;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getQueue_name() {
        return queue_name;
    }

    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }
}
