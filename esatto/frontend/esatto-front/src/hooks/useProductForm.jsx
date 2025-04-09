function useProductForm({
  setDisabledFields,
  setActiveOperation,
  setFormData,
  initialFormState,
  formData,
  executeSearch,
  executePaging,
}) {
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

  return {
    handleAdd,
    handleDelete,
    handleUpdate,
    handleGetById,
    handleSort,
    handleSearch,
    handlePage,
    handleExternalSource,
  };
}

export default useProductForm;
