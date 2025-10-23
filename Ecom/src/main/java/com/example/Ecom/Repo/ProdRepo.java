package com.example.Ecom.Repo;

import com.example.Ecom.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdRepo extends JpaRepository<Product, Long> {
}
