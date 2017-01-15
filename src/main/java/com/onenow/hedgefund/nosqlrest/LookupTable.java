package com.onenow.hedgefund.nosqlrest;

import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.discrete.TableName;

/**
 * Created by pablo on 9/30/16.
 */
public class LookupTable {

    public static String getKey(TableName tableName, DeployEnv nosqlDB) {

        return tableName+"-"+nosqlDB;
    }
}
