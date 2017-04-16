package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.responsenosql.DynamoResponse;
import com.onenow.hedgefund.time.Pacing;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by pablo on 10/4/16.
 */
public class CrudTest
{

    // @Test
    // Disabled while upgrading DynamoResource
    public void getInvestments() {
        DynamoResponse response = null;
        try {
            response = NoSqlService.GET("SPY-STOCK", TableName.INVESTMENTS);
            Watchr.log("RESPONSE " + response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(response != null);
        Assert.assertTrue(response.resources.get(0).getItemJson().equals("{\"ticker\":\"SPY\",\"invType\":\"STOCK\"}"));

        Pacing.sleep(1); // wait for log to print
    }

    // @Test
    // Disabled while upgrading DynamoResource
    public void getContracts() {
        DynamoResponse response = null;
        try
        {
            response = NoSqlService.GET("SRTY-STOCK", TableName.CONTRACTS);
            Watchr.log("RESPONSE " + response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Assert.assertTrue(response != null);
        Assert.assertTrue(!response.resources.get(0).getItemJson().equals(""));
        Assert.assertTrue(!response.resources.get(0).getItemJson().equals(""));

        Pacing.sleep(1); // wait for log to print
    }

}
