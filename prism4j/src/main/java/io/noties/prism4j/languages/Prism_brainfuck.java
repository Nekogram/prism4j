package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_brainfuck {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar("brainfuck",
                GrammarUtils.token("pointer", GrammarUtils.pattern(compile("<|>"), false, false, "keyword")),
                GrammarUtils.token("increment", GrammarUtils.pattern(compile("\\+"), false, false, "inserted")),
                GrammarUtils.token("decrement", GrammarUtils.pattern(compile("-"), false, false, "deleted")),
                GrammarUtils.token("branching", GrammarUtils.pattern(compile("\\[|\\]"), false, false, "important")),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[.,]"))),
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("\\S+")))
        );
    }
}
