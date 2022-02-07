package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Pattern;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Cloner {

    public static Cloner create() {
        return new Cloner();
    }

    @NotNull
    Grammar clone(@NotNull Grammar grammar) {
        return clone(new Context(), grammar);
    }

    @NotNull
    Token clone(@NotNull Token token) {
        return clone(new Context(), token);
    }

    @NotNull
    Pattern clone(@NotNull Pattern pattern) {
        return clone(new Context(), pattern);
    }

    @NotNull
    private Grammar clone(@NotNull Context context, @NotNull Grammar grammar) {

        Grammar clone = context.grammar(grammar);
        if (clone != null) {
            return clone;
        }

        final List<Token> tokens = grammar.tokens();
        final List<Token> out = new ArrayList<>(tokens.size());

        clone = new Grammar(grammar.name(), out);
        context.save(grammar, clone);

        for (Token token : tokens) {
            out.add(clone(context, token));
        }

        return clone;
    }

    @NotNull
    private Token clone(@NotNull Context context, @NotNull Token token) {

        Token clone = context.token(token);
        if (clone != null) {
            return clone;
        }

        final List<Pattern> patterns = token.patterns();
        final List<Pattern> out = new ArrayList<>(patterns.size());

        clone = new Token(token.name(), out);
        context.save(token, clone);

        for (Pattern pattern : patterns) {
            out.add(clone(context, pattern));
        }

        return clone;
    }

    @NotNull
    private Pattern clone(@NotNull Context context, @NotNull Pattern pattern) {

        Pattern clone = context.pattern(pattern);
        if (clone != null) {
            return clone;
        }

        final Grammar inside = pattern.inside();

        clone = new Pattern(
                pattern.regex(),
                pattern.lookbehind(),
                pattern.greedy(),
                pattern.alias(),
                inside != null ? clone(context, inside) : null
        );

        context.save(pattern, clone);

        return clone;
    }

    private static class Context {

        private final Map<Integer, Object> cache = new HashMap<>(3);

        private static int key(@NotNull Object o) {
            return System.identityHashCode(o);
        }

        @Nullable
        public Grammar grammar(@NotNull Grammar origin) {
            return (Grammar) cache.get(key(origin));
        }

        @Nullable
        public Token token(@NotNull Token origin) {
            return (Token) cache.get(key(origin));
        }

        @Nullable
        public Pattern pattern(@NotNull Pattern origin) {
            return (Pattern) cache.get(key(origin));
        }

        public void save(@NotNull Grammar origin, @NotNull Grammar clone) {
            cache.put(key(origin), clone);
        }

        public void save(@NotNull Token origin, @NotNull Token clone) {
            cache.put(key(origin), clone);
        }

        public void save(@NotNull Pattern origin, @NotNull Pattern clone) {
            cache.put(key(origin), clone);
        }
    }
}
