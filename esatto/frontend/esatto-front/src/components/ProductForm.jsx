import React from "react";

function ProductForm({ formData, setFormData, onSubmit, currentProduct }) {
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
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
        />
      </div>
      <button type="submit">Submit</button>
    </form>
  );
}

export default ProductForm;
