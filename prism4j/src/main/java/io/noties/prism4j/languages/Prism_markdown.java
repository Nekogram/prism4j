package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Extend;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Extend("markup")
public class Prism_markdown {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar markdown = GrammarUtils.extend(
                GrammarUtils.require(prism4j, "markup"),
                "markdown"
        );

        final Token bold = token("bold", pattern(
                compile("(^|[^\\\\])(\\*\\*|__)(?:(?:\\r?\\n|\\r)(?!\\r?\\n|\\r)|.)+?\\2"),
                true,
                false,
                null,
                grammar("inside", token("punctuation", pattern(compile("^\\*\\*|^__|\\*\\*$|__$"))))
        ));

        final Token italic = token("italic", pattern(
                compile("(^|[^\\\\])([*_])(?:(?:\\r?\\n|\\r)(?!\\r?\\n|\\r)|.)+?\\2"),
                true,
                false,
                null,
                grammar("inside", token("punctuation", pattern(compile("^[*_]|[*_]$"))))
        ));

        final Token url = token("url", pattern(
                compile("!?\\[[^\\]]+\\](?:\\([^\\s)]+(?:[\\t ]+\"(?:\\\\.|[^\"\\\\])*\")?\\)| ?\\[[^\\]\\n]*\\])"),
                false,
                false,
                null,
                grammar("inside",
                        token("variable", pattern(compile("(!?\\[)[^\\]]+(?=\\]$)"), true)),
                        token("string", pattern(compile("\"(?:\\\\.|[^\"\\\\])*\"(?=\\)$)")))
                )
        ));

        GrammarUtils.insertBeforeToken(markdown, "prolog",
                token("blockquote", pattern(compile("^>(?:[\\t ]*>)*", MULTILINE))),
                token("code",
                        pattern(compile("^(?: {4}|\\t).+", MULTILINE), false, false, "keyword"),
                        pattern(compile("``.+?``|`[^`\\n]+`"), false, false, "keyword")
                ),
                token(
                        "title",
                        pattern(
                                compile("\\w+.*(?:\\r?\\n|\\r)(?:==+|--+)"),
                                false,
                                false,
                                "important",
                                grammar("inside", token("punctuation", pattern(compile("==+$|--+$"))))
                        ),
                        pattern(
                                compile("(^\\s*)#+.+", MULTILINE),
                                true,
                                false,
                                "important",
                                grammar("inside", token("punctuation", pattern(compile("^#+|#+$"))))
                        )
                ),
                token("hr", pattern(
                        compile("(^\\s*)([*-])(?:[\\t ]*\\2){2,}(?=\\s*$)", MULTILINE),
                        true,
                        false,
                        "punctuation"
                )),
                token("list", pattern(
                        compile("(^\\s*)(?:[*+-]|\\d+\\.)(?=[\\t ].)", MULTILINE),
                        true,
                        false,
                        "punctuation"
                )),
                token("url-reference", pattern(
                        compile("!?\\[[^\\]]+\\]:[\\t ]+(?:\\S+|<(?:\\\\.|[^>\\\\])+>)(?:[\\t ]+(?:\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|\\((?:\\\\.|[^)\\\\])*\\)))?"),
                        false,
                        false,
                        "url",
                        grammar("inside",
                                token("variable", pattern(compile("^(!?\\[)[^\\]]+"), true)),
                                token("string", pattern(compile("(?:\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|\\((?:\\\\.|[^)\\\\])*\\))$"))),
                                token("punctuation", pattern(compile("^[\\[\\]!:]|[<>]")))
                        )
                )),
                bold,
                italic,
                url
        );

        add(GrammarUtils.findFirstInsideGrammar(bold), url, italic);
        add(GrammarUtils.findFirstInsideGrammar(italic), url, bold);

        return markdown;
    }

    private static void add(@Nullable Grammar grammar, @NotNull Token first, @NotNull Token second) {
        if (grammar != null) {
            grammar.tokens().add(first);
            grammar.tokens().add(second);
        }
    }
}
