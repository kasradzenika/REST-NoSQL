package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.logging.InitLogger;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.beans.Model;
import com.onenow.hedgefund.nosql.utils.ExceptionUtil;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@Api(value = "/nosql", description = "This is nosql service where you can access various method manipulate the operations")
@Path("/nosql")
public class NoSqlEndpoint
{

    static {
        InitLogger.run(Level.FINEST);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(httpMethod = "POST", value = "/", notes = "This is POST method use to save json of defined table with lookup")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
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
    @ApiOperation(httpMethod = "PUT", value = "/", notes = "This is PUT method use to update json of defined table with lookup")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
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
    @ApiOperation(httpMethod = "GET", value = "/", notes = "This is GET method use to retrieve table data")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
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
    @ApiOperation(httpMethod = "GET", value = "/{ID}", notes = "This is GET method use to retrieve table data on bases of lookup")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
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
    @ApiOperation(httpMethod = "GET", value = "/query", notes = "This is GET method use to retrieve table data on date range")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
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
    @ApiOperation(httpMethod = "DELETE", value = "/{ID}", notes = "This is DELETE method use to delete table data on the bases of lookup")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Internal exception generated")})
    public Response DELETE(@PathParam("ID") String lookup, @QueryParam("tableName") String tableName)
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