package com.onenow.hedgefund.nosqlrest;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.dynamo.*;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlrest.utils.AppConsants;
import com.onenow.hedgefund.nosqlrest.utils.DateUtil;
import com.onenow.hedgefund.nosqlclient.DynamoResource;

import java.util.ArrayList;
import java.util.List;


@DynamoDBTable(tableName = "")
public class ReadWriteTable extends ReadWrite {

    private static String columnName = "date_time"; // dateTime is protected


    public ReadWriteTable()
    {
        this("", "");
    }

    public ReadWriteTable(String lookup,
                          String json) {
        super(lookup, json);
        super.dateTime = DateUtil.getCurrentDate();
    }

    public static List<DynamoResource> get(String whenAt, String lookup, String tableName,
                                           DeployEnv nosqlDB) {
        return get(getList(tableName, ExpressionFactory.getWhen(whenAt, lookup, columnName)), nosqlDB);
    }

    public static List<DynamoResource> get(String lookup, String tableName, DeployEnv nosqlDB) {
        return get(getList(tableName, getQuery(lookup)), nosqlDB);
    }

    public static List<DynamoResource> getByDateRange(String fromDate, String toDate, String dateFormat, String timeZone,
                                                      String tableName, DeployEnv nosqlDB) {

        // adjust date to standard format
        fromDate = getDate(fromDate, dateFormat, timeZone);
        toDate = getDate(toDate, dateFormat, timeZone);

        return get(getList(tableName, ExpressionFactory.getFromTo(fromDate, toDate, columnName)), nosqlDB);
    }

    public static List<DynamoResource> get(PaginatedQueryList<ReadWriteTable> list, DeployEnv nosqlDB) {

        List<DynamoResource> resources = new ArrayList<>();

        for (ReadWriteTable item:list) {
            String json = item.getJson();
            // Watchr.log("ITEM: " + json);
            resources.add(new DynamoResource(json, nosqlDB));
        }

        return resources;
    }

    private static String getDate(String toDate, String dateFormat, String timeZone) {
        return DateUtil.getDate(DateUtil.getDate(toDate, timeZone, dateFormat), AppConsants.DEFAULT_TIMEZONE, AppConsants.DEFAULT_DATE_FORMAT);
    }

    private static PaginatedQueryList<ReadWriteTable> getList(String tableName, DynamoDBQueryExpression<ReadWriteTable> queryExpression) {
        return (PaginatedQueryList<ReadWriteTable>) DynamoQuery.query(ReadWriteTable.class, queryExpression, tableName);
    }

    private static DynamoDBQueryExpression<ReadWriteTable> getQuery(String lookup) {
        ReadWriteTable partitionKey = new ReadWriteTable();
        partitionKey.setLookup(lookup);

        return new DynamoDBQueryExpression<ReadWriteTable>()
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
