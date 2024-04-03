package com.matthewlim.ecommercewebapp.unit.controllers;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matthewlim.ecommercewebapp.controllers.UserController;
import com.matthewlim.ecommercewebapp.enums.Role;
import com.matthewlim.ecommercewebapp.models.User;
import com.matthewlim.ecommercewebapp.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@MockBean
	private UserService userService;	
	
	private User testUser;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
		
		testUser = new User();
		testUser.setUserId(1L);
		testUser.setUsername("user");
	}
	
	@Test
	@WithMockUser
	public void testGetUsers() throws Exception {
		List<User> userList = new ArrayList<User>();
		when(userService.findAllUsers()).thenReturn(userList);
		
		mockMvc.perform(get("/api/v1/users"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))))
			.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@WithMockUser
	public void testGetUser() throws Exception {
		String username = testUser.getUsername();
		String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidXNlciIsImV4cCI6MTcxMjE0MTE2MSwiaWF0IjoxNzEyMTM3NTYxLCJzY29wZSI6IlVTRVIifQ.EeDl1zXuo01GMpfLgB3JXOT0SruoiVlTm8cLqL6obnoqMBLEMJp2FenpPvGWoHhUTOx_AvIdWam8bzGe26aUlBlsbUd67jFL6I1QPH90isOHivzhRPVm6pqFjI4urUkIYVfG0PrE7-UEvsbzIAPrWFF91OslRrTvg78yWrBjrxaagVyoT9L6qIDjGxgwaEZqxSBU2xkqpiYOgq17eKluiy2WZY9l3ewXk6FJLQSjsPClSPc0EDg7n1f8nfRIPtrf54vfiHYKSBmkVwdc7N6pjwIt5ulfH8s4GEi77wtYWDDZspY-EhH07tlYfU2CichSBc3OIyrUecF2nEkjVHs9rQ";
		when(userService.findByUsername(username)).thenReturn(testUser);
		
	    mockMvc.perform(get("/api/v1/user")
	    	.header("Authorization", "Bearer " + jwtToken))
	    	.andDo(print())
	    	.andExpect(status().isOk())
	    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	    	.andExpect(jsonPath("$.username", is(username)))
	    	.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCreateUser() throws Exception {
		when(userService.addUser(testUser)).thenReturn(testUser);
		
		mockMvc.perform(post("/api/v1/users")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(testUser)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.userId", is(testUser.getUserId().intValue())))
			.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	@WithMockUser
	public void testUpdateUser() throws Exception {
        Long userId = testUser.getUserId();
        User updatedUser = new User("johnwick", "yeahhh", "johnwick@gmail.com", "John", "Wick", Role.USER);
        when(userService.updateUser(userId, testUser)).thenReturn(updatedUser);
        
        mockMvc.perform(put("/api/v1/user/{userId}", userId)
        	.with(csrf())
        	.contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testPartialUpdateUser() throws Exception {
        Long userId = testUser.getUserId();
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("username", "keanureeves");
        fieldsToUpdate.put("email", "keanureeves@gmail.com");

        mockMvc.perform(patch("/api/v1/user/{userId}", userId)
        	.with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fieldsToUpdate)))
            .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testDeleteUser() throws Exception {
		Long userId = testUser.getUserId();
		
		mockMvc.perform(delete("/api/v1/user/{userId}", userId)
			.with(csrf()))
			.andExpect(status().isOk());
	}
}