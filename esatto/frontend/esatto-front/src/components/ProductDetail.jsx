import React from "react";
import "./../styles/ProductDetails.css";

function ProductDetail({ product }) {
  if (!product) {
    return <div className="product-detail-container">No product selected</div>;
  }

  const fields = [
    { label: "ID", value: product.id },
    { label: "Base Currency", value: product.baseCurrency },
    { label: "Target Currency", value: product.targetCurrency },
    { label: "Cost", value: product.cost },
    { label: "Date", value: product.date || "N/A" },
  ];

  if (product.cheaper !== undefined) {
    fields.push({
      label: "Cheaper",
      value: product.cheaper ? "Yes" : "No",
    });
  }

  return (
    <div className="product-detail-container">
      <h2 className="product-detail-title">Product Details</h2>
      <div className="product-detail-rows">
        {fields.map((field, index) => (
          <div
            key={index}
            className={`product-detail-row ${
              index % 2 === 1 ? "gray-row" : ""
            }`}
          >
            <span className="product-label">{field.label}:</span>
            <span className="product-value">{field.value}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ProductDetail;
