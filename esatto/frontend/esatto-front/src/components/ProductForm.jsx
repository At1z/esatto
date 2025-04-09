import React from "react";

function ProductForm({
  formData,
  setFormData,
  onSubmit,
  disabledFields,
  activeOperation,
}) {
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const getSubmitButtonText = () => {
    if (!activeOperation) return "Submit";

    switch (activeOperation) {
      case "add":
        return "Add Product";
      case "update":
        return "Update Product";
      case "delete":
        return "Delete Product";
      case "getById":
        return "Get Product";
      case "search":
        return "Search Products";
      case "page":
        return "Get Page";
      case "external":
        return "Get External Soure";
      default:
        return "Submit";
    }
  };

  const getFieldStyle = (fieldName) => {
    return {
      backgroundColor: disabledFields[fieldName] ? "#e0e0e0" : "white",
      width: "95%",
      padding: "7px",
      borderRadius: "4px",
      border: "1px solid #645e5e",
      fontSize: "14px",
    };
  };

  return (
    <form onSubmit={onSubmit}>
      <div className="form-group">
        <label htmlFor="id">ID</label>
        <input
          type="text"
          id="id"
          name="id"
          value={formData.id}
          onChange={handleInputChange}
          disabled={disabledFields.id}
          style={getFieldStyle("id")}
        />
      </div>
      <div className="form-group">
        <label htmlFor="baseCurrency">Base Currency</label>
        <input
          type="text"
          id="baseCurrency"
          name="baseCurrency"
          value={formData.baseCurrency}
          onChange={handleInputChange}
          disabled={disabledFields.baseCurrency}
          style={getFieldStyle("baseCurrency")}
        />
      </div>
      <div className="form-group">
        <label htmlFor="targetCurrency">Target Currency</label>
        <input
          type="text"
          id="targetCurrency"
          name="targetCurrency"
          value={formData.targetCurrency}
          onChange={handleInputChange}
          disabled={disabledFields.targetCurrency}
          style={getFieldStyle("targetCurrency")}
        />
      </div>
      <div className="form-group">
        <label htmlFor="cost">Cost</label>
        <input
          type="number"
          id="cost"
          name="cost"
          min="0"
          step="0.01"
          value={formData.cost}
          onChange={handleInputChange}
          disabled={disabledFields.cost}
          style={getFieldStyle("cost")}
        />
      </div>
      <div className="form-group">
        <label htmlFor="page">Page</label>
        <input
          type="number"
          id="page"
          name="page"
          min="0"
          value={formData.page}
          onChange={handleInputChange}
          disabled={disabledFields.page}
          style={getFieldStyle("page")}
        />
      </div>
      <div className="form-group">
        <label htmlFor="size">Size</label>
        <input
          type="number"
          id="size"
          name="size"
          min="0"
          value={formData.size}
          onChange={handleInputChange}
          disabled={disabledFields.size}
          style={getFieldStyle("size")}
        />
      </div>
      <button type="submit">{getSubmitButtonText()}</button>
    </form>
  );
}

export default ProductForm;
