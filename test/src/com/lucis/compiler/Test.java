package com.lucis.compiler;

import lucis.compiler.Compiler;
import compiler.entity.Unit;
import compiler.parser.LRParser;
import compiler.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        compiler.compile(new File("test-source.lux"));
    }

}
