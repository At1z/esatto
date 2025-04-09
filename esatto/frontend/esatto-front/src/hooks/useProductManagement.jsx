import { useEffect } from "react";
import { productService } from "../services/productService";
import useProductState from "./useProductState";
import useProductOperations from "./useProductOperations";
import useProductForm from "./useProductForm";

function useProductManagement() {
  const {
    formData,
    setFormData,
    products,
    setProducts,
    currentProduct,
    setCurrentProduct,
    paginationInfo,
    setPaginationInfo,
    displayMode,
    setDisplayMode,
    loading,
    setLoading,
    disabledFields,
    setDisabledFields,
    activeOperation,
    setActiveOperation,
    initialFormState,
    resetFieldsState,
    clearForm,
  } = useProductState();

  const {
    fetchAllProducts,
    executeGetById,
    executeSort,
    executeSearch,
    executePaging,
    handleExternalFetch,
  } = useProductOperations({
    formData,
    setProducts,
    setCurrentProduct,
    setDisplayMode,
    setPaginationInfo,
    setLoading,
    resetFieldsState,
  });

  const {
    handleAdd,
    handleDelete,
    handleUpdate,
    handleGetById,
    handleSort,
    handleSearch,
    handlePage,
    handleExternalSource,
  } = useProductForm({
    setDisabledFields,
    setActiveOperation,
    setFormData,
    initialFormState,
    formData,
    executeSearch,
    executePaging,
    executeGetById,
  });

  useEffect(() => {
    fetchAllProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

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

        case "external":
          await handleExternalFetch();
          fetchAllProducts();
          return;
      }

      fetchAllProducts();
      clearForm();
    } catch (error) {
      console.error("Error submitting form:", error);
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
