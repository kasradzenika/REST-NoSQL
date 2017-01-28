package com.onenow.hedgefund.nosqlrest;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pablo on 1/27/17.
 */
public class ExpressionFactory {


    protected static DynamoDBQueryExpression<ReadWriteTable> getWhen(String whenAt, String lookup, String columnName) {
        return new DynamoDBQueryExpression<ReadWriteTable>()
                .withKeyConditionExpression(columnName + " " + " = :val1 and lookup = :val2")
                .withExpressionAttributeValues(getExpressionAttributeValues(whenAt, lookup));
    }

    protected static DynamoDBQueryExpression<ReadWriteTable> getFromTo(String fromDate, String toDate, String columnName) {
        return new DynamoDBQueryExpression<ReadWriteTable>()
                .withKeyConditionExpression(columnName + " " + "between :val1 and :val2")
                .withExpressionAttributeValues(getExpressionAttributeValues(fromDate, toDate));
    }

    private static Map<String, AttributeValue> getExpressionAttributeValues(String fromDate, String toDate) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(fromDate));
        eav.put(":val2", new AttributeValue().withS(toDate));
        return eav;
    }

}
