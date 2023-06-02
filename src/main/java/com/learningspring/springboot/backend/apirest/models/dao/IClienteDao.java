package com.learningspring.springboot.backend.apirest.models.dao;

import com.learningspring.springboot.backend.apirest.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao  extends CrudRepository<Cliente,Long> {

}
