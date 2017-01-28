package com.onenow.hedgefund.nosqlrest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationConfig extends Application
{

    public ApplicationConfig() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
//        resources.add(JacksonFeature.class);
//        resources.add(NoSqlEndpoint.class);
//        resources.add(ApiListingResource.class);
//        resources.add(SwaggerSerializers.class);

        return Collections.unmodifiableSet(resources);
    }
}
