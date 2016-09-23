package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.contractclient.ContractIB;
import com.onenow.hedgefund.contractclient.ContractsResponse;
import com.onenow.hedgefund.ibcontract.ContractDetails;
import com.onenow.hedgefund.investment.InvIndex;
import com.onenow.hedgefund.logging.Watchr;
import org.testng.annotations.Test;

import java.util.ArrayList;


public class CrudTest {

     @Test
    public void CRUD() {


         // write new
         ContractIB contract = new ContractIB(new InvIndex("SPX"), new ArrayList<ContractDetails>());
         NoSqlService.POST(contract);

         // read
         ContractsResponse response = NoSqlService.GET();
         Watchr.log("GET() RESPONSE " + response.toString());

//         // count existing resources
//         Watchr.log("RESOURCES SIZE " + response.resources.size());
//         Assert.assertTrue(response.resources.size()>0);
//         // Assert.assertTrue(portfolio.name.equals(response.resources.get(0).portfolio.name));
//
//          // DELETE TEST
//          int countBeforeDelete = response.resources.size();
//          // PortfoliosEndpoint.DELETE(portfolio.name);
//          // int countAfterDelete = PortfoliosResponse.deserialize(PortfoliosEndpoint.GET()).resources.size();
//          // Watchr.log(countBeforeDelete + " ITEMS BEFORE DELETE, " + countAfterDelete + " AFTER");
//          // Assert.assertTrue(countAfterDelete+1==countBeforeDelete);
//
//         // PortfolioService.DELETE();

    }
}
