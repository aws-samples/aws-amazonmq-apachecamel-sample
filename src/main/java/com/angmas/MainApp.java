/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.angmas;

import org.apache.camel.main.Main;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.camel.component.amqp.AMQPConnectionDetails;

/**
 * A Camel Application
 */
public class MainApp {
    public static String BROKER_URL = System.getenv("BROKER_URL");
    public static String AMQP_URL = "amqps://"+BROKER_URL+":5671";
    public static String BROKER_USERNAME = System.getenv("BROKER_USERNAME");
    public static String BROKER_PASSWORD = System.getenv("BROKER_PASSWORD");
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.bind("amqp", getAMQPconnection());
        main.bind("s3Client", AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build());
        main.addRouteBuilder(new MyRouteBuilder());
        main.addRouteBuilder(new MessageProducerBuilder());
        main.run(args);
    }

    public static AMQPConnectionDetails getAMQPconnection() {
        return new AMQPConnectionDetails(AMQP_URL, BROKER_USERNAME, BROKER_PASSWORD);
    }

}

