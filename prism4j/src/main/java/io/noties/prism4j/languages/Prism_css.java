package io.noties.prism4j.languages;

import io.noties.prism4j.*;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_css {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar css = grammar(
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
        final Token atrule = css.tokens().get(1);
        final Grammar inside = Grammar.findFirstInsideGrammar(atrule);
        if (inside != null) {
            for (Token token : css.tokens()) {
                if (!"atrule".equals(token.name())) {
                    inside.tokens().add(token);
                }
            }
        }

        // modify with CSS-Extras
        final Token selector = css.findToken("selector");
        if (selector != null) {
            final Pattern pattern = pattern(
                    compile("[^{}\\s][^{}]*(?=\\s*\\{)"),
                    false,
                    false,
                    null,
                    grammar("inside",
                            token("pseudo-element", pattern(compile(":(?:after|before|first-letter|first-line|selection)|::[-\\w]+"))),
                            token("pseudo-class", pattern(compile(":[-\\w]+(?:\\(.*\\))?"))),
                            token("class", pattern(compile("\\.[-:.\\w]+"))),
                            token("id", pattern(compile("#[-:.\\w]+"))),
                            token("attribute", pattern(compile("\\[[^\\]]+\\]")))
                    )
            );
            selector.patterns().clear();
            selector.patterns().add(pattern);
        }

        css.insertBeforeToken("function",
                token("hexcode", pattern(compile("#[\\da-f]{3,8}", CASE_INSENSITIVE))),
                token("entity", pattern(compile("\\\\[\\da-fA-F]{1,8}", CASE_INSENSITIVE))),
                token("number", pattern(compile("(-|)[\\d%.]+(px|)")))
        );


        return css;
    }
}
