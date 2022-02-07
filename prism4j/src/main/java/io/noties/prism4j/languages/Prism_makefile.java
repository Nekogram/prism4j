package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_makefile {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar("makefile",
                GrammarUtils.token("comment", GrammarUtils.pattern(
                        compile("(^|[^\\\\])#(?:\\\\(?:\\r\\n|[\\s\\S])|[^\\\\\\r\\n])*"),
                        true
                )),
                GrammarUtils.token("string", GrammarUtils.pattern(
                        compile("([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"),
                        false,
                        true
                )),
                GrammarUtils.token("builtin-target",
                        GrammarUtils.pattern(compile("\\.[A-Z][^:#=\\s]+(?=\\s*:(?!=))"), false, false, "builtin")
                ),
                GrammarUtils.token("target", GrammarUtils.pattern(
                        compile("^[^:=\\r\\n]+(?=\\s*:(?!=))", MULTILINE),
                        false,
                        false,
                        "symbol",
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("variable", GrammarUtils.pattern(compile("\\$+(?:[^(){\\}:#=\\s]+|(?=[({]))")))
                        )
                )),
                GrammarUtils.token("variable", GrammarUtils.pattern(compile("\\$+(?:[^(){\\}:#=\\s]+|\\([@*%<^+?][DF]\\)|(?=[({]))"))),
                GrammarUtils.token("keyword",
                        GrammarUtils.pattern(compile("-include\\b|\\b(?:define|else|endef|endif|export|ifn?def|ifn?eq|include|override|private|sinclude|undefine|unexport|vpath)\\b"))
                       /* pattern(
                                compile("(\\()(?:addsuffix|abspath|and|basename|call|dir|error|eval|file|filter(?:-out)?|findstring|firstword|flavor|foreach|guile|if|info|join|lastword|load|notdir|or|origin|patsubst|realpath|shell|sort|strip|subst|suffix|value|warning|wildcard|word(?:s|list)?)(?=[ \\t])"),
                                true
                        )*/
                ),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("(\\()(?:abspath|addsuffix|and|basename|call|dir|error|eval|file|filter(?:-out)?|findstring|firstword|flavor|foreach|guile|if|info|join|lastword|load|notdir|or|origin|patsubst|realpath|shell|sort|strip|subst|suffix|value|warning|wildcard|word(?:list|s)?)(?=[ \\t])"), true)),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("(?:::|[?:+!])?=|[|@]"))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[:;(){\\}]")))
        );
    }
}
