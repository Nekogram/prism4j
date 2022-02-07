package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_markdown {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar markdown = prism4j.requireGrammar("markup").extend(
                "markdown"
        );

        final Token bold = GrammarUtils.token("bold", GrammarUtils.pattern(
                compile("(^|[^\\\\])(\\*\\*|__)(?:(?:\\r?\\n|\\r)(?!\\r?\\n|\\r)|.)+?\\2"),
                true,
                false,
                null,
                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^\\*\\*|^__|\\*\\*$|__$"))))
        ));

        final Token italic = GrammarUtils.token("italic", GrammarUtils.pattern(
                compile("(^|[^\\\\])([*_])(?:(?:\\r?\\n|\\r)(?!\\r?\\n|\\r)|.)+?\\2"),
                true,
                false,
                null,
                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^[*_]|[*_]$"))))
        ));

        final Token url = GrammarUtils.token("url", GrammarUtils.pattern(
                compile("!?\\[[^\\]]+\\](?:\\([^\\s)]+(?:[\\t ]+\"(?:\\\\.|[^\"\\\\])*\")?\\)| ?\\[[^\\]\\n]*\\])"),
                false,
                false,
                null,
                GrammarUtils.grammar("inside",
                        GrammarUtils.token("variable", GrammarUtils.pattern(compile("(!?\\[)[^\\]]+(?=\\]$)"), true)),
                        GrammarUtils.token("string", GrammarUtils.pattern(compile("\"(?:\\\\.|[^\"\\\\])*\"(?=\\)$)")))
                )
        ));

        markdown.insertBeforeToken("prolog",
                GrammarUtils.token("blockquote", GrammarUtils.pattern(compile("^>(?:[\\t ]*>)*", MULTILINE))),
                GrammarUtils.token("code",
                        GrammarUtils.pattern(compile("^(?: {4}|\\t).+", MULTILINE), false, false, "keyword"),
                        GrammarUtils.pattern(compile("``.+?``|`[^`\\n]+`"), false, false, "keyword")
                ),
                GrammarUtils.token(
                        "title",
                        GrammarUtils.pattern(
                                compile("\\w+.*(?:\\r?\\n|\\r)(?:==+|--+)"),
                                false,
                                false,
                                "important",
                                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("==+$|--+$"))))
                        ),
                        GrammarUtils.pattern(
                                compile("(^\\s*)#+.+", MULTILINE),
                                true,
                                false,
                                "important",
                                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^#+|#+$"))))
                        )
                ),
                GrammarUtils.token("hr", GrammarUtils.pattern(
                        compile("(^\\s*)([*-])(?:[\\t ]*\\2){2,}(?=\\s*$)", MULTILINE),
                        true,
                        false,
                        "punctuation"
                )),
                GrammarUtils.token("list", GrammarUtils.pattern(
                        compile("(^\\s*)(?:[*+-]|\\d+\\.)(?=[\\t ].)", MULTILINE),
                        true,
                        false,
                        "punctuation"
                )),
                GrammarUtils.token("url-reference", GrammarUtils.pattern(
                        compile("!?\\[[^\\]]+\\]:[\\t ]+(?:\\S+|<(?:\\\\.|[^>\\\\])+>)(?:[\\t ]+(?:\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|\\((?:\\\\.|[^)\\\\])*\\)))?"),
                        false,
                        false,
                        "url",
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("variable", GrammarUtils.pattern(compile("^(!?\\[)[^\\]]+"), true)),
                                GrammarUtils.token("string", GrammarUtils.pattern(compile("(?:\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|\\((?:\\\\.|[^)\\\\])*\\))$"))),
                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^[\\[\\]!:]|[<>]")))
                        )
                )),
                bold,
                italic,
                url
        );

        add(Grammar.findFirstInsideGrammar(bold), url, italic);
        add(Grammar.findFirstInsideGrammar(italic), url, bold);

        return markdown;
    }

    private static void add(@Nullable Grammar grammar, @NotNull Token first, @NotNull Token second) {
        if (grammar != null) {
            grammar.tokens().add(first);
            grammar.tokens().add(second);
        }
    }
}
