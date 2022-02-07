package io.noties.prism4j.languages;


import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases("dotnet")
public class Prism_csharp {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar classNameInsidePunctuation = GrammarUtils.grammar("inside",
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\.")))
        );

        final Grammar csharp = prism4j.requireGrammar("clike").extend(
                "csharp",
                GrammarUtils.token("keyword", GrammarUtils.pattern(compile("\\b(?:abstract|add|alias|as|ascending|async|await|base|bool|break|byte|case|catch|char|checked|class|const|continue|decimal|default|delegate|descending|do|double|dynamic|else|enum|event|explicit|extern|false|finally|fixed|float|for|foreach|from|get|global|goto|group|if|implicit|in|int|interface|internal|into|is|join|let|lock|long|namespace|new|null|object|operator|orderby|out|override|params|partial|private|protected|public|readonly|ref|remove|return|sbyte|sealed|select|set|short|sizeof|stackalloc|static|string|struct|switch|this|throw|true|try|typeof|uint|ulong|unchecked|unsafe|ushort|using|value|var|virtual|void|volatile|where|while|yield)\\b"))),
                GrammarUtils.token("string",
                        GrammarUtils.pattern(compile("@(\"|')(?:\\1\\1|\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1"), false, true),
                        GrammarUtils.pattern(compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*?\\1"), false, true)
                ),
                GrammarUtils.token("class-name",
                        GrammarUtils.pattern(
                                compile("\\b[A-Z]\\w*(?:\\.\\w+)*\\b(?=\\s+\\w+)"),
                                false,
                                false,
                                null,
                                classNameInsidePunctuation
                        ),
                        GrammarUtils.pattern(
                                compile("(\\[)[A-Z]\\w*(?:\\.\\w+)*\\b"),
                                true,
                                false,
                                null,
                                classNameInsidePunctuation
                        ),
                        GrammarUtils.pattern(
                                compile("(\\b(?:class|interface)\\s+[A-Z]\\w*(?:\\.\\w+)*\\s*:\\s*)[A-Z]\\w*(?:\\.\\w+)*\\b"),
                                true,
                                false,
                                null,
                                classNameInsidePunctuation
                        ),
                        GrammarUtils.pattern(
                                compile("((?:\\b(?:class|interface|new)\\s+)|(?:catch\\s+\\())[A-Z]\\w*(?:\\.\\w+)*\\b"),
                                true,
                                false,
                                null,
                                classNameInsidePunctuation
                        )
                ),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("\\b0x[\\da-f]+\\b|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)f?", CASE_INSENSITIVE)))
        );

        csharp.insertBeforeToken("class-name",
                GrammarUtils.token("generic-method", GrammarUtils.pattern(
                        compile("\\w+\\s*<[^>\\r\\n]+?>\\s*(?=\\()"),
                        false,
                        false,
                        null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("function", GrammarUtils.pattern(compile("^\\w+"))),
                                GrammarUtils.token("class-name", GrammarUtils.pattern(compile("\\b[A-Z]\\w*(?:\\.\\w+)*\\b"), false, false, null, classNameInsidePunctuation)),
                                csharp.findToken("keyword"),
                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[<>(),.:]")))
                        )
                )),
                GrammarUtils.token("preprocessor", GrammarUtils.pattern(
                        compile("(^\\s*)#.*", MULTILINE),
                        true,
                        false,
                        "property",
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("directive", GrammarUtils.pattern(
                                        compile("(\\s*#)\\b(?:define|elif|else|endif|endregion|error|if|line|pragma|region|undef|warning)\\b"),
                                        true,
                                        false,
                                        "keyword"
                                ))
                        )
                ))
        );

        return csharp;
    }
}
