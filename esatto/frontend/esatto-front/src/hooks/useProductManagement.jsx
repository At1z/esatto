import { useState, useEffect } from "react";
import { productService, currencyService } from "../services/productService";

function useProductManagement() {
  const initialFormState = {
    id: "",
    baseCurrency: "",
    targetCurrency: "",
    cost: "",
    page: "0",
    size: "5",
    sortBy: "date",
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
    sortBy: false,
  });
  const [activeOperation, setActiveOperation] = useState(null);

  useEffect(() => {
    fetchAllProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      sortBy: false,
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
          executeGetById();
          return;

        case "search":
          executeSearch();
          return;

        case "page":
          executePaging();
          return;

        case "sort":
          executeSort();
          return;

        case "external": {
          const exchangeData = await currencyService.getExchangeRate({
            baseCurrency: formData.baseCurrency,
            targetCurrency: formData.targetCurrency,
          });

          if (exchangeData) {
            const formattedData = Array.isArray(exchangeData)
              ? exchangeData
              : [exchangeData].map((item) => ({
                  id: item.id || "external-" + Date.now(),
                  baseCurrency: item.baseCurrency || formData.baseCurrency,
                  targetCurrency:
                    item.targetCurrency || formData.targetCurrency,
                  cost: item.rate || item.cost || 0,
                  date: item.date || new Date().toISOString().split("T")[0],
                }));

            setProducts(formattedData);
            setDisplayMode("list");
          }
        }
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
      sortBy: true,
    });
    setActiveOperation("add");
    setFormData({
      ...initialFormState,
      id: "",
    });
  };

  const handleExternalSource = () => {
    setDisabledFields({
      id: true,
      baseCurrency: false,
      targetCurrency: false,
      cost: true,
      page: true,
      size: true,
      sortBy: true,
    });
    setActiveOperation("external");
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
      sortBy: true,
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
      sortBy: true,
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
      sortBy: true,
    });
    setActiveOperation("getById");

    setFormData({
      ...initialFormState,
      id: formData.id || "",
    });
  };

  const executeGetById = async () => {
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
          sortBy: formData.sortBy,
        });
        setDisplayMode("detail");
      }
    } catch (error) {
      console.error("Error fetching product:", error);
      alert("Error: Could not find product with ID " + formData.id);
    } finally {
      setLoading(false);
    }
  };

  const handleSort = () => {
    setDisabledFields({
      id: true,
      baseCurrency: true,
      targetCurrency: true,
      cost: true,
      page: true,
      size: true,
      sortBy: false,
    });
    setActiveOperation("sort");

    setFormData({
      ...initialFormState,
      sortBy: formData.sortBy || "date",
    });
  };

  const executeSort = async () => {
    setLoading(true);
    try {
      const sortField = formData.sortBy || "date";
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
      sortBy: true,
    });
    setActiveOperation("search");

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
      sortBy: true,
    });
    setActiveOperation("page");

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
    handleExternalSource,
  };
}

export default useProductManagement;
