package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.beans.Model;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class NoSqlCrudTest
{

    private Client client;
    private String lookup;
    private String tableName;

    public NoSqlCrudTest() {
        lookup = UUID.randomUUID().toString();
        tableName = "ITEMS";
    }

    public Client getClientInstance()
    {
        if (client == null)
        {
            client = ClientBuilder.newClient();
           return client;
        }
        else
        {
            return client;
        }
    }

    @Test
    public void test(){
        saveObject();
        updateObject();
        deleteObject();
    }

    private void saveObject()
    {
        JSONObject json = new JSONObject();
        json.put("name", "Maggi");
        json.put("barcode", "123456789");
        json.put("cost", 2.5);
        json.put("price", 3.5);
        json.put("tax", 0.5);
        json.put("stock", 25);

        Model model = new Model();
        model.setLookup(lookup);
        model.setTableName(tableName);
        model.setItemJson(json.toJSONString());

        String url = getContextBaseUrl() + "/";

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .post(Entity.entity(model, MediaType.APPLICATION_JSON));

        int status = response.getStatus();
        if (Response.ok().build().getStatus() != status)
        {
            Watchr.log(response.getEntity().toString());
        }

        assertEquals("Failed to save item!", Response.ok().build().getStatus(), status);
    }

    private void updateObject()
    {
        JSONObject json = new JSONObject();
        json.put("name", "Maggi");
        json.put("barcode", "987654321");
        json.put("cost", 3.5);
        json.put("price", 4.5);
        json.put("tax", 1);
        json.put("stock", 50);

        Model model = new Model();
        model.setLookup(lookup);
        model.setTableName(tableName);
        model.setItemJson(json.toJSONString());

        String url = getContextBaseUrl() + "/";

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .put(Entity.entity(model, MediaType.APPLICATION_JSON));

        int status = response.getStatus();
        if (Response.ok().build().getStatus() != status)
        {
            Watchr.log(response.getEntity().toString());
        }

        assertEquals("Failed to update item!", Response.ok().build().getStatus(), status);
    }

    private void deleteObject()
    {
        String url = getContextBaseUrl() + "/" + lookup;

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .delete();

        int status = response.getStatus();
        if (Response.ok().build().getStatus() != status)
        {
            Watchr.log(response.getEntity().toString());
        }

        assertEquals("Failed to delete item!", Response.ok().build().getStatus(), status);
    }

    private String getContextBaseUrl() {
        return "http://localhost:8080/services/nosql";
    }
}
