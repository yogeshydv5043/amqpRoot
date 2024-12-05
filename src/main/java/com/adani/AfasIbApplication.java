package com.adani;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.camel.quarkus.main.CamelMainApplication;

@QuarkusMain
public class AfasIbApplication {
    public static void main(String... args) {
        Quarkus.run(CamelMainApplication.class, args);
    }
}
