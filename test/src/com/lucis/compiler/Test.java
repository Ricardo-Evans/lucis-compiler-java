package com.lucis.compiler;

import lucis.compiler.entity.Unit;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) throws IOException {
        Parser parser = new LRParser.Builder("goal")
                .define("goal:list", units -> units[0].value())
                .define("list:list pair", units -> {
                    List<Integer> list = units[0].value();
                    list.add(units[1].value());
                    return list;
                })
                .define("list: ", units -> new LinkedList<>())
                .define("pair:( pair )", units -> (Integer) units[1].value() + 1)
                .define("pair:( )", units -> 1)
                .build();
        String[] source = {"(", ")", "(", "(", ")", ")"};
        Unit goal = parser.parse(Stream.concat(Arrays.stream(source).map(s -> new Unit(s, s, null)), Stream.of((Unit) null)));
        System.out.println("parse successfully");
    }
}
