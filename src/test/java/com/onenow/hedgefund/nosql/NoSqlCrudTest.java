package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.logging.Watchr;
import com.onenow.hedgefund.nosql.beans.Model;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.UUID;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@Test
public class NoSqlCrudTest
{
    private Logger log = Logger.getLogger(NoSqlCrudTest.class.getName());

    private String host = "localhost";
    private int port = 9090;
    private Client client;
    private HttpServer server;

    private String lookup;
    private String tableName;

    public NoSqlCrudTest()
    {
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

    @Before
    public void setUp() throws IOException
    {
        URI uri = UriBuilder.fromUri("http://" + host + "/").port(port).build();

        // Create an HTTP server listening at port 8080
        server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);
        // Create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new ApplicationConfig(), HttpHandler.class);
        // Map JAX-RS handler to the server root
        server.createContext(uri.getPath(), handler);
        // Start the server
        server.start();
        log.info("Server started...");
    }

    @After
    public void destroy()
    {
        if (server != null)
        {
            server.stop(0);
            log.info("Server stopped...");
        }
    }

    @Test
    public void test()
    {
        saveObject();
//        updateObject();
//        deleteObject();
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
        log.info("URL: " + url);

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .post(Entity.entity(model, MediaType.APPLICATION_JSON));

        assertEquals("Failed to save item!", Response.ok().build().getStatus(), response.getStatus());
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

    private String getContextBaseUrl()
    {
        return "http://" + host + ":" + port + "/nosql";
    }
}
