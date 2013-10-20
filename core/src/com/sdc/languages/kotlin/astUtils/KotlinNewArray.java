package com.sdc.languages.kotlin.astUtils;

import com.sdc.ast.expressions.Expression;
import com.sdc.ast.expressions.NewArray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class KotlinNewArray extends NewArray {
    private Expression myInitializer;

    public KotlinNewArray(final int dimensionsCount, final @NotNull String type, final @NotNull List<Expression> dimensions) {
        super(dimensionsCount, type, dimensions);
    }

    @Nullable
    public Expression getInitializer() {
        return myInitializer;
    }

    public void setInitializer(final @NotNull Expression initializer) {
        this.myInitializer = initializer;
    }
}
