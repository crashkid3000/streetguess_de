package de.braack.streetguess_de.interpreter.literals;

public abstract class Literal {
    private final String value;

    public Literal(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
