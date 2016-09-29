package com.onenow.hedgefund.nosqlrest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

import com.onenow.hedgefund.logging.InitLogger;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlrest.beans.Model;
import com.onenow.hedgefund.nosqlrest.utils.ExceptionUtil;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;

@Path("/nosql")
public class NoSqlEndpoint
{

//    static {
//        try
//        {
//            InitLogger.run(Level.FINEST);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

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
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
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
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response GET() {
        try
        {
            return Response.ok(Piping.serialize(NoSqlService.GET("CONTRACTS-STAGING"))).build();
        }
        catch (Exception ex)
        {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
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
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
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
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
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
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @DELETE
    @Path("/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DELETE(@PathParam("ID") String lookup,
                           @QueryParam("tableName") String tableName)
    {

        try
        {
            NoSqlService.DELETE(lookup, tableName);
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

}