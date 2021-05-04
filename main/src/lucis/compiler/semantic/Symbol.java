package lucis.compiler.semantic;

import compiler.entity.Position;

import java.io.Serializable;

public final class Symbol implements Serializable {
    private static final long serialVersionUID = -2876757869241629319L;
    public final String name;
    public final Position position;
    public final String type;

    public Symbol(String name, Position position, String type) {
        this.name = name;
        this.position = position;
        this.type = type;
    }
}
