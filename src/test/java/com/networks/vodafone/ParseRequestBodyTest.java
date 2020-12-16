package com.networks.vodafone;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

public class ParseRequestBodyTest {
    
    @Test
    public void parseRequestBodyEmptyBody() throws Exception {
        assertThat(Utils.parseRequestBody(new HashMap<String, Object>())).isEqualTo(new ArrayList<Network>());
    }
    
    @Test
    public void parseRequestBodyNoNetworks() throws Exception {

        String JSON = "{ \"Berlin\": {}, \"Paris\": {}} ";

        Map<String, Object> payload = new ObjectMapper().readValue(JSON, HashMap.class);

        assertThat(Utils.parseRequestBody(payload)).isEqualTo(new ArrayList<Network>());
    }
    
    @Test
    public void parseRequestBodyOK() throws Exception {

        String JSON = "{ \"Berlin\": { \"BER-1\": { \"security_level\": 1, \"networks\": { \"192.168.0.0/24\": [ { \"address\": \"255.255.255.0\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168..0.3\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.288\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"invalid\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.1\", \"available\": false, \"last_used\": \"30/01/20 16:00:00\" }, { \"address\": \"192.168.0.4\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.2\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.3\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.1.1\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" } ], \"10.0.8.0/22\": [ { \"address\": \"10.0.11.254\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"10.0.8.1\", \"available\": false, \"last_used\": \"30/01/20 16:00:00\" }, { \"address\": \"10.0.8.0\", \"available\": false, \"last_used\": \"30/01/20 16:00:00\" }, { \"address\": \"10.0.12.1\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"10.0.10.a\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" } ] } }, \"BER-203\": { \"security_level\": 3, \"networks\": { \"192.168.10.0/24\": [ { \"address\": \"192.168.10.8\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.10.5\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.10.6\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.7\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" } ], \"192.168.11.0/24\": [ { \"address\": \"192.168.11.1\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.2.1\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.11.522\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" } ] } }, \"BER-4000\": { \"security_level\": 3, \"networks\": { \"192.168.100.0/24\": [ { \"address\": \"192.168.100.1\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" } ] } }, \"TEST-1\": { \"security_level\": 3, \"networks\": { \"192.168.200.0/24\": [ { \"address\": \"192.168.200.8\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" } ] } } }, \"Paris\": { \"PAR-1\": { \"security_level\": 5, \"networks\": { \"192.168.203.0/24\": [ { \"address\": \"192.168.203.20\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.203.21\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.203.19\", \"available\": false, \"last_used\": \"30/01/20 17:00:00\" }, { \"address\": \"192.168.0.0\", \"available\": true, \"last_used\": \"30/01/20 17:00:00\" } ] } }, \"XPAR-2\": { \"security_level\": 0, \"networks\": {  } } } } ";

        Map<String, Object> payload = new ObjectMapper().readValue(JSON, HashMap.class);

        List<Network> result = Utils.parseRequestBody(payload);
        assertThat(result.size()).isEqualTo(7);
    }
}
