package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.bus.BusNoSqlFanout;
import com.onenow.hedgefund.bus.LookupTable;
import com.onenow.hedgefund.dynamo.Dynamo;
import com.onenow.hedgefund.discovery.EnvironmentDatabase;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.monitor.MonitoringTimer;
import com.onenow.hedgefund.nosql.ReadWrite;
import com.onenow.hedgefund.nosql.ReadWriteTable;
import com.onenow.hedgefund.responsenosql.DynamoResponse;
import com.onenow.hedgefund.time.DateTime;
import com.onenow.hedgefund.time.Pacing;

import java.util.HashMap;
import java.util.List;


public class NoSqlService
{

    // CACHE
    private static Integer timeToLiveSec = 300;
    private static HashMap<String, Long> responseStamp = new HashMap<>();
    private static HashMap<String, DynamoResponse> lastResponse = new HashMap<>();


    private static DeployEnv getNosqlDB() {
        return EnvironmentDatabase.getNosqlValue();
    }


    public NoSqlService() {

    }

    public static void POST(String key,
                            String value,
                            TableName tableName)
            throws Exception {

        BusNoSqlFanout.write(key, value, tableName);
    }

    public static void PUT(String key,
                           String value,
                           TableName tableName)
            throws Exception {

        BusNoSqlFanout.write(key, value, tableName);
    }


    public static DynamoResponse GET(TableName tableName)
            throws Exception {

        String tableEnv = LookupTable.getKey(tableName, getNosqlDB());
        String documentKey = "all";

        Dynamo.createTableIfDoesnotExist(tableEnv);

        DynamoResponse response;
        Long nowMs = DateTime.getTimeMilisecondsNow();

        if(MonitoringTimer.elapsed(nowMs, responseStamp.get(documentKey), timeToLiveSec)) {
            response = getResponse(tableEnv);
            saveResponse(documentKey, response, nowMs);
        } else {
            response = lastResponse.get(documentKey);
        }

        //        Watchr.log("GET() FROM " + tableName + " RETURNED RESPONSE " + response.resources.toString());
        return response;
    }

    private static DynamoResponse getResponse(String tableEnv) {

        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(tableEnv)) {
            ReadWrite.get(lookup, tableEnv, response, getNosqlDB());
            Pacing.sleepMillis(10); // reduce pressure on DynamoDB
        }
        return response;
    }

    public static List<String> GET_TABLE_NAMES()
            throws Exception {

        return Dynamo.getTables();
    }


    public static DynamoResponse GET(String itemLookup, TableName tableName)
            throws Exception {

        String tableEnv = LookupTable.getKey(tableName, getNosqlDB());
        String documentKey = LookupDocument.getKey(itemLookup, tableEnv);

        DynamoResponse response;
        Long nowMs = DateTime.getTimeMilisecondsNow();

        if(MonitoringTimer.elapsed(nowMs, responseStamp.get(documentKey), timeToLiveSec)) {
            response = getResponse(itemLookup, tableEnv);
            saveResponse(documentKey, response, nowMs);
        } else {
            response = lastResponse.get(documentKey);
        }

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());
        return response;
    }

    private static DynamoResponse getResponse(String itemLookup, String tableEnv) {
        DynamoResponse response = new DynamoResponse();
        ReadWrite.get(itemLookup, tableEnv, response, getNosqlDB());
        return response;
    }

    public static DynamoResponse GET(TableName tableName,
                                     String fromDate, String toDate, String dateFormat, String timeZone)
            throws Exception {

        String tableEnv = LookupTable.getKey(tableName, getNosqlDB());
        String documentKey = LookupDocument.getKey(fromDate, toDate, dateFormat, timeZone, tableEnv);

        DynamoResponse response;
        Long nowMs = DateTime.getTimeMilisecondsNow();

        if(MonitoringTimer.elapsed(nowMs, responseStamp.get(documentKey), timeToLiveSec)) {
            response = getResponse(fromDate, toDate, dateFormat, timeZone, tableEnv);
            saveResponse(documentKey, response, nowMs);
        } else {
            response = lastResponse.get(documentKey);
        }

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());
        return response;
    }

    private static void saveResponse(String documentKey, DynamoResponse response, Long nowMs) {
        lastResponse.put(documentKey, response);
        responseStamp.put(documentKey, nowMs);
    }

    private static DynamoResponse getResponse(String fromDate, String toDate, String dateFormat, String timeZone, String tableEnv) {
        DynamoResponse response = new DynamoResponse();
        ReadWrite.getByDateRange(fromDate, toDate, dateFormat, timeZone, tableEnv, response, getNosqlDB());
        return response;
    }

    public static void DELETE(String itemLookup,
                              TableName tableName)
            throws Exception {

        String columnName = "LOOKUP";
        Dynamo.deleteItem(columnName, itemLookup, LookupTable.getKey(tableName, getNosqlDB()));
        Watchr.log("DELETING WITH COLUMN " + columnName + " AND KEY " + itemLookup + " FROM TABLE " + tableName);
    }

    public static void DELETE(TableName tableName)
            throws Exception {

        Dynamo.deleteTable(LookupTable.getKey(tableName, getNosqlDB()));
    }

}
