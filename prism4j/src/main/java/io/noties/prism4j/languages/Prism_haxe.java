package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_haxe {

    public static Grammar create(@NotNull Prism4j prism4j) {
        final Grammar haxe = GrammarUtils.extend(GrammarUtils.require(prism4j, "clike"), "haxe",
                token("string",
                        pattern(compile("\"(?:[^\"\\\\]|\\\\[\\s\\S])*\""), false, true)
                ),
                token("class-name", pattern(compile("(\\b(?:abstract|class|enum|extends|implements|interface|new|typedef)\\s+)[A-Z_]\\w*"), true), pattern(compile("\\b[A-Z]\\w*"))),
                token("keyword", pattern(compile("\\bthis\\b|\\b(?:abstract|as|break|case|cast|catch|class|continue|default|do|dynamic|else|enum|extends|extern|final|for|from|function|if|implements|import|in|inline|interface|macro|new|null|operator|overload|override|package|private|public|return|static|super|switch|throw|to|try|typedef|untyped|using|var|while)(?!\\.)\\b"))),
                token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*(?:<[^<>]*>\\s*)?\\()", CASE_INSENSITIVE), false, true)),
                token("operator", pattern(compile("\\.{3}|\\+\\+|--|&&|\\|\\||->|=>|(?:<<?|>{1,3}|[-+*/%!=&|^])=?|[?:~]")))
        );

        GrammarUtils.insertBeforeToken(haxe, "string",
                token("string-interpolation", pattern(compile("'(?:[^'\\\\]|\\\\[\\s\\S])*'"), false, true, null,
                        grammar("inside",
                                token("interpolation", pattern(compile("(^|[^\\\\])\\$(?:\\w+|\\{[^{}]+\\})"), true, false, null,
                                        grammar("inside",
                                                token("interpolation-punctuation", pattern(compile("^\\$\\{?|\\}$"), false, false, "punctuation")),
                                                token("expression", pattern(compile("[\\s\\S]+"), false, false, null, haxe))
                                        )
                                )),
                                token("string", pattern(compile("[\\s\\S]+")))
                        )
                ))
        );

        GrammarUtils.insertBeforeToken(haxe, "class-name",
                token("regex", pattern(compile("~/(?:[^/\\\\\\r\\n]|\\\\.)+/[a-z]*"), false, true, null,
                        grammar("inside",
                                token("regex-flags", pattern(compile("\\b[a-z]+$"))),
                                token("regex-source", pattern(compile("^(~/)[\\s\\S]+(?=/$)"), true, false, "language-regex" /* TODO check regex language (here and js) */)),
                                token("regex-delimiter", pattern(compile("^~/|/$")))
                        )
                ))
        );

        GrammarUtils.insertBeforeToken(haxe, "keyword",
                token("preprocessor", pattern(compile("#(?:else|elseif|end|if)\\b.*"), false, false, "property")),
                token("metadata", pattern(compile("@:?[\\w.]+"), false, false, "symbol")),
                token("reification", pattern(compile("\\$(?:\\w+|(?=\\{))"), false, false, "important"))
        );

        return haxe;
    }

}