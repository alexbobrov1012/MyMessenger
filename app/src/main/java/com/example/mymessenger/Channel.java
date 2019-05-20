package com.example.mymessenger;

import java.util.List;

public class Channel {

    private List<String> name;

    private List<String> owners;

    public Channel() {
    }

    public Channel(List<String> name, List<String> owners) {
        this.name = name;
        this.owners = owners;
    }


    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }
}
