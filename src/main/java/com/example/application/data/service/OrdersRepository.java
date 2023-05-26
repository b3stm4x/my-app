package com.example.application.data.service;

import com.example.application.data.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdersRepository
        extends
            JpaRepository<Orders, Long>,
            JpaSpecificationExecutor<Orders> {

}
