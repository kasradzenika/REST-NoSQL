package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.DeployEnv;

/**
 * Created by pablo on 9/30/16.
 */
public class LookupTable {

    public static String getKey(String tableName, DeployEnv nosqlDB) {

        return tableName+"-"+nosqlDB;
    }
}
