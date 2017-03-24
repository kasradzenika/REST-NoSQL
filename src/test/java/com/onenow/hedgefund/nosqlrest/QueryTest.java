package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.responsenosql.DynamoResponse;
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
                                        "2017-03-01 00:00:00",
                                        "2017-03-31 23:59:59",
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
//        Assert.assertTrue(!response.resources.getWhen(0).item.equals(""));
    }
}
