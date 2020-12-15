package com.networks.vodafone;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Location {

    private @Id @GeneratedValue Long id;
    private String name;

    Location() {
    }

    Location(String name) {

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Location))
            return false;
        Location location = (Location) o;
        return Objects.equals(this.id, location.id) && Objects.equals(this.name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "Location{" + "id=" + this.id + ", name='" + this.name + '\'' + '}';
    }
}
