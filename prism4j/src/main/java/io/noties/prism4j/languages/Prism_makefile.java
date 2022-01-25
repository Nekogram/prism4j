package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_makefile {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar("makefile",
                token("comment", pattern(
                        compile("(^|[^\\\\])#(?:\\\\(?:\\r\\n|[\\s\\S])|[^\\\\\\r\\n])*"),
                        true
                )),
                token("string", pattern(
                        compile("([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"),
                        false,
                        true
                )),
                token("builtin-target",
                        pattern(compile("\\.[A-Z][^:#=\\s]+(?=\\s*:(?!=))"), false, false, "builtin")
                ),
                token("target", pattern(
                        compile("^[^:=\\r\\n]+(?=\\s*:(?!=))", MULTILINE),
                        false,
                        false,
                        "symbol",
                        grammar("inside",
                                token("variable", pattern(compile("\\$+(?:[^(){\\}:#=\\s]+|(?=[({]))")))
                        )
                )),
                token("variable", pattern(compile("\\$+(?:[^(){\\}:#=\\s]+|\\([@*%<^+?][DF]\\)|(?=[({]))"))),
                token("keyword",
                        pattern(compile("-include\\b|\\b(?:define|else|endef|endif|export|ifn?def|ifn?eq|include|override|private|sinclude|undefine|unexport|vpath)\\b"))
                       /* pattern(
                                compile("(\\()(?:addsuffix|abspath|and|basename|call|dir|error|eval|file|filter(?:-out)?|findstring|firstword|flavor|foreach|guile|if|info|join|lastword|load|notdir|or|origin|patsubst|realpath|shell|sort|strip|subst|suffix|value|warning|wildcard|word(?:s|list)?)(?=[ \\t])"),
                                true
                        )*/
                ),
                token("function", pattern(compile("(\\()(?:abspath|addsuffix|and|basename|call|dir|error|eval|file|filter(?:-out)?|findstring|firstword|flavor|foreach|guile|if|info|join|lastword|load|notdir|or|origin|patsubst|realpath|shell|sort|strip|subst|suffix|value|warning|wildcard|word(?:list|s)?)(?=[ \\t])"), true)),
                token("operator", pattern(compile("(?:::|[?:+!])?=|[|@]"))),
                token("punctuation", pattern(compile("[:;(){\\}]")))
        );
    }
}
