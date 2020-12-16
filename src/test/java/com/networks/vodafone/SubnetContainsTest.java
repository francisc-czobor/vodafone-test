package com.networks.vodafone;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

public class SubnetContainsTest {

    @Test
    public void subnetContainsOK() throws Exception {
        assertThat(Utils.subnetContains("192.168.1.0/24", "192.168.1.1")).isTrue();
        assertThat(Utils.subnetContains("192.168.1.0/24", "192.168.2.1")).isFalse();
        assertThat(Utils.subnetContains("1::/64", "1::1")).isTrue();
        assertThat(Utils.subnetContains("1::/64", "2::1")).isFalse();
    }

    @Test
    public void subnetContainsInvalid() throws Exception {
        assertThat(Utils.subnetContains("192.168.1.0/24", "asd")).isFalse();
        assertThat(Utils.subnetContains("asd", "192.168.1.1")).isFalse();
        assertThat(Utils.subnetContains("asd", "asd")).isFalse();
        assertThat(Utils.subnetContains(null, null)).isFalse();
    }
}
