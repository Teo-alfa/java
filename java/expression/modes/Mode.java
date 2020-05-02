package expression.modes;

public interface Mode<T> {
    T add(T left, T right);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
    T negate(T value);
    T parseConst(String number);
    T min(T left, T right);
    T max(T left, T right);
    T count(T value);
}