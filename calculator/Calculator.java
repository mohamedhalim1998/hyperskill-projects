package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;


public class Calculator extends JFrame {
    private JLabel equation;
    private JLabel answer;
    private int openParentheses = 0;
    private final ArrayList<CalculatorButton> buttons = new ArrayList<>();

    {


        buttons.add(new CalculatorButton("Parentheses", "()", actionEvent -> {
            if (openParentheses == 0
                    || equation.getText().charAt(equation.getText().length() - 1) == '('
                    || isOperator(equation.getText().charAt(equation.getText().length() - 1))) {
                equation.setText(equation.getText() + "(");
                openParentheses++;
            } else {
                equation.setText(equation.getText() + ")");
                openParentheses--;
            }
        }));
        buttons.add(new CalculatorButton("ClearE", "CE",
                actionEvent -> {
                    equation.setText("");
                    equation.setForeground(Color.GREEN);
                    openParentheses = 0;
                }));
        buttons.add(new CalculatorButton("Clear", "C",
                actionEvent -> {
                    equation.setText("");
                    equation.setForeground(Color.GREEN);
                    openParentheses = 0;
                }));
        buttons.add(new CalculatorButton("Delete", "del",
                ac -> {
                    if (equation.getText().charAt(equation.getText().length() - 1) == '(') {
                        openParentheses--;
                    } else if (equation.getText().charAt(equation.getText().length() - 1) == ')') {
                        openParentheses++;
                    }
                    equation.setText(equation.getText().substring(0, equation.getText().length() - 1));
                    equation.setForeground(Color.GREEN);
                }));
        buttons.add(new CalculatorButton("PowerTwo", "X\u00B2", actionEvent -> equation.setText(equation.getText() + "^(2)")));
        buttons.add(new CalculatorButton("PowerY", "X\u02b8", actionEvent -> {
            equation.setText(equation.getText() + "^(");
            openParentheses++;
        }));
        buttons.add(new CalculatorButton("SquareRoot", "\u221A", actionEvent -> {
            equation.setText(equation.getText() + "\u221A(");
            openParentheses++;
        }));
        buttons.add(new CalculatorButton("Divide", "\u00F7"));
        buttons.add(new CalculatorButton("Seven", "7"));
        buttons.add(new CalculatorButton("Eight", "8"));
        buttons.add(new CalculatorButton("Nine", "9"));
        buttons.add(new CalculatorButton("Multiply", "\u00D7"));
        buttons.add(new CalculatorButton("Four", "4"));
        buttons.add(new CalculatorButton("Five", "5"));
        buttons.add(new CalculatorButton("Six", "6"));
        buttons.add(new CalculatorButton("Subtract", "-"));
        buttons.add(new CalculatorButton("One", "1"));
        buttons.add(new CalculatorButton("Two", "2"));
        buttons.add(new CalculatorButton("Three", "3"));
        buttons.add(new CalculatorButton("Add", "\u002B"));
        buttons.add(new CalculatorButton("PlusMinus", "\u00B1", actionEvent -> {
            if (equation.getText().length() > 1 && equation.getText().charAt(1) == '-' && equation.getText().charAt(0) == '(') {
                equation.setText(equation.getText().substring(2));
                openParentheses--;
            } else if (equation.getText().length() == 0) {
                equation.setText(equation.getText() + "(-");
                openParentheses++;
            } else if (equation.getText().length() == 1) {
                equation.setText("(-" + equation.getText());
                openParentheses++;
            } else {
                equation.setText("(-(" + equation.getText() + ")");
                openParentheses++;
            }
        }));
        buttons.add(new CalculatorButton("Zero", "0"));
        buttons.add(new CalculatorButton("Dot", "."));
        buttons.add(new CalculatorButton("Equals", "=", actionEvent -> {
            try {
                String s = equation.getText();
                System.out.println(s);
                ArrayList<String> postFix = convertToPostFix(s);
                System.out.println(postFix);
                answer.setText(calculateAnswer(postFix));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                equation.setForeground(Color.RED.darker());
            }

        }));
    }


    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);
        initAnswerLabel();
        initEquationLabel();
        setLocationRelativeTo(null);
        initNumbersButtons();
