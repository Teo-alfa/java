package expression.parser;

public class ExpressionSource {
    String source;
    int position;
    public ExpressionSource(String source) {
        this.source = source;
        position = 0;
    }

    public char next() {
        return source.charAt(position++);
    }

    public char back() {
        return source.charAt(--position);
    }

    public char token() {
        return source.charAt(position);
    }

    public boolean hasNext() {
        return position < source.length();
    }

    public int position() {
        return position;
    }
}