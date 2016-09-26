package com.onenow.hedgefund.nosql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.ContractIB;
import com.onenow.hedgefund.util.Piping;


@Path("/contracts")
public class ContractsEndpoint
{


    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public static void POST(String itemJson,
                            String tableName)
    {  // must be passed as TXT json to use custom deserializer

        ContractIB contract = (ContractIB) Piping.deserialize(itemJson, ContractIB.class);
        NoSqlService.POST(contract.getContractID().toString(), itemJson, tableName);
    }

    @PUT
    @Path("/{ID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void PUT(@PathParam("ID") String lookup)
    {

        // TODO
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@QueryParam("tableName") String tableName)
    {

        return Piping.serialize(NoSqlService.GET(tableName));

        // ContractsResponse response = NoSqlService.GET();
        // String serialized = Piping.serialize(response);
        // return serialized;

    }

    @GET
    @Path("/{ID}")
//    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@PathParam("ID") String lookup,
                             @QueryParam("tableName") String tableName)
    {

        Watchr.log("LOOKING UP " + lookup);
        return Piping.serialize(NoSqlService.GET(lookup));

        // ContractsResponse response = NoSqlService.GET(lookup);
        // String serialized = Piping.serialize(response);
        // return serialized;
    }

    @DELETE
    @Path("/{ID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void DELETE(@PathParam("ID") String lookup)
    {

        NoSqlService.DELETE(lookup);

    }

}