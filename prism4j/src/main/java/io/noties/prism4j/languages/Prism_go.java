package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_go {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar go = GrammarUtils.extend(
                prism4j.requireGrammar("clike"),
                "go",
                token -> !"class-name".equals(token.name()),
                token("string", pattern(
                        compile("(^|[^\\\\])\"(?:\\\\.|[^\"\\\\\\r\\n])*\"|`[^`]*`"),
                        false,
                        true
                )),
                token("keyword", pattern(compile("\\b(?:break|case|chan|const|continue|default|defer|else|fallthrough|for|func|go(?:to)?|if|import|interface|map|package|range|return|select|struct|switch|type|var)\\b"))),
                token("boolean", pattern(compile("\\b(?:_|iota|nil|true|false)\\b"))),
                token("number",
                        pattern(compile("\\b0(?:b[01_]+|o[0-7_]+)i?\\b", CASE_INSENSITIVE)),
                        pattern(compile("\\b0x(?:[a-f\\d_]+(?:\\.[a-f\\d_]*)?|\\.[a-f\\d_]+)(?:p[+-]?\\d+(?:_\\d+)*)?i?(?!\\w)", CASE_INSENSITIVE)),
                        pattern(compile("(?:\\b\\d[\\d_]*(?:\\.[\\d_]*)?|\\B\\.\\d[\\d_]*)(?:e[+-]?[\\d_]+)?i?(?!\\w)", CASE_INSENSITIVE))
                ),
                token("operator", pattern(compile("[*\\/%^!=]=?|\\+[=+]?|-[=-]?|\\|[=|]?|&(?:=|&|\\^=?)?|>(?:>=?|=)?|<(?:<=?|=|-)?|:=|\\.\\.\\.")))
        );

        GrammarUtils.insertBeforeToken(go, "string",
                token("char", pattern(compile("'(?:\\\\.|[^'\\\\\\r\\n]){0,10}'"), false, true))
        );

        // clike doesn't have builtin
        GrammarUtils.insertBeforeToken(go, "boolean",
                token("builtin", pattern(compile("\\b(?:bool|byte|complex(?:64|128)|error|float(?:32|64)|rune|string|u?int(?:8|16|32|64)?|uintptr|append|cap|close|complex|copy|delete|imag|len|make|new|panic|print(?:ln)?|real|recover)\\b")))
        );

        return go;
    }
}
