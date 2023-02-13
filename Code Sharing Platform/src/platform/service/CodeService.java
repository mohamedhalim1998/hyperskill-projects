package platform.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import platform.model.CodeSnippet;
import platform.repo.CodeRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeService {
    private CodeRepository repository;

    public CodeService(CodeRepository repository) {
        this.repository = repository;
    }

    public CodeSnippet addCode(CodeSnippet codeSnippet) {
        codeSnippet.setDate(LocalDateTime.now());
        codeSnippet.setId(UUID.randomUUID().toString());
        if (codeSnippet.getRemainTime() > 0) {
            codeSnippet.setTimeLimit(codeSnippet.getRemainTime());
        }
        if (codeSnippet.getViewLimit() > 0) {
            codeSnippet.setHasViewLimit(true);
        }
        repository.save(codeSnippet);
        return codeSnippet;
    }

    public CodeSnippet getCode(String id) {

        Optional<CodeSnippet> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException();
        }

        CodeSnippet code = optional.get();
        if (code.getTimeLimit() > 0) {
            long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            long posted = code.getDate().toEpochSecond(ZoneOffset.UTC);
            System.out.println("now: " + now);
            System.out.println("posted: " + posted);
            System.out.println("DIFF: " + (now - posted));
            long remain = code.getTimeLimit() - now + posted;
            if (remain > 0) {
                code.setRemainTime(remain);
            } else {
                repository.delete(code);
                throw new RuntimeException();
            }
        }
        if (code.hasViewLimit()) {
            if (code.getViewLimit() > 0) {
                code.setViewLimit(code.getViewLimit() - 1);
                repository.save(code);
            } else {
                repository.delete(code);
                throw new RuntimeException();
            }
        }

        return code;
    }

    public List<CodeSnippet> getLatestCode() {
        return repository.getLatestCode(PageRequest.of(0, 10));
    }
}
