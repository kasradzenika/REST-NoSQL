package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractsResponse;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.util.Piping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/contracts")
public class ContractsEndpoint {


    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public static void POST(String itemJson) {  // must be passed as TXT json to use custom deserializer

        NoSqlService.POST(ContractIB.deserialize(itemJson));
    }

    @PUT
    @Path("/{ID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void PUT(@PathParam("ID") String lookup) {

        // TODO
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET() {

        return Piping.serialize(NoSqlService.GET());

        // ContractsResponse response = NoSqlService.GET();
        // String serialized = Piping.serialize(response);
        // return serialized;

    }

    @GET
    @Path("/{ID}")
//    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@PathParam("ID") String lookup) {

        Watchr.log("LOOKING UP " + lookup);
        return Piping.serialize(NoSqlService.GET(lookup));

        // ContractsResponse response = NoSqlService.GET(lookup);
        // String serialized = Piping.serialize(response);
        // return serialized;
    }

    @DELETE
    @Path("/{ID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void DELETE(@PathParam("ID") String lookup) {

        NoSqlService.DELETE(lookup);

    }

}