package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.awsec.LookupService;
import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractsResponse;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.discrete.ServiceType;
import com.onenow.hedgefund.logging.Watchr;


public class NoSqlService {

    // TODO: actual environment
    private static DeployEnv nosqlEnv = DeployEnv.STAGING; // WhereAmI.getDBname();

    public static String tableName = LookupService.getDeployName(ServiceType.CONTRACTS.toString(), nosqlEnv);


    public NoSqlService() {

    }

    public static void POST(ContractIB contract) {
        Dynamo.createTableIfDoesnotExist(tableName);

        DynamoReadWrite.save(contract, tableName);
        // DynamoReadWrite.save(contract, nosqlEnv);
        Watchr.log("POST TO TABLE <" + tableName + "> OF: " + contract.toString());
    }

    public static void PUT(ContractIB contract) {

        // TODO:
    }

    public static ContractsResponse GET() {
        ContractsResponse response = new ContractsResponse();

        try {
            for(String lookup: Dynamo.getLookups(tableName)){
                DynamoReadWrite.get(lookup, response, nosqlEnv);
            }

            Watchr.log("GET() FROM " + tableName + " RETURNED RESPONSE " + response.resources.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ContractsResponse GET(String lookup) {
        ContractsResponse response = new ContractsResponse();
        DynamoReadWrite.get(lookup, response, nosqlEnv);

        try {
            Watchr.log("GET() RESPONSE " + response.resources.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static void DELETE(String lookup) {
        String key = "LOOKUP";
        Dynamo.deleteItem(key, lookup, tableName);
        Watchr.log("DELETING WITH KEY " + key + " AND VALUE " + lookup + " FROM TABLE " + tableName);
    }

    public static void DELETE() {
        Dynamo.deleteTable(tableName);
    }

}
