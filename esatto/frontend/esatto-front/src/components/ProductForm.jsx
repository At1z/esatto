import React from "react";
import "./../styles/ProductForm.css";

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
      case "sort":
        return "Sort Products";
      case "external":
        return "Get External Source";
      default:
        return "Submit";
    }
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
          className={`input-field ${disabledFields.id ? "disabled" : ""}`}
        />
      </div>
      <div className="form-group">
        <label htmlFor="baseCurrency">Base Currency</label>
        <input
          type="text"
          id="baseCurrency"
          name="baseCurrency"
          pattern="[A-Z]{3}"
          title="Must be 3 uppercase letters (e.g., USD)"
          value={formData.baseCurrency}
          onChange={handleInputChange}
          disabled={disabledFields.baseCurrency}
          className={`input-field ${
            disabledFields.baseCurrency ? "disabled" : ""
          }`}
        />
      </div>
      <div className="form-group">
        <label htmlFor="targetCurrency">Target Currency</label>
        <input
          type="text"
          id="targetCurrency"
          name="targetCurrency"
          pattern="[A-Z]{3}"
          title="Must be 3 uppercase letters (e.g., USD)"
          value={formData.targetCurrency}
          onChange={handleInputChange}
          disabled={disabledFields.targetCurrency}
          className={`input-field ${
            disabledFields.targetCurrency ? "disabled" : ""
          }`}
        />
      </div>
      <div className="form-group">
        <label htmlFor="cost">Cost</label>
        <input
          type="number"
          id="cost"
          name="cost"
          min="0.01"
          step="0.01"
          value={formData.cost}
          onChange={handleInputChange}
          disabled={disabledFields.cost}
          className={`input-field ${disabledFields.cost ? "disabled" : ""}`}
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
          className={`input-field ${disabledFields.page ? "disabled" : ""}`}
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
          className={`input-field ${disabledFields.size ? "disabled" : ""}`}
        />
      </div>
      <div className="form-group">
        <label htmlFor="sortBy">Sort By</label>
        <select
          id="sortBy"
          name="sortBy"
          value={formData.sortBy}
          onChange={handleInputChange}
          disabled={disabledFields.sortBy}
          className={`input-field ${disabledFields.sortBy ? "disabled" : ""}`}
        >
          <option value="date">Date</option>
          <option value="baseCurrency">Base Currency</option>
          <option value="targetCurrency">Target Currency</option>
        </select>
      </div>
      <button type="submit">{getSubmitButtonText()}</button>
    </form>
  );
}

export default ProductForm;
