package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by pablo on 1/29/17.
 */
public class DeleteTest {

    @Test
    public void test() {

        String key = "testng-client-nosql";

        TableName tableName = TableName.INVESTMENTS;

        try {
            NoSqlService.DELETE(key, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DynamoResponse response = null;
        try {
            response = NoSqlService.GET(key, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(response!=null);
        Assert.assertTrue(response.resources.size()==0);

    }
}
