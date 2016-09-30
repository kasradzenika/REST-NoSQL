package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;

import java.util.List;


public class NoSqlService
{

    // TODO: actual environment
    private static DeployEnv nosqlEnv = DeployEnv.STAGING; // WhereAmI.getDBname();

//    public static String tableName = LookupService.getDeployName(ServiceType.CONTRACTS.toString(), nosqlEnv);


    public NoSqlService()
    {

    }

    public static void POST(String key,
                            String value,
                            String tableName)
            throws Exception
    {
        Dynamo.createTableIfDoesnotExist(tableName);

        DynamoReadWrite.save(key, value, tableName);
//        Watchr.log("POST TO TABLE <" + tableName + "> OF: " + value.toString());
    }

    public static void PUT(String key,
                           String value,
                           String tableName)
            throws Exception
    {
        DynamoReadWrite.save(key, value, tableName);
//        Watchr.log("PUT TO TABLE <" + tableName + "> OF: " + value.toString());
    }


    public static DynamoResponse GET(String tableName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(tableName))
        {
            DynamoReadWrite.get(lookup, tableName, response, nosqlEnv);
        }

//        Watchr.log("GET() FROM " + tableName + " RETURNED RESPONSE " + response.resources.toString());
        return response;
    }

    public static List<String> GET_TABLE_NAMES()
            throws Exception
    {
        return Dynamo.getTables();
    }

    public static DynamoResponse GET(String lookup,
                                     String tableName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.get(lookup, tableName, response, nosqlEnv);

//        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static DynamoResponse GET(String fromDate,
                                     String toDate,
                                     String tableName)
            throws Exception
    {
        DynamoResponse response = new DynamoResponse();
        DynamoReadWrite.getByDateRange(fromDate, toDate, tableName, response, nosqlEnv);

//        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static void DELETE(String lookup,
                              String tableName)
            throws Exception
    {
        String key = "LOOKUP";
        Dynamo.deleteItem(key, lookup, tableName);
//        Watchr.log("DELETING WITH KEY " + key + " AND VALUE " + lookup + " FROM TABLE " + tableName);
    }

    public static void DELETE(String tableName)
            throws Exception
    {
        Dynamo.deleteTable(tableName);
    }

}
