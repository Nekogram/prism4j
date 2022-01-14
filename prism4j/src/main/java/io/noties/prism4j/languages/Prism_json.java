package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
@Aliases("webmanifest")
public class Prism_json {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar(
                "json",
                token("property", pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?=\\s*:)"), false, true)),
                token("string", pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?!\\s*:)"), false, true)),
                token("comment", pattern(compile("//.*|/\\*[\\s\\S]*?(?:\\*/|$)"), false, true)),
                token("number", pattern(compile("-?\\b\\d+(?:\\.\\d+)?(?:e[+-]?\\d+)?\\b", CASE_INSENSITIVE))),
                token("punctuation", pattern(compile("[{}\\[\\],]"))),
                // not sure about this one...
                token("operator", pattern(compile(":"))),
                token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
                token("null", pattern(compile("\\bnull\\b"), false, false, "keyword"))
        );
    }
}
