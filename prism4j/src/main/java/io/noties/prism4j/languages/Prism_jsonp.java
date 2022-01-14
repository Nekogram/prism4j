package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_jsonp {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        Grammar grammar = GrammarUtils.extend(GrammarUtils.require(prism4j, "clike"), "json",
                token("puctuation", pattern(compile("[{\\}\\[\\]();,.]"))));
        GrammarUtils.insertBeforeToken(grammar, "punctuation",
                token("function", pattern(compile("[_$a-zA-Z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*(?=\\s*\\()"))));
        return grammar;
    }
}
