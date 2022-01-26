package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_scala {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Grammar scala = GrammarUtils.extend(
                prism4j.requireGrammar("java"),
                "scala",
                token -> {
                    final String name = token.name();
                    return !"class-name".equals(name) && !"function".equals(name);
                },
                token("triple-quoted-string", pattern(compile("\"\"\"[\\s\\S]*?\"\"\""), false, true, "string")),
                token("string",
                        pattern(compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                token("keyword", pattern(
                        compile("<-|=>|\\b(?:abstract|case|catch|class|def|do|else|extends|final|finally|for|forSome|if|implicit|import|lazy|match|new|null|object|override|package|private|protected|return|sealed|self|super|this|throw|trait|try|type|val|var|while|with|yield)\\b")
                )),
                token("number", pattern(
                        compile("\\b0x[\\da-f]*\\.?[\\da-f]+|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:e\\d+)?[dfl]?", CASE_INSENSITIVE)
                ))
        );

        scala.tokens().add(
                token("symbol", pattern(compile("'[^\\d\\s\\\\]\\w*")))
        );

        GrammarUtils.insertBeforeToken(scala, "number",
                token("builtin", pattern(compile("\\b(?:String|Int|Long|Short|Byte|Boolean|Double|Float|Char|Any|AnyRef|AnyVal|Unit|Nothing)\\b")))
        );

        GrammarUtils.insertBeforeToken(scala, "triple-quoted-string",
                token("string-interpolation",
                        pattern(compile("\\b[a-z]\\w*(?:\"\"\"(?:[^$]|\\$(?:[^{]|\\{(?:[^{\\}]|\\{[^{\\}]*\\})*\\}))*?\"\"\"|\"(?:[^$\"\\r\\n]|\\$(?:[^{]|\\{(?:[^{\\}]|\\{[^{}]*\\})*\\}))*\")", CASE_INSENSITIVE),
                                false, true, null, grammar("inside",
                                        token("id", pattern(compile("^\\w+"), false, true, "function")),
                                        token("escape", pattern(compile("\\\\\\$\"|\\$[$\"]"), false, true, "symbol")),
                                        token("interpolation",
                                                pattern(compile("\\$(?:\\w+|\\{(?:[^{\\}]|\\{[^{\\}]*\\})*\\})"), false, true, null,
                                                        grammar("inside",
                                                                token("punctuation", pattern(compile("^\\$\\{?|\\}$"))),
                                                                token("expression", pattern(compile("[\\s\\S]+"), false, false, null, scala))
                                                        )
                                                )
                                        ),
                                        token("string", pattern(compile("[\\s\\S]+")))
                                )
                        )
                )
        );

        return scala;
    }
}
