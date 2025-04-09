import { useState, useEffect } from "react";
import { productService } from "../services/productService";

function useProductManagement() {
  const initialFormState = {
    id: "",
    baseCurrency: "",
    targetCurrency: "",
    cost: "",
    page: "0",
    size: "5",
  };

  const [formData, setFormData] = useState(initialFormState);
  const [products, setProducts] = useState([]);
  const [currentProduct, setCurrentProduct] = useState(null);
  const [paginationInfo, setPaginationInfo] = useState(null);
  const [displayMode, setDisplayMode] = useState("list");
  const [loading, setLoading] = useState(true);
  const [disabledFields, setDisabledFields] = useState({
    id: false,
    baseCurrency: false,
    targetCurrency: false,
    cost: false,
    page: false,
    size: false,
  });
  const [activeOperation, setActiveOperation] = useState(null);

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
      resetFieldsState();
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const resetFieldsState = () => {
    setDisabledFields({
      id: false,
      baseCurrency: false,
      targetCurrency: false,
      cost: false,
      page: false,
      size: false,
    });
    setActiveOperation(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!activeOperation) {
      alert("Please select an operation first (Add, Delete, Update, etc.)");
      return;
    }

    try {
      switch (activeOperation) {
        case "add":
          await productService.addProduct({
            baseCurrency: formData.baseCurrency,
            targetCurrency: formData.targetCurrency,
            cost: parseFloat(formData.cost),
          });
          break;

        case "update":
          if (!formData.id) {
            alert("ID is required for update operation");
            return;
          }
          await productService.updateProduct(formData.id, {
            baseCurrency: formData.baseCurrency,
            targetCurrency: formData.targetCurrency,
            cost: parseFloat(formData.cost),
          });
          break;

        case "delete":
          if (!formData.id) {
            alert("ID is required for delete operation");
            return;
          }
          await productService.deleteProduct(formData.id);
          break;

        case "getById":
          handleGetById();
          return;

        case "search":
          executeSearch();
          return;

        case "page":
          executePaging();
          return;
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
    resetFieldsState();
  };

  const handleAdd = () => {
    setDisabledFields({
      id: true,
      baseCurrency: false,
      targetCurrency: false,
      cost: false,
      page: true,
      size: true,
    });
    setActiveOperation("add");
    setFormData({
      ...initialFormState,
      id: "",
    });
  };

  const handleDelete = () => {
    setDisabledFields({
      id: false,
      baseCurrency: true,
      targetCurrency: true,
      cost: true,
      page: true,
      size: true,
    });
    setActiveOperation("delete");
    setFormData({
      ...initialFormState,
      baseCurrency: "",
      targetCurrency: "",
      cost: "",
    });
  };

  const handleUpdate = () => {
    setDisabledFields({
      id: false,
      baseCurrency: false,
      targetCurrency: false,
      cost: false,
      page: true,
      size: true,
    });
    setActiveOperation("update");
  };

  const handleGetById = () => {
    setDisabledFields({
      id: false,
      baseCurrency: true,
      targetCurrency: true,
      cost: true,
      page: true,
      size: true,
    });
    setActiveOperation("getById");

    if (!formData.id) {
      alert("Please enter an ID");
      return;
    }

    executeGetById();
  };

  const executeGetById = async () => {
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
    resetFieldsState();
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

  const handleSearch = () => {
    setDisabledFields({
      id: true,
      baseCurrency: false,
      targetCurrency: false,
      cost: false,
      page: true,
      size: true,
    });
    setActiveOperation("search");

    // Wykonaj wyszukiwanie od razu, jeśli pola są wypełnione
    if (formData.baseCurrency || formData.targetCurrency || formData.cost) {
      executeSearch();
    }
  };

  const executeSearch = async () => {
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

  const handlePage = () => {
    setDisabledFields({
      id: true,
      baseCurrency: false,
      targetCurrency: false,
      cost: true,
      page: false,
      size: false,
    });
    setActiveOperation("page");

    // Wykonaj paginację od razu z domyślnymi wartościami
    executePaging();
  };

  const executePaging = async () => {
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
    disabledFields,
    activeOperation,
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
