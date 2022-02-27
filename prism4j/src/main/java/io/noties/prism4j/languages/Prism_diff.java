package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.languages.GrammarUtils.*;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_diff {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return grammar("diff",
                token("coord",
                        pattern(compile("^(?:\\*{3}|-{3}|\\+{3}).*$", MULTILINE)),
                        pattern(compile("^@@.*@@$", MULTILINE)),
                        pattern(compile("^\\d.*$", MULTILINE))
                ),
                token("deleted-sign",
                        pattern(
                                compile("^(?:[-].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                "deleted",
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "deleted-sign"))
                                )
                        )
                ),
                token("deleted-arrow",
                        pattern(
                                compile("^(?:[<].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                "deleted",
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "deleted-arrow"))
                                )
                        )
                ),
                token("inserted-sign",
                        pattern(
                                compile("^(?:[+].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                "inserted",
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "inserted-sign"))
                                )
                        )
                ),
                token("inserted-arrow",
                        pattern(
                                compile("^(?:[>].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                "inserted",
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "inserted-arrow"))
                                )
                        )
                ),
                token("unchanged",
                        pattern(
                                compile("^(?:[ ].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                null,
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "unchanged"))
                                )
                        )
                ),
                token("diff",
                        pattern(
                                compile("^(?:[!].*(?:\\r\\n?|\\n|(?![\\s\\S])))+", MULTILINE),
                                false,
                                false,
                                "bold",
                                grammar("inside",
                                        token("line", pattern(compile("(.)(?=[\\s\\S]).*(?:\\r\\n?|\\n)?"), true)),
                                        token("prefix", pattern(compile("[\\s\\S]"), false, false, "diff"))
                                )
                        )
                )
        );
    }
}
