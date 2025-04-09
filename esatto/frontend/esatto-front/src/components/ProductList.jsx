import React from "react";

const thStyle = {
  borderBottom: "2px solid #ccc",
  padding: "10px",
  textAlign: "center",
};

const tdStyle = {
  padding: "8px 10px",
  borderBottom: "1px solid #eee",
};

function ProductList({ products, paginationInfo }) {
  if (!products || products.length === 0) {
    return <div>No products found</div>;
  }

  // Check if any product has the 'cheaper' property
  const hasCheeperProperty = products.some(
    (item) => item.cheaper !== undefined
  );

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h2 style={{ marginTop: 0, marginBottom: "15px" }}>
        Products ({products.length})
      </h2>

      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          fontSize: "1rem",
        }}
      >
        <thead>
          <tr style={{ backgroundColor: "#f5f5f5" }}>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>Base Currency</th>
            <th style={thStyle}>Target Currency</th>
            <th style={thStyle}>Cost</th>
            <th style={thStyle}>Date</th>
            {hasCheeperProperty && <th style={thStyle}>Cheaper</th>}
          </tr>
        </thead>
        <tbody>
          {products.map((item, index) => (
            <tr
              key={item.id}
              style={{
                backgroundColor: index % 2 === 0 ? "#ffffff" : "#c9c9c9",
              }}
            >
              <td style={tdStyle}>{item.id}</td>
              <td style={tdStyle}>{item.baseCurrency}</td>
              <td style={tdStyle}>{item.targetCurrency}</td>
              <td style={tdStyle}>{item.cost}</td>
              {hasCheeperProperty && (
                <td style={tdStyle}>{item.cheaper ? "Yes" : "No"}</td>
              )}
              <td style={tdStyle}>{item.date}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {paginationInfo && (
        <div
          style={{
            marginTop: "15px",
            textAlign: "center",
            fontSize: "0.95rem",
            color: "#555",
          }}
        >
          Page {paginationInfo.number + 1} of {paginationInfo.totalPages} |
          Total items: {paginationInfo.totalElements}
        </div>
      )}
    </div>
  );
}

export default ProductList;
