package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractsResponse;
import com.onenow.hedgefund.contractclient.LookupContract;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.util.Piping;


public class ResourceDynamo {

    public String lookup = "";
    public String json = "";

    public ResourceDynamo(){

    }

    public ResourceDynamo(String lookup, String json) {
        this.lookup = lookup;
        this.json = json;
    }

    // saves two entries for every request: to lookup by contract ID or investor lookup
    public static void save(ContractIB item, DeployEnv nosqlEnv) {
        try {
            if(nosqlEnv.equals(DeployEnv.DEVELOPMENT)) {
                Dynamo.mapper.save(new ResourceDynamoDevelopment(LookupContract.getContractKey(item), getValue(item)));
                Dynamo.mapper.save(new ResourceDynamoDevelopment(LookupContract.getInvestmentKey(item), getValue(item)));
            }
            if(nosqlEnv.equals(DeployEnv.TESTING)) {
                Dynamo.mapper.save(new ResourceDynamoTesting(LookupContract.getContractKey(item), getValue(item)));
                Dynamo.mapper.save(new ResourceDynamoTesting(LookupContract.getInvestmentKey(item), getValue(item)));
            }
            if(nosqlEnv.equals(DeployEnv.STAGING)) {
                Dynamo.mapper.save(new ResourceDynamoStaging(LookupContract.getContractKey(item), getValue(item)));
                Dynamo.mapper.save(new ResourceDynamoStaging(LookupContract.getInvestmentKey(item), getValue(item)));
            }
            if(nosqlEnv.equals(DeployEnv.PRODUCTION)) {
                Dynamo.mapper.save(new ResourceDynamoProduction(LookupContract.getContractKey(item), getValue(item)));
                Dynamo.mapper.save(new ResourceDynamoProduction(LookupContract.getInvestmentKey(item), getValue(item)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(ContractIB contract) {
        return Piping.serialize(contract);
    }

    public static void get(String lookup, ContractsResponse response, DeployEnv nosqlEnv) {
        if(nosqlEnv.equals(DeployEnv.DEVELOPMENT)) {
            response.resources.addAll(ResourceDynamoDevelopment.get(lookup, nosqlEnv));
        }
        if(nosqlEnv.equals(DeployEnv.TESTING)) {
            response.resources.addAll(ResourceDynamoTesting.get(lookup, nosqlEnv));
        }
        if(nosqlEnv.equals(DeployEnv.STAGING)) {
            response.resources.addAll(ResourceDynamoStaging.get(lookup, nosqlEnv));
        }
        if(nosqlEnv.equals(DeployEnv.PRODUCTION)) {
            response.resources.addAll(ResourceDynamoProduction.get(lookup, nosqlEnv));
        }
    }


}
