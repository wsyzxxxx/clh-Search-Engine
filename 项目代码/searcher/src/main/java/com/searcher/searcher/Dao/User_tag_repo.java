package com.searcher.searcher.Dao;

import com.searcher.searcher.Domain.SysUser;
import com.searcher.searcher.Domain.User_tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface User_tag_repo extends JpaRepository<User_tags, Long> {

    public List<User_tags> findAllByUser(String user);
}
