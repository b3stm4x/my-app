package com.example.application.data.service;

import com.example.application.data.entity.Orders;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    private final OrdersRepository repository;

    public OrdersService(OrdersRepository repository){
        this.repository = repository;
    }
    public Optional<Orders> get(Long id){
        return repository.findById(id);
    }

    public Orders update(Orders entity){
        return repository.save(entity);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Page<Orders> list(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Orders> list(Pageable pageable, Specification<Orders> filter){
        return repository.findAll(filter, pageable);
    }

    public int count(){
        return (int) repository.count();
    }
}
