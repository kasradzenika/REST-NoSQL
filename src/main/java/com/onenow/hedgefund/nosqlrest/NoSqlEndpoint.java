package com.onenow.hedgefund.nosqlrest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.ModelNosql;
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
    public Response POST(ModelNosql modelNosql)
    {
        try
        {
            NoSqlService.POST(modelNosql.getItemKey(), modelNosql.getItemJson(), modelNosql.getTableName());
            return Response.ok(modelNosql).build();
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
    public Response PUT(ModelNosql modelNosql)
    {
        try
        {
            NoSqlService.PUT(modelNosql.getItemKey(), modelNosql.getItemJson(), modelNosql.getTableName());
            return Response.ok().build();
        }
        catch (Exception ex)
        {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    // lists all the tables for all the DB environments: ie. CONTRACTS-DEVELOPMENT, ORDERS-STAGING
    // this is distinct from a "tableName", which include: CONTRACTS, ORDERS
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
    public static Response GET(@PathParam("tableName") String tableName) {
        try {
            return Response.ok(Piping.serialize(NoSqlService.GET(tableName))).build();
        }
        catch (Exception ex) {
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