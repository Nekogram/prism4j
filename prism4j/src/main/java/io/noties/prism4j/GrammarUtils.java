package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;

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
     * Used when extending an existing grammar to filter out tokens that should not be cloned.
     *
     * @see Grammar#extend(String, TokenFilter, Token...)
     */
    public interface TokenFilter {

        /**
         * @param token {@link Token} to validate
         * @return a boolean indicating if supplied token should be included (passes the test)
         */
        boolean test(@NotNull Token token);
    }
}
