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
import com.onenow.hedgefund.nosqlclient.DynamoResource;

import java.util.ArrayList;
import java.util.List;


// TODO: make the name of the table dynamic
// http://stackoverflow.com/questions/36347774/how-do-i-dynamically-change-the-table-accessed-using-dynamodbs-java-mapper
@DynamoDBTable(tableName="CONTRACTS-STAGING")
public class DynamoTable extends DynamoReadWrite {

    public DynamoTable(){
        super();
    }

    public DynamoTable(String lookup, String json) {
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

    public static List<DynamoResource> get(String lookup, DeployEnv nosqlEnv) {
        List<DynamoResource> resources = new ArrayList<DynamoResource>();
        DynamoDBQueryExpression<DynamoTable> queryExpression = getQuery(lookup);
        for(DynamoTable item: Dynamo.mapper.query(DynamoTable.class, queryExpression)) {
            String json = item.getJson();
            Watchr.log("ITEM: " + json);
            resources.add(new DynamoResource(json, nosqlEnv));
        }
        return resources;
    }

    private static DynamoDBQueryExpression<DynamoTable> getQuery(String lookup) {
        DynamoTable partitionKey = new DynamoTable();
        partitionKey.setLookup(lookup);

        return new DynamoDBQueryExpression<DynamoTable>()
                .withHashKeyValues(partitionKey);
    }

}
