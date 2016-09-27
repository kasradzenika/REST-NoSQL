package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.beans.Model;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/nosql")
public class NoSqlEndpoint
{
    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void POST(Model model)
    {
        NoSqlService.POST(model.getLookup(), model.getItemJson(), model.getTableName());
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void PUT(Model model)
    {

        NoSqlService.PUT(model.getLookup(), model.getItemJson(), model.getTableName());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@QueryParam("tableName") String tableName)
    {

        return Piping.serialize(NoSqlService.GET(tableName));
    }

    @GET
    @Path("/{ID}")
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@PathParam("ID") String lookup,
                             @QueryParam("tableName") String tableName)
    {

        Watchr.log("LOOKING UP " + lookup);
        return Piping.serialize(NoSqlService.GET(lookup, tableName));

    }

    @GET
    @Path("/query")
    @Produces(MediaType.TEXT_PLAIN)
    // https://www.mkyong.com/webservices/jax-rs/jax-rs-queryparam-example/
    public static String GET(@QueryParam("fromDate") String fromDate,
                             @QueryParam("toDate") String toDate,
                             @QueryParam("dateFormat") String dateFormat,
                             @QueryParam("timeZone") String timeZone,
                             @QueryParam("tableName") String tableName)
    {

        // String log = "INPUT: " + fromDate + " " + toDate + " " + dateFormat + " " + timeZone;
        // Watchr.log(log);

        // TODO LATER: date format change if necessary... dateFormat, timeZone

        DynamoResponse response = NoSqlService.GET(fromDate, toDate, tableName);
        String serialized = Piping.serialize(response);

        return serialized;
    }

    @DELETE
    @Path("/{ID}")
    @Consumes(MediaType.TEXT_PLAIN)
    public static void DELETE(@PathParam("ID") String lookup)
    {

        NoSqlService.DELETE(lookup);

    }

}