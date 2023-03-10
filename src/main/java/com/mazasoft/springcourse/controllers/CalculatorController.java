package com.mazasoft.springcourse.controllers;

import com.mazasoft.springcourse.common.Action;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@Controller
@RequestMapping("/calc")
public class CalculatorController {

    @GetMapping("/calculator")
    public String execute(@RequestParam("a") Double a, @RequestParam("b") Double b, @RequestParam("action") String actionStr, Model model) {
        Action action = Action.fromString(actionStr);
        Double result = null;
        switch (action) {
            case ADDITION:
                result = a + b;
                break;
            case SUBTRACTION:
                result = a - b;
                break;
            case MULTIPLICATION:
                result = a * b;
                break;
            case DIVISION:
                result = a / b;
                break;
            default:
                result = null;
                break;
        }
        model.addAttribute("result", result);
        return "calc/calculator";
    }
}
