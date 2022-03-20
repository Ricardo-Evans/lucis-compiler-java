package compiler.entity;

public record Unit(String name, Object value, Position startPosition, Position endPosition) {
}
