package com.example.todolist.service;

import com.example.todolist.repository.ToDoListUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoListUserDetailsService implements UserDetailsService {
    private final ToDoListUserRepository toDoListUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(toDoListUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("To-Do-List user not found"));
    }
}
