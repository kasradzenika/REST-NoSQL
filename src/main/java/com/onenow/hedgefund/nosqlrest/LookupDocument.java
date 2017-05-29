package com.onenow.hedgefund.nosqlrest;

/**
 * Created by pablo on 5/28/17.
 */
public class LookupDocument {

    public static String getKey(String itemLookup, String tableEnv) {
        return itemLookup + "-" + tableEnv;
    }
}
