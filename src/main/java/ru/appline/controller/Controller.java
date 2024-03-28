package ru.appline.controller;

import javafx.util.Pair;
import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.HashMap;
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
            return new Pair<>("description", "Первый питомец успешно создан!");
        } else {
            return new Pair<>("description", "Питомец успешно создан!");
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

    /*
        {
            "id": 1,
            "pet":{
                "name": "SomeName",
                "type": "SomeType",
                "age": 33
            }
        }
     */
    @PutMapping(value = "/putPet", consumes = "application/json", produces = "application/json")
    public Pair<String, String> putPet(@RequestBody Map<String, Object> newPet) {
        if (petModal.getAll().containsKey(newPet.get("id"))) {
            HashMap<String, Object> petMap = (HashMap<String, Object>) newPet.get("pet");
            Pet pet = new Pet(petMap.get("name").toString(), petMap.get("type").toString(),
                    Integer.parseInt(petMap.get("age").toString()));

            petModal.edit(pet, Integer.parseInt(newPet.get("id").toString()));

            return new Pair<>("description", "Питомец успешно изменён!");
        } else {
            return new Pair<>("description", "Питомца с заданным номером не найдено для изменения!");
        }
    }
}