package calculator;

import java.awt.event.ActionListener;

public class CalculatorButton {
    private String name;
    private String text;
    private ActionListener actionListener;

    public CalculatorButton(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public CalculatorButton(String name, String text, ActionListener actionListener) {
        this.name = name;
        this.text = text;
        this.actionListener = actionListener;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }
}
