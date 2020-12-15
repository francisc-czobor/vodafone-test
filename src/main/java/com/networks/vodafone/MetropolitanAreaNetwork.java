package com.networks.vodafone;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class MetropolitanAreaNetwork {

    private @Id @GeneratedValue Long id;
    private String name;
    private int securityLevel;
    private Location location;

    MetropolitanAreaNetwork() {
    }

    MetropolitanAreaNetwork(String name, int securityLevel, Location location) {

        this.name = name;
        this.securityLevel = securityLevel;
        this.location = location;
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

    public int getSecurityLevel() {
        return this.securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof MetropolitanAreaNetwork))
            return false;
        MetropolitanAreaNetwork metropolitanAreaNetwork = (MetropolitanAreaNetwork) o;
        return Objects.equals(this.id, metropolitanAreaNetwork.id)
                && Objects.equals(this.name, metropolitanAreaNetwork.name)
                && Objects.equals(this.securityLevel, metropolitanAreaNetwork.securityLevel)
                && Objects.equals(this.location, metropolitanAreaNetwork.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.securityLevel, this.location);
    }

    @Override
    public String toString() {
        return "MetropolitanAreaNetwork{" + "id=" + this.id + ", name='" + this.name + '\''
                + ", securityLevel=" + this.securityLevel + ", location='" + this.location + '\'' + '}';
    }
}
