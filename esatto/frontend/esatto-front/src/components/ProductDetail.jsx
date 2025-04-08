import React from "react";

function ProductDetail({ product }) {
  if (!product) {
    return <div>No product selected</div>;
  }

  return (
    <div style={{ padding: "10px", textAlign: "left" }}>
      <h3 style={{ marginTop: 0 }}>Product Details</h3>
      <p style={{ margin: "5px 0" }}>ID: {product.id}</p>
      <p style={{ margin: "5px 0" }}>Base Currency: {product.baseCurrency}</p>
      <p style={{ margin: "5px 0" }}>
        Target Currency: {product.targetCurrency}
      </p>
      <p style={{ margin: "5px 0" }}>Cost: {product.cost}</p>
      <p style={{ margin: "5px 0" }}>Date: {product.date || "N/A"}</p>
    </div>
  );
}

export default ProductDetail;
