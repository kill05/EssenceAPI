package com.github.kill05.essenceapi.core.commands.arguments;

import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class EnumConstantArgument<E extends Enum<E>> extends CommandArgument {

    private final Class<E> enumClass;

    public EnumConstantArgument(Class<E> enumClass, E defaultValue) {
        super(enumClass.getSimpleName().toLowerCase(Locale.ENGLISH), defaultValue == null ? null : defaultValue.name());
        this.enumClass = enumClass;

        setTabCompleter(Arrays.stream(enumClass.getEnumConstants())
                .map(enumConstant -> enumConstant.name().toLowerCase(Locale.ENGLISH))
                .collect(Collectors.toList())
        );
    }

    public EnumConstantArgument(Class<E> argEnum){
        this(argEnum, null);
    }


    @Override
    public String getValidatedArg(String arg) {
        E constant = EnumUtils.getEnumIgnoreCase(enumClass, arg);
        return constant != null ? constant.name() : null;
    }


}
