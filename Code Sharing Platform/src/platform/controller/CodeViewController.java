package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import platform.model.CodeSnippet;
import platform.service.CodeService;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/code")
public class CodeViewController {

    @Autowired
    private CodeService service;

    @GetMapping("/{id}")
    public String getCode(Model model, @PathVariable String id, HttpServletResponse response) {
        try {
            model.addAttribute("code", service.getCode(id));
            return "codeview";
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }
    }

    @GetMapping("/latest")
    public String getCode(Model model) {

        model.addAttribute("codeList", service.getLatestCode());
        return "latestcode";
    }

    @GetMapping("/new")
    public String postCode(Model model, CodeSnippet code) {
        model.addAttribute("code", code);
        return "code_form";
    }
}
