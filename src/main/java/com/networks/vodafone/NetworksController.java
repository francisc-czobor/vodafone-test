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

@RestController
class NetworksController {

    private final NetworkRepository networkRepository;

    private static final Logger log = LoggerFactory.getLogger(NetworksController.class);

    NetworksController(NetworkRepository networkRepository) {

        this.networkRepository = networkRepository;
    }

    @PostMapping("/networks")
    ResponseEntity<?> insertNetwork(@RequestBody Map<String, Object> body) {

        List<Network> networks = Utils.parseRequestBody(body);
        int numberOfNetworks = networks.size();

        for (Network network : networks) {
            try {
                networkRepository.save(network);
            } catch (IllegalArgumentException e) {
                log.error("network is null", e);
                continue;
            }
            log.info("Network: " + network);
        }

        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("number of saved networks", numberOfNetworks);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/networks")
    ResponseEntity<?> insertNetwork(@RequestParam String ip) {

        try {
            new IPAddressString(ip).toAddress();
        } catch (AddressStringException e) {
            log.warn("The IP" + ip + " is improperly formatted.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Bad Request")
                            .withDetail("The given IP is improperly formatted."));
        }

        List<Network> networks = networkRepository.findAll();
        List<Network> response = new ArrayList<Network>();

        for (Network network : networks) {
            if (Utils.subnetContains(network.getIp(), ip)) {
                response.add(network);
            }
        }

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
