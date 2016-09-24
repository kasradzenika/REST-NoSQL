package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractsResponse;
import com.onenow.hedgefund.contractclient.LookupContract;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.nosqlclient.DynamoResponse;
import com.onenow.hedgefund.util.Piping;


public class DynamoReadWrite {

    public String lookup = "";
    public String json = "";

    public DynamoReadWrite(){

    }

    public DynamoReadWrite(String lookup, String json) {
        this.lookup = lookup;
        this.json = json;
    }

    // saves two entries for every request: to lookup by contract ID or investor lookup
    public static void save(ContractIB item, String tableName) {
        try {

            // TODO: use the tableName argument to save the item
            Dynamo.mapper.save(new DynamoTable(LookupContract.getContractKey(item), getValue(item)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(ContractIB contract) {
        return Piping.serialize(contract);
    }

    public static void get(String lookup, DynamoResponse response, DeployEnv nosqlEnv) {

        // TODO
        if(nosqlEnv.equals(DeployEnv.STAGING)) {
            response.resources.addAll(DynamoTable.get(lookup, nosqlEnv));
        }
    }


}
