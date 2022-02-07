package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_ini {
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar("ini",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("(^[ \\f\\t\\v]*)[#;][^\\n\\r]*", MULTILINE), true)),
                GrammarUtils.token("header", GrammarUtils.pattern(compile("(^[ \\f\\t\\v]*)\\[[^\\n\\r\\]]*]?", MULTILINE), true, false, null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("section-name", GrammarUtils.pattern(compile("(^\\[[ \\f\\t\\v]*)[^ \\f\\t\\v\\]]+(?:[ \\f\\t\\v]+[^ \\f\\t\\v\\]]+)*"), true, false, "selector")),
                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\[|\\]")))
                        ))
                ),
                GrammarUtils.token("key", GrammarUtils.pattern(compile("(^[ \\f\\t\\v]*)[^ \\f\\r\\t\\v=]+(?:[ \\f\\t\\v]+[^ \\f\\r\\t\\v=]+)*(?=[ \\f\\t\\v]*=)", MULTILINE), true, false, "attr-name")),
                GrammarUtils.token("value", GrammarUtils.pattern(compile("(=[ \\f\\t\\v]*)[^ \\f\\n\\r\\t\\v]+(?:[ \\f\\t\\v]+[^ \\f\\n\\r\\t\\v]+)*"), true, false, "attr-value",
                        GrammarUtils.grammar("inside", GrammarUtils.token("inner-value", GrammarUtils.pattern(compile("^(\"|').+(?=\\1$)"), true)))
                )),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("=")))
        );
    }
}
