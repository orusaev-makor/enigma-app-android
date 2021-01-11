package com.example.enigmaapp.web.trade.dataset;

import java.util.ArrayList;

public class TradeDatasetCounterparty {

    private int id;
    private String name, company, broker;
    private ArrayList<String> mail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public ArrayList<String> getMail() {
        return mail;
    }

    public void setMail(ArrayList<String> mail) {
        this.mail = mail;
    }
}
