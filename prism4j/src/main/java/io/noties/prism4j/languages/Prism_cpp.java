package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_cpp {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        String keywordPattern = "\\b(?:alignas|alignof|asm|auto|bool|break|case|catch|char|char8_t|char16_t|char32_t|class|compl|concept|const|consteval|constexpr|constinit|const_cast|continue|co_await|co_return|co_yield|decltype|default|delete|do|double|dynamic_cast|else|enum|explicit|export|extern|final|float|for|friend|goto|if|import|inline|int|int8_t|int16_t|int32_t|int64_t|uint8_t|uint16_t|uint32_t|uint64_t|long|module|mutable|namespace|new|noexcept|nullptr|operator|override|private|protected|public|register|reinterpret_cast|requires|return|short|signed|sizeof|static|static_assert|static_cast|struct|switch|template|this|thread_local|throw|try|typedef|typeid|typename|union|unsigned|using|virtual|void|volatile|wchar_t|while)\\b";

        final Grammar cpp = prism4j.requireGrammar("c").extend(
                "cpp",
                token("class-name",
                        pattern(compile("(\\b(?:class|concept|enum|struct|typename)\\s+)(?!" + keywordPattern + ")\\w+"), true),
                        pattern(compile("\\b[A-Z]\\w*(?=\\s*::\\s*\\w+\\s*\\()")),
                        pattern(compile("\\b[A-Z_]\\w*(?=\\s*::\\s*~\\w+\\s*\\()", CASE_INSENSITIVE)),
                        pattern(compile("\\b\\w+(?=\\s*<(?:[^<>]|<(?:[^<>]|<[^<>]*>)*>)*>\\s*::\\s*\\w+\\s*\\()"))
                ),
                token("keyword", pattern(compile(keywordPattern))),
                token("number", pattern(compile("(?:\\b0b[01']+|\\b0x(?:[\\da-f']+(?:\\.[\\da-f']*)?|\\.[\\da-f']+)(?:p[+-]?[\\d']+)?|(?:\\b[\\d']+(?:\\.[\\d']*)?|\\B\\.[\\d']+)(?:e[+-]?[\\d']+)?)[ful]{0,4}", CASE_INSENSITIVE), false, true)),
                token("operator", pattern(compile(">>=?|<<=?|->|--|\\+\\+|&&|\\|\\||[?:~]|<=>|[-+*/%&|^!=<>]=?|\\b(?:and|and_eq|bitand|bitor|not|not_eq|or|or_eq|xor|xor_eq)\\b")))
        );

        cpp.insertBeforeToken("function",
                token("boolean", pattern(compile("\\b(?:true|false)\\b")))
        );

        cpp.insertBeforeToken("string",
                token("module", pattern(
                        compile("(\\b(?:module|import)\\s+)(?:\"(?:\\\\(?:\\r\\n|[\\s\\S])|[^\"\\\\\\r\\n])*\"|<[^<>\\r\\n]*>|\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b(?:\\s*:\\s*\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b)?|:\\s*\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b)"),
                        true, true, null,
                        grammar("inside", token("string", pattern(compile("^[<\"][\\s\\S]+"))), token("operator", pattern(compile(":"))), token("punctuation", pattern(compile("\\."))))
                )),
                token("raw-string", pattern(compile("R\"([^()\\\\ ]{0,16})\\([\\s\\S]*?\\)\\1\""), false, true, "string"))
        );

        cpp.insertBeforeToken("keyword",
                token("generic-function", pattern(compile("\\b(?!operator\\b)[a-z_]\\w*\\s*<(?:[^<>]|<[^<>])*>*>(?=\\s*\\()", CASE_INSENSITIVE),
                                false, false, null,
                                grammar("inside",
                                        token("function", pattern(compile("^\\w+"))),
                                        token("generic", pattern(compile("<[\\s\\S]+"),
                                                false, false, "class-name", cpp)))
                        )
                )
        );

        cpp.insertBeforeToken("operator", token("double-colon", pattern(compile("::"))));

        final Grammar baseClause = GrammarUtils.clone(cpp);
        baseClause.insertBeforeToken("operator", token("class-name", pattern(compile("\\b[a-z_]\\w*\\b(?!\\s*::)", CASE_INSENSITIVE), false, true)));

        cpp.insertBeforeToken("class-name",
                token("base-clause", pattern(compile("(\\b(?:class|struct)\\s+\\w+\\s*:\\s*)[^;{\\}\"']+?(?=\\s*[;{])"), true, true, null, baseClause))
        );

        return cpp;
    }
}
