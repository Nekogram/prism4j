package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_haxe {

    public static Grammar create(@NotNull Prism4j prism4j) {
        final Grammar haxe = prism4j.requireGrammar("clike").extend("haxe",
                GrammarUtils.token("string",
                        GrammarUtils.pattern(compile("\"(?:[^\"\\\\]|\\\\[\\s\\S])*\""), false, true)
                ),
                GrammarUtils.token("class-name", GrammarUtils.pattern(compile("(\\b(?:abstract|class|enum|extends|implements|interface|new|typedef)\\s+)[A-Z_]\\w*"), true), GrammarUtils.pattern(compile("\\b[A-Z]\\w*"))),
                GrammarUtils.token("keyword", GrammarUtils.pattern(compile("\\bthis\\b|\\b(?:abstract|as|break|case|cast|catch|class|continue|default|do|dynamic|else|enum|extends|extern|final|for|from|function|if|implements|import|in|inline|interface|macro|new|null|operator|overload|override|package|private|public|return|static|super|switch|throw|to|try|typedef|untyped|using|var|while)(?!\\.)\\b"))),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("\\b[a-z_]\\w*(?=\\s*(?:<[^<>]*>\\s*)?\\()", CASE_INSENSITIVE), false, true)),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("\\.{3}|\\+\\+|--|&&|\\|\\||->|=>|(?:<<?|>{1,3}|[-+*/%!=&|^])=?|[?:~]")))
        );

        haxe.insertBeforeToken("string",
                GrammarUtils.token("string-interpolation", GrammarUtils.pattern(compile("'(?:[^'\\\\]|\\\\[\\s\\S])*'"), false, true, null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("(^|[^\\\\])\\$(?:\\w+|\\{[^{}]+\\})"), true, false, null,
                                        GrammarUtils.grammar("inside",
                                                GrammarUtils.token("interpolation-punctuation", GrammarUtils.pattern(compile("^\\$\\{?|\\}$"), false, false, "punctuation")),
                                                GrammarUtils.token("expression", GrammarUtils.pattern(compile("[\\s\\S]+"), false, false, null, haxe))
                                        )
                                )),
                                GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))
                        )
                ))
        );

        haxe.insertBeforeToken("class-name",
                GrammarUtils.token("regex", GrammarUtils.pattern(compile("~/(?:[^/\\\\\\r\\n]|\\\\.)+/[a-z]*"), false, true, null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("regex-flags", GrammarUtils.pattern(compile("\\b[a-z]+$"))),
                                GrammarUtils.token("regex-source", GrammarUtils.pattern(compile("^(~/)[\\s\\S]+(?=/$)"), true, false, "language-regex", prism4j.requireGrammar("regex"))),
                                GrammarUtils.token("regex-delimiter", GrammarUtils.pattern(compile("^~/|/$")))
                        )
                ))
        );

        haxe.insertBeforeToken("keyword",
                GrammarUtils.token("preprocessor", GrammarUtils.pattern(compile("#(?:else|elseif|end|if)\\b.*"), false, false, "property")),
                GrammarUtils.token("metadata", GrammarUtils.pattern(compile("@:?[\\w.]+"), false, false, "symbol")),
                GrammarUtils.token("reification", GrammarUtils.pattern(compile("\\$(?:\\w+|(?=\\{))"), false, false, "important"))
        );

        return haxe;
    }

}