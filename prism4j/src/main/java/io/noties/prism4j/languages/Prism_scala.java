package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_scala {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Grammar scala = prism4j.requireGrammar("java").extend(
                "scala",
                token -> {
                    final String name = token.name();
                    return !"class-name".equals(name) && !"function".equals(name);
                },
                GrammarUtils.token("triple-quoted-string", GrammarUtils.pattern(compile("\"\"\"[\\s\\S]*?\"\"\""), false, true, "string")),
                GrammarUtils.token("string",
                        GrammarUtils.pattern(compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                GrammarUtils.token("keyword", GrammarUtils.pattern(
                        compile("<-|=>|\\b(?:abstract|case|catch|class|def|do|else|extends|final|finally|for|forSome|if|implicit|import|lazy|match|new|null|object|override|package|private|protected|return|sealed|self|super|this|throw|trait|try|type|val|var|while|with|yield)\\b")
                )),
                GrammarUtils.token("number", GrammarUtils.pattern(
                        compile("\\b0x[\\da-f]*\\.?[\\da-f]+|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:e\\d+)?[dfl]?", CASE_INSENSITIVE)
                ))
        );

        scala.tokens().add(
                GrammarUtils.token("symbol", GrammarUtils.pattern(compile("'[^\\d\\s\\\\]\\w*")))
        );

        scala.insertBeforeToken("number",
                GrammarUtils.token("builtin", GrammarUtils.pattern(compile("\\b(?:String|Int|Long|Short|Byte|Boolean|Double|Float|Char|Any|AnyRef|AnyVal|Unit|Nothing)\\b")))
        );

        scala.insertBeforeToken("triple-quoted-string",
                GrammarUtils.token("string-interpolation",
                        GrammarUtils.pattern(compile("\\b[a-z]\\w*(?:\"\"\"(?:[^$]|\\$(?:[^{]|\\{(?:[^{\\}]|\\{[^{\\}]*\\})*\\}))*?\"\"\"|\"(?:[^$\"\\r\\n]|\\$(?:[^{]|\\{(?:[^{\\}]|\\{[^{}]*\\})*\\}))*\")", CASE_INSENSITIVE),
                                false, true, null, GrammarUtils.grammar("inside",
                                        GrammarUtils.token("id", GrammarUtils.pattern(compile("^\\w+"), false, true, "function")),
                                        GrammarUtils.token("escape", GrammarUtils.pattern(compile("\\\\\\$\"|\\$[$\"]"), false, true, "symbol")),
                                        GrammarUtils.token("interpolation",
                                                GrammarUtils.pattern(compile("\\$(?:\\w+|\\{(?:[^{\\}]|\\{[^{\\}]*\\})*\\})"), false, true, null,
                                                        GrammarUtils.grammar("inside",
                                                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^\\$\\{?|\\}$"))),
                                                                GrammarUtils.token("expression", GrammarUtils.pattern(compile("[\\s\\S]+"), false, false, null, scala))
                                                        )
                                                )
                                        ),
                                        GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))
                                )
                        )
                )
        );

        return scala;
    }
}
