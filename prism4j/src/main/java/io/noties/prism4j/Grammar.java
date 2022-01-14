package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Grammar {

    private final String name;
    private final List<Token> tokens;

    public Grammar(@NotNull String name, @NotNull List<Token> tokens) {
        this.name = name;
        this.tokens = tokens;
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public List<Token> tokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
