package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.dynamo.Dynamo;
import com.onenow.hedgefund.nosql.ReadWrite;
import com.onenow.hedgefund.responsenosql.DynamoResponse;
import com.onenow.hedgefund.time.Pacing;

/**
 * Created by pablo on 5/28/17.
 */
public class ResponseBuilder {

    public static DynamoResponse getResponse(String itemLookup, String tableName, DeployEnv nosqlDb) {
        DynamoResponse response = new DynamoResponse();
        ReadWrite.get(itemLookup, tableName, response, nosqlDb);
        return response;
    }

    public static DynamoResponse getResponse(String fromDate, String toDate, String dateFormat, String timeZone,
                                              String tableName, DeployEnv nosqlDb) {
        DynamoResponse response = new DynamoResponse();
        ReadWrite.getByDateRange(fromDate, toDate, dateFormat, timeZone, tableName, response, nosqlDb);
        return response;
    }

    public static DynamoResponse getResponse(String tableName, DeployEnv nosqlDb) {

        DynamoResponse response = new DynamoResponse();

        for (String lookup : Dynamo.getLookups(tableName)) {
            ReadWrite.get(lookup, tableName, response, nosqlDb);
            Pacing.sleepMillis(10); // reduce pressure on DynamoDB
        }
        return response;
    }

}
