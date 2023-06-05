package com.github.kill05.essenceapi.core.commands.arguments;


public class StringArgument extends CommandArgument{

    public StringArgument(String defaultValue) {
        super("string", defaultValue);
    }

    public StringArgument(){
        this(null);
    }


    @Override
    public String getValidatedArg(String arg) {
        return arg;
    }

}
