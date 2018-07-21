package com.searcher.searcher.Service;

import com.searcher.searcher.Dao.SysUserRepository;
import com.searcher.searcher.Domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    SysUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        System.out.println("s:"+s);
        System.out.println("username:"+user.getUsername()+";password:"+user.getPassword());
        return user;
    }
    public  boolean existUser(String name)
    {
        if(userRepository.findByUsername(name)!=null)
        {
            return true;
        }
        else
            return false;
    }
    public void addUser(SysUser user)
    {
        userRepository.save(user);
    }
}