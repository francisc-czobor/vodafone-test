package com.networks.vodafone;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class IPAddr {

    private @Id @GeneratedValue Long id;
    private String ip;
    private Boolean available;
    private String last_used;
    private Network network;

    IPAddr() {
    }

    IPAddr(String ip, Boolean available, String last_used, Network network) {

        this.ip = ip;
        this.available = available;
        this.last_used = last_used;
        this.network = network;
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

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof IPAddr))
            return false;
        IPAddr ipAddr = (IPAddr) o;
        return Objects.equals(this.id, ipAddr.id) && Objects.equals(this.ip, ipAddr.ip)
                && Objects.equals(this.network, ipAddr.network)
                && Objects.equals(this.available, ipAddr.available)
                && Objects.equals(this.last_used, ipAddr.last_used);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.ip, this.network, this.available, this.last_used);
    }

    @Override
    public String toString() {
        return "Network{" + "id=" + this.id + ", ip='" + this.ip + '\'' + ", network='" + this.network + '\''
                + ", last_used='" + this.last_used + '\''
                + ", available=" + this.available + '}';
    }
}
