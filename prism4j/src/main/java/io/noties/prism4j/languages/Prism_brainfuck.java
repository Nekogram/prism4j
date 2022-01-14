package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_brainfuck {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar("brainfuck",
                token("pointer", pattern(compile("<|>"), false, false, "keyword")),
                token("increment", pattern(compile("\\+"), false, false, "inserted")),
                token("decrement", pattern(compile("-"), false, false, "deleted")),
                token("branching", pattern(compile("\\[|\\]"), false, false, "important")),
                token("operator", pattern(compile("[.,]"))),
                token("comment", pattern(compile("\\S+")))
        );
    }
}
