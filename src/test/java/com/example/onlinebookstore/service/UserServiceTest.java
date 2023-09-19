package com.example.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.example.onlinebookstore.dto.user.UserResponseDto;
import com.example.onlinebookstore.enums.RoleName;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.exception.RegistrationException;
import com.example.onlinebookstore.mapper.UserMapper;
import com.example.onlinebookstore.model.Role;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.RoleRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.repository.UserRepository;
import com.example.onlinebookstore.service.impl.UserServiceImpl;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static UserRegistrationRequestDto userDto;
    private static User user;
    private static ShoppingCart shoppingCart;
    private static UserResponseDto userResponseDto;
    private static Role role;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void beforeAll() {
        userDto = new UserRegistrationRequestDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("johndoe@example.com");
        userDto.setPassword("password123");
        userDto.setRepeatPassword("password123");
        userDto.setShippingAddress("123 Main St");
        role = new Role();
        role.setId(1L);
        role.setRoleName(RoleName.ROLE_USER);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        user.setShippingAddress("123 Main St");

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        userResponseDto = new UserResponseDto();
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setShippingAddress("123 Main St");
    }

    @SneakyThrows
    @Test
    public void register_validRequest_returnRegisteredUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(userDto.getPassword());
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toRegisterDto(any(User.class))).thenReturn(userResponseDto);
        when(userMapper.toModel(any(UserRegistrationRequestDto.class))).thenReturn(user);
        when(roleRepository.getRoleByRoleName(RoleName.ROLE_USER)).thenReturn(role);

        assertEquals(userResponseDto, userService.register(userDto));
        verify(userRepository).findByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
        verify(userRepository).save(any(User.class));
        verify(userMapper).toRegisterDto(any(User.class));
        verify(userMapper).toModel(any(UserRegistrationRequestDto.class));
        verify(roleRepository).getRoleByRoleName(RoleName.ROLE_USER);
    }

    @SneakyThrows
    @Test
    public void register_nonValidRequest_returnRegisteredUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        RegistrationException exception = assertThrows(
                RegistrationException.class, () -> userService.register(userDto));
        String expected = "User with email: johndoe@example.com already registered";
        assertEquals(expected, exception.getMessage());
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    public void getAuthenticatedUser_invalidRequest_throwsException() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getAuthenticatedUser());
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    public void getAuthenticatedUser_validRequest_returnUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertEquals(user, userService.getAuthenticatedUser());
        verify(userRepository).findByEmail(anyString());
    }
}
