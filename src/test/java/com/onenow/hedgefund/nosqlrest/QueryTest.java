package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by pablo on 1/5/17.
 */
public class QueryTest {

    @Test
    public void getContractsInDateRange()
    {
        DynamoResponse response = null;
        try
        {
            response = NoSqlService.GET(TableName.CONTRACTS,
                                        "2016-01-01 00:00:00",
                                        "2016-12-31 23:59:59",
                                        "yyyy-MM-dd hh:mm:ss",
                                        "UTC");
            Watchr.log("RESPONSE: " + response);
            Watchr.log("RESPONSE: " + (response != null ? response.resources : "NULL"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        Assert.assertTrue(response != null);
//        Assert.assertTrue(!response.resources.get(0).item.equals(""));
    }
}
