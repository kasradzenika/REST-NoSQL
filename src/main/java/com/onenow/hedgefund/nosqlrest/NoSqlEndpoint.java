package com.onenow.hedgefund.nosqlrest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

import com.onenow.hedgefund.aws.Dynamo;
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
            return Response.ok(model).build();
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
    @Path("/tables")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response GET() {
        try
        {
            return Response.ok(Piping.serialize(NoSqlService.GET_TABLE_NAMES())).build();
        }
        catch (Exception ex)
        {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/table/{tableName}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response GET(@PathParam("tableName") String tableName, @QueryParam("ID") String lookup) {
        try
        {
            if(lookup != null && !lookup.isEmpty())
            {
                return Response.ok(Piping.serialize(NoSqlService.GET(lookup, tableName))).build();
            } else
            {
                return Response.ok(Piping.serialize(NoSqlService.GET(tableName))).build();
            }
        }
        catch (Exception ex)
        {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/table/{tableName}/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GET(@PathParam("tableName") String tableName,
                        @QueryParam("fromDate") String fromDate,
                        @QueryParam("toDate") String toDate,
                        @QueryParam("dateFormat") String dateFormat,
                        @QueryParam("timeZone") String timeZone)
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
    @Path("/table/{tableName}/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DELETE(@PathParam("ID") String lookup,
                           @PathParam("tableName") String tableName)
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