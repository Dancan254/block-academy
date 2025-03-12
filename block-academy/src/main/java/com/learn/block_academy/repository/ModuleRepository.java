package com.learn.block_academy.repository;

import com.learn.block_academy.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}