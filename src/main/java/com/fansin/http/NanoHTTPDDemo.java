package com.fansin.http;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * The type Nano httpd demo.
 *
 * @author zhaofeng
 */
public class NanoHTTPDDemo extends NanoHTTPD {

    /**
     * Instantiates a new Nano httpd demo.
     *
     * @throws IOException the io exception
     */
    public NanoHTTPDDemo() throws IOException {
        super(8080);
        System.setProperty("javax.net.ssl.trustStore", new File("src/main/resources/keystore.jks").getAbsolutePath());
        makeSecure(NanoHTTPD.makeSSLSocketFactory("/keystore.jks", "password".toCharArray()), null);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to https://localhost:8080/ \n");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            new NanoHTTPDDemo();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" +
                   "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse(msg + "</body></html>\n");
    }
}
