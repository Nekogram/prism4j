package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_clike {

    private Prism_clike() {
    }

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar(
                "clike",
                GrammarUtils.token(
                        "comment",
                        GrammarUtils.pattern(compile("(^|[^\\\\])/\\*[\\s\\S]*?(?:\\*/|$)"), true, true),
                        GrammarUtils.pattern(compile("(^|[^\\\\:])//.*"), true, true)
                ),
                GrammarUtils.token(
                        "string",
                        GrammarUtils.pattern(compile("([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                GrammarUtils.token(
                        "class-name",
                        GrammarUtils.pattern(
                                compile("(\\b(?:class|interface|extends|implements|trait|instanceof|new)\\s+|\\bcatch\\s+\\()[\\w.\\\\]+"),
                                true,
                                false,
                                null,
                                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[.\\\\]"))))
                        )
                ),
                GrammarUtils.token(
                        "keyword",
                        GrammarUtils.pattern(compile("\\b(?:if|else|while|do|for|return|in|instanceof|function|new|try|throw|catch|finally|null|break|continue)\\b"))
                ),
                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:true|false)\\b"))),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("\\b\\w+(?=\\()"))),
                GrammarUtils.token(
                        "number",
                        GrammarUtils.pattern(compile("\\b0x[\\da-f]+\\b|(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?", Pattern.CASE_INSENSITIVE))
                ),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[<>]=?|[!=]=?=?|--?|\\+\\+?|&&?|\\|\\|?|[?*/~^%]"))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[{\\}\\[\\];(),.:]")))
        );
    }
}
