package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class VendorController {

    public static final String ROOT_URL = "/api/v1/vendors";
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping(ROOT_URL)
    Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping(ROOT_URL + "/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ROOT_URL)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping(ROOT_URL + "/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping(ROOT_URL + "/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor patchedVendor) {
        Vendor vendor = vendorRepository.findById(id).block();
        assert vendor != null;
        if(!Objects.equals(patchedVendor.getName(), vendor.getName())) {
            vendor.setName(patchedVendor.getName());
            return vendorRepository.save(vendor);
        }
        return Mono.just(vendor);
    }
}
