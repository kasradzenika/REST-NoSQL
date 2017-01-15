package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.dynamo.Dynamo;
import com.onenow.hedgefund.discovery.EnvironmentDatabase;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;

import java.util.List;


public class NoSqlService
{

    private static DeployEnv nosqlDB = EnvironmentDatabase.getNosqlValue();


    public NoSqlService() {

    }

    public static void POST(String key,
                            String value,
                            TableName tableName)
            throws Exception {

        Dynamo.createTableIfDoesnotExist(LookupTable.getKey(tableName, nosqlDB));

        DynamoReadWrite.save(key, value, LookupTable.getKey(tableName, nosqlDB));
        //        Watchr.log("POST TO TABLE <" + tableName + "> OF: " + value.toString());
    }

    public static void PUT(String key,
                           String value,
                           TableName tableName)
            throws Exception {
        DynamoReadWrite.save(key, value, LookupTable.getKey(tableName, nosqlDB));
        //        Watchr.log("PUT TO TABLE <" + tableName + "> OF: " + value.toString());
    }


    public static DynamoResponse GET(TableName tableName)
            throws Exception {

        Dynamo.createTableIfDoesnotExist(LookupTable.getKey(tableName, nosqlDB));

        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(LookupTable.getKey(tableName, nosqlDB))) {
            DynamoReadWrite.get(lookup, LookupTable.getKey(tableName, nosqlDB), response, nosqlDB);
        }

        //        Watchr.log("GET() FROM " + tableName + " RETURNED RESPONSE " + response.resources.toString());
        return response;
    }

    public static List<String> GET_TABLE_NAMES()
            throws Exception {

        return Dynamo.getTables();
    }

    public static DynamoResponse GET(String itemLookup,
                                     TableName tableName)
            throws Exception {

        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.get(itemLookup, LookupTable.getKey(tableName, nosqlDB), response, nosqlDB);

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static DynamoResponse GET(TableName tableName,
                                     String fromDate, String toDate, String dateFormat, String timeZone)
            throws Exception {

        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.getByDateRange(fromDate, toDate, dateFormat, timeZone,
                LookupTable.getKey(tableName, nosqlDB), response, nosqlDB);

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static void DELETE(String itemLookup,
                              TableName tableName)
            throws Exception {

        String key = "LOOKUP";
        Dynamo.deleteItem(key, itemLookup, LookupTable.getKey(tableName, nosqlDB));
        //        Watchr.log("DELETING WITH KEY " + key + " AND VALUE " + lookup + " FROM TABLE " + tableName);
    }

    public static void DELETE(TableName tableName)
            throws Exception
    {
        Dynamo.deleteTable(LookupTable.getKey(tableName, nosqlDB));
    }

}
