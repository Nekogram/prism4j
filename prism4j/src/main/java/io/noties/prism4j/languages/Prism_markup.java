package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Aliases({"xml", "html", "mathml", "svg"})
public class Prism_markup {

    private Prism_markup() {
    }

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Token entity = GrammarUtils.token("entity", GrammarUtils.pattern(compile("&#?[\\da-z]{1,8};", Pattern.CASE_INSENSITIVE)));
        final Grammar markup = GrammarUtils.grammar(
                "markup",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("<!--[\\s\\S]*?-->"))),
                GrammarUtils.token("prolog", GrammarUtils.pattern(compile("<\\?[\\s\\S]+?\\?>"))),
                GrammarUtils.token("doctype", GrammarUtils.pattern(compile("<!DOCTYPE[\\s\\S]+?>", Pattern.CASE_INSENSITIVE))),
                GrammarUtils.token("cdata", GrammarUtils.pattern(compile("<!\\[CDATA\\[[\\s\\S]*?]]>", Pattern.CASE_INSENSITIVE))),
                GrammarUtils.token(
                        "tag",
                        GrammarUtils.pattern(
                                compile("<\\/?(?!\\d)[^\\s>\\/=$<%]+(?:\\s+[^\\s>\\/=]+(?:=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+))?)*\\s*\\/?>", Pattern.CASE_INSENSITIVE),
                                false,
                                true,
                                null,
                                GrammarUtils.grammar(
                                        "inside",
                                        GrammarUtils.token(
                                                "tag",
                                                GrammarUtils.pattern(
                                                        compile("^<\\/?[^\\s>\\/]+", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        GrammarUtils.grammar(
                                                                "inside",
                                                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^<\\/?"))),
                                                                GrammarUtils.token("namespace", GrammarUtils.pattern(compile("^[^\\s>\\/:]+:")))
                                                        )
                                                )
                                        ),
                                        GrammarUtils.token(
                                                "attr-value",
                                                GrammarUtils.pattern(
                                                        compile("=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+)", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        GrammarUtils.grammar(
                                                                "inside",
                                                                GrammarUtils.token(
                                                                        "punctuation",
                                                                        GrammarUtils.pattern(compile("^=")),
                                                                        GrammarUtils.pattern(compile("(^|[^\\\\])[\"']"), true)
                                                                ),
                                                                entity
                                                        )
                                                )
                                        ),
                                        GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\/?>"))),
                                        GrammarUtils.token(
                                                "attr-name",
                                                GrammarUtils.pattern(
                                                        compile("[^\\s>\\/]+"),
                                                        false,
                                                        false,
                                                        null,
                                                        GrammarUtils.grammar(
                                                                "inside",
                                                                GrammarUtils.token("namespace", GrammarUtils.pattern(compile("^[^\\s>\\/:]+:")))
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                entity
        );

        // modify with CSS
        markup.insertBeforeToken("tag",
                GrammarUtils.token(
                        "style",
                        GrammarUtils.pattern(
                                compile("(<style[\\s\\S]*?>)[\\s\\S]*?(?=</style>)", CASE_INSENSITIVE),
                                true,
                                true,
                                "language-css",
                                prism4j.requireGrammar("css")
                        )
                )
        );

        // important thing here is to clone found grammar
        // otherwise we will have stackoverflow (inside tag references style-attr, which
        // references inside tag, etc.)
        final Grammar markupTagInside;
        {
            Grammar _temp = null;
            final Token token = markup.findToken("tag");
            if (token != null) {
                _temp = Grammar.findFirstInsideGrammar(token);
                if (_temp != null) {
                    _temp = GrammarUtils.clone(_temp);
                }
            }
            markupTagInside = _temp;
        }

        markup.insertBeforeToken("tag/attr-value",
                GrammarUtils.token(
                        "style-attr",
                        GrammarUtils.pattern(
                                compile("\\s*style=(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1", CASE_INSENSITIVE),
                                false,
                                false,
                                "language-css",
                                GrammarUtils.grammar(
                                        "inside",
                                        GrammarUtils.token(
                                                "attr-name",
                                                GrammarUtils.pattern(
                                                        compile("^\\s*style", CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        markupTagInside
                                                )
                                        ),
                                        GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^\\s*=\\s*['\"]|['\"]\\s*$"))),
                                        GrammarUtils.token(
                                                "attr-value",
                                                GrammarUtils.pattern(
                                                        compile(".+", CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        prism4j.requireGrammar("css")
                                                )
                                        )

                                )
                        )
                )
        );

        // modify with JavaScript
        markup.insertBeforeToken("tag",
                GrammarUtils.token(
                        "script", GrammarUtils.pattern(
                                compile("(<script[\\s\\S]*?>)[\\s\\S]*?(?=</script>)", CASE_INSENSITIVE),
                                true,
                                true,
                                "language-javascript",
                                prism4j.requireGrammar("javascript")
                        )
                )
        );

        return markup;
    }
}
