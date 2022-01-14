package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Token {

    private final String name;
    private final List<Pattern> patterns;

    public Token(@NotNull String name, @NotNull List<Pattern> patterns) {
        this.name = name;
        this.patterns = patterns;
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public List<Pattern> patterns() {
        return patterns;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
