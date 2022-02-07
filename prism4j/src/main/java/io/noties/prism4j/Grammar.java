package io.noties.prism4j;

import io.noties.prism4j.languages.GrammarUtils;
import io.noties.prism4j.languages.TokenFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    @NotNull
    public Grammar extend(
            @NotNull String name,
            Token... tokens) {

        // we clone the whole grammar, but override top-most tokens that are passed here

        final int size = tokens != null
                ? tokens.length
                : 0;

        if (size == 0) {
            return new Grammar(name, GrammarUtils.clone(this).tokens());
        }

        final Map<String, Token> overrides = new HashMap<>(size);
        for (Token token : tokens) {
            overrides.put(token.name(), token);
        }

        final List<Token> origins = tokens();
        final List<Token> out = new ArrayList<>(origins.size());

        Token override;

        for (Token origin : origins) {
            override = overrides.get(origin.name());
            if (override != null) {
                out.add(override);
            } else {
                out.add(GrammarUtils.clone(origin));
            }
        }

        return new Grammar(name, out);
    }

    @NotNull
    public Grammar extend(
            @NotNull String name,
            @NotNull TokenFilter filter,
            Token... tokens) {

        final int size = tokens != null
                ? tokens.length
                : 0;

        final Map<String, Token> overrides;
        if (size == 0) {
            overrides = Collections.emptyMap();
        } else {
            overrides = new HashMap<>(size);
            for (Token token : tokens) {
                overrides.put(token.name(), token);
            }
        }

        final List<Token> origins = tokens();
        final List<Token> out = new ArrayList<>(origins.size());

        Token override;

        for (Token origin : origins) {

            // filter out undesired tokens
            if (!filter.test(origin)) {
                continue;
            }

            override = overrides.get(origin.name());
            if (override != null) {
                out.add(override);
            } else {
                out.add(GrammarUtils.clone(origin));
            }
        }

        return new Grammar(name, out);
    }

    /**
     * Helper method to find a token inside grammar. Supports lookup in `inside` grammars. For
     * example given the path: {@code first-token/then-another/and-more } this method will do:
     * <ul>
     * <li>Look for `first-token` at root level of supplied grammar</li>
     * <li>If it\'s found search for first pattern with `inside` grammar</li>
     * <li>If it\'s found search for `then-another` token in this inside grammar</li>
     * <li>etc</li>
     * </ul>
     * Simple path {@code simple-root-level } is also supported
     *
     * @param path    argument to find a {@link Token}
     * @return a found {@link Token} or null
     */
    @Nullable
    public Token findToken(@NotNull String path) {
        final String[] parts = path.split("/");
        return findToken(parts, 0);
    }

    @Nullable
    private Token findToken(@NotNull String[] parts, int index) {

        final String part = parts[index];
        final boolean last = index == parts.length - 1;

        for (Token token : tokens()) {
            if (part.equals(token.name())) {
                if (last) {
                    return token;
                } else {
                    final Grammar inside = findFirstInsideGrammar(token);
                    if (inside != null) {
                        return findToken(parts, index + 1);
                    } else {
                        break;
                    }
                }
            }
        }

        return null;
    }

    // won't work if there are multiple patterns provided for a token (each with inside grammar)
    public void insertBeforeToken(
            @NotNull String path,
            Token... tokens
    ) {

        if (tokens == null
                || tokens.length == 0) {
            return;
        }

        final String[] parts = path.split("/");

        insertBeforeToken(this, parts, 0, tokens);
    }

    private void insertBeforeToken(
            @NotNull Grammar grammar,
            @NotNull String[] parts,
            int index,
            @NotNull Token[] tokens) {

        final String part = parts[index];
        final boolean last = index == parts.length - 1;

        final List<Token> grammarTokens = grammar.tokens();

        Token token;

        for (int i = 0, size = grammarTokens.size(); i < size; i++) {

            token = grammarTokens.get(i);

            if (part.equals(token.name())) {

                // here we must decide what to do next:
                //  - it can be out found one
                //  - or we need to go deeper (c)
                if (last) {
                    // here we go, it's our token
                    insertTokensAt(i, grammarTokens, tokens);
                } else {
                    // now we must find a grammar that is inside
                    // token can have multiple patterns
                    // but as they are not identified somehow (no name or anything)
                    // we will try to find first pattern with inside grammar
                    final Grammar inside = findFirstInsideGrammar(token);
                    if (inside != null) {
                        insertBeforeToken(inside, parts, index + 1, tokens);
                    }
                }

                // break after we have found token with specified name (most likely it won't repeat itself)
                break;
            }
        }
    }

    @Nullable
    public static Grammar findFirstInsideGrammar(@NotNull Token token) {
        Grammar grammar = null;
        for (Pattern pattern : token.patterns()) {
            if (pattern.inside() != null) {
                grammar = pattern.inside();
                break;
            }
        }
        return grammar;
    }

    private static void insertTokensAt(
            int start,
            @NotNull List<Token> grammarTokens,
            @NotNull Token[] tokens
    ) {
        for (int i = 0, length = tokens.length; i < length; i++) {
            grammarTokens.add(start + i, tokens[i]);
        }
    }
}
