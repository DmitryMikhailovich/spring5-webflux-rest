package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Vendor acme = new Vendor();
        acme.setId(UUID.randomUUID().toString());
        acme.setName("ACME Corporation");

        Vendor magnit = new Vendor();
        magnit.setId(UUID.randomUUID().toString());
        magnit.setName("Magnit");

        vendorRepository.saveAll(Arrays.asList(acme, magnit)).blockLast();

        System.out.println("Total vendors: " + vendorRepository.count().block());

        Category toys = new Category();
        toys.setId(UUID.randomUUID().toString());
        toys.setDescription("Toys");

        Category food = new Category();
        food.setId(UUID.randomUUID().toString());
        food.setDescription("Food");

        Category weapons = new Category();
        weapons.setId(UUID.randomUUID().toString());
        weapons.setDescription("Weapons");

        categoryRepository.saveAll(Arrays.asList(toys, food, weapons)).blockLast();

        System.out.println("Total categories: " + categoryRepository.count().block());
    }
}
