package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;

public class Text implements Node {

    private final String literal;

    public Text(@NotNull String literal) {
        this.literal = literal;
    }

    @Override
    public int textLength() {
        return literal.length();
    }

    @Override
    public final boolean isSyntax() {
        return false;
    }

    @NotNull
    public String literal() {
        return literal;
    }

    @Override
    public String toString() {
        return "TextImpl{" +
                "literal='" + literal + '\'' +
                '}';
    }
}
