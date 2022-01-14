package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Extend;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Extend("clike")
public class Prism_c {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Token commentToken = token("comment", pattern(compile("//(?:[^\\r\\n\\\\]|\\\\(?:\\r\\n?|\\n|(?![\\r\\n])))*|/\\*[\\s\\S]*?(?:\\*/|$)"), false, true));
        final Grammar c = GrammarUtils.extend(
                GrammarUtils.require(prism4j, "clike"),
                "c",
                token -> {
                    final String name = token.name();
                    return !"boolean".equals(name);
                },
                commentToken,
                token("string", pattern(compile("\"(?:\\\\(?:\\r\\n|[\\s\\S])|[^\"\\\\\\r\\n])*\""), false, true)),
                token("class-name", pattern(compile("(\\b(?:enum|struct)\\s+(?:__attribute__\\s*\\(\\([\\s\\S]*?\\)\\)\\s*)?)\\w+|\\b[a-z]\\w*_t\\b"), true)),
                token("keyword", pattern(compile("\\b(?:__attribute__|_Alignas|_Alignof|_Atomic|_Bool|_Complex|_Generic|_Imaginary|_Noreturn|_Static_assert|_Thread_local|asm|typeof|inline|auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)\\b"))),
                token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE))),
                token("number", pattern(compile("(?:\\b0x(?:[\\da-f]+(?:\\.[\\da-f]*)?|\\.[\\da-f]+)(?:p[+-]?\\d+)?|(?:\\b\\d+(?:\\.\\d*)?|\\B\\.\\d+)(?:e[+-]?\\d+)?)[ful]{0,4}", CASE_INSENSITIVE))),
                token("operator", pattern(compile(">>=?|<<=?|->|([-+&|:])\\1|[?:~]|[-+*/%&|^!=<>]=?")))
        );

        GrammarUtils.insertBeforeToken(c, "string",
                token("char", pattern(compile("'(?:\\\\(?:\\r\\n|[\\s\\S])|[^'\\\\\\r\\n]){0,32}'"))),
                token("macro", pattern(
                        compile("(^[\\t ]*)#\\s*[a-z](?:[^\\r\\n\\\\/]|/(?!\\*)|/\\*(?:[^*]|\\*(?!/))*\\*/|\\\\(?:\\r\\n|[\\s\\S]))*", CASE_INSENSITIVE | MULTILINE),
                        true,
                        true,
                        "property",
                        grammar("inside",
                                token("string",
                                        pattern(
                                                compile("^(#\\s*include\\s*)<[^>]+>"),
                                                true),
                                        Objects.requireNonNull(GrammarUtils.findToken(GrammarUtils.require(prism4j, "clike"), "string")).patterns().get(0)),
                                commentToken,
                                token("macro-name",
                                        pattern(compile("(^#\\s*define\\s+)\\w+\\b(?!\\()", CASE_INSENSITIVE), true),
                                        pattern(compile("(^#\\s*define\\s+)\\w+\\b(?=\\()", CASE_INSENSITIVE), true, false, "function")),
                                token("directive", pattern(
                                        compile("^(#\\s*)[a-z]+"),
                                        true,
                                        false,
                                        "keyword"
                                )),
                                token("directive-hash", pattern(compile("^#"))),
                                token("punctuation", pattern(compile("##|\\\\(?=[\\r\\n])"))),
                                token("expression", pattern(compile("\\S[\\s\\S]*"), false, false, null, c))
                        )
                )),
                token("constant", pattern(compile("\\b(?:__FILE__|__LINE__|__DATE__|__TIME__|__TIMESTAMP__|__func__|EOF|NULL|SEEK_CUR|SEEK_END|SEEK_SET|stdin|stdout|stderr)\\b")))
        );

        return c;
    }
}
