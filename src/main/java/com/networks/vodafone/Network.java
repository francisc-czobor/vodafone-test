package com.networks.vodafone;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Network {

    private @Id @GeneratedValue Long id;
    private String ip;
    private MetropolitanAreaNetwork man;

    Network() {
    }

    Network(String ip, MetropolitanAreaNetwork man) {

        this.ip = ip;
        this.man = man;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetropolitanAreaNetwork getMetropolitanAreaNetwork() {
        return this.man;
    }

    public void setMetropolitanAreaNetwork(MetropolitanAreaNetwork man) {
        this.man = man;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Network))
            return false;
        Network network = (Network) o;
        return Objects.equals(this.id, network.id)
                && Objects.equals(this.ip, network.ip)
                && Objects.equals(this.man, network.man);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.ip, this.man);
    }

    @Override
    public String toString() {
        return "Network{" + "id=" + this.id + ", ip='" + this.ip + '\''
                + ", man='" + this.man + '\'' + '}';
    }
}
