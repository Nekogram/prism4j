package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases("yml")
public class Prism_yaml {

    private final static String anchorOrAlias = "[*&][^\\s\\[\\]{\\},]+";
    private final static String tag = "!(?:<[\\w\\-%#;/?:@&=+$,.!~*'()\\[\\]]+>|(?:[a-zA-Z\\d-]*!)?[\\w\\-%#;/?:@&=+$.~*'()]+)?";
    private final static String properties = "(?:" + tag + "(?:[ \\t]+" + anchorOrAlias + ")?|" + anchorOrAlias + "(?:[ \\t]+" + tag + ")?)";

    private static Pattern createValuePattern(String value, int flags) {
        final String pattern = "([:\\-,\\[{]\\s*(?:\\s" + properties + "[ \\t]+)?)(?:" + value + ")(?=[ \\t]*(?:$|,|]|\\}|(?:[\\r\\n]\\s*)?#))";
        return compile(pattern, flags);
    }

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        final String plainKey = "(?:[^\\s\\x00-\\x08\\x0e-\\x1f!\"#%&'*,\\-:>?@\\[\\]`{|\\}\\x7f-\\x84\\x86-\\x9f\\ud800-\\udfff\\ufffe\\uffff]|[?:-][^\\s\\x00-\\x08\\x0e-\\x1f,\\[\\]{\\}\\x7f-\\x84\\x86-\\x9f\\ud800-\\udfff\\ufffe\\uffff])(?:[ \\t]*(?:(?![#:])[^\\s\\x00-\\x08\\x0e-\\x1f,\\[\\]{}\\x7f-\\x84\\x86-\\x9f\\ud800-\\udfff\\ufffe\\uffff]|:[^\\s\\x00-\\x08\\x0e-\\x1f,\\[\\]{\\}\\x7f-\\x84\\x86-\\x9f\\ud800-\\udfff\\ufffe\\uffff]))*";
        final String string = "\"(?:[^\"\\\\\\r\\n]|\\\\.)*\"|'(?:[^'\\\\\\r\\n]|\\\\.)*'";

        return grammar("yaml",
                token("scalar", pattern(
                        compile("([\\-:]\\s*(?:\\s" + properties + "[ \\t]+)?[|>])[ \\t]*(?:((?:\\r?\\n|\\r)[ \\t]+)\\S[^\\r\\n]*(?:\\2[^\\r\\n]+)*)"),
                        true,
                        false,
                        "string"
                )),
                token("comment", pattern(compile("#.*"))),
                token("key", pattern(
                        compile("((?:^|[:\\-,\\[{\\r\\n?])[ \\t]*(?:" + properties + "[ \\t]+)?)(?:" + plainKey + "|" + string + ")(?=\\s*:\\s)"),
                        true,
                        true,
                        "atrule"
                )),
                token("directive", pattern(
                        compile("(^[ \\t]*)%.+", MULTILINE),
                        true,
                        false,
                        "important"
                )),
                token("datetime", pattern(
                        createValuePattern("\\d{4}-\\d\\d?-\\d\\d?(?:[tT]|[ \\t]+)\\d\\d?:\\d{2}:\\d{2}(?:\\.\\d*)?(?:[ \\t]*(?:Z|[-+]\\d\\d?(?::\\d{2})?))?|\\d{4}-\\d{2}-\\d{2}|\\d\\d?:\\d{2}(?::\\d{2}(?:\\.\\d*)?)?", MULTILINE),
                        true,
                        false,
                        "number"
                )),
                token("boolean", pattern(
                        createValuePattern("true|false", MULTILINE | CASE_INSENSITIVE),
                        true,
                        false,
                        "important"
                )),
                token("null", pattern(
                        createValuePattern("null|~", MULTILINE | CASE_INSENSITIVE),
                        true,
                        false,
                        "important"
                )),
                token("string", pattern(
                        createValuePattern(string, MULTILINE),
                        true,
                        true
                )),
                token("number", pattern(
                        createValuePattern("[+-]?(?:0x[\\da-f]+|0o[0-7]+|(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:e[+-]?\\d+)?|\\.inf|\\.nan)", MULTILINE | CASE_INSENSITIVE),
                        true
                )),
                token("tag", pattern(compile(tag))),
                token("important", pattern(compile(anchorOrAlias))),
                token("punctuation", pattern(compile("---|[:\\[\\]{\\}\\-,|>?]|\\.\\.\\.")))
        );

    }
}
