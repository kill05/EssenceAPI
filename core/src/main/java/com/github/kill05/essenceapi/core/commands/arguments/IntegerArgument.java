package com.github.kill05.essenceapi.core.commands.arguments;

import com.github.kill05.essenceapi.core.utils.EssenceUtils;
import com.github.kill05.essenceapi.core.utils.NumberUtils;

public class IntegerArgument extends CommandArgument {

    private String argName = "integer";
    private Integer min = null;
    private Integer max = null;

    public IntegerArgument(int defaultValue) {
        super("integer", String.valueOf(defaultValue));
    }

    public IntegerArgument(){
        super("integer", null);
    }


    public IntegerArgument setBounds(int min, int max) {
        setMin(min);
        setMax(max);
        return this;
    }

    public IntegerArgument resetBounds() {
        resetMin();
        resetMax();
        return this;
    }

    public IntegerArgument setMin(int min) {
        this.min = min;
        updateUsage();
        return this;
    }

    public IntegerArgument setMax(int max) {
        this.max = max;
        updateUsage();
        return this;
    }

    public IntegerArgument resetMin() {
        this.min = null;
        updateUsage();
        return this;
    }

    public IntegerArgument resetMax() {
        this.max = null;
        updateUsage();
        return this;
    }

    private void updateUsage() {
        if(min != null && max != null) {
            super.setArgumentName(this.argName + " (" + min + "-" + max + ")");
            return;
        }

        if(min != null) {
            super.setArgumentName(this.argName + " (" + min + "+)");
            return;
        }

        if(max != null) {
            super.setArgumentName(this.argName + " (" + max + "-)");
            return;
        }

        super.setArgumentName(this.argName);
    }

    @Override
    public CommandArgument setArgumentName(String argumentName) {
        this.argName = argumentName;
        return this;
    }

    @Override
    public String getValidatedArg(String arg) {
        if(NumberUtils.isInteger(arg)) {
            int intArg = Integer.parseInt(arg);
            if ((min != null && intArg < min) || (max != null && intArg > max)) return null;
            return arg;
        }

        return null;
    }

}
