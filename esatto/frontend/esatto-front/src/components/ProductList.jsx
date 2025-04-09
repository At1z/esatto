import React from "react";
import "./../styles/ProductList.css";

function ProductList({ products, paginationInfo }) {
  if (!products || products.length === 0) {
    return <div>No products found</div>;
  }

  const hasCheeperProperty = products.some(
    (item) => item.cheaper !== undefined
  );

  return (
    <div className="product-list-container">
      <h2 className="product-list-title">Products ({products.length})</h2>

      <table className="product-table">
        <thead>
          <tr>
            <th className="product-th">ID</th>
            <th className="product-th">Base Currency</th>
            <th className="product-th">Target Currency</th>
            <th className="product-th">Cost</th>
            {hasCheeperProperty && <th className="product-th">Cheaper</th>}
            <th className="product-th">Date</th>
          </tr>
        </thead>
        <tbody>
          {products.map((item) => (
            <tr key={item.id} className="product-row">
              <td className="product-td">{item.id}</td>
              <td className="product-td">{item.baseCurrency}</td>
              <td className="product-td">{item.targetCurrency}</td>
              <td className="product-td">{item.cost}</td>
              {hasCheeperProperty && (
                <td className="product-td">{item.cheaper ? "Yes" : "No"}</td>
              )}
              <td className="product-td">{item.date}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {paginationInfo && (
        <div className="pagination-info">
          Page {paginationInfo.number + 1} of {paginationInfo.totalPages} |
          Total items: {paginationInfo.totalElements}
        </div>
      )}
    </div>
  );
}

export default ProductList;
