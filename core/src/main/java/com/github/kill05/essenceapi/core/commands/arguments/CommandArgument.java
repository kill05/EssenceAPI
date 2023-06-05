package com.github.kill05.essenceapi.core.commands.arguments;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class CommandArgument {

    private Supplier<String> defaultValue;
    private String argumentName;
    private boolean optional;
    private Supplier<List<String>> tabCompleter;

    public CommandArgument(String argumentName, String defaultValue) {
        this.argumentName = argumentName;
        this.defaultValue = () -> defaultValue;
    }

    /**
     * Return null if arg is invalid
     */
    public abstract String getValidatedArg(String arg);

    public boolean isArgValid(String arg) {
        return getValidatedArg(arg) != null;
    }

    public CommandArgument setDefaultValue(Supplier<String> supplier) {
        this.defaultValue = supplier;
        return this;
    }

    public String getDefaultValue(CommandSender sender){
        return defaultValue != null ? defaultValue.get() : null;
    }

    public boolean hasDefaultValue(CommandSender sender){
        return getDefaultValue(sender) != null;
    }


    public CommandArgument setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public boolean isOptional(CommandSender sender) {
        return optional || hasDefaultValue(sender);
    }


    public String getArgumentName(){
        return this.argumentName;
    }

    public CommandArgument setArgumentName(String argumentName){
        this.argumentName = argumentName;
        return this;
    }

    /**
     * Creates a modular tab completer that can change after the command is built.
     * Example: If you want an argument for players, you need to provide a dynamic one as players can join and quit after the command is registered.
     * If you provide a static tab completer in this case, the players in the tab completion will be the players that were online at the time the command was registered.
     *
     * @param tabCompleter a Supplier<List<String>> that dynamically provides the arguments.
     * @return an instance of this CommandArgument to allow for method chaining.
     */
    public CommandArgument setTabCompleter(Supplier<List<String>> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    /**
     * Creates a static tab completer that doesn't update its values after the command is built.
     * Example: if you want to make a command to spawn custom bosses and your bosses can only be changed if the server stops/your plugin is disabled,
     * you can use this kind of completer.
     *
     * @param tabCompleter a List<String> that holds the arguments.
     * @return an instance of this CommandArgument to allow for method chaining.
     */
    public CommandArgument setTabCompleter(List<?> tabCompleter){
        List<String> completers = new ArrayList<>();

        tabCompleter.forEach(completer -> completers.add(String.valueOf(completer)));
        this.tabCompleter = () -> completers;

        return this;
    }

    public CommandArgument setTabCompleter(String... tabCompleter){
        return setTabCompleter(Arrays.asList(tabCompleter));
    }

    public List<String> getTabCompleter() {
        return tabCompleter == null ? new ArrayList<>() : tabCompleter.get();
    }

}
