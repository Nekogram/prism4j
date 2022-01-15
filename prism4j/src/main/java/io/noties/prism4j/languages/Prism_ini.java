package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_ini {
    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar("ini",
                token("comment", pattern(compile("(^[ \\f\\t\\v]*)[#;][^\\n\\r]*", MULTILINE), true)),
                token("header", pattern(compile("(^[ \\f\\t\\v]*)\\[[^\\n\\r\\]]*]?", MULTILINE), true, false, null,
                        grammar("inside",
                                token("section-name", pattern(compile("(^\\[[ \\f\\t\\v]*)[^ \\f\\t\\v\\]]+(?:[ \\f\\t\\v]+[^ \\f\\t\\v\\]]+)*"), true, false, "selector")),
                                token("punctuation", pattern(compile("\\[|\\]")))
                        ))
                ),
                token("key", pattern(compile("(^[ \\f\\t\\v]*)[^ \\f\\r\\t\\v=]+(?:[ \\f\\t\\v]+[^ \\f\\r\\t\\v=]+)*(?=[ \\f\\t\\v]*=)", MULTILINE), true, false, "attr-name")),
                token("value", pattern(compile("(=[ \\f\\t\\v]*)[^ \\f\\n\\r\\t\\v]+(?:[ \\f\\t\\v]+[^ \\f\\n\\r\\t\\v]+)*"), true, false, "attr-value",
                        grammar("inside", token("inner-value", pattern(compile("^(\"|').+(?=\\1$)"), true)))
                )),
                token("punctuation", pattern(compile("=")))
        );
    }
}
