import { useState } from "react";

function useProductState() {
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
    id: true,
    baseCurrency: true,
    targetCurrency: true,
    cost: true,
    page: true,
    size: true,
    sortBy: true,
  });
  const [activeOperation, setActiveOperation] = useState(null);

  const resetFieldsState = () => {
    setDisabledFields({
      id: true,
      baseCurrency: true,
      targetCurrency: true,
      cost: true,
      page: true,
      size: true,
      sortBy: true,
    });
    setActiveOperation(null);
  };

  const clearForm = () => {
    setFormData(initialFormState);
    setCurrentProduct(null);
    resetFieldsState();
  };

  return {
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
  };
}

export default useProductState;
