package br.com.pedr0limpio.samples;

import org.eclipse.microprofile.graphql.Type;
//TODO[#8]: Delete/remove this file and package samples
@Type
public class Greeting {
    private final String name;
    private final String surname;

    public Greeting(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;

    }
}
