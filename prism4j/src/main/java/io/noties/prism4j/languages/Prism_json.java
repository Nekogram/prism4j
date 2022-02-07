package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Aliases("webmanifest")
public class Prism_json {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar(
                "json",
                GrammarUtils.token("property", GrammarUtils.pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?=\\s*:)"), false, true)),
                GrammarUtils.token("string", GrammarUtils.pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?!\\s*:)"), false, true)),
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("//.*|/\\*[\\s\\S]*?(?:\\*/|$)"), false, true)),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("-?\\b\\d+(?:\\.\\d+)?(?:e[+-]?\\d+)?\\b", CASE_INSENSITIVE))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[{\\}\\[\\],]"))),
                // not sure about this one...
                GrammarUtils.token("operator", GrammarUtils.pattern(compile(":"))),
                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:true|false)\\b"))),
                GrammarUtils.token("null", GrammarUtils.pattern(compile("\\bnull\\b"), false, false, "keyword"))
        );
    }
}
