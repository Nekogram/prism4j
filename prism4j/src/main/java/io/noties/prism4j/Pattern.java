package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Pattern {

    private final java.util.regex.Pattern regex;
    private final boolean lookbehind;
    private final boolean greedy;
    private final String alias;
    private final Grammar inside;

    public Pattern(
            @NotNull java.util.regex.Pattern regex,
            boolean lookbehind,
            boolean greedy,
            @Nullable String alias,
            @Nullable Grammar inside) {
        this.regex = regex;
        this.lookbehind = lookbehind;
        this.greedy = greedy;
        this.alias = alias;
        this.inside = inside;
    }

    @NotNull
    public java.util.regex.Pattern regex() {
        return regex;
    }

    public boolean lookbehind() {
        return lookbehind;
    }

    public boolean greedy() {
        return greedy;
    }

    @Nullable
    public String alias() {
        return alias;
    }

    @Nullable
    public Grammar inside() {
        return inside;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
