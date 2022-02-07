package io.noties.prism4j.languages;

import io.noties.prism4j.*;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_css {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar css = GrammarUtils.grammar(
                "css",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("\\/\\*[\\s\\S]*?\\*\\/"))),
                GrammarUtils.token(
                        "atrule",
                        GrammarUtils.pattern(
                                compile("@[\\w-]+?.*?(?:;|(?=\\s*\\{))", CASE_INSENSITIVE),
                                false,
                                false,
                                null,
                                GrammarUtils.grammar(
                                        "inside",
                                        GrammarUtils.token("rule", GrammarUtils.pattern(compile("@[\\w-]+")))
                                )
                        )
                ),
                GrammarUtils.token(
                        "url",
                        GrammarUtils.pattern(compile("url\\((?:([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1|.*?)\\)", CASE_INSENSITIVE))
                ),
                GrammarUtils.token("selector", GrammarUtils.pattern(compile("[^{\\}\\s][^{\\};]*?(?=\\s*\\{)"))),
                GrammarUtils.token(
                        "string",
                        GrammarUtils.pattern(compile("(\"|')(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                GrammarUtils.token(
                        "property",
                        GrammarUtils.pattern(compile("[-_a-z\\xA0-\\uFFFF][-\\w\\xA0-\\uFFFF]*(?=\\s*:)", CASE_INSENSITIVE))
                ),
                GrammarUtils.token("important", GrammarUtils.pattern(compile("\\B!important\\b", CASE_INSENSITIVE))),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("[-a-z0-9]+(?=\\()", CASE_INSENSITIVE))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[(){\\};:]")))
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
            final Pattern pattern = GrammarUtils.pattern(
                    compile("[^{}\\s][^{}]*(?=\\s*\\{)"),
                    false,
                    false,
                    null,
                    GrammarUtils.grammar("inside",
                            GrammarUtils.token("pseudo-element", GrammarUtils.pattern(compile(":(?:after|before|first-letter|first-line|selection)|::[-\\w]+"))),
                            GrammarUtils.token("pseudo-class", GrammarUtils.pattern(compile(":[-\\w]+(?:\\(.*\\))?"))),
                            GrammarUtils.token("class", GrammarUtils.pattern(compile("\\.[-:.\\w]+"))),
                            GrammarUtils.token("id", GrammarUtils.pattern(compile("#[-:.\\w]+"))),
                            GrammarUtils.token("attribute", GrammarUtils.pattern(compile("\\[[^\\]]+\\]")))
                    )
            );
            selector.patterns().clear();
            selector.patterns().add(pattern);
        }

        css.insertBeforeToken("function",
                GrammarUtils.token("hexcode", GrammarUtils.pattern(compile("#[\\da-f]{3,8}", CASE_INSENSITIVE))),
                GrammarUtils.token("entity", GrammarUtils.pattern(compile("\\\\[\\da-fA-F]{1,8}", CASE_INSENSITIVE))),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("(-|)[\\d%.]+(px|)")))
        );


        return css;
    }
}
