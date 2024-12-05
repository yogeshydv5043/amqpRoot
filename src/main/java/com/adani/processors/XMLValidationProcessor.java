package com.adani.processors;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.builder.Namespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@ApplicationScoped
@Named("XMLValidationProcessor")
@RegisterForReflection
public class XMLValidationProcessor implements Processor {
    private static Logger log = LoggerFactory.getLogger(XMLValidationProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        Namespaces ns = new Namespaces("ns", "http://www.iata.org/IATA/2007/00");
        String xmlRequestStatus="";
        try {
            // Create a DocumentBuilder to parse the XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // Enable namespace handling
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the XML input
            Document document = builder.parse(new ByteArrayInputStream(body.getBytes()));
            // Get the root element (IATA_AIDX_FlightLegNotifRQ)
            Element root = document.getDocumentElement();
            // Extract the value of the TransactionIdentifier attribute
            String transactionIdentifier = root.getAttribute("TransactionIdentifier");
            // Set the extracted value as a header or property
            exchange.getIn().setHeader("transaction_identifier", transactionIdentifier);
            exchange.setProperty("transaction_identifier", transactionIdentifier);
            xmlRequestStatus="XML request is an valid.";
        } catch (SAXException e) {
            exchange.setProperty("transaction_identifier", "");
            xmlRequestStatus="XML request is an invalid.";
        }
        log.info(xmlRequestStatus);
    }
}
