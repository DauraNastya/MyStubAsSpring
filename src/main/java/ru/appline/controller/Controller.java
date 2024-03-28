package ru.appline.controller;

import javafx.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petModal = PetModel.getInstance();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @PostMapping(value = "/createPet", consumes = "application/json", produces = "application/json")
    public Pair<String, String> createPet(@RequestBody Pet pet) {
        int id = newId.getAndIncrement();
        petModal.add(pet, id);
        if (id == 1) {
            return new Pair<String, String>("description", "Первый питомец успешно создан!");
        } else {
            return new Pair<String, String>("description", "Питомец успешно создан!");
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer, Pet> getAll() {
        return petModal.getAll();
    }

    /*
        {
            "id": 1
        }
     */
    @GetMapping(value = "/getPet", consumes = "application/json", produces = "application/json")
    public Pet getPet(@RequestBody Map<String, Integer> id) {
        return petModal.getFromList(id.get("id"));
    }
}