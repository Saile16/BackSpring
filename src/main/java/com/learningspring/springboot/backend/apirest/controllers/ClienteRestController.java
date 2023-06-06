package com.learningspring.springboot.backend.apirest.controllers;

import com.learningspring.springboot.backend.apirest.models.entity.Cliente;
import com.learningspring.springboot.backend.apirest.models.services.ClienteServiceImp;
import com.learningspring.springboot.backend.apirest.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index(){
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> mostrar(@PathVariable Long id){
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();
        try{
            cliente = clienteService.findById(id);
        }
        catch (DataAccessException e){
            response.put("El mensaje","Error al realizar la consulta en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(cliente == null){
            response.put("mensaje","El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
        //return clienteService.findById(id);
    }

    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Cliente cliente){
        Cliente clienteNew = null;
        Map<String, Object> response = new HashMap<>();
        try{
            clienteNew = clienteService.save(cliente);
            clienteNew.setCreateAt(new Date());
        }catch (DataAccessException e){
            response.put("mensaje","Error al realizar el insert en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","El cliente ha sido credo con éxito!");
        response.put("cliente",clienteNew);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
        // return clienteService.save(cliente);
    }


    //@ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@RequestBody Cliente cliente,@PathVariable Long id){

        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdated = null;
        Map<String, Object> response = new HashMap<>();

        if(cliente == null){
            response.put("mensaje","Error, no se puedo editar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());

        }catch(DataAccessException e){
            response.put("mensaje","Error al actualizar en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        clienteUpdated=clienteService.save(clienteActual);
        response.put("mensaje","El cliente ha sido actualizado con éxito!");
        response.put("cliente",clienteUpdated);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    //@ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            clienteService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje","Error al eliminar en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","El cliente eliminado con éxito!");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
