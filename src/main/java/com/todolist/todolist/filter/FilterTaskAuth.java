package com.todolist.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.todolist.todolist.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class FilterTaskAuth extends OncePerRequestFilter{

	
	
	@Autowired
	private UserRepository userRepository;
	
	
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			
			var servletPath = request.getServletPath();
			if(servletPath.startsWith("/tasks/")) {
				// Autenticação usuário e senha;
				var authorization = request.getHeader("Authorization");
				
				// Decodando o Basic64;
				if (authorization != null && authorization.startsWith("Basic")) {
				var authEncoded = authorization.substring("Basic".length()).trim();
				byte[] authDecode = Base64.getDecoder().decode(authEncoded);
				var authString = new String (authDecode);
				String [] credentials = authString.split(":");
				String username = credentials[0];
				String password = credentials[1];
				
				// Validar usuário;
				var user = this.userRepository.findByUsername(username);
				if (user == null) {
		            response.sendError(401, "Usuário não encontrado");
		        } else {
		            // Verificar a senha usando JBCrypt
		            if (BCrypt.checkpw(password, user.getPassword())) {
		            	
		            	request.setAttribute("idUser", user.getId());
		                filterChain.doFilter(request, response);
		            } else {
		                response.sendError(401, "Senha incorreta");
		            }
		        }
		    } else {
		        response.sendError(401, "Cabeçalho de autorização inválido");
		    }
		} else {
			filterChain.doFilter(request, response);
		}
					
	}

}
