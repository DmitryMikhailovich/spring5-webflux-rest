package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    @Test
    void create() {
        when(categoryRepository.saveAll(any(Publisher.class))).thenReturn(Flux.just(new Category()));

        Category category = new Category();
        category.setDescription("Some cat");
        Mono<Category> categoryMono = Mono.just(category);

        webTestClient.post().uri(CategoryController.ROOT_URL)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    void update() {
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(new Category()));

        Category category = new Category();
        category.setDescription("New desc");

        Mono<Category> categoryMono = Mono.just(category);
        webTestClient.put().uri(CategoryController.ROOT_URL + "/ID")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchWithChanges() {
        Category existCategory = new Category();
        existCategory.setDescription("Old desc");
        when(categoryRepository.findById(any(String.class))).thenReturn(Mono.just(existCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(new Category()));

        Category category = new Category();
        category.setDescription("New desc");

        Mono<Category> categoryMono = Mono.just(category);
        webTestClient.patch().uri(CategoryController.ROOT_URL + "/ID")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).findById(any(String.class));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void patchWithoutChanges() {
        Category existCategory = new Category();
        existCategory.setDescription("Old desc");
        when(categoryRepository.findById(any(String.class))).thenReturn(Mono.just(existCategory));

        Mono<Category> categoryMono = Mono.just(existCategory);
        webTestClient.patch().uri(CategoryController.ROOT_URL + "/ID")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository).findById(any(String.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }
}