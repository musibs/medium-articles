package io.springbytes.controller;

import io.springbytes.exception.InvalidPocketException;
import io.springbytes.model.Pocket;
import io.springbytes.repository.PocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pockets")
public class PocketController {

    private PocketRepository pocketRepository;

    @Autowired
    public PocketController(PocketRepository pocketRepository) {
        this.pocketRepository = pocketRepository;
    }

    @GetMapping
    public Iterable<Pocket> getAllPockets() {
        return pocketRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pocket getPocketById(@PathVariable String id) {
        Optional<Pocket> pocket = pocketRepository.findById(Long.parseLong(id));
        if (pocket.isPresent()) {
            return pocket.get();
        }
        throw new InvalidPocketException("No such pocket with " + id + " available.");
    }

    @PostMapping
    public Pocket createPocket(@RequestBody Pocket pocket) {
        return pocketRepository.save(pocket);
    }

    @PutMapping
    public Pocket updatePocket(@RequestBody Pocket pocket) {
        return pocketRepository.save(pocket);
    }

    @DeleteMapping
    public void deletePocket(@PathVariable String id) {
        pocketRepository.deleteById(Long.parseLong(id));
    }

}