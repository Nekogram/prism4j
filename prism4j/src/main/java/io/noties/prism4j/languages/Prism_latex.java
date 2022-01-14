package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases({"tex", "context"})
public class Prism_latex {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Pattern funcPattern = compile("\\\\(?:[^a-z()\\[\\]]|[a-z*]+)", CASE_INSENSITIVE);

        final Grammar insideEqu = grammar("inside",
                token("equation-command", pattern(funcPattern, false, false, "regex"))
        );

        return grammar("latex",
                token("comment", pattern(compile("%.*", MULTILINE))),
                token("cdata", pattern(
                                compile("(\\\\begin\\{((?:verbatim|lstlisting)\\*?)\\})[\\s\\S]*?(?=\\\\end\\{\\2\\})"),
                                true
                        )
                ),
                token("equation",
                        pattern(
                                compile("\\$\\$(?:\\\\[\\s\\S]|[^\\\\$])+\\$\\$|\\$(?:\\\\[\\s\\S]|[^\\\\$])+\\$|\\\\\\([\\s\\S]*?\\\\\\)|\\\\\\[[\\s\\S]*?\\\\\\]"),
                                false,
                                false,
                                "string",
                                insideEqu
                        ),
                        pattern(
                                compile("(\\\\begin\\{((?:equation|math|eqnarray|align|multline|gather)\\*?)\\})[\\s\\S]*?(?=\\\\end\\{\\2\\})"),
                                true,
                                false,
                                "string",
                                insideEqu
                        )
                ),
                token("keyword", pattern(
                        compile("(\\\\(?:begin|end|ref|cite|label|usepackage|documentclass)(?:\\[[^\\]]+\\])?\\{)[^\\}]+(?=\\})"),
                        true
                )),
                token("url", pattern(
                        compile("(\\\\url\\{)[^\\}]+(?=\\})"),
                        true
                )),
                token("headline", pattern(
                        compile("(\\\\(?:part|chapter|section|subsection|frametitle|subsubsection|paragraph|subparagraph|subsubparagraph|subsubsubparagraph)\\*?(?:\\[[^\\]]+\\])?\\{)[^}]+(?=\\}(?:\\[[^\\]]+\\])?)"),
                        true,
                        false,
                        "class-name"
                )),
                token("function", pattern(
                        funcPattern,
                        false,
                        false,
                        "selector"
                )),
                token("punctuation", pattern(compile("[\\[\\]{\\}&]")))
        );
    }
}
