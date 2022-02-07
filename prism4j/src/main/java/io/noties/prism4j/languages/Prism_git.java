package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_git {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar("git",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("^#.*", MULTILINE))),
                GrammarUtils.token("deleted", GrammarUtils.pattern(compile("^[-–].*", MULTILINE))),
                GrammarUtils.token("inserted", GrammarUtils.pattern(compile("^\\+.*", MULTILINE))),
                GrammarUtils.token("string", GrammarUtils.pattern(compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1", MULTILINE))),
                GrammarUtils.token("command", GrammarUtils.pattern(
                        compile("^.*\\$ git .*$", MULTILINE),
                        false,
                        false,
                        null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("parameter", GrammarUtils.pattern(compile("\\s--?\\w+", MULTILINE)))
                        )
                )),
                GrammarUtils.token("coord", GrammarUtils.pattern(compile("^@@.*@@$", MULTILINE))),
                GrammarUtils.token("commit-sha1", GrammarUtils.pattern(compile("^commit \\w{40}$", MULTILINE)))
        );
    }
}
