package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Syntax implements Prism4j.Node {

    private final String type;
    private final List<? extends Prism4j.Node> children;
    private final String alias;
    private final String matchedString;
    private final boolean greedy;
    private final boolean tokenized;

    public Syntax(
            @NotNull String type,
            @NotNull List<? extends Prism4j.Node> children,
            @Nullable String alias,
            @NotNull String matchedString,
            boolean greedy,
            boolean tokenized) {
        this.type = type;
        this.children = children;
        this.alias = alias;
        this.matchedString = matchedString;
        this.greedy = greedy;
        this.tokenized = tokenized;
    }

    @Override
    public int textLength() {
        return matchedString.length();
    }

    @Override
    public final boolean isSyntax() {
        return true;
    }

    @NotNull
    public String type() {
        return type;
    }

    @NotNull
    public List<? extends Prism4j.Node> children() {
        return children;
    }

    @Nullable
    public String alias() {
        return alias;
    }

    @NotNull
    public String matchedString() {
        return matchedString;
    }

    public boolean greedy() {
        return greedy;
    }

    public boolean tokenized() {
        return tokenized;
    }

    @Override
    public String toString() {
        return "SyntaxImpl{" +
                "type='" + type + '\'' +
                ", children=" + children +
                ", alias='" + alias + '\'' +
                ", matchedString='" + matchedString + '\'' +
                ", greedy=" + greedy +
                ", tokenized=" + tokenized +
                '}';
    }
}
