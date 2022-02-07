package io.noties.prism4j;

/**
 * Basic structure that represents parsing state
 *
 * @see Text
 * @see Syntax
 */
public interface Node {

    /**
     * @return raw text length. For {@link Text} node it\'s {@link Text#literal()} length
     * and for {@link Syntax} it is {@link Syntax#matchedString()} length
     */
    int textLength();

    /**
     * As we have only two types maybe doing a lot of `instanceof` checks is not that required
     *
     * @return a boolean indicating if this node is an instance of {@link Syntax}
     */
    boolean isSyntax();
}
