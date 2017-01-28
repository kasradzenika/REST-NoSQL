package com.onenow.hedgefund.nosqlrest;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.dynamo.DynamoReadWrite;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;

import java.util.UUID;


public class ReadWrite
{
    protected String lookup = UUID.randomUUID().toString();
    protected String json = "";
    protected String dateTime = "";


    public ReadWrite()
    {

    }

    public ReadWrite(String lookup,
                     String json)
    {
        this.lookup = lookup;
        this.json = json;
    }

    // saves two entries for every request: to lookup by contract ID or investor lookup
//        ReadWriteTable dt = new ReadWriteTable(key, value);
    public static void save(ReadWriteTable dt, String tableLookupName)
            throws Exception
    {
        DynamoReadWrite.save(dt, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableLookupName)));
    }

//    // saves two entries for every request: to lookup by contract ID or investor lookup
//    public static void save(String key,
//                            String value,
//                            String tableLookupName)
//            throws Exception
//    {
//        ReadWriteTable dt = new ReadWriteTable(key, value);
//        mapper.save(dt, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableLookupName)));
//    }

    public static void get(String lookup,
                           String tableLookupName,
                           DynamoResponse response,
                           DeployEnv nosqlDB)
    {

        response.resources.addAll(ReadWriteTable.get(lookup, tableLookupName, nosqlDB));
    }

    public static void get(String whenAt, String lookup, String tableName, DynamoResponse response, DeployEnv nosqlDB)
    {
        response.resources.addAll(ReadWriteTable.get(whenAt, lookup, tableName, nosqlDB));
    }

    public static void getByDateRange(String fromDate, String toDate, String dateFormat, String timeZone,
                                      String tableName, DynamoResponse response, DeployEnv nosqlDB)
    {
        response.resources.addAll(ReadWriteTable.getByDateRange(fromDate, toDate, dateFormat, timeZone,
                tableName, nosqlDB));
    }
}