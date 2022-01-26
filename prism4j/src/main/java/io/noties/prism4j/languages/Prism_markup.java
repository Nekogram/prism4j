package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Aliases({"xml", "html", "mathml", "svg"})
public abstract class Prism_markup {

    private Prism_markup() {
    }

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Token entity = token("entity", pattern(compile("&#?[\\da-z]{1,8};", Pattern.CASE_INSENSITIVE)));
        final Grammar markup = grammar(
                "markup",
                token("comment", pattern(compile("<!--[\\s\\S]*?-->"))),
                token("prolog", pattern(compile("<\\?[\\s\\S]+?\\?>"))),
                token("doctype", pattern(compile("<!DOCTYPE[\\s\\S]+?>", Pattern.CASE_INSENSITIVE))),
                token("cdata", pattern(compile("<!\\[CDATA\\[[\\s\\S]*?]]>", Pattern.CASE_INSENSITIVE))),
                token(
                        "tag",
                        pattern(
                                compile("<\\/?(?!\\d)[^\\s>\\/=$<%]+(?:\\s+[^\\s>\\/=]+(?:=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+))?)*\\s*\\/?>", Pattern.CASE_INSENSITIVE),
                                false,
                                true,
                                null,
                                grammar(
                                        "inside",
                                        token(
                                                "tag",
                                                pattern(
                                                        compile("^<\\/?[^\\s>\\/]+", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        grammar(
                                                                "inside",
                                                                token("punctuation", pattern(compile("^<\\/?"))),
                                                                token("namespace", pattern(compile("^[^\\s>\\/:]+:")))
                                                        )
                                                )
                                        ),
                                        token(
                                                "attr-value",
                                                pattern(
                                                        compile("=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+)", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        grammar(
                                                                "inside",
                                                                token(
                                                                        "punctuation",
                                                                        pattern(compile("^=")),
                                                                        pattern(compile("(^|[^\\\\])[\"']"), true)
                                                                ),
                                                                entity
                                                        )
                                                )
                                        ),
                                        token("punctuation", pattern(compile("\\/?>"))),
                                        token(
                                                "attr-name",
                                                pattern(
                                                        compile("[^\\s>\\/]+"),
                                                        false,
                                                        false,
                                                        null,
                                                        grammar(
                                                                "inside",
                                                                token("namespace", pattern(compile("^[^\\s>\\/:]+:")))
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
                token(
                        "style",
                        pattern(
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
                token(
                        "style-attr",
                        pattern(
                                compile("\\s*style=(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1", CASE_INSENSITIVE),
                                false,
                                false,
                                "language-css",
                                grammar(
                                        "inside",
                                        token(
                                                "attr-name",
                                                pattern(
                                                        compile("^\\s*style", CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        markupTagInside
                                                )
                                        ),
                                        token("punctuation", pattern(compile("^\\s*=\\s*['\"]|['\"]\\s*$"))),
                                        token(
                                                "attr-value",
                                                pattern(
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

        return markup;
    }
}
