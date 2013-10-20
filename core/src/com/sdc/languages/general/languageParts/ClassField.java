package com.sdc.languages.general.languageParts;

import com.sdc.ast.expressions.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;


public abstract class ClassField {
    protected final String myModifier;
    protected final String myType;
    protected final String myName;

    protected Expression myInitializer;
    protected Set<Method> myConstructors = new HashSet<Method>();

    protected final int myTextWidth;
    protected final int myNestSize;

    public ClassField(final @NotNull String modifier,
                      final @NotNull String type,
                      final @NotNull String name,
                      final int textWidth,
                      final int nestSize) {
        this.myModifier = modifier;
        this.myType = type;
        this.myName = name;
        this.myTextWidth = textWidth;
        this.myNestSize = nestSize;
    }

    @NotNull
    public String getModifier() {
        return myModifier;
    }

    @NotNull
    public String getType() {
        return myType;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    public int getNestSize() {
        return myNestSize;
    }

    public void setInitializer(final @NotNull Expression initializer) {
        this.myInitializer = initializer;
    }

    @Nullable
    public Expression getInitializer() {
        return myInitializer;
    }

    public boolean hasInitializer() {
        return myInitializer != null;
    }

    public void addConstructor(final @NotNull Method method) {
        myConstructors.add(method);
    }

    public boolean hasInitializer(final @NotNull Method method) {
        return myConstructors.contains(method);
    }
}
