package com.networks.vodafone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for the functionality of the server.
 */
@RestController
class NetworksController {

    private final NetworkRepository networkRepository;

    private static final Logger log = LoggerFactory.getLogger(NetworksController.class);

    NetworksController(NetworkRepository networkRepository) {

        this.networkRepository = networkRepository;
    }

    /**
     * API endpoint for receiving network configurations to be saved.
     * 
     * @param body  the request body
     * @return      a JSON formatted HTTP response containing the number of saved networks
     */
    @PostMapping("/networks")
    ResponseEntity<?> insertNetwork(@RequestBody Map<String, Object> body) {

        // call the utils method for parsing the request body
        List<Network> networks = Utils.parseRequestBody(body);
        int numberOfNetworks = networks.size();

        // save all the networks into the database
        for (Network network : networks) {
            try {
                networkRepository.save(network);
            } catch (IllegalArgumentException e) {
                log.error("network is null", e);
                continue;
            }
            log.info("Network: " + network);
        }

        // build the response
        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("number of saved networks", numberOfNetworks);
        return ResponseEntity.ok().body(response);
    }

    /**
     * API endpoint for checking in which networks a given ip address fits into.
     * 
     * @param ip    the given ip address to be checked
     * @return      a list of networks in which the ip address fits
     */
    @GetMapping("/networks")
    ResponseEntity<?> getNetworksByIP(@RequestParam(required = false) String ip) {

        // if the ip is missing from the querystring
        if (ip == null || ip == "") {
            log.warn("The IP missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Bad Request")
                            .withDetail("The IP is missing."));
        }

        // if the ip address is invalid
        try {
            new IPAddressString(ip).toAddress();
        } catch (AddressStringException e) {
            log.warn("The IP " + ip + " is improperly formatted.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Bad Request")
                            .withDetail("The given IP is improperly formatted."));
        }

        List<Network> networks = networkRepository.findAll();
        List<Network> response = new ArrayList<Network>();

        // check if the ip address fits into the networks
        for (Network network : networks) {
            if (Utils.subnetContains(network.getIp(), ip)) {
                response.add(network);
            }
        }

        // if the ip address didn't fit into any network
        if (response.size() == 0) {
            log.info("No networks match the IP " + ip + ".");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("No content")
                            .withDetail("No networks match the given IP."));
        }

        return ResponseEntity.ok().body(response);
    }
}
