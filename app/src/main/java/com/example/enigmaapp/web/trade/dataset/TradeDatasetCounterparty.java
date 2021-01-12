package com.example.enigmaapp.web.trade.dataset;

import java.util.ArrayList;

public class TradeDatasetCounterparty {

    private String name, company, broker, id;
    private ArrayList<String> mail;
    private boolean isChecked = false;

    public boolean getIsChecked() { return isChecked; }

    public void setIsChecked(boolean checked) { isChecked = checked; }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
