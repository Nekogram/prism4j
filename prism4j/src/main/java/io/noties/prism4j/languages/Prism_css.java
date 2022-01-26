package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Modify;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_css {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar grammar = grammar(
                "css",
                token("comment", pattern(compile("\\/\\*[\\s\\S]*?\\*\\/"))),
                token(
                        "atrule",
                        pattern(
                                compile("@[\\w-]+?.*?(?:;|(?=\\s*\\{))", CASE_INSENSITIVE),
                                false,
                                false,
                                null,
                                grammar(
                                        "inside",
                                        token("rule", pattern(compile("@[\\w-]+")))
                                )
                        )
                ),
                token(
                        "url",
                        pattern(compile("url\\((?:([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1|.*?)\\)", CASE_INSENSITIVE))
                ),
                token("selector", pattern(compile("[^{\\}\\s][^{\\};]*?(?=\\s*\\{)"))),
                token(
                        "string",
                        pattern(compile("(\"|')(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                token(
                        "property",
                        pattern(compile("[-_a-z\\xA0-\\uFFFF][-\\w\\xA0-\\uFFFF]*(?=\\s*:)", CASE_INSENSITIVE))
                ),
                token("important", pattern(compile("\\B!important\\b", CASE_INSENSITIVE))),
                token("function", pattern(compile("[-a-z0-9]+(?=\\()", CASE_INSENSITIVE))),
                token("punctuation", pattern(compile("[(){\\};:]")))
        );

        // can we maybe add some helper to specify simplified location?

        // now we need to put the all tokens from grammar inside `atrule` (except the `atrule` of cause)
        final Token atrule = grammar.tokens().get(1);
        final Grammar inside = Grammar.findFirstInsideGrammar(atrule);
        if (inside != null) {
            for (Token token : grammar.tokens()) {
                if (!"atrule".equals(token.name())) {
                    inside.tokens().add(token);
                }
            }
        }

        return grammar;
    }
}
