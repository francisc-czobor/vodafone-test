package com.networks.vodafone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    ResponseEntity<?> insertNetwork(@RequestBody(required = false) Map<String, Object> body) {

        // if the body is empty
        if (body == null || body.isEmpty()) {
            log.warn("The body is empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Bad Request")
                            .withDetail("The body is empty."));
        }

        // call the utils method for parsing the request body
        List<Network> networks = Utils.parseRequestBody(body);
        int numberOfNetworks = networks.size();

        // save all the networks into the database
        for (Network network : networks) {
            try {
                networkRepository.save(network);
            } catch (IllegalArgumentException | DataIntegrityViolationException e) {
                log.warn("network is null or already exists");
                numberOfNetworks--;
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
        
        List<Network> response = new ArrayList<Network>();

        // paging the networks from the db to not stress it too much
        Pageable page = PageRequest.of(0, 2);
        Page<Network> networks = networkRepository.findAll(page);

        // check if the ip address fits into the first page of networks
        for (Network network : networks.toList()) {
            if (Utils.subnetContains(network.getIp(), ip)) {
                response.add(network);
            }
        }
        // the rest of the pages
        if (networks.getTotalPages() > 1) {
            do {
                page = networks.nextPageable();
                networks = networkRepository.findAll(page);
    
                // check if the ip address fits into the current page of networks
                for (Network network : networks.toList()) {
                    if (Utils.subnetContains(network.getIp(), ip)) {
                        response.add(network);
                    }
                }
            } while (networks.hasNext());
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