//        equation.setText("(-(√()9))");
        setVisible(true);
    }

    private void initNumbersButtons() {
        JPanel numberGrid = new JPanel();
        numberGrid.setLayout(new GridLayout(6, 4, 20, 20));
        numberGrid.setBounds(25, 150, 350, 300);
        for (CalculatorButton button : buttons) {
            numberGrid.add(createButton(button));
        }
        add(numberGrid);
    }

    private Component createButton(CalculatorButton calculatorButton) {
        JButton button = new JButton();
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setText(calculatorButton.getText());
        button.setName(calculatorButton.getName());
        button.addActionListener(calculatorButton.getActionListener() != null ? calculatorButton.getActionListener() :
                actionEvent -> {
                    equation.setText(equation.getText() + calculatorButton.getText());
                    if (isOperator(calculatorButton.getText().charAt(0))) {
                        formatEquation();
                    }
                }
        );
        return button;
    }

    private void formatEquation() {
        StringBuilder builder = new StringBuilder(equation.getText());
        if (builder.length() == 1 || isOperator(builder.charAt(builder.length() - 2))) {
            builder.deleteCharAt(builder.length() > 1 ? builder.length() - 2 : builder.length() - 1);
            equation.setText(builder.toString());
            return;
        }
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '.') {
                if (i > 0 && !Character.isDigit(builder.charAt(i - 1))) {
                    builder.insert(i - 1, 0);
                    i++;
                }
                if (i == 0) {
                    builder.insert(0, 0);
                    i++;
                }
                if (i + 1 < builder.length() && !Character.isDigit(builder.charAt(i + 1))) {
                    builder.insert(i + 1, 0);
                }
                if (i + 1 == builder.length()) {
                    builder.append(0);
                }

            }
        }
        equation.setText(builder.toString());
    }


    private String calculateAnswer(ArrayList<String> postFix) {
        Stack<Double> stack = new Stack<>();
        for (String s : postFix) {
            try {
                double num = Double.parseDouble(s);
                stack.push(num);
            } catch (NumberFormatException e) {
                switch (s.charAt(0)) {
                    case '\u002B': {
                        double a = stack.pop();
                        double b = stack.pop();
                        stack.push(a + b);
                        break;
                    }
                    case '-': {
                        double a = stack.pop();
                        double b = stack.pop();
                        stack.push(b - a);
                        break;
                    }
                    case '\u00F7': {
                        double a = stack.pop();
                        double b = stack.pop();
                        if (a == 0)
                            throw new IllegalArgumentException();
                        stack.push(b / a);
                        break;
                    }
                    case '\u00D7': {
                        double a = stack.pop();
                        double b = stack.pop();
                        stack.push(a * b);
                        break;
                    }
                    case '^': {
                        double a = stack.pop();
                        double b = stack.pop();
                        stack.push(Math.pow(b, a));
                        break;
                    }
                    case '\u221A': {
                        double a = stack.pop();
                        stack.push(Math.sqrt(a));
                        break;
                    }
                }
            }
        }
        double ans = stack.pop();
        if (ans - ((int) ans) == 0) {
            return ((int) ans) + "";
        } else {
            return ans + "";
        }
    }

    private ArrayList<String> convertToPostFix(String s) {
        s = s.replace("(-", "(0-");
        Stack<Character> operations = new Stack<>();
        ArrayList<String> ans = new ArrayList<>();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                num.append(c);
            } else if (c == ')') {
                if (num.length() != 0)
                    ans.add(num.toString());
                if (s.charAt(i - 1) == '(') {
                    throw new IllegalArgumentException();
                }
                while (operations.peek() != '(') {
                    ans.add(operations.pop().toString());
                }
                operations.pop();
                num = new StringBuilder();
            } else if (c == '(') {
                operations.push(c);
                num = new StringBuilder();
            } else {
                if (num.length() != 0)
                    ans.add(num.toString());
                num = new StringBuilder();
                while (!operations.isEmpty() && precedence(c) <= precedence(operations.peek())) {
                    ans.add(operations.pop().toString());
                }
                operations.add(c);
            }
        }
        if (num.length() != 0)
            ans.add(num.toString());
        while (!operations.isEmpty()) {
            ans.add(operations.pop().toString());
        }
        return ans;

    }

    private int precedence(Character c) {
        switch (c) {
            case '\u002B':
            case '-':
                return 1;
            case '\u00F7':
            case '\u00D7':
                return 2;
            case '^':
                return 3;
            case '\u221A':
                return 4;
            default:
                return 0;
        }
    }

    private boolean isOperator(char c) {
        return c == '\u002B' || c == '-' || c == '\u00F7' || c == '\u00D7' || c == '^';
    }

    private void initEquationLabel() {
        equation = new JLabel();
        equation.setBounds(25, 50, 350, 20);
        add(equation);
        Font f = new Font(null, Font.PLAIN, 14);
        equation.setHorizontalAlignment(SwingConstants.RIGHT);
        equation.setFont(f);
        equation.setForeground(Color.GREEN.darker());
        equation.setName("EquationLabel");
    }

    private void initAnswerLabel() {
        answer = new JLabel();
        answer.setBounds(25, 20, 350, 20);
        add(answer);
        answer.setName("ResultLabel");
        Font f = new Font(null, Font.PLAIN, 20);
        answer.setHorizontalAlignment(SwingConstants.RIGHT);
        answer.setFont(f);
    }


}
