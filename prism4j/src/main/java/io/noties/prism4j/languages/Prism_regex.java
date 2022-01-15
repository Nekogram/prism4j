package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Pattern;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_regex {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Pattern specialEscape = pattern(compile("\\\\[\\\\(){\\}\\[\\]^$+*?|.]"), false, false, "escape");
        final String escape = "\\\\(?:x[\\da-fA-F]{2}|u[\\da-fA-F]{4}|u\\{[\\da-fA-F]+\\}|0[0-7]{0,2}|[123][0-7]{2}|c[a-zA-Z]|.)";
        final String rangeChar = "(?:[^\\\\-]|" + escape + ")";
        final Token groupName = token("group-name", pattern(compile("(<|')[^<>']+(?=[>']$)"), true, false, "variable"));

        return grammar("regex",
                token("char-class", pattern(compile("((?:^|[^\\\\])(?:\\\\\\\\)*)\\[(?:[^\\\\\\]]|\\\\[\\s\\S])*\\]"), true, false, null,
                        grammar("inside",
                                token("char-class-negation", pattern(compile("(^\\[)\\^"), true, false, "operator")),
                                token("char-class-punctuation", pattern(compile("^\\[|\\]$"), false, false, "punctuation")),
                                token("range", pattern(compile(rangeChar + "-" + rangeChar), false, false, null,
                                        grammar("inside",
                                                token("escape", pattern(compile(escape))),
                                                token("range-punctuation", pattern(compile("-"), false, false, "operator"))
                                        )
                                )),
                                token("special-escape", specialEscape),
                                token("char-set", pattern(compile("\\\\[wsd]|\\\\p\\{[^{\\}]+\\}", CASE_INSENSITIVE), false, false, "class-name")),
                                token("escape", pattern(compile(escape)))
                        )
                )),
                token("special-escape", specialEscape),
                token("char-set", pattern(compile("\\.|\\\\[wsd]|\\\\p\\{[^{\\}]+\\}", CASE_INSENSITIVE), false, false, "class-name")),
                token("backreference",
                        pattern(compile("\\\\(?![123][0-7]{2})[1-9]"), false, false, "keyword"),
                        pattern(compile("\\\\k<[^<>']+>"), false, false, "keyword",
                                grammar("inside", groupName)
                )),
                token("anchor", pattern(compile("[$^]|\\\\[ABbGZz]"), false, false, "function")),
                token("escape", pattern(compile(escape))),
                token("group",
                        pattern(compile("\\((?:\\?(?:<[^<>']+>|'[^<>']+'|[>:]|<?[=!]|[idmnsuxU]+(?:-[idmnsuxU]+)?:?))?"), false, false, "punctuation", grammar("inside", groupName)),
                        pattern(compile("\\)"), false, false, "punctuation")
                ),
                token("quantifier", pattern(compile("(?:[+*?]|\\{\\d+(?:,\\d*)?\\})[?+]?"), false, false, "number")),
                token("alternation", pattern(compile("\\|"), false, false, "keyword"))
        );
    }

}
