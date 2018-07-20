package com.searcher.searcher.Dao;

import com.searcher.searcher.Domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    public SysUser findByUsername(String username);


}