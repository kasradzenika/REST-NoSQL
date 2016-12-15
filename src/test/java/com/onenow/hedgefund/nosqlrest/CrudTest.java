package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.TableName;
import org.testng.annotations.Test;

/**
 * Created by pablo on 10/4/16.
 */
public class CrudTest {

    @Test
    public void getOrders() {
        try {
            NoSqlService.GET(TableName.ORDERS.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getContracts() {
        try {
            NoSqlService.GET(TableName.CONTRACTS.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
