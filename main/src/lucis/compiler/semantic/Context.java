package lucis.compiler.semantic;

import lucis.compiler.ir.LucisObject;
import lucis.compiler.ir.LucisType;
import lucis.compiler.ir.LucisVariable;

import java.util.Optional;

public interface Context {
    Optional<Context> parent();

    Optional<LucisType> findType(String name);

    Optional<LucisVariable> findVariable(String name);

    Optional<LucisObject> findConstant(String name);
}
