package com.networks.vodafone;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

/**
 * POJO for defining an IP address.
 * 
 * It contains the ip, an availability flag and a last used timestamp.
 */
@Entity
class IPAddr {

    // primary key
    private @Id @GeneratedValue Long id;

    private String ip;
    private Boolean available;
    private String lastUsed;

    // the many to one relation to the Network table
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "network_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Network network;

    IPAddr() {
    }

    IPAddr(String ip, Boolean available, String lastUsed, Network network) {

        this.ip = ip;
        this.available = available;
        this.lastUsed = lastUsed;
        this.network = network;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
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
                && Objects.equals(this.lastUsed, ipAddr.lastUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.ip, this.network, this.available, this.lastUsed);
    }

    @Override
    public String toString() {
        return "Network{" + "id=" + this.id + ", ip='" + this.ip + '\''
                + ", last_used='" + this.lastUsed + '\''
                + ", available=" + this.available
                + ", network='" + this.network + '\'' + '}';
    }
}
