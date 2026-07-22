package com.carlos.commons.controller;

import com.carlos.commons.service.CrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class CommonController<RQ, RS, S extends CrudService<RQ, RS>> {

    protected final S service;

    protected CommonController(S service) {
        this.service = service;
    }

    @GetMapping
    public List<RS> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public RS obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public RS registrar(@RequestBody RQ request) {
        return service.registrar(request);
    }

    @PutMapping("/{id}")
    public RS actualizar(@RequestBody RQ request, @PathVariable Long id) {
        return service.actualizar(request, id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
