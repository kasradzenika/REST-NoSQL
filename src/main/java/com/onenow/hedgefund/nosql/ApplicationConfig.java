package com.onenow.hedgefund.nosql;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationConfig extends Application
{

    public ApplicationConfig()
    {
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(JacksonFeature.class);
        resources.add(NoSqlEndpoint.class);

        return Collections.unmodifiableSet(resources);
    }
}
