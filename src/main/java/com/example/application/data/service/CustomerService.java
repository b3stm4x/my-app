package com.example.application.data.service;

import com.example.application.data.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository){
        this.repository = repository;
    }
    public Optional<CustomerEntity> get(Long id){
        return repository.findById(id);
    }
    public CustomerEntity update(CustomerEntity entity){
        return repository.save(entity);
    }
    public void delete(Long id){
        repository.deleteById(id);
    }
    public Page<CustomerEntity> list(Pageable pageable){
        return repository.findAll(pageable);
    }
    public int count() {
        return (int) repository.count();
    }
    public CustomerRepository getRepository() {
        return repository;
    }

}
