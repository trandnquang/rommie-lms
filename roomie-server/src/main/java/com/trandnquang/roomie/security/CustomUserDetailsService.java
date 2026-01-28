package com.trandnquang.roomie.security;

import com.trandnquang.roomie.entity.User;
import com.trandnquang.roomie.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // MAPPING QUAN TRỌNG:
        // Chuyển Enum Role của ta thành Authority của Spring Security.
        // Spring Security mặc định cần prefix "ROLE_" nếu dùng hasRole().
        // Enum của ta là ROLE_ADMIN -> Khớp chuẩn.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(authority) // Đã thêm quyền vào đây
        );
    }
}