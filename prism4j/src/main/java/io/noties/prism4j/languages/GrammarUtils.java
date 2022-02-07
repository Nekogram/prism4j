package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Pattern;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GrammarUtils {

    private static final Cloner CLONER = Cloner.create();

    private GrammarUtils() {
    }

    @NotNull
    public static Grammar clone(@NotNull Grammar grammar) {
        return CLONER.clone(grammar);
    }

    @NotNull
    public static Token clone(@NotNull Token token) {
        return CLONER.clone(token);
    }

    @NotNull
    public static Pattern clone(@NotNull Pattern pattern) {
        return CLONER.clone(pattern);
    }

    /**
     * Factory method to create a {@link Grammar}
     *
     * @param name   of the defined grammar
     * @param tokens a list of {@link Token}s
     * @return an instance of {@link Grammar}
     */
    @NotNull
    public static Grammar grammar(@NotNull String name, @NotNull List<Token> tokens) {
        return new Grammar(name, tokens);
    }

    @NotNull
    public static Grammar grammar(@NotNull String name, Token... tokens) {
        return new Grammar(name, toList(tokens));
    }

    @NotNull
    public static Token token(@NotNull String name, @NotNull List<Pattern> patterns) {
        return new Token(name, patterns);
    }

    @NotNull
    public static Token token(@NotNull String name, Pattern... patterns) {
        return new Token(name, toList(patterns));
    }

    @NotNull
    public static Pattern pattern(@NotNull java.util.regex.Pattern regex) {
        return new Pattern(regex, false, false, null, null);
    }

    @NotNull
    public static Pattern pattern(@NotNull java.util.regex.Pattern regex, boolean lookbehind) {
        return new Pattern(regex, lookbehind, false, null, null);
    }

    @NotNull
    public static Pattern pattern(
            @NotNull java.util.regex.Pattern regex,
            boolean lookbehind,
            boolean greedy) {
        return new Pattern(regex, lookbehind, greedy, null, null);
    }

    @NotNull
    public static Pattern pattern(
            @NotNull java.util.regex.Pattern regex,
            boolean lookbehind,
            boolean greedy,
            @Nullable String alias) {
        return new Pattern(regex, lookbehind, greedy, alias, null);
    }

    @NotNull
    public static Pattern pattern(
            @NotNull java.util.regex.Pattern regex,
            boolean lookbehind,
            boolean greedy,
            @Nullable String alias,
            @Nullable Grammar inside) {
        return new Pattern(regex, lookbehind, greedy, alias, inside);
    }

    @SafeVarargs
    @NotNull
    static <T> List<T> toList(T... args) {
        final int length = args != null
                ? args.length
                : 0;
        final List<T> list = new ArrayList<>(length);
        if (length > 0) {
            Collections.addAll(list, args);
        }
        return list;
    }

}
