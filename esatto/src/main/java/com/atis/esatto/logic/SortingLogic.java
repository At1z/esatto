package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SortingLogic {


    public List<Product> sortingBy(String sortBy, List<Product> allProducts) {


        switch (sortBy.toLowerCase()) {
            case "date":
                allProducts.sort(Comparator.comparing(Product::getDate));
                break;
            case "basecurrency":
                allProducts.sort(Comparator.comparing(Product::getBaseCurrency));
                break;
            case "targetcurrency":
                allProducts.sort(Comparator.comparing(Product::getTargetCurrency));
                break;
            default:
                allProducts.sort(Comparator.comparing(Product::getDate));
        }

        return allProducts;
    }
}
