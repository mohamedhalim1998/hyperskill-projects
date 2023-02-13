package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.model.CodeSnippet;
import platform.service.CodeService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeApiController {

    private CodeService service;

    public CodeApiController(CodeService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public CodeSnippet getCode(HttpServletResponse response, @PathVariable String id) {
        response.addHeader("Content-Type", "application/json");
        try {
            return service.getCode(id);
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @GetMapping("/latest")
    public List<CodeSnippet> getLatestCode(HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
        return service.getLatestCode();
    }


    @PostMapping("/new")
    public HashMap<String, String> postNewCode(@RequestBody CodeSnippet codeSnippet) {
        service.addCode(codeSnippet);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", codeSnippet.getId());
        return map;
    }

}
