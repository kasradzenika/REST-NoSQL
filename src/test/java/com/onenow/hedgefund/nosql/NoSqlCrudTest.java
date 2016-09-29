package com.onenow.hedgefund.nosql;

import com.onenow.hedgefund.discovery.WhereAmI;
import com.onenow.hedgefund.nosql.beans.Model;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
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
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;

public class NoSqlCrudTest
{

    @BeforeMethod
    // TODO: keepTrack on Testing environment with a Gateway
    protected void checkEnvironment() {
        if (!WhereAmI.isDevelopmentEnv()) {
            throw new SkipException("Skipping tests in Jenkins for now.");
        }
    }

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

    @BeforeTest
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

    @AfterTest
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
        log.info("URL: " + url);

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .post(Entity.entity(model, MediaType.APPLICATION_JSON));

        assertEquals(response.getStatus(), Response.ok().build().getStatus(), "Failed to save item!");
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

        assertEquals(response.getStatus(), Response.ok().build().getStatus(), "Failed to update item!");
    }

    private void deleteObject()
    {
        String url = getContextBaseUrl() + "/" + lookup;

        WebTarget resource = getClientInstance().target(url);
        Response response = resource.request(MediaType.APPLICATION_JSON)
                                    .delete();

        assertEquals(response.getStatus(), Response.ok().build().getStatus(), "Failed to delete item!");
    }

    private String getContextBaseUrl()
    {
        return "http://" + host + ":" + port + "/nosql";
    }
}
