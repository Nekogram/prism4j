package io.noties.prism4j.languages;

import io.noties.prism4j.*;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
public class Prism_java {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final String keywordPattern = "\\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|exports|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|module|native|new|non-sealed|null|open|opens|package|permits|private|protected|provides|public|record|requires|return|sealed|short|static|strictfp|super|switch|synchronized|this|throw|throws|to|transient|transitive|try|uses|var|void|volatile|while|with|yield)\\b";
        final Token keyword = GrammarUtils.token("keyword", GrammarUtils.pattern(compile(keywordPattern)));

        final String classNamePrefix = "(^|[^\\w.])(?:[a-z]\\w*\\s*\\.\\s*)*(?:[A-Z]\\w*\\s*\\.\\s*)*";
        final Pattern className = GrammarUtils.pattern(compile(classNamePrefix + "[A-Z](?:[\\d_A-Z]*[a-z]\\w*)?\\b"),
                true, false, null, GrammarUtils.grammar("inside",
                        GrammarUtils.token("namespace",
                                GrammarUtils.pattern(compile("^[a-z]\\w*(?:\\s*\\.\\s*[a-z]\\w*)*(?:\\s*\\.)?"),
                                        false, false, null,
                                        GrammarUtils.grammar("inside",
                                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\.")))
                                        )
                                )
                        ),
                        GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\.")))
                )
        );

        final Grammar java = prism4j.requireGrammar("clike").extend("java",
                GrammarUtils.token("string", GrammarUtils.pattern(compile("(^|[^\\\\])\"(?:\\\\.|[^\"\\\\\\r\\n])*\""), true, true)),
                GrammarUtils.token("class-name", className, GrammarUtils.pattern(compile(classNamePrefix + "[A-Z]\\w*(?=\\s+\\w+\\s*[;,=()])"), true, false, null, className.inside())),
                keyword,
                GrammarUtils.token("function", GrammarUtils.pattern(compile("\\b\\w+(?=\\()")), GrammarUtils.pattern(compile("(::\\s*)[a-z_]\\w*"), true)),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("\\b0b[01][01_]*L?\\b|\\b0x(?:\\.[\\da-f_p+-]+|[\\da-f_]+(?:\\.[\\da-f_p+-]+)?)\\b|(?:\\b\\d[\\d_]*(?:\\.[\\d_]*)?|\\B\\.\\d[\\d_]*)(?:e[+-]?\\d[\\d_]*)?[dfl]?", CASE_INSENSITIVE))),
                GrammarUtils.token("operator", GrammarUtils.pattern(
                        compile("(^|[^.])(?:<<=?|>>>?=?|->|--|\\+\\+|&&|\\|\\||::|[?:~]|[-+*/%&|^!=<>]=?)", MULTILINE),
                        true
                ))
        );

        java.insertBeforeToken("string",
                GrammarUtils.token("triple-quoted-string", GrammarUtils.pattern(compile("\"\"\"[ \\t]*[\\r\\n](?:(?:\"|\"\")?(?:\\\\.|[^\"\\\\]))*\"\"\""), false, true, "string")),
                GrammarUtils.token("char", GrammarUtils.pattern(compile("'(?:\\\\.|[^'\\\\\\r\\n]){1,6}'"), false, true))
        );

        java.insertBeforeToken("class-name",
                GrammarUtils.token("annotation", GrammarUtils.pattern(
                        compile("(^|[^.])@\\w+(?:\\s*\\.\\s*\\w+)*"),
                        true,
                        false,
                        "punctuation"
                )),
                GrammarUtils.token("generics", GrammarUtils.pattern(compile("<(?:[\\w\\s,.?]|&(?!&)|<(?:[\\w\\s,.?]|&(?!&)|<(?:[\\w\\s,.?]|&(?!&)|<(?:[\\w\\s,.?]|&(?!&))*>)*>)*>)*>"), false, false, null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("class-name", className),
                                keyword,
                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[<>(),.:]"))),
                                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[?&|]")))))
                ),
                GrammarUtils.token("namespace", GrammarUtils.pattern(compile("(\\b(?:exports|import(?:\\s+static)?|module|open|opens|package|provides|requires|to|transitive|uses|with)\\s+)(?!" + keywordPattern + ")[a-z]\\w*(?:\\.[a-z]\\w*)*\\.?"), true, false, null, GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\."))))))
        );

        return java;
    }
}
