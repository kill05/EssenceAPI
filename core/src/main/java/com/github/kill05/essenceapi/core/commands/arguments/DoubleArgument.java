package com.github.kill05.essenceapi.core.commands.arguments;

import com.github.kill05.essenceapi.core.utils.EssenceUtils;
import com.github.kill05.essenceapi.core.utils.NumberUtils;

public class DoubleArgument extends CommandArgument {

    public DoubleArgument(int defaultValue) {
        super("number", String.valueOf(defaultValue));
    }

    public DoubleArgument(){
        super("number", null);
    }

    @Override
    public String getValidatedArg(String arg) {
        if(NumberUtils.isDouble(arg)){
            return arg;
        } else {
            return null;
        }
    }

}
