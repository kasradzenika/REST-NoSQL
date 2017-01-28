package com.onenow.hedgefund.nosqlrest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.DynamoResource;
import com.onenow.hedgefund.nosqlclient.ModelNosql;
import com.onenow.hedgefund.nosqlrest.utils.ExceptionUtil;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;

@Path("/nosql")
public class NoSqlEndpoint {

    @POST
    @Path("/{tableName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response POST(ModelNosql modelNosql, @PathParam("tableName") String tableName) {
        try {
            NoSqlService.POST(modelNosql.getItemKey(), modelNosql.getItemJson(), TableName.valueOf(tableName));
            return Response.ok().build();
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @PUT
    @Path("/{tableName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response PUT(ModelNosql modelNosql, @PathParam("tableName") String tableName) {
        try {
            NoSqlService.PUT(modelNosql.getItemKey(), modelNosql.getItemJson(), TableName.valueOf(tableName));
            return Response.ok().build();
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/") // tables
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET() {
        try {
            return (Piping.serialize(NoSqlService.GET_TABLE_NAMES()));
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return "{\"error\":\"" + ex + "\"}";//Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/{tableName}") // /table/{tableName}
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@PathParam("tableName") String tableName) {
        try {
            DynamoResponse items = NoSqlService.GET(TableName.valueOf(tableName));
            return getJson(items);
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return "{\"error\":\"" + ex + "\"}";//Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    private static String getJson(DynamoResponse items) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DynamoResource.class, new DynamoResourceSerializer())
                .create();
        return gson.toJson(items);
    }

    @GET
    @Path("/{tableName}/{ID}")  // /table/{tableName}/{ID}
    @Produces(MediaType.TEXT_PLAIN)
    public static String GET(@PathParam("tableName") String tableName,
                             @PathParam("ID") String lookup) {
        try {
            if (lookup != null && !lookup.isEmpty()) {
                DynamoResponse items = NoSqlService.GET(lookup, TableName.valueOf(tableName));
                return getJson(items);
            } else {
                DynamoResponse items = NoSqlService.GET(TableName.valueOf(tableName));
                return getJson(items);
            }
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return "{\"error\":\"" + ex + "\"}";//Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @GET
    @Path("/{tableName}/query") // /table/{tableName}/query
    @Produces(MediaType.TEXT_PLAIN)
    public String GET(@PathParam("tableName") String tableName,
                      @QueryParam("fromDate") String fromDate,
                      @QueryParam("toDate") String toDate,
                      @QueryParam("dateFormat") String dateFormat,
                      @QueryParam("timeZone") String timeZone) {
        // String log = "INPUT: " + fromDate + " " + toDate + " " + dateFormat + " " + timeZone;
        // Watchr.log(log);

        // TODO LATER: date format change if necessary... dateFormat, timeZone

        try {
            DynamoResponse response = NoSqlService.GET(TableName.valueOf(tableName), fromDate, toDate, dateFormat, timeZone);
            String serialized = Piping.serialize(response);
            return (serialized);
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return "{\"error\":\"" + ex + "\"}";//Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }

    @DELETE
    @Path("/{tableName}/{ID}") // /table/{tableName}/{ID}
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DELETE(@PathParam("tableName") String tableName,
                           @PathParam("ID") String lookup) {
        try {
            NoSqlService.DELETE(lookup, TableName.valueOf(tableName));
            return Response.ok().build();
        } catch (Exception ex) {
            Watchr.log(ExceptionUtil.exceptionToString(ex));
            return Response.status(Response.Status.BAD_REQUEST).entity(ExceptionUtil.exceptionToString(ex)).build();
        }
    }
}