package com.example.application.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class CustomerEntity extends AbstractEntity{

    private int kundennummer;
    private String firma1;
    private String firma2;
    private String customerStadt;
    private String customerStrasse;
    private int customerHausnummer;
    private String customerLand;
    private int customerPLZ;
    private String customerTelefonnummer;
    @Email
    private String customerEmail;
    private String customerKontaktperson;
    private String customerKontakttelefon;
    @Email
    private String customerKontaktmail;
    private String customerUmsatzsteueridentifikation;
    private String customerNotiz;

    public String getCustomerStadt() {
        return customerStadt;
    }

    public void setCustomerStadt(String customerStadt) {
        this.customerStadt = customerStadt;
    }

    public String getCustomerLand() {
        return customerLand;
    }

    public void setCustomerLand(String customerLand) {
        this.customerLand = customerLand;
    }
    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kundennummer) {
        this.kundennummer = kundennummer;
    }

    public String getFirma1() {
        return firma1;
    }

    public void setFirma1(String firma1) {
        this.firma1 = firma1;
    }

    public String getFirma2() {
        return firma2;
    }

    public void setFirma2(String firma2) {
        this.firma2 = firma2;
    }

    public String getCustomerStrasse() {
        return customerStrasse;
    }

    public void setCustomerStrasse(String customerStrasse) {
        this.customerStrasse = customerStrasse;
    }

    public int getCustomerHausnummer() {
        return customerHausnummer;
    }

    public void setCustomerHausnummer(int customerHausnummer) {
        this.customerHausnummer = customerHausnummer;
    }

    public int getCustomerPLZ() {
        return customerPLZ;
    }

    public void setCustomerPLZ(int customerPLZ) {
        this.customerPLZ = customerPLZ;
    }

    public String getCustomerTelefonnummer() {
        return customerTelefonnummer;
    }

    public void setCustomerTelefonnummer(String customerTelefonnummer) {
        this.customerTelefonnummer = customerTelefonnummer;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerKontaktperson() {
        return customerKontaktperson;
    }

    public void setCustomerKontaktperson(String customerKontaktperson) {
        this.customerKontaktperson = customerKontaktperson;
    }

    public String getCustomerKontakttelefon() {
        return customerKontakttelefon;
    }

    public void setCustomerKontakttelefon(String customerKontakttelefon) {
        this.customerKontakttelefon = customerKontakttelefon;
    }

    public String getCustomerKontaktmail() {
        return customerKontaktmail;
    }

    public void setCustomerKontaktmail(String customerKontaktmail) {
        this.customerKontaktmail = customerKontaktmail;
    }

    public String getCustomerUmsatzsteueridentifikation() {
        return customerUmsatzsteueridentifikation;
    }

    public void setCustomerUmsatzsteueridentifikation(String customerUmsatzsteueridentifikation) {
        this.customerUmsatzsteueridentifikation = customerUmsatzsteueridentifikation;
    }

    public String getCustomerNotiz() {
        return customerNotiz;
    }

    public void setCustomerNotiz(String customerNotiz) {
        this.customerNotiz = customerNotiz;
    }
}
