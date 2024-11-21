package com.example.demo.controller;

import com.example.demo.entity.RenewableEnergy;
import com.example.demo.repository.RenewableEnergyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/renewableEnergy")
@Transactional(readOnly = true)
@AllArgsConstructor
public class RenewableEnergyController {
    private final RenewableEnergyRepository repository;

    @GetMapping("/")
    public List<RenewableEnergy> get() { return repository.findAll(); }

    @PostMapping("/")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> post(@RequestBody RenewableEnergy entity) {
        if (entity.year != null && repository.existsById(entity.year)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        repository.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<RenewableEnergy> getAll() { return repository.findAll(); }

    @PostMapping("/all")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> postAll(@RequestBody List<RenewableEnergy> entity) {
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

    @GetMapping("/readUncommitted")
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public List<RenewableEnergy> readUncommitted() { return repository.findAll(); }

    @GetMapping("/readCommitted")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<RenewableEnergy> readCommitted() { return repository.findAll(); }

    @GetMapping("/repeatableRead")
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<RenewableEnergy> repeatableRead() { return repository.findAll(); }

    @GetMapping("/id/{id}")
    @Transactional
    public ResponseEntity<?> findById(@PathVariable("id") Short id) {
        Optional<RenewableEnergy> found = repository.findById(id);

        if (found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(found.get(), HttpStatus.OK);
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    @Transactional
    public ResponseEntity<?> put(@RequestBody RenewableEnergy entity, @PathVariable("id") short id) {
        if (entity.year != null && entity.year != id) {
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
