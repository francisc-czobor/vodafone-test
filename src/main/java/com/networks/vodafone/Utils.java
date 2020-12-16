package com.networks.vodafone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.IPAddress;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(NetworksController.class);
    
    @SuppressWarnings("unchecked")
    public static List<Network> parseRequestBody(Map<String, Object> body) {

        List<Network> list = new ArrayList<Network>();

        for (String location : body.keySet()) {
            Map<String, Object> mans = (Map<String, Object>) body.get(location);
            for (String man : mans.keySet()) {
                Map<String, Object> manInfo = (Map<String, Object>) mans.get(man);
                Map<String, Object> networks;
                try {
                    networks = (Map<String, Object>) manInfo.get("networks");
                } catch (NullPointerException e) {
                    log.warn(man + " has no networks");
                    continue;
                }
                for (String network : networks.keySet()) {
                    try {
                        new IPAddressString(network).toAddress();
                    } catch (AddressStringException e) {
                        log.warn(network + " is invalid");
                        continue;
                    }
                    int securityLevel = 0;
                    try {
                        securityLevel = (int) manInfo.get("security_level");
                    } catch (NullPointerException e) {
                        log.warn(man + " has no security level, defaults to 0");
                    }
                    Network newNetwork = new Network(network, location, man, securityLevel);
                    List<Map<String, Object>> addresses = (List<Map<String, Object>>)networks.get(network);
                    Set<IPAddr> newAddresses = new HashSet<IPAddr>();
                    for (Map<String, Object> address : addresses) {
                        try {
                            String addressIP = (String) address.get("address");
                            try {
                                new IPAddressString(addressIP).toAddress();
                            } catch (AddressStringException e) {
                                log.warn(addressIP + " is invalid");
                                continue;
                            }
                            if (subnetContains(network, addressIP)) {
                                IPAddr newAddress = new IPAddr();
                                newAddress.setIp((String)address.get("address"));
                                newAddress.setAvailable((Boolean)address.get("available"));
                                newAddress.setLastUsed((String)address.get("last_used"));
                                newAddresses.add(newAddress);
                            } else {
                                log.warn(addressIP + " is not in " + network); 
                            }
                        } catch (NullPointerException e) {
                            log.warn("malformed address " + address);
                        }
                    }
                    newNetwork.setAdresses(newAddresses);
                    list.add(newNetwork);
                }
            }
        }

        return list;
    }

    public static boolean subnetContains(String network, String address) {
        IPAddressString networkString = new IPAddressString(network);
        IPAddress networkIPAddr = networkString.getAddress().toPrefixBlock();
        IPAddress addressIPAddr = new IPAddressString(address).getAddress();

        boolean result = networkIPAddr.contains(addressIPAddr);

        log.info(networkString + " block " + networkIPAddr + " contains " + addressIPAddr + " " + result);
        return result;
    }
}
