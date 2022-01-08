package io.noties.prism4j;

import org.junit.Test;

public class ToStringTest {

    @Test
    public void test() {

        final GrammarLocator locator = new DefaultGrammarLocator();
        final Prism4j prism4j = new Prism4j(locator);

        Prism4j.Grammar grammar;

        for (String language : locator.languages()) {
            grammar = prism4j.grammar(language);
            if (grammar != null) {
                System.err.printf("language: %s, toString: %s%n", language, ToString.toString(grammar));
            }
        }
    }
}
