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

/**
 * Class for different utils methods.
 */
public class Utils {

    // logger configured for the NetworksController class
    private static final Logger log = LoggerFactory.getLogger(NetworksController.class);
    

    /**
     * Method for parsing the request body received on /networks.
     * 
     * It creates the Network and IPAddr objects defined within the request body and returns the
     * list of Network objects.
     * 
     * @param body  the request body received on /networks
     * @return      the list of Network objects defined by the request body
     * @see         Network
     * @see         IPAddr
     * @see         NetworksController
     */
    public static List<Network> parseRequestBody(Map<String, Object> body) {

        // the Network objects list to be returned
        List<Network> list = new ArrayList<Network>();

        // iterating through the locations in the body
        for (String location : body.keySet()) {
            Map<String, Object> mans = (Map<String, Object>) body.get(location);
            // iterating through the metropolitan area networks in the location
            for (String man : mans.keySet()) {
                Map<String, Object> manInfo = (Map<String, Object>) mans.get(man);
                Map<String, Object> networks;
                // if the man is missing the networks attribute
                try {
                    networks = (Map<String, Object>) manInfo.get("networks");
                } catch (NullPointerException e) {
                    log.warn(man + " has no networks");
                    continue;
                }
                // iterating through the networks in the man
                for (String network : networks.keySet()) {
                    // if the network address is invalid
                    try {
                        new IPAddressString(network).toAddress();
                    } catch (AddressStringException e) {
                        log.warn(network + " is invalid");
                        continue;
                    }
                    int securityLevel = 0;
                    // if the security level attribute is missing
                    try {
                        securityLevel = (int) manInfo.get("security_level");
                    } catch (NullPointerException e) {
                        log.warn(man + " has no security level, defaults to 0");
                    }
                    // create a new Network object
                    Network newNetwork = new Network(network, location, man, securityLevel);
                    List<Map<String, Object>> addresses = (List<Map<String, Object>>)networks.get(network);
                    Set<IPAddr> newAddresses = new HashSet<IPAddr>();
                    // iterating through the addresses in the network
                    for (Map<String, Object> address : addresses) {
                        try {
                            String addressIP = (String) address.get("address");
                            // if the address is invalid
                            try {
                                new IPAddressString(addressIP).toAddress();
                            } catch (AddressStringException e) {
                                log.warn(addressIP + " is invalid");
                                continue;
                            }
                            // if the address is indeed part of the network then create the IPAddr object
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
                    // add the addresses on the network object
                    newNetwork.setAdresses(newAddresses);
                    // add the network object in the final result
                    list.add(newNetwork);
                }
            }
        }

        return list;
    }

    /**
     * Checks if a given ip address fits into a given network.
     * 
     * @param network   the given network address
     * @param address   the given ip address to be checked
     * @return          true if the ip address fits into the network, false otherwise
     */
    public static boolean subnetContains(String network, String address) {
        IPAddressString networkString = new IPAddressString(network);
        IPAddress networkIPAddr = networkString.getAddress().toPrefixBlock();
        IPAddress addressIPAddr = new IPAddressString(address).getAddress();

        boolean result = networkIPAddr.contains(addressIPAddr);

        log.info(networkString + " block " + networkIPAddr + " contains " + addressIPAddr + " " + result);
        return result;
    }
}
