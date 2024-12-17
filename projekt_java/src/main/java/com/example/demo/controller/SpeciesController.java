package com.example.demo.controller;

import com.example.demo.entity.Species;
import com.example.demo.repository.SpeciesRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/species")
@Transactional(readOnly = true)
@AllArgsConstructor
public class SpeciesController {
    private final SpeciesRepository repository;

    @GetMapping("/")
    public List<Species> get() { return repository.findAll(); }

    @PostMapping("/")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> post(@RequestBody Species entity) {
        if ( repository.existsById(entity.id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        repository.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PostMapping("/all")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> postAll(@RequestBody List<Species> entity) {
        repository.saveAll(entity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> deleteAll() {
        repository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/id/{id}")
    @Transactional
    public ResponseEntity<?> findById(@PathVariable("id") Short id) {
        Optional<Species> found = repository.findById(id);

        if (found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(found.get(), HttpStatus.OK);
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> put(@RequestBody Species entity, @PathVariable("id") short id) {
        if (entity.id != id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (repository.existsById(id)) {
            repository.save(entity);

            return new ResponseEntity<>(entity, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Short id) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
