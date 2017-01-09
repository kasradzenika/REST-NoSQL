package com.onenow.hedgefund.nosqlrest;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlrest.utils.AppConsants;
import com.onenow.hedgefund.nosqlrest.utils.DateUtil;
import com.onenow.hedgefund.nosqlclient.DynamoResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "")
public class DynamoTable
        extends DynamoReadWrite
{

    public DynamoTable()
    {
        this("", "");
    }

    public DynamoTable(String lookup,
                       String json)
    {
        super(lookup, json);
        super.dateTime = DateUtil.getCurrentDate();
    }

    public static List<DynamoResource> get(String whenAt, String lookup, String tableName,
                                           DeployEnv nosqlDB)
    {
        List<DynamoResource> resources = new ArrayList<>();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(whenAt));
        eav.put(":val2", new AttributeValue().withS(lookup));

        DynamoDBQueryExpression<DynamoTable> queryExpression = new DynamoDBQueryExpression<DynamoTable>()
                .withKeyConditionExpression("dateTime = :val1 and lookup = :val2")
                .withExpressionAttributeValues(eav);

        for (DynamoTable item : Dynamo.mapper.query(DynamoTable.class, queryExpression, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableName))))
        {
            String json = item.getJson();
            Watchr.log("ITEM: " + json);
            resources.add(new DynamoResource(json, nosqlDB));
        }
        return resources;
    }

    public static List<DynamoResource> get(String lookup, String tableName,
                                           DeployEnv nosqlDB)
    {
        List<DynamoResource> resources = new ArrayList<>();
        DynamoDBQueryExpression<DynamoTable> queryExpression = getQuery(lookup);
        for (DynamoTable item : Dynamo.mapper.query(DynamoTable.class, queryExpression, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableName))))
        {
            String json = item.getJson();
            // Watchr.log("ITEM: " + json);
            resources.add(new DynamoResource(json, nosqlDB));
        }
        return resources;
    }

    // SAMPLE INPUTS
    // defaultFormat = "yyyy-MM-dd HH:mm:ss";
    // defaultTimeZone = "UTC";
    public static List<DynamoResource> getByDateRange(String fromDate, String toDate, String dateFormat, String timeZone,
                                                      String tableName, DeployEnv nosqlDB) {
        List<DynamoResource> resources = new ArrayList<>();

        /**
         * Needs to verify, waiting for permission
         * */
//        fromDate = DateUtil.getDate(DateUtil.getDate(fromDate, timeZone, dateFormat), AppConsants.DEFAULT_TIMEZONE, AppConsants.DEFAULT_DATE_FORMAT);
//        toDate = DateUtil.getDate(DateUtil.getDate(toDate, timeZone, dateFormat), AppConsants.DEFAULT_TIMEZONE, AppConsants.DEFAULT_DATE_FORMAT);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(fromDate));
        eav.put(":val2", new AttributeValue().withS(toDate));

        DynamoDBQueryExpression<DynamoTable> queryExpression = new DynamoDBQueryExpression<DynamoTable>()
                .withKeyConditionExpression("dateTime between :val1 and :val2")
                .withExpressionAttributeValues(eav);

        for (DynamoTable item : Dynamo.mapper.query(DynamoTable.class, queryExpression, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(tableName))))
        {
            String json = item.getJson();
            Watchr.log("ITEM: " + json);
            resources.add(new DynamoResource(json, nosqlDB));
        }
        return resources;
    }

    private static DynamoDBQueryExpression<DynamoTable> getQuery(String lookup)
    {
        DynamoTable partitionKey = new DynamoTable();
        partitionKey.setLookup(lookup);

        return new DynamoDBQueryExpression<DynamoTable>()
                .withHashKeyValues(partitionKey);
    }

    @DynamoDBHashKey(attributeName = "LOOKUP")
    public String getLookup()
    {
        return lookup;
    }

    public void setLookup(String lookup)
    {
        this.lookup = lookup;
    }

    @DynamoDBAttribute(attributeName = "json")
    public String getJson()
    {
        return json;
    }

    public void setJson(String json)
    {
        this.json = json;
    }

    @DynamoDBAttribute(attributeName = "date_time")
    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    @Override
    public String toString()
    {
        return lookup + "\t" + json;
    }

}
