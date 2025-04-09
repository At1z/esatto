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
        case "add": {
          const currencyRegex = /^[A-Z]{3}$/;
          if (!currencyRegex.test(formData.baseCurrency)) {
            alert("Base Currency must be 3 uppercase letters (e.g., USD, EUR)");
            return;
          }
          if (!currencyRegex.test(formData.targetCurrency)) {
            alert(
              "Target Currency must be 3 uppercase letters (e.g., USD, EUR)"
            );
            return;
          }
          if (formData.baseCurrency === formData.targetCurrency) {
            alert("Base Currency and Target Currency cannot be the same");
            return;
          }
          if (!formData.cost || isNaN(parseFloat(formData.cost))) {
            alert("Cost must be a valid number");
            return;
          }
          await productService.addProduct({
            baseCurrency: formData.baseCurrency,
            targetCurrency: formData.targetCurrency,
            cost: parseFloat(formData.cost),
          });
          break;
        }

        case "update": {
          if (
            !formData.id ||
            !/^\d+$/.test(formData.id) ||
            parseInt(formData.id) <= 0
          ) {
            alert("ID must be a positive number");
            return;
          }
          const currencyRegex = /^[A-Z]{3}$/;
          if (!currencyRegex.test(formData.baseCurrency)) {
            alert("Base Currency must be 3 uppercase letters (e.g., USD, EUR)");
            return;
          }
          if (!currencyRegex.test(formData.targetCurrency)) {
            alert(
              "Target Currency must be 3 uppercase letters (e.g., USD, EUR)"
            );
            return;
          }
          if (formData.baseCurrency === formData.targetCurrency) {
            alert("Base Currency and Target Currency cannot be the same");
            return;
          }
          if (!formData.cost || isNaN(parseFloat(formData.cost))) {
            alert("Cost must be a valid number");
            return;
          }
          try {
            const result = await productService.updateProduct(formData.id, {
              baseCurrency: formData.baseCurrency,
              targetCurrency: formData.targetCurrency,
              cost: parseFloat(formData.cost),
            });

            if (result === null) {
              alert(`Product with ID ${formData.id} does not exist (update)`);
              return;
            }

            // Success case - show confirmation
            alert(`Product with ID ${formData.id} updated successfully`);
          } catch (error) {
            console.error("Error updating product:", error);
            alert(`Error updating product with ID ${formData.id}`);
            return;
          }
          break;
        }

        case "delete": {
          if (
            !formData.id ||
            !/^\d+$/.test(formData.id) ||
            parseInt(formData.id) <= 0
          ) {
            alert("ID must be a positive number");
            return;
          }

          try {
            const result = await productService.deleteProduct(formData.id);
            if (result === null) {
              alert(`Product with ID ${formData.id} does not exist (delete)`);
              return;
            }
          } catch (error) {
            console.error("Error deleting product:", error);
            alert(`Error deleting product with ID ${formData.id}`);
            return;
          }
          break;
        }
        case "getById":
          if (
            !formData.id ||
            !/^\d+$/.test(formData.id) ||
            parseInt(formData.id) <= 0
          ) {
            alert("ID must be a positive number");
            return;
          }
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
          const currencyRegex = /^[A-Z]{3}$/;
          if (!currencyRegex.test(formData.baseCurrency)) {
            alert("Base Currency must be 3 uppercase letters (e.g., USD, EUR)");
            return;
          }
          if (!currencyRegex.test(formData.targetCurrency)) {
            alert(
              "Target Currency must be 3 uppercase letters (e.g., USD, EUR)"
            );
            return;
          }
          if (formData.baseCurrency === formData.targetCurrency) {
            alert("Base Currency and Target Currency cannot be the same");
            return;
          }
          await handleExternalFetch();
          fetchAllProducts();
          return;
        }
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
