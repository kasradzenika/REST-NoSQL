package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.bus.BusNoSqlFanout;
import com.onenow.hedgefund.bus.LookupTable;
import com.onenow.hedgefund.dynamo.Dynamo;
import com.onenow.hedgefund.discovery.EnvironmentDatabase;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.ReadWrite;
import com.onenow.hedgefund.nosql.ReadWriteTable;
import com.onenow.hedgefund.responsenosql.DynamoResponse;

import java.util.List;


public class NoSqlService
{

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

        Dynamo.createTableIfDoesnotExist(LookupTable.getKey(tableName, getNosqlDB()));

        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(LookupTable.getKey(tableName, getNosqlDB()))) {
            ReadWrite.get(lookup, LookupTable.getKey(tableName, getNosqlDB()), response, getNosqlDB());
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
        ReadWrite.get(itemLookup, LookupTable.getKey(tableName, getNosqlDB()), response, getNosqlDB());

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

        return response;
    }

    public static DynamoResponse GET(TableName tableName,
                                     String fromDate, String toDate, String dateFormat, String timeZone)
            throws Exception {

        DynamoResponse response = new DynamoResponse();
        ReadWrite.getByDateRange(fromDate, toDate, dateFormat, timeZone,
                LookupTable.getKey(tableName, getNosqlDB()), response, getNosqlDB());

        //        Watchr.log("GET() RESPONSE " + response.resources.toString());

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
