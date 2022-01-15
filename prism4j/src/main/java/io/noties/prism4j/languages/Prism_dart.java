package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Pattern;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_dart {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Pattern[] keywords = {
                pattern(compile("\\b(?:async|sync|yield)\\*")),
                pattern(compile("\\b(?:abstract|assert|async|await|break|case|catch|class|const|continue|covariant|default|deferred|do|dynamic|else|enum|export|extension|external|extends|factory|final|finally|for|get|hide|if|implements|interface|import|in|library|mixin|new|null|on|operator|part|rethrow|return|set|show|static|super|switch|sync|this|throw|try|typedef|var|void|while|with|yield)\\b"))
        };
        final Grammar classNameInside = grammar("inside", token("namespace", pattern(compile("^[a-z]\\w*(?:\\s*\\.\\s*[a-z]\\w*)*(?:\\s*\\.)?"), false, false, null, grammar("inside", token("punctuation", pattern(compile("\\.")))))));
        final Pattern className = pattern(compile("(^|[^\\w.])(?:[a-z]\\w*\\s*\\.\\s*)*(?:[A-Z]\\w*\\s*\\.\\s*)*[A-Z](?:[\\d_A-Z]*[a-z]\\w*)?\\b"), true, false, null, classNameInside);
        final Grammar dart = GrammarUtils.extend(
                GrammarUtils.require(prism4j, "clike"),
                "dart",
                token("class-name",
                        className,
                        pattern(compile("(^|[^\\w.])(?:[a-z]\\w*\\s*\\.\\s*)*(?:[A-Z]\\w*\\s*\\.\\s*)*[A-Z]\\w*(?=\\s+\\w+\\s*[;,=()])"), true, false, null, classNameInside)
                ),
                token("keyword", keywords),
                token("operator", pattern(compile("\\bis!|\\b(?:as|is)\\b|\\+\\+|--|&&|\\|\\||<<=?|>>=?|~(?:\\/=?)?|[+\\-*\\/%&^|=!<>]=?|\\?")))
        );

        GrammarUtils.insertBeforeToken(dart, "string",
                token("string-literal", pattern(compile("r?(?:(\"\"\"|''')[\\s\\S]*?\\1|([\"'])(?:\\\\.|(?!\\2)[^\\\\\\r\\n])*\\2(?!\\2))"), false, true, null, grammar("inside",
                        token("interpolation", pattern(compile("((?:^|[^\\\\])(?:\\\\{2})*)\\$(?:\\w+|\\{(?:[^{\\}]|\\{[^{}]*\\})*\\})"), true, false, null,
                                grammar("inside", token("punctuation", pattern(compile("^\\$\\{?|\\}$"))),
                                        token("expression", pattern(compile("[\\s\\S]+"), false, false, null, dart))
                                ))), token("string", pattern(compile("[\\s\\S]+"))))))
                /*token("string",
                        pattern(compile("r?(\"\"\"|''')[\\s\\S]*?\\1"), false, true),
                        pattern(compile("r?(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                )*/
        );

        GrammarUtils.insertBeforeToken(dart, "class-name",
                token("metadata", pattern(compile("@\\w+"), false, false, "function"))
        );

        GrammarUtils.insertBeforeToken(dart, "class-name", token("generics", pattern(compile("<(?:[\\w\\s,.&?]|<(?:[\\w\\s,.&?]|<(?:[\\w\\s,.&?]|<[\\w\\s,.&?]*>)*>)*>)*>"), false, false, null, grammar("inside",
                token("class-name", className),
                token("keyword", keywords),
                token("punctuation", pattern(compile("[<>(),.:]"))),
                token("operator", pattern(compile("[?&|]")))
        ))));

        return dart;
    }
}
