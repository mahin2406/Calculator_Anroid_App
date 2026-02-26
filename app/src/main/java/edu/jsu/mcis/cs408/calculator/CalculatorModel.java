package edu.jsu.mcis.cs408.calculator;

public class CalculatorModel extends AbstractModel {

    public static final String PROP_DISPLAY = "display";

    private enum State { ENTERING_LEFT, OP_PENDING, ENTERING_RIGHT, SHOWING_RESULT, ERROR }

    private State state = State.ENTERING_LEFT;

    private String display = "0";
    private double left = 0.0;
    private double right = 0.0;
    private Character op = null;

    public String getDisplay() {
        return display;
    }

    private void setDisplay(String newValue) {
        String old = this.display;
        this.display = newValue;
        firePropertyChange(PROP_DISPLAY, old, newValue);
    }

    public void clear() {
        state = State.ENTERING_LEFT;
        left = 0.0;
        right = 0.0;
        op = null;
        setDisplay("0");
    }

    public void inputDigit(char d) {
        if (state == State.ERROR) return;

        if (state == State.SHOWING_RESULT) {
            op = null;
            left = right = 0.0;
            state = State.ENTERING_LEFT;
            setDisplay("0");
        }

        if (state == State.OP_PENDING) {
            state = State.ENTERING_RIGHT;
            setDisplay("0");
        }

        if (display.equals("0")) setDisplay(String.valueOf(d));
        else setDisplay(display + d);
    }

    public void inputDecimal() {
        if (state == State.ERROR) return;

        if (state == State.SHOWING_RESULT) {
            op = null;
            left = right = 0.0;
            state = State.ENTERING_LEFT;
            setDisplay("0");
        }

        if (state == State.OP_PENDING) {
            state = State.ENTERING_RIGHT;
            setDisplay("0");
        }

        if (!display.contains(".")) setDisplay(display + ".");
    }

    public void inputBinaryOp(char newOp) {
        if (state == State.ERROR) return;

        if (state == State.ENTERING_LEFT || state == State.SHOWING_RESULT) {
            left = parse(display);
            op = newOp;
            state = State.OP_PENDING;
            setDisplay(format(left));
            return;
        }

        if (state == State.OP_PENDING) {
            op = newOp;
            return;
        }

        right = parse(display);
        Double r = apply(left, op, right);
        if (r == null) { setError(); return; }

        left = r;
        op = newOp;
        state = State.OP_PENDING;
        setDisplay(format(left));
    }

    public void equals() {
        if (state == State.ERROR) return;
        if (op == null) return;

        if (state == State.ENTERING_RIGHT) {
            right = parse(display);
            Double r = apply(left, op, right);
            if (r == null) { setError(); return; }

            left = r;
            setDisplay(format(left));
            state = State.SHOWING_RESULT;
        }
    }

    public void negate() {
        if (state == State.ERROR) return;
        double v = parse(display);
        setDisplay(format(-v));
    }

    public void sqrt() {
        if (state == State.ERROR) return;
        double v = parse(display);
        if (v < 0) { setError(); return; }
        setDisplay(format(Math.sqrt(v)));
    }

    public void percent() {
        if (state == State.ERROR) return;

        // Windows-style: A op B% => B = A*B/100
        if (op != null && (state == State.ENTERING_RIGHT || state == State.OP_PENDING)) {
            double b = parse(display);
            b = left * b / 100.0;
            setDisplay(format(b));
            state = State.ENTERING_RIGHT;
            return;
        }

        // Otherwise: value / 100
        double v = parse(display) / 100.0;
        setDisplay(format(v));
    }

    private Double apply(double a, Character operator, double b) {
        if (operator == null) return a;
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return (b == 0.0) ? null : (a / b);
            default: return null;
        }
    }

    private void setError() {
        state = State.ERROR;
        op = null;
        setDisplay("Error");
    }

    private double parse(String s) {
        if (s == null || s.isEmpty() || s.equals("Error")) return 0.0;
        if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
        if (s.equals("-")) return 0.0;
        return Double.parseDouble(s);
    }

    private String format(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-10) return String.valueOf((long) Math.rint(v));

        String s = Double.toString(v);
        if (s.contains(".")) {
            while (s.endsWith("0")) s = s.substring(0, s.length() - 1);
            if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}