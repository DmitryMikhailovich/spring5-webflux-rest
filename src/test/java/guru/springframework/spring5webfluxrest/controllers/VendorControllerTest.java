package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
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
class VendorControllerTest {

    @Mock
    VendorRepository vendorRepository;

    @InjectMocks
    VendorController vendorController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        Vendor vendor1 = new Vendor();
        vendor1.setId("id1");
        vendor1.setName("Vendor 1");

        Vendor vendor2 = new Vendor();
        vendor2.setId("id2");
        vendor2.setName("Vendor 2");

        when(vendorRepository.findAll()).thenReturn(Flux.just(vendor1, vendor1));

        webTestClient.get().uri(VendorController.ROOT_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);

    }

    @Test
    void getById() {
        Vendor vendor = new Vendor();
        vendor.setId("id");
        vendor.setName("ACME");

        when(vendorRepository.findById(any(String.class))).thenReturn(Mono.just(vendor));

        webTestClient.get().uri(VendorController.ROOT_URL + "/" + vendor.getId())
                .exchange()
                .expectBody(Vendor.class);
    }
}