package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by pablo on 10/4/16.
 */
public class CrudTest
{

    @Test
    public void getInvestments()
    {
        DynamoResponse response = null;
        try
        {
            response = NoSqlService.GET("SPY-STOCK", TableName.INVESTMENTS.toString());
            Watchr.log("RESPONSE " + response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Assert.assertTrue(response != null);
        Assert.assertTrue(response.resources.get(0).item.equals("{\"ticker\":\"SPY\",\"invType\":\"STOCK\"}"));
    }

    @Test
    public void getContracts()
    {
        DynamoResponse response = null;
        try
        {
            response = NoSqlService.GET("SRTY-STOCK", TableName.CONTRACTS.toString());
            Watchr.log("RESPONSE " + response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Assert.assertTrue(response != null);
        Assert.assertTrue(!response.resources.get(0).item.equals(""));
        Assert.assertTrue(!response.resources.get(0).item.equals(""));
    }

}
