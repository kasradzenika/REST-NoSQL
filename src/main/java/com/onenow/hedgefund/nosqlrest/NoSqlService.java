package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;

import java.util.List;


public class NoSqlService
{

    // TODO: actual environment
    private static DeployEnv nosqlDB = DeployEnv.STAGING; // WhereAmI.getDBname();

//    public static String tableName = LookupService.getDeployName(ServiceType.CONTRACTS.toString(), nosqlDB);


    public NoSqlService()
    {

    }

    public static void POST(String key,
                            String value,
                            String tableName)
            throws Exception
    {
        Dynamo.createTableIfDoesnotExist(LookupTable.getKey(tableName, nosqlDB));

        DynamoReadWrite.save(key, value, LookupTable.getKey(tableName, nosqlDB));
        //        Watchr.log("POST TO TABLE <" + tableName + "> OF: " + value.toString());
    }

    public static void PUT(String key,
                           String value,
                           String tableName)
            throws Exception
    {
        DynamoReadWrite.save(key, value, LookupTable.getKey(tableName, nosqlDB));
        //        Watchr.log("PUT TO TABLE <" + tableName + "> OF: " + value.toString());
    }


    public static DynamoResponse GET(String tableName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(LookupTable.getKey(tableName, nosqlDB)))
        {
            DynamoReadWrite.get(lookup, LookupTable.getKey(tableName, nosqlDB), response, nosqlDB);
        }

        //        Watchr.log("GET() FROM " + tableName + " RETURNED RESPONSE " + response.resources.toString());
        return response;
    }

    public static List<String> GET_TABLE_NAMES()
            throws Exception
    {
        return Dynamo.getTables();
    }

    public static DynamoResponse GET(String itemLookup,
                                     String tableName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.get(itemLookup, LookupTable.getKey(tableName, nosqlDB), response, nosqlDB);

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static DynamoResponse GET(String fromDate,
                                     String toDate,
                                     String tableLookupName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.getByDateRange(fromDate, toDate, tableLookupName, response, nosqlDB);

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static void DELETE(String itemLookup,
                              String tableLookupName)
            throws Exception
    {
        String key = "LOOKUP";
        Dynamo.deleteItem(key, itemLookup, tableLookupName);
        //        Watchr.log("DELETING WITH KEY " + key + " AND VALUE " + lookup + " FROM TABLE " + tableName);
    }

    public static void DELETE(String tableLookupName)
            throws Exception
    {
        Dynamo.deleteTable(tableLookupName);
    }

}
