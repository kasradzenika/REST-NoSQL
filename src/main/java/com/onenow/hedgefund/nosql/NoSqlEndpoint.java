package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.beans.Model;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/nosql")
public class NoSqlEndpoint
{
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response POST(Model model)
    {
        try
        {
            NoSqlService.POST(model.getLookup(), model.getItemJson(), model.getTableName());
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response PUT(Model model)
    {
        try
        {
            NoSqlService.PUT(model.getLookup(), model.getItemJson(), model.getTableName());
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GET(@QueryParam("tableName") String tableName)
    {

        try
        {
            return Response.ok(Piping.serialize(NoSqlService.GET(tableName))).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GET(@PathParam("ID") String lookup,
                      @QueryParam("tableName") String tableName)
    {
        Watchr.log("LOOKING UP " + lookup);
        try
        {
            return Response.ok(Piping.serialize(NoSqlService.GET(lookup, tableName))).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    // https://www.mkyong.com/webservices/jax-rs/jax-rs-queryparam-example/
    public Response GET(@QueryParam("fromDate") String fromDate,
                      @QueryParam("toDate") String toDate,
                      @QueryParam("dateFormat") String dateFormat,
                      @QueryParam("timeZone") String timeZone,
                      @QueryParam("tableName") String tableName)
    {

        // String log = "INPUT: " + fromDate + " " + toDate + " " + dateFormat + " " + timeZone;
        // Watchr.log(log);

        // TODO LATER: date format change if necessary... dateFormat, timeZone

        try
        {
            DynamoResponse response = NoSqlService.GET(fromDate, toDate, tableName);
            String serialized = Piping.serialize(response);
            return Response.ok(serialized).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DELETE(@PathParam("ID") String lookup)
    {

        try
        {
            NoSqlService.DELETE(lookup);
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

}