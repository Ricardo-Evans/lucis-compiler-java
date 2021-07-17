package com.lucis.compiler;

import lucis.compiler.Compiler;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        compiler.compile(new File("test-source.lux"));
    }

}
