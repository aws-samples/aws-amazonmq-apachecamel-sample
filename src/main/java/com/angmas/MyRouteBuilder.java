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

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java8 DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {
        from("amqp:filequeue")
        .process()
            .message(this::setExtensionHeader)
        .choice()
            .when(header("ext").isEqualTo("txt"))
                .setHeader("CamelAwsS3Key").simple("text-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?amazonS3Client=#s3Client")
            .when(header("ext").isEqualTo("html"))
                .setHeader("CamelAwsS3Key").simple("html-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?amazonS3Client=#s3Client")
            .otherwise()
                .setHeader("CamelAwsS3Key").simple("other-files/${headers.CamelFileName}")
                .to("aws-s3://my-camel-example-bucket?amazonS3Client=#s3Client");

    }

    private void setExtensionHeader(Message m) {
        String fileName = (String) m.getHeader("CamelFileName");
        String ext = fileName.substring(fileName.lastIndexOf(".")+1);
        m.setHeader("ext", ext);
    }

}
