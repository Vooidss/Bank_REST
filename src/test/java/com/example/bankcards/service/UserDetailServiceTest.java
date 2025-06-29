package com.example.bankcards.service;

import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.security.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailService userDetailService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of(Role.ADMIN));
    }

    @Test
    void loadUserByUsername_Success() {
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        UserDetails details = userDetailService.loadUserByUsername("testuser");
        assertThat(details).isInstanceOf(MyUserDetails.class);
        assertThat(details.getUsername()).isEqualTo("testuser");
        assertThat(details.getPassword()).isEqualTo("password");
        assertThat(details.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Test
    void loadUserByUsername_NotFound() {
        when(userService.getUserByUsername("unknown")).thenThrow(new UsernameNotFoundException("not found"));
        assertThatThrownBy(() -> userDetailService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("not found");
    }
}
