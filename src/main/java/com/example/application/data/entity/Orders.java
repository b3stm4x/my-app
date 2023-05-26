package com.example.application.data.entity;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Entity;

@Entity
public class Orders extends AbstractEntity {
    private int orderId;
    private int customerId;
    private String objectDescription;
    private String pickupAdress;
    private String pickupCountry;
    private String deliveryAdress;
    private String deliveryCountry;
    private int vehicleId;
    private LocalDate etd;
    private LocalDate eta;


    public int getOrderId(){
        return this.orderId;
    }
    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
    
    public int getCustomerId(){
        return this.customerId;
    }
    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }

    public String getObjectDescription(){
        return this.objectDescription;
    }
    public void setObjectDescription(String objectDescription){
        this.objectDescription = objectDescription;
    }

    public String getPickupAdress(){
        return this.pickupAdress;
    }
    public void setPickupAdress(String pickupAdress){
        this.pickupAdress = pickupAdress;
    }

    public String getPickupCountry(){
        return this.pickupCountry;
    }
    public void setPickupCountry(String pickupCountry){
        this.pickupCountry = pickupCountry;
    }

    public String getDeliveryAdress(){
        return this.deliveryAdress;
    }
    public void setDeliveryAdress(String deliveryAdress){
        this.deliveryAdress = deliveryAdress;
    }

    public String getDeliveryCountry(){
        return this.deliveryCountry;
    }
    public void setDeliveryCountry(String deliveryCountry){
        this.deliveryCountry = deliveryCountry;
    }

    public int getVehicleId(){
        return this.vehicleId;
    }
    public void setVehicleId(int vehicleId){
        this.vehicleId = vehicleId;
    }

    public LocalDate getEtd(){
        return this.etd;
    }
    public void setEtd(LocalDate etd){
        this.etd = etd;
    }

    public LocalDate getEta(){
        return this.eta;
    }
    public void setEta(LocalDate eta){
        this.eta = eta;
    }
}
