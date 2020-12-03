package com.lucis.compiler;

import lucis.compiler.Compiler;
import lucis.compiler.entity.Unit;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Test {

    private static void testBracketGrammar() {
        Parser parser = new LRParser.Builder("goal")
                .define("goal:list", handle -> handle.at(0))
                .define("list:list pair", handle -> {
                    List<Integer> list = handle.at(0);
                    list.add(handle.at(1));
                    return list;
                })
                .define("list: ", handle -> new LinkedList<>())
                .define("pair:( pair )", handle -> (Integer) handle.at(1) + 1)
                .define("pair:( )", handle -> 1)
                .build();
        String[] source = {"(", ")", "(", "(", ")", ")"};
        LinkedList<Integer> list = parser.parse(Stream.concat(Arrays.stream(source).map(s -> new Unit(s, s, null)), Stream.of((Unit) null)));
        System.out.println("parse successfully");
    }

    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        compiler.compile(new File("test-source.lux"));
    }
}
