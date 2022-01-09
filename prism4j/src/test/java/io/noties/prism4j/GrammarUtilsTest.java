package io.noties.prism4j;

import ix.Ix;
import org.junit.Before;
import org.junit.Test;

public class GrammarUtilsTest {

    private GrammarLocator grammarLocator;
    private Prism4j prism4j;

    @Before
    public void before() {
        grammarLocator = new DefaultGrammarLocator();
        prism4j = new Prism4j(grammarLocator);
    }

    @Test
    public void clone_grammar() {

        Ix.from(grammarLocator.languages())
                .orderBy(s -> s)
                .foreach(s -> {
                    final Prism4j.Grammar grammar = prism4j.grammar(s);
                    if (grammar != null) {
                        System.err.printf("cloning language: %s%n", s);
                        GrammarUtils.clone(grammar);
                    }
                });
    }
}