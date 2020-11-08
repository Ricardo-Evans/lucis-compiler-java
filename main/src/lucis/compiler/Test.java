package lucis.compiler;

import lucis.compiler.entity.Constants;
import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.entity.Lexeme;
import lucis.compiler.io.ChannelReader;
import lucis.compiler.io.Reader;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;
import lucis.compiler.lexer.DFALexer;
import lucis.compiler.lexer.Lexer;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

public class Test {
    private static class SimpleLexer implements Lexer {
        private final String[] content = {"(", "(", ")", ")", "(", ")"};

        @Override
        public Supplier<SyntaxTree> resolve(Reader reader) {
            return new Supplier<>() {
                private int index = 0;

                @Override
                public SyntaxTree get() {
                    if (index < content.length) return new Lexeme(null, content[index++], null);
                    return null;
                }
            };
        }
    }

    private static class Pair implements SyntaxTree {
        private final Pair pair;

        public Pair(Pair pair) {
            this.pair = pair;
        }

        @Override
        public String tag() {
            return "pair";
        }
    }

    private static class List implements SyntaxTree {
        private List list;
        private Pair pair;

        public List(List list, Pair pair) {
            this.list = list;
            this.pair = pair;
        }

        @Override
        public String tag() {
            return "list";
        }
    }

    private static class Goal implements SyntaxTree {
        private List list;

        public Goal(List list) {
            this.list = list;
        }

        @Override
        public String tag() {
            return "goal";
        }
    }

    private static class SimpleToken implements SyntaxTree {
        private final String content;

        public SimpleToken(String content) {
            this.content = content;
        }

        @Override
        public String tag() {
            return null;
        }

        @Override
        public String toString() {
            return "SimpleToken{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }

    private static void testCorrectGrammar() {
        // Correct grammar
        Parser parser = new LRParser.Builder("goal")
                .define("goal:list", nodes -> new Goal((List) nodes[0]))
                .define("list:list pair", nodes -> new List((List) nodes[0], (Pair) nodes[1]))
                .define("list:pair", nodes -> new List(null, (Pair) nodes[0]))
                .define("pair:( pair )", nodes -> new Pair((Pair) nodes[1]))
                .define("pair:( )", nodes -> new Pair(null))
                .build();
        SyntaxTree goal = parser.parse(new SimpleLexer().resolve(null));
        System.out.println("Parse correct grammar successfully!");
    }

    private static void testWrongGrammar() {
        Parser.Reduction discard = nodes -> null;
        Parser parser = new LRParser.Builder("goal")
                .define("goal:statement", discard)
                .define("statement:if expression statement", discard)
                .define("statement:if expression statement else statement", discard)
                .build();
        System.out.println("Wrong grammar accepted!");
    }

    private static void testCorrectLexer() throws IOException {
        Lexer lexer = new DFALexer.Builder()
                .define(Constants.IDENTIFIER, SimpleToken::new)
                .define(Constants.IF_KEYWORD, SimpleToken::new, 1)
                .define(Constants.BLANK, SimpleToken::new)
                .define(Constants.STRING_LITERAL, SimpleToken::new)
                .build();
        File testSource = new File("test-source.lux");
        Supplier<SyntaxTree> lexemes = lexer.resolve(new ChannelReader(FileChannel.open(testSource.toPath(), StandardOpenOption.READ)));
        SyntaxTree lexeme;
        while ((lexeme = lexemes.get()) != null) {
            System.out.println(lexeme);
        }
    }

    public static void main(String[] args) throws IOException {
        testCorrectLexer();
    }
}
