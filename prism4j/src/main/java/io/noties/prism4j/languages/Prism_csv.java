package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_csv {

    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar("csv",
                token("value", pattern(compile("[^\\r\\n,\"]+|\"(?:[^\"]|\"\")*\"(?!\")"))),
                token("punctuation", pattern(compile(",")))
        );
    }

}
