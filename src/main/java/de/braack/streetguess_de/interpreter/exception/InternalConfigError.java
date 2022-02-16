package de.braack.streetguess_de.interpreter.exception;

public class InternalConfigError extends Error {

    public InternalConfigError(){
        super();
    }

    public InternalConfigError(Throwable ex){
        super(ex);
    }

    public InternalConfigError(String msg){
        super(msg);
    }

    public InternalConfigError(String msg, Throwable ex){
        super(msg, ex);
    }
}
