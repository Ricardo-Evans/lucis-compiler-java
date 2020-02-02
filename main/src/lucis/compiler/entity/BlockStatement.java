package lucis.compiler.entity;

import java.util.List;

public interface BlockStatement extends StatementTree {
    List<StatementTree> getStatements();
}
