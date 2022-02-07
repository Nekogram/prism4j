package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;

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
