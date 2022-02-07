package io.noties.prism4j.languages;

import io.noties.prism4j.*;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_regex {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final Pattern specialEscape = GrammarUtils.pattern(compile("\\\\[\\\\(){\\}\\[\\]^$+*?|.]"), false, false, "escape");
        final String escape = "\\\\(?:x[\\da-fA-F]{2}|u[\\da-fA-F]{4}|u\\{[\\da-fA-F]+\\}|0[0-7]{0,2}|[123][0-7]{2}|c[a-zA-Z]|.)";
        final String rangeChar = "(?:[^\\\\-]|" + escape + ")";
        final Token groupName = GrammarUtils.token("group-name", GrammarUtils.pattern(compile("(<|')[^<>']+(?=[>']$)"), true, false, "variable"));

        return GrammarUtils.grammar("regex",
                GrammarUtils.token("char-class", GrammarUtils.pattern(compile("((?:^|[^\\\\])(?:\\\\\\\\)*)\\[(?:[^\\\\\\]]|\\\\[\\s\\S])*\\]"), true, false, null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("char-class-negation", GrammarUtils.pattern(compile("(^\\[)\\^"), true, false, "operator")),
                                GrammarUtils.token("char-class-punctuation", GrammarUtils.pattern(compile("^\\[|\\]$"), false, false, "punctuation")),
                                GrammarUtils.token("range", GrammarUtils.pattern(compile(rangeChar + "-" + rangeChar), false, false, null,
                                        GrammarUtils.grammar("inside",
                                                GrammarUtils.token("escape", GrammarUtils.pattern(compile(escape))),
                                                GrammarUtils.token("range-punctuation", GrammarUtils.pattern(compile("-"), false, false, "operator"))
                                        )
                                )),
                                GrammarUtils.token("special-escape", specialEscape),
                                GrammarUtils.token("char-set", GrammarUtils.pattern(compile("\\\\[wsd]|\\\\p\\{[^{\\}]+\\}", CASE_INSENSITIVE), false, false, "class-name")),
                                GrammarUtils.token("escape", GrammarUtils.pattern(compile(escape)))
                        )
                )),
                GrammarUtils.token("special-escape", specialEscape),
                GrammarUtils.token("char-set", GrammarUtils.pattern(compile("\\.|\\\\[wsd]|\\\\p\\{[^{\\}]+\\}", CASE_INSENSITIVE), false, false, "class-name")),
                GrammarUtils.token("backreference",
                        GrammarUtils.pattern(compile("\\\\(?![123][0-7]{2})[1-9]"), false, false, "keyword"),
                        GrammarUtils.pattern(compile("\\\\k<[^<>']+>"), false, false, "keyword",
                                GrammarUtils.grammar("inside", groupName)
                )),
                GrammarUtils.token("anchor", GrammarUtils.pattern(compile("[$^]|\\\\[ABbGZz]"), false, false, "function")),
                GrammarUtils.token("escape", GrammarUtils.pattern(compile(escape))),
                GrammarUtils.token("group",
                        GrammarUtils.pattern(compile("\\((?:\\?(?:<[^<>']+>|'[^<>']+'|[>:]|<?[=!]|[idmnsuxU]+(?:-[idmnsuxU]+)?:?))?"), false, false, "punctuation", GrammarUtils.grammar("inside", groupName)),
                        GrammarUtils.pattern(compile("\\)"), false, false, "punctuation")
                ),
                GrammarUtils.token("quantifier", GrammarUtils.pattern(compile("(?:[+*?]|\\{\\d+(?:,\\d*)?\\})[?+]?"), false, false, "number")),
                GrammarUtils.token("alternation", GrammarUtils.pattern(compile("\\|"), false, false, "keyword"))
        );
    }

}
