package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.languages.GrammarUtils.pattern;
import static io.noties.prism4j.languages.GrammarUtils.token;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_jsonp {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        Grammar jsonp = prism4j.requireGrammar("clike").extend("jsonp",
                token("puctuation", pattern(compile("[{\\}\\[\\]();,.]"))));
        jsonp.insertBeforeToken("punctuation",
                token("function", pattern(compile("[_$a-zA-Z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*(?=\\s*\\()"))));
        return jsonp;
    }
}
