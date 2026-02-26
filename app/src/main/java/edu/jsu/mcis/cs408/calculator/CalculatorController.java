package edu.jsu.mcis.cs408.calculator;

public class CalculatorController extends AbstractController {

    private final CalculatorModel model;

    public CalculatorController(CalculatorModel model) {
        this.model = model;
        addModel(model);
    }

    public void handleInput(String tag) {

        if (tag != null && tag.matches("btn[0-9]")) {
            model.inputDigit(tag.charAt(3));
            return;
        }

        switch (tag) {
            case "btnDecimal":  model.inputDecimal(); break;
            case "btnClear":    model.clear(); break;

            case "btnPlus":     model.inputBinaryOp('+'); break;
            case "btnMinus":    model.inputBinaryOp('-'); break;
            case "btnMultiply": model.inputBinaryOp('*'); break;
            case "btnDivide":   model.inputBinaryOp('/'); break;

            case "btnEquals":   model.equals(); break;

            case "btnSqrt":     model.sqrt(); break;
            case "btnSign":     model.negate(); break;
            case "btnPercent":  model.percent(); break;

            default:
                break;
        }
    }
}