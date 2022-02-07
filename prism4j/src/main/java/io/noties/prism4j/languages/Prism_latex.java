package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases({"tex", "context"})
public class Prism_latex {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Pattern funcPattern = compile("\\\\(?:[^a-z()\\[\\]]|[a-z*]+)", CASE_INSENSITIVE);

        final Grammar insideEqu = GrammarUtils.grammar("inside",
                GrammarUtils.token("equation-command", GrammarUtils.pattern(funcPattern, false, false, "regex"))
        );

        return GrammarUtils.grammar("latex",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("%.*", MULTILINE))),
                GrammarUtils.token("cdata", GrammarUtils.pattern(
                                compile("(\\\\begin\\{((?:verbatim|lstlisting)\\*?)\\})[\\s\\S]*?(?=\\\\end\\{\\2\\})"),
                                true
                        )
                ),
                GrammarUtils.token("equation",
                        GrammarUtils.pattern(
                                compile("\\$\\$(?:\\\\[\\s\\S]|[^\\\\$])+\\$\\$|\\$(?:\\\\[\\s\\S]|[^\\\\$])+\\$|\\\\\\([\\s\\S]*?\\\\\\)|\\\\\\[[\\s\\S]*?\\\\\\]"),
                                false,
                                false,
                                "string",
                                insideEqu
                        ),
                        GrammarUtils.pattern(
                                compile("(\\\\begin\\{((?:equation|math|eqnarray|align|multline|gather)\\*?)\\})[\\s\\S]*?(?=\\\\end\\{\\2\\})"),
                                true,
                                false,
                                "string",
                                insideEqu
                        )
                ),
                GrammarUtils.token("keyword", GrammarUtils.pattern(
                        compile("(\\\\(?:begin|end|ref|cite|label|usepackage|documentclass)(?:\\[[^\\]]+\\])?\\{)[^\\}]+(?=\\})"),
                        true
                )),
                GrammarUtils.token("url", GrammarUtils.pattern(
                        compile("(\\\\url\\{)[^\\}]+(?=\\})"),
                        true
                )),
                GrammarUtils.token("headline", GrammarUtils.pattern(
                        compile("(\\\\(?:part|chapter|section|subsection|frametitle|subsubsection|paragraph|subparagraph|subsubparagraph|subsubsubparagraph)\\*?(?:\\[[^\\]]+\\])?\\{)[^}]+(?=\\}(?:\\[[^\\]]+\\])?)"),
                        true,
                        false,
                        "class-name"
                )),
                GrammarUtils.token("function", GrammarUtils.pattern(
                        funcPattern,
                        false,
                        false,
                        "selector"
                )),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[\\[\\]{\\}&]")))
        );
    }
}
