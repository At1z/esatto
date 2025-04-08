import { useState, useEffect } from "react";
import "./MainPage.css";
import { productService } from "./CurrencyService";

// Product Form Component
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

// Product List Component
function ProductList({ products, paginationInfo }) {
  if (!products || products.length === 0) {
    return <div>No products found</div>;
  }

  return (
    <div style={{ padding: "10px", textAlign: "left" }}>
      <h3 style={{ marginTop: 0 }}>Products ({products.length})</h3>
      <div style={{ fontSize: "0.9em" }}>
        {products.map((item) => (
          <div
            key={item.id}
            style={{
              marginBottom: "4px",
              borderBottom: "1px solid #ddd",
              paddingBottom: "3px",
            }}
          >
            ID: {item.id} | {item.baseCurrency} → {item.targetCurrency} | Cost:{" "}
            {item.cost}
          </div>
        ))}
      </div>

      {paginationInfo && (
        <div style={{ marginTop: "10px", textAlign: "center" }}>
          Page {paginationInfo.number + 1} of {paginationInfo.totalPages} |
          Total items: {paginationInfo.totalElements}
        </div>
      )}
    </div>
  );
}

// Product Detail Component
function ProductDetail({ product }) {
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

// Main Component
function MainPage() {
  const initialFormState = {
    id: "",
    baseCurrency: "",
    targetCurrency: "",
    cost: "",
    page: "0",
    size: "5",
  };

  const [formData, setFormData] = useState(initialFormState);
  const [products, setProducts] = useState([]);
  const [currentProduct, setCurrentProduct] = useState(null);
  const [paginationInfo, setPaginationInfo] = useState(null);
  const [displayMode, setDisplayMode] = useState("list"); // "list" or "detail"
  const [loading, setLoading] = useState(true);

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (formData.id) {
        await productService.updateProduct(formData.id, {
          baseCurrency: formData.baseCurrency,
          targetCurrency: formData.targetCurrency,
          cost: parseFloat(formData.cost),
        });
      } else {
        await productService.addProduct({
          baseCurrency: formData.baseCurrency,
          targetCurrency: formData.targetCurrency,
          cost: parseFloat(formData.cost),
        });
      }
      fetchAllProducts();
      clearForm();
    } catch (error) {
      console.error("Error submitting form:", error);
    }
  };

  // Clear form fields
  const clearForm = () => {
    setFormData(initialFormState);
    setCurrentProduct(null);
  };

  // Fetch all products
  const fetchAllProducts = async () => {
    setLoading(true);
    try {
      const data = await productService.getAllProducts();
      setProducts(data);
      setDisplayMode("list");
      setPaginationInfo(null);
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  // Handler functions for buttons
  const handleAdd = () => clearForm();

  const handleDelete = async () => {
    if (!formData.id) {
      alert("Please enter an ID to delete");
      return;
    }

    try {
      await productService.deleteProduct(formData.id);
      fetchAllProducts();
      clearForm();
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };

  const handleUpdate = () => {
    if (!formData.id || !currentProduct) {
      alert("Please get a product by ID first");
      return;
    }

    // Submit form - the form handler will take care of the update
    document.querySelector("form button[type='submit']").click();
  };

  const handleGetById = async () => {
    if (!formData.id) {
      alert("Please enter an ID");
      return;
    }

    setLoading(true);
    try {
      const product = await productService.getProductById(formData.id);
      if (product) {
        setCurrentProduct(product);
        setFormData({
          id: product.id.toString(),
          baseCurrency: product.baseCurrency,
          targetCurrency: product.targetCurrency,
          cost: product.cost.toString(),
          page: formData.page,
          size: formData.size,
        });
        setDisplayMode("detail");
      }
    } catch (error) {
      console.error("Error fetching product:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSort = async () => {
    const sortField = prompt(
      "Sort by (date, baseCurrency, targetCurrency):",
      "date"
    );

    if (!sortField) return;

    setLoading(true);
    try {
      const sortedProducts = await productService.getSortedProducts(sortField);
      setProducts(sortedProducts);
      setDisplayMode("list");
    } catch (error) {
      console.error("Error sorting products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    setLoading(true);
    try {
      const searchParams = {
        baseCurrency: formData.baseCurrency || null,
        targetCurrency: formData.targetCurrency || null,
        maxCost: formData.cost ? parseFloat(formData.cost) : null,
      };

      const filteredProducts = await productService.searchProducts(
        searchParams
      );
      setProducts(filteredProducts);
      setDisplayMode("list");
    } catch (error) {
      console.error("Error searching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handlePage = async () => {
    setLoading(true);
    try {
      const page = parseInt(formData.page) || 0;
      const size = parseInt(formData.size) || 5;
      const pagedData = await productService.getPagedProducts(
        page,
        size,
        formData.baseCurrency || null,
        formData.targetCurrency || null
      );

      if (pagedData && pagedData.content) {
        setProducts(pagedData.content);
        setPaginationInfo(pagedData);
        setDisplayMode("list");
      }
    } catch (error) {
      console.error("Error fetching paged products:", error);
    } finally {
      setLoading(false);
    }
  };

  // Load initial data
  useEffect(() => {
    fetchAllProducts();
  }, []);

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
