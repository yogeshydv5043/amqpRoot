//package com.adani.routes;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;
//import jakarta.inject.Named;
//import org.apache.qpid.jms.JmsConnectionFactory;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManagerFactory;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//@ApplicationScoped
//public class AmqpConnectionConfig {
//
//    @ConfigProperty(name = "route.certPath")
//    String certPath;
//
//    @ConfigProperty(name = "route.certPass")
//    String certPass;
//
//    @ConfigProperty(name = "route.connectionFactoryRemoteUri")
//    String connectionFactoryRemoteUri;
//
//    @ConfigProperty(name = "route.connectionFactoryUsername")
//    String connectionFactoryUsername;
//
//    @ConfigProperty(name = "route.connectionFactoryPassword")
//    String connectionFactoryPassword;
////
//   @Produces
//   @ApplicationScoped
//   @Named("createConnectionFactory")
//    public JmsConnectionFactory createConnectionFactory() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
//
//        KeyStore trustStore = KeyStore.getInstance("JKS");
//        try (FileInputStream trustStoreStream = new FileInputStream(certPath)) {
//            trustStore.load(trustStoreStream, certPass.toCharArray());
//        }
//
//        // Create TrustManagerFactory
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm()
//        );
//        trustManagerFactory.init(trustStore);
//
//        // Create SSLContext
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
//
//
//        JmsConnectionFactory connectionFactory = new JmsConnectionFactory();
//        connectionFactory.setRemoteURI(connectionFactoryRemoteUri);
//        connectionFactory.setUsername(connectionFactoryUsername);
//        connectionFactory.setPassword(connectionFactoryPassword);
//        connectionFactory.setSslContext(sslContext);
//
//        return connectionFactory;
//    }
//}