package com.github.kill05.essenceapi.core.commands.arguments;


import java.util.Locale;

public class BooleanArgument extends CommandArgument {

    public BooleanArgument(Boolean defaultValue) {
        super("boolean", String.valueOf(defaultValue));
    }

    public BooleanArgument(){
        this(null);
    }

    @Override
    public String getValidatedArg(String arg) {
        if(arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return arg.toLowerCase(Locale.ENGLISH);
        }
        return null;
    }

}
