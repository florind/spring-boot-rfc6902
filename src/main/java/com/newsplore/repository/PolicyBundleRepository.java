package com.newsplore.repository;

import com.newsplore.domain.PolicyBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PolicyBundleRepository  extends JpaRepository<PolicyBundle, Long> {
}
