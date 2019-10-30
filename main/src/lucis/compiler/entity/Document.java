package lucis.compiler.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Document implements Serializable {
    private static final long serialVersionUID = -6223780197809439054L;
    private List<Token> tokens;

    public Document(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }

}
