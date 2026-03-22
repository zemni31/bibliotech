package com.bibliotech.web.controller;

import com.bibliotech.data.entity.Book;
import com.bibliotech.data.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postBooks_shouldCreateBook_whenPayloadValid() throws Exception {
        String json = """
                {
                  "isbn":"9780132350884",
                  "title":"Clean Code",
                  "stockDisponible":5
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.title").value("Clean Code"));

        assertTrue(bookRepository.findByIsbn("9780132350884").isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postBooks_shouldReturnBadRequest_whenIsbnAlreadyExists() throws Exception {
        bookRepository.save(Book.builder()
                .isbn("9780132350884")
                .title("Existing Book")
                .stockDisponible(2)
                .build());

        String json = """
                {
                  "isbn":"9780132350884",
                  "title":"Another Book",
                  "stockDisponible":3
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("ISBN deja existant")));
    }
}
