package com.networks.vodafone;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Standard repository for Network objects.
 */
interface NetworkRepository extends JpaRepository<Network, Long> {

}
