package com.example.Ecom.Service;

import com.example.Ecom.Model.Product;
import com.example.Ecom.Repo.ProdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProdRepo prodRepository;

    public List<Product> getAllProduct(){
        return prodRepository.findAll();
    }

    public Product getProductById(Long id){
        return prodRepository.findById(id).orElse(null);
    }

    public Product addProduct(Product product){
        return prodRepository.save(product);
    }

    public void deleteProd(Long id){
        prodRepository.deleteById(id);
    }



}
