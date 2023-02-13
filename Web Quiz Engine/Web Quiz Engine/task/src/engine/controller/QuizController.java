package engine.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.model.Complete;
import engine.model.Quiz;
import engine.model.Result;
import engine.model.User;
import engine.repo.CompleteRepository;
import engine.repo.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class QuizController {
    private final QuizRepository quizRepository;
    private final CompleteRepository completeRepository;

    public QuizController(QuizRepository quizRepository, CompleteRepository completeRepository) {
        this.quizRepository = quizRepository;
        this.completeRepository = completeRepository;
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        return quizRepository.findById(id).get();
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int id, Authentication auth) {

        Quiz quiz = quizRepository.findById(id).get();
        if (Objects.equals(quiz.getUser().getEmail(), auth.getName())) {
            quizRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam("page") int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    @PostMapping("/quizzes")
    public Quiz postQuiz(@RequestBody @Validated Quiz quiz, Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        quiz.setUser(new User(details.getUsername(), details.getPassword()));
        return quizRepository.save(quiz);
    }

    @PostMapping("/quizzes/{id}/solve")
    public Result postAnswer(@PathVariable int id, @RequestBody JsonNode answer, Authentication auth) throws IOException {
        System.out.println("solve:  " + answer);
        if (new ObjectMapper().readerFor(new TypeReference<List<Integer>>() {
        }).readValue(answer.get("answer")).equals(quizRepository.getById(id).getAnswer())) {
            completeRepository.save(new Complete(id, auth.getName(), LocalDateTime.now()));
            return new Result(true, "Congratulations, you're right!");
        } else {
            return new Result(false, "Wrong answer! Please, try again.");
        }

    }
    @GetMapping("/quizzes/completed")
    public Page<Complete> completedQuizzes(Authentication auth, @RequestParam(value = "page", defaultValue = "0") int page) {
        return completeRepository.findCompletionsByUserEmail(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "time")), auth.getName());
    }

    @ExceptionHandler({IndexOutOfBoundsException.class, EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Void> handleIdNotFound() {
        return ResponseEntity.notFound().build();
    }


}