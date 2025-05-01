package com.example.garazojazdowapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pojazdy")
public class PojazdAPI {

    private List<Pojazd> listaPojazdow = new ArrayList<>();

    public PojazdAPI(){
        listaPojazdow.add(new Pojazd(1,"Audi","A6","Black"));
        listaPojazdow.add(new Pojazd(2,"BMW","X5","Khaki"));
        listaPojazdow.add(new Pojazd(3,"Porsche","Cayenne","Burgund"));
    }



    @GetMapping
    public  ResponseEntity<?> getPojazd(@RequestParam (required = false) Long id, @RequestParam (required = false) String color) {

        if (id != null) {
            Optional<Pojazd> first = listaPojazdow.stream().filter(pojazd -> pojazd.getId() == id).findFirst();

            if (first.isPresent()) {
                return new ResponseEntity<>(first.get(), HttpStatus.OK);
            }
        }

         if (color != null) {
            Optional<Pojazd> second = listaPojazdow.stream().filter(pojazd -> pojazd.getColor().equals(color)).findFirst();

            if (second.isPresent()) {
                return new ResponseEntity<>(second.get(), HttpStatus.OK);
            }
        }
         if(color == null && id == null)
             return new ResponseEntity<>(listaPojazdow, HttpStatus.OK);

         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping
    public ResponseEntity<Pojazd> addPojazd(@RequestBody Pojazd newPojazd){
       boolean add = listaPojazdow.add(newPojazd);
       if(add){
           return new ResponseEntity<>(HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Pojazd> updatePojazd(@PathVariable long id, @RequestBody Pojazd newPojazd){
        Optional<Pojazd> first = listaPojazdow.stream().filter(pojazd -> pojazd.getId() == id).findFirst();
        if(first.isPresent()){
            listaPojazdow.remove(first.get());
            listaPojazdow.add(newPojazd);
            return  new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pojazd> modifyPojazd(@PathVariable long id, @RequestBody Map<String,String> param){
        Optional<Pojazd> first = listaPojazdow.stream().filter(pojazd -> pojazd.getId() == id).findFirst();
        if(first.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pojazd updated = first.get();

        param.forEach((key,value) -> {
            if (param.containsKey("mark")) {
                updated.setMark(value);
            } else if (param.containsKey("color")) {
                updated.setColor(value);
            } else if (param.containsKey("model")) {
                updated.setModel(value);
            }
        });

        return new ResponseEntity<>(updated,HttpStatus.OK);


    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Pojazd> deletePojazd(@PathVariable long id){
        Optional<Pojazd> first = listaPojazdow.stream().filter(pojazd -> pojazd.getId() == id).findFirst();
        if(first.isPresent()){
            listaPojazdow.remove(first.get());
            return new ResponseEntity<>(first.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
