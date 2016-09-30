package com.onenow.hedgefund.nosqlrest;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;

import java.util.UUID;


public class DynamoReadWrite
{
    protected String lookup = UUID.randomUUID().toString();
    protected String json = "";
    protected String dateTime = "";


    public DynamoReadWrite()
    {

    }

    public DynamoReadWrite(String lookup,
                           String json)
    {
        this.lookup = lookup;
        this.json = json;
    }

    // saves two entries for every request: to lookup by contract ID or investor lookup
    public static void save(String key,
                            String value,
                            String tableLookupName)
            throws Exception
    {
        DynamoTable dt = new DynamoTable(key, value);
        Dynamo.mapper.save(dt, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableLookupName)));
    }

    public static void get(String lookup,
                           String tableLookupName,
                           DynamoResponse response,
                           DeployEnv nosqlDB)
    {

        response.resources.addAll(DynamoTable.get(lookup, tableLookupName, nosqlDB));
    }

    public static void get(String whenAt, String lookup, String tableName, DynamoResponse response, DeployEnv nosqlDB)
    {
        response.resources.addAll(DynamoTable.get(whenAt, lookup, tableName, nosqlDB));
    }

    public static void getByDateRange(String fromDate, String toDate, String tableName, DynamoResponse response, DeployEnv nosqlDB)
    {
        response.resources.addAll(DynamoTable.getByDateRange(fromDate, toDate, tableName, nosqlDB));
    }
}
