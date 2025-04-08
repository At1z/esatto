// CurrencyService.js
const API_BASE_URL = "http://localhost:8080";

// Product endpoints
export const productService = {
  // Add a new product
  addProduct: async (product) => {
    try {
      const response = await fetch(`${API_BASE_URL}/products`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
      return await response.json();
    } catch (error) {
      console.error("Error adding product:", error);
      throw error;
    }
  },

  // Delete a product by ID
  deleteProduct: async (id) => {
    try {
      await fetch(`${API_BASE_URL}/products/${id}`, {
        method: "DELETE",
      });
      return true;
    } catch (error) {
      console.error("Error deleting product:", error);
      throw error;
    }
  },

  // Update a product
  updateProduct: async (id, product) => {
    try {
      const response = await fetch(`${API_BASE_URL}/products/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(product),
      });
      return await response.json();
    } catch (error) {
      console.error("Error updating product:", error);
      throw error;
    }
  },

  // Get all products
  getAllProducts: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/products`);
      return await response.json();
    } catch (error) {
      console.error("Error fetching all products:", error);
      throw error;
    }
  },

  // Get product by ID
  getProductById: async (id) => {
    try {
      const response = await fetch(`${API_BASE_URL}/products/${id}`);
      return await response.json();
    } catch (error) {
      console.error("Error fetching product by ID:", error);
      throw error;
    }
  },

  // Sort products
  getSortedProducts: async (sortBy = "date") => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/products/sorted?sortBy=${sortBy}`
      );
      return await response.json();
    } catch (error) {
      console.error("Error fetching sorted products:", error);
      throw error;
    }
  },

  // Search products
  searchProducts: async (params) => {
    try {
      const queryParams = new URLSearchParams();
      if (params.baseCurrency)
        queryParams.append("baseCurrency", params.baseCurrency);
      if (params.targetCurrency)
        queryParams.append("targetCurrency", params.targetCurrency);
      if (params.maxCost) queryParams.append("maxCost", params.maxCost);

      const response = await fetch(
        `${API_BASE_URL}/products/search?${queryParams}`
      );
      return await response.json();
    } catch (error) {
      console.error("Error searching products:", error);
      throw error;
    }
  },

  // Get paged products
  getPagedProducts: async (
    page = 0,
    size = 5,
    baseCurrency,
    targetCurrency
  ) => {
    try {
      const queryParams = new URLSearchParams();
      queryParams.append("page", page);
      queryParams.append("size", size);
      if (baseCurrency) queryParams.append("baseCurrency", baseCurrency);
      if (targetCurrency) queryParams.append("targetCurrency", targetCurrency);

      const response = await fetch(
        `${API_BASE_URL}/products/paged?${queryParams}`
      );
      return await response.json();
    } catch (error) {
      console.error("Error fetching paged products:", error);
      throw error;
    }
  },
};

// Currency exchange endpoint
export const currencyService = {
  getExchangeRate: async (base, target) => {
    try {
      const formData = new URLSearchParams();
      formData.append("base", base);
      formData.append("target", target);

      const response = await fetch(`${API_BASE_URL}/currency/exchange-rates`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData,
      });
      return await response.json();
    } catch (error) {
      console.error("Error fetching exchange rate:", error);
      throw error;
    }
  },
};
