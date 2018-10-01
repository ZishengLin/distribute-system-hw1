package com.example;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client the length of the sent information
     *
     * @return information that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public int postText(String content){
        return (content.length());
    }
}
