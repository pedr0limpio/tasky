package br.com.pedr0limpio.samples;

import org.eclipse.microprofile.graphql.*;
//TODO[#8]: Delete/remove this file and package samples
@GraphQLApi
public class HelloGraphQLResource {

    @Query
    @Description("Say hello")
    public Greeting sayHello(@DefaultValue("Renan") String name,
                             @DefaultValue("Silva") String surname) {
        return new Greeting(name, surname);
    }
}

