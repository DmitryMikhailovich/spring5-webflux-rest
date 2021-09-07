package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    WebTestClient webTestClient;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        Category category1 = new Category();
        category1.setId("id1");
        category1.setDescription("Cat1");
        Category category2 = new Category();
        category1.setId("id2");
        category2.setDescription("Cat2");
        when(categoryRepository.findAll()).thenReturn(Flux.just(category1, category2));

        webTestClient.get().uri(CategoryController.ROOT_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        Category category = new Category();
        category.setId("id1");
        category.setDescription("Cat1");

        when(categoryRepository.findById(any(String.class))).thenReturn(Mono.just(category));

        webTestClient.get().uri(CategoryController.ROOT_URL + "/" + category.getId())
                .exchange()
                .expectBody(Category.class);
    }
}