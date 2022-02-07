package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_cpp {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        String keywordPattern = "\\b(?:alignas|alignof|asm|auto|bool|break|case|catch|char|char8_t|char16_t|char32_t|class|compl|concept|const|consteval|constexpr|constinit|const_cast|continue|co_await|co_return|co_yield|decltype|default|delete|do|double|dynamic_cast|else|enum|explicit|export|extern|final|float|for|friend|goto|if|import|inline|int|int8_t|int16_t|int32_t|int64_t|uint8_t|uint16_t|uint32_t|uint64_t|long|module|mutable|namespace|new|noexcept|nullptr|operator|override|private|protected|public|register|reinterpret_cast|requires|return|short|signed|sizeof|static|static_assert|static_cast|struct|switch|template|this|thread_local|throw|try|typedef|typeid|typename|union|unsigned|using|virtual|void|volatile|wchar_t|while)\\b";

        final Grammar cpp = prism4j.requireGrammar("c").extend(
                "cpp",
                GrammarUtils.token("class-name",
                        GrammarUtils.pattern(compile("(\\b(?:class|concept|enum|struct|typename)\\s+)(?!" + keywordPattern + ")\\w+"), true),
                        GrammarUtils.pattern(compile("\\b[A-Z]\\w*(?=\\s*::\\s*\\w+\\s*\\()")),
                        GrammarUtils.pattern(compile("\\b[A-Z_]\\w*(?=\\s*::\\s*~\\w+\\s*\\()", CASE_INSENSITIVE)),
                        GrammarUtils.pattern(compile("\\b\\w+(?=\\s*<(?:[^<>]|<(?:[^<>]|<[^<>]*>)*>)*>\\s*::\\s*\\w+\\s*\\()"))
                ),
                GrammarUtils.token("keyword", GrammarUtils.pattern(compile(keywordPattern))),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("(?:\\b0b[01']+|\\b0x(?:[\\da-f']+(?:\\.[\\da-f']*)?|\\.[\\da-f']+)(?:p[+-]?[\\d']+)?|(?:\\b[\\d']+(?:\\.[\\d']*)?|\\B\\.[\\d']+)(?:e[+-]?[\\d']+)?)[ful]{0,4}", CASE_INSENSITIVE), false, true)),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile(">>=?|<<=?|->|--|\\+\\+|&&|\\|\\||[?:~]|<=>|[-+*/%&|^!=<>]=?|\\b(?:and|and_eq|bitand|bitor|not|not_eq|or|or_eq|xor|xor_eq)\\b")))
        );

        cpp.insertBeforeToken("function",
                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:true|false)\\b")))
        );

        cpp.insertBeforeToken("string",
                GrammarUtils.token("module", GrammarUtils.pattern(
                        compile("(\\b(?:module|import)\\s+)(?:\"(?:\\\\(?:\\r\\n|[\\s\\S])|[^\"\\\\\\r\\n])*\"|<[^<>\\r\\n]*>|\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b(?:\\s*:\\s*\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b)?|:\\s*\\b(?!" + keywordPattern + ")\\w+(?:\\s*\\.\\s*\\w+)*\\b)"),
                        true, true, null,
                        GrammarUtils.grammar("inside", GrammarUtils.token("string", GrammarUtils.pattern(compile("^[<\"][\\s\\S]+"))), GrammarUtils.token("operator", GrammarUtils.pattern(compile(":"))), GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\."))))
                )),
                GrammarUtils.token("raw-string", GrammarUtils.pattern(compile("R\"([^()\\\\ ]{0,16})\\([\\s\\S]*?\\)\\1\""), false, true, "string"))
        );

        cpp.insertBeforeToken("keyword",
                GrammarUtils.token("generic-function", GrammarUtils.pattern(compile("\\b(?!operator\\b)[a-z_]\\w*\\s*<(?:[^<>]|<[^<>])*>*>(?=\\s*\\()", CASE_INSENSITIVE),
                                false, false, null,
                                GrammarUtils.grammar("inside",
                                        GrammarUtils.token("function", GrammarUtils.pattern(compile("^\\w+"))),
                                        GrammarUtils.token("generic", GrammarUtils.pattern(compile("<[\\s\\S]+"),
                                                false, false, "class-name", cpp)))
                        )
                )
        );

        cpp.insertBeforeToken("operator", GrammarUtils.token("double-colon", GrammarUtils.pattern(compile("::"))));

        final Grammar baseClause = GrammarUtils.clone(cpp);
        baseClause.insertBeforeToken("operator", GrammarUtils.token("class-name", GrammarUtils.pattern(compile("\\b[a-z_]\\w*\\b(?!\\s*::)", CASE_INSENSITIVE), false, true)));

        cpp.insertBeforeToken("class-name",
                GrammarUtils.token("base-clause", GrammarUtils.pattern(compile("(\\b(?:class|struct)\\s+\\w+\\s*:\\s*)[^;{\\}\"']+?(?=\\s*[;{])"), true, true, null, baseClause))
        );

        return cpp;
    }
}
