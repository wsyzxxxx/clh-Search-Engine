package com.searcher.searcher.Service;

import com.searcher.searcher.Dao.User_tag_repo;
import com.searcher.searcher.Domain.User_tags;
import com.searcher.searcher.Domain.user_tag_id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {
    @Autowired
    User_tag_repo user_tag_repo;

    public void add_tag(String username,String tag)
    {
        user_tag_id id=new user_tag_id();
        id.setTag(tag);
        id.setUsername(username);
        User_tags user_tags=new User_tags();
        user_tags.setUser(username);
        user_tags.setUser_tag(id);
        user_tag_repo.save(user_tags);
    }

    public Set<String> getTags(String username)
    {
        List<User_tags> results=user_tag_repo.findAllByUser(username);
        HashSet<String> tags=new HashSet<>();
        for(User_tags i:results)
        {
            tags.add(i.getUser_tag().getTag());
        }
        return tags;
    }
}
