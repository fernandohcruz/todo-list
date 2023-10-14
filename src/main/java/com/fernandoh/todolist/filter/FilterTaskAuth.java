package com.fernandoh.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import com.fernandoh.todolist.model.UserModel;
import com.fernandoh.todolist.repository.UserModelRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    private final UserModelRepository userModelRepository;

    public FilterTaskAuth(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();
        if (!servletPath.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization").substring("Basic".length()).trim();
        String[] credentials = new String(Base64.getDecoder().decode(authorization)).split(":");
        String username = credentials[0];
        String password = credentials[1];

        Optional<UserModel> userModel = userModelRepository.findByUsername(username);
        if (userModel.isEmpty()) {
            response.sendError(401, "Usuário sem autorização");
            return;
        }

        Result result = BCrypt.verifyer().verify(password.toCharArray(), userModel.get().getPassword());
        if (!result.verified) {
            response.sendError(401, "Usuário sem autorização");
            return;
        }

        request.setAttribute("userId", userModel.get().getId());
        filterChain.doFilter(request, response);
    }
}
