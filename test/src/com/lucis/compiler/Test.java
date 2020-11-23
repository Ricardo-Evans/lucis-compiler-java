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
        Parser parser = new LRParser.Builder("goal", "empty")
                .define("goal:list", units -> new Unit("goal", units[0].value(), null))
                .define("list:list pair", units -> {
                    List<Integer> list = units[0].value();
                    list.add(units[1].value());
                    return new Unit("list", list, null);
                })
                .define("list:pair", units -> {
                    List<Integer> list = new LinkedList<>();
                    list.add(units[0].value());
                    return new Unit("list", list, null);
                })
                .define("pair:( pair )", units -> new Unit("pair", (Integer) units[1].value() + 1, null))
                .define("pair:( )", units -> new Unit("pair", 1, null))
                .build();
        String[] source = {"(", ")", "(", "(", ")", ")"};
        Unit goal = parser.parse(Stream.concat(Arrays.stream(source).map(s -> new Unit(s, s, null)), Stream.of((Unit) null)));
        System.out.println("parse successfully");
    }
}
