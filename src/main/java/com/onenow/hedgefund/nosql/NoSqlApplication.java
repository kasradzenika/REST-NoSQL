package com.onenow.hedgefund.nosql;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("webresources")
public class NoSqlApplication extends Application
{

    public NoSqlApplication()
    {
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(JacksonFeature.class);

        return resources;
    }
}
