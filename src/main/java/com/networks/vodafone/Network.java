package com.networks.vodafone;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
class Network {

    private @Id @GeneratedValue Long id;
    private String ip;
    private String location;
    private String man;
    private int securityLevel;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private Set<IPAddr> addresses;

    Network() {
    }

    Network(String ip, String location, String man, int securityLevel) {

        this.ip = ip;
        this.location = location;
        this.man = man;
        this.securityLevel = securityLevel;
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

    public String getMan() {
        return this.man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSecurityLevel() {
        return this.securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Set<IPAddr> getAdresses() {
        return this.addresses;
    }

    public void setAdresses(Set<IPAddr> addresses) {
        this.addresses = addresses;

        for (IPAddr address : addresses) {
            address.setNetwork(this);
        }
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
                && Objects.equals(this.man, network.man)
                && Objects.equals(this.location, network.location)
                && Objects.equals(this.securityLevel, network.securityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.ip, this.man, this.location, this.securityLevel);
    }

    @Override
    public String toString() {
        return "Network{" + "id=" + this.id + ", ip='" + this.ip + '\''
                + ", man='" + this.man + '\'' + ", location='" + this.location + '\''
                + ", securityLevel=" + this.securityLevel + '}';
    }
}
