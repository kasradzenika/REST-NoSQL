package com.onenow.hedgefund.nosql;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.onenow.hedgefund.aws.Dynamo;
import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractResource;
import com.onenow.hedgefund.discrete.DeployEnv;
import com.onenow.hedgefund.logging.Watchr;

import java.util.ArrayList;
import java.util.List;


// TODO: make the name of the table dynamic
// http://stackoverflow.com/questions/36347774/how-do-i-dynamically-change-the-table-accessed-using-dynamodbs-java-mapper
@DynamoDBTable(tableName="CONTRACTS-STAGING")
public class DynamoResource extends DynamoReadWrite {

    public DynamoResource(){
        super();
    }

    public DynamoResource(String lookup, String json) {
        super.lookup = lookup;
        super.json = json;
    }

    @DynamoDBHashKey(attributeName="LOOKUP")
    public String getLookup() { return lookup; }
    public void setLookup(String lookup) { this.lookup = lookup; }

    @DynamoDBAttribute(attributeName="json")
    public String getJson() { return json; }
    public void setJson(String json) { this.json = json; }

    public String toString() {
        return lookup + "\t" + json;
    }

    public static List<ContractResource> get(String lookup, DeployEnv nosqlEnv) {
        List<ContractResource> resources = new ArrayList<ContractResource>();
        DynamoDBQueryExpression<DynamoResource> queryExpression = getQuery(lookup);
        for(DynamoResource item: Dynamo.mapper.query(DynamoResource.class, queryExpression)) {
            String json = item.getJson();
            Watchr.log("ITEM: " + json);
            ContractIB contract = ContractIB.deserialize(json);
            resources.add(new ContractResource(contract, nosqlEnv));
        }
        return resources;
    }

    private static DynamoDBQueryExpression<DynamoResource> getQuery(String lookup) {
        DynamoResource partitionKey = new DynamoResource();
        partitionKey.setLookup(lookup);

        return new DynamoDBQueryExpression<DynamoResource>()
                .withHashKeyValues(partitionKey);
    }

}
