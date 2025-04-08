import { useState, useEffect } from "react";
import { productService } from "../services/productService";

function useProductManagement() {
  const initialFormState = {
    id: "",
    baseCurrency: "",
    targetCurrency: "",
    cost: "",
    page: "0",
    size: "",
  };

  const [formData, setFormData] = useState(initialFormState);
  const [products, setProducts] = useState([]);
  const [currentProduct, setCurrentProduct] = useState(null);
  const [paginationInfo, setPaginationInfo] = useState(null);
  const [displayMode, setDisplayMode] = useState("list");
  const [loading, setLoading] = useState(true);

  // Initialize by fetching all products
  useEffect(() => {
    fetchAllProducts();
  }, []);

  const fetchAllProducts = async () => {
    setLoading(true);
    try {
      const data = await productService.getAllProducts();
      setProducts(data);
      setDisplayMode("list");
      setPaginationInfo(null);
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (formData.id) {
        await productService.updateProduct(formData.id, {
          baseCurrency: formData.baseCurrency,
          targetCurrency: formData.targetCurrency,
          cost: parseFloat(formData.cost),
        });
      } else {
        await productService.addProduct({
          baseCurrency: formData.baseCurrency,
          targetCurrency: formData.targetCurrency,
          cost: parseFloat(formData.cost),
        });
      }
      fetchAllProducts();
      clearForm();
    } catch (error) {
      console.error("Error submitting form:", error);
    }
  };

  const clearForm = () => {
    setFormData(initialFormState);
    setCurrentProduct(null);
  };

  const handleAdd = () => clearForm();

  const handleDelete = async () => {
    if (!formData.id) {
      alert("Please enter an ID to delete");
      return;
    }

    try {
      await productService.deleteProduct(formData.id);
      fetchAllProducts();
      clearForm();
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };

  const handleUpdate = () => {
    if (!formData.id || !currentProduct) {
      alert("Please get a product by ID first");
      return;
    }

    // Simulate clicking the submit button
    document.querySelector("form button[type='submit']").click();
  };

  const handleGetById = async () => {
    if (!formData.id) {
      alert("Please enter an ID");
      return;
    }

    setLoading(true);
    try {
      const product = await productService.getProductById(formData.id);
      if (product) {
        setCurrentProduct(product);
        setFormData({
          id: product.id.toString(),
          baseCurrency: product.baseCurrency,
          targetCurrency: product.targetCurrency,
          cost: product.cost.toString(),
          page: formData.page,
          size: formData.size,
        });
        setDisplayMode("detail");
      }
    } catch (error) {
      console.error("Error fetching product:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSort = async () => {
    const sortField = prompt(
      "Sort by (date, baseCurrency, targetCurrency):",
      "date"
    );

    if (!sortField) return;

    setLoading(true);
    try {
      const sortedProducts = await productService.getSortedProducts(sortField);
      setProducts(sortedProducts);
      setDisplayMode("list");
    } catch (error) {
      console.error("Error sorting products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    setLoading(true);
    try {
      const searchParams = {
        baseCurrency: formData.baseCurrency || null,
        targetCurrency: formData.targetCurrency || null,
        maxCost: formData.cost ? parseFloat(formData.cost) : null,
      };

      const filteredProducts = await productService.searchProducts(
        searchParams
      );
      setProducts(filteredProducts);
      setDisplayMode("list");
    } catch (error) {
      console.error("Error searching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handlePage = async () => {
    setLoading(true);
    try {
      const page = parseInt(formData.page) || 0;
      const size = parseInt(formData.size) || 5;
      const pagedData = await productService.getPagedProducts(
        page,
        size,
        formData.baseCurrency || null,
        formData.targetCurrency || null
      );

      if (pagedData && pagedData.content) {
        setProducts(pagedData.content);
        setPaginationInfo(pagedData);
        setDisplayMode("list");
      }
    } catch (error) {
      console.error("Error fetching paged products:", error);
    } finally {
      setLoading(false);
    }
  };

  return {
    formData,
    setFormData,
    products,
    currentProduct,
    paginationInfo,
    displayMode,
    loading,
    handleSubmit,
    handleAdd,
    handleDelete,
    handleUpdate,
    handleGetById,
    handleSort,
    handleSearch,
    handlePage,
    fetchAllProducts,
  };
}

export default useProductManagement;
