package com.wave.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto
{
    @Size(min = 5, max = 40, message = "Имя пользователя должно содержать от 5 до 40 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;
    @Size(max = 255, message = "Пароль должен содержать до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}