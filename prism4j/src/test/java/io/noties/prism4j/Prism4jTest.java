package io.noties.prism4j;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class Prism4jTest {
    @Test
    public void testTokenize() {
        Prism4j prism4j = new Prism4j();

        // test with Visitor closs
        String input = "package io.noties.prism4j;\n" +
                "\n" +
                "import org.jetbrains.annotations.NotNull;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "public abstract class Visitor {\n" +
                "\n" +
                "    public void visit(@NotNull List<? extends Node> nodes) {\n" +
                "        for (Node node : nodes) {\n" +
                "            if (node.isSyntax()) {\n" +
                "                visitSyntax((Syntax) node);\n" +
                "            } else {\n" +
                "                visitText((Text) node);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    protected abstract void visitText(@NotNull Text text);\n" +
                "\n" +
                "    // do not forget to call visit(syntax.children()) inside\n" +
                "    protected abstract void visitSyntax(@NotNull Syntax syntax);\n" +
                "}\n";

        StringBuilder output = new StringBuilder();

        prism4j.visit(new Visitor() {
            @Override
            protected void visitText(@NotNull Text text) {
                output.append(text.literal());
            }

            @Override
            protected void visitSyntax(@NotNull Syntax syntax) {
                output.append(syntax.matchedString());
            }
        }, input, "java");

        Assert.assertEquals(input, output.toString());
    }
}
