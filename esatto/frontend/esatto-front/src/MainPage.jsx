import React from "react";
import "./styles/MainPage.css";
import ProductForm from "./components/ProductForm";
import ProductList from "./components/ProductList";
import ProductDetail from "./components/ProductDetail";
import useProductManagement from "./hooks/useProductManagement";

function MainPage() {
  const {
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
  } = useProductManagement();

  return (
    <div>
      <div className="Header">Currency Exchanger</div>
      <div className="Columns">
        <div className="LeftColumn">
          <button onClick={handleAdd}>Add</button>
          <button onClick={handleDelete}>Delete</button>
          <button onClick={handleUpdate}>Update</button>
          <button onClick={fetchAllProducts}>Show All</button>
        </div>

        <div className="MiddleColumn">
          <div
            className="MainPanel"
            style={{ overflow: "auto", maxHeight: "100%" }}
          >
            {loading ? (
              "Loading..."
            ) : displayMode === "list" ? (
              <ProductList
                products={products}
                paginationInfo={paginationInfo}
              />
            ) : (
              <ProductDetail product={currentProduct} />
            )}
          </div>

          <div className="FieldHolder">
            <ProductForm
              formData={formData}
              setFormData={setFormData}
              onSubmit={handleSubmit}
              currentProduct={currentProduct}
              disabledFields={disabledFields}
              activeOperation={activeOperation}
            />
          </div>
        </div>

        <div className="RightColumn">
          <button onClick={handleGetById}>Get by ID</button>
          <button onClick={handleSort}>Sort By</button>
          <button onClick={handleSearch}>Search for</button>
          <button onClick={handlePage}>Page</button>
        </div>
      </div>
    </div>
  );
}

export default MainPage;
