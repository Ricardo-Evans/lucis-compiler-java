package lucis.compiler.semantic.concept;

public class LucisTuple implements LucisObject {
    private final LucisObject[] contents;

    public LucisTuple(LucisObject... contents) {
        this.contents = contents;
    }

    public LucisObject at(int i) {
        return contents[i];
    }

    public int size() {
        return contents.length;
    }
}
