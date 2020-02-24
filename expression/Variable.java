package expression;

import java.util.Objects;

public class Variable implements CommonExpression {
    private final String name;

    public Variable(String str) {
        this.name = str;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int evaluate(int x) {
        return evaluate(x, x, x);
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return 
            name.equals("x") ? x : 
            name.equals("y") ? y : 
            name.equals("z") ? z : 
            0;
    }
    
    @Override
    public boolean equals(Object object) {
        return object != null 
                && object.getClass() == this.getClass()
                && this.name.equals(((Variable) object).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getClass());
    }
}