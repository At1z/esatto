import { productService, currencyService } from "../services/productService";

function useProductOperations({
  formData,
  setProducts,
  setCurrentProduct,
  setDisplayMode,
  setPaginationInfo,
  setLoading,
  resetFieldsState,
}) {
  const fetchAllProducts = async () => {
    setLoading(true);
    try {
      const data = await productService.getAllProducts();
      setProducts(data);
      setDisplayMode("list");
      setPaginationInfo(null);
      resetFieldsState();
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const executeGetById = async () => {
    if (!formData.id) {
      alert("Please enter an ID");
      return;
    }


    if (!/^\d+$/.test(formData.id)) {
      alert("ID must be a numeric value");
      return;
    }

    setLoading(true);
    try {
      const product = await productService.getProductById(formData.id);

      if (product === null) {
        alert(`Product with ID ${formData.id} was not found.`);
        setDisplayMode("list");
        setCurrentProduct(null);
        return;
      }

      setCurrentProduct(product);
      setDisplayMode("detail");
    } catch (error) {
      console.error(`Error fetching product:`, error);

      let errorMessage = `Could not retrieve product with ID ${formData.id}`;
      if (error.message.includes("NetworkError")) {
        errorMessage = "Network error - please check your connection";
      }

      alert(errorMessage);
      setDisplayMode("list");
      setCurrentProduct(null);
    } finally {
      setLoading(false);
    }
  };

  const executeSort = async () => {
    setLoading(true);
    try {
      const sortField = formData.sortBy || "date";
      const sortedProducts = await productService.getSortedProducts(sortField);
      setProducts(sortedProducts);
      setDisplayMode("list");
    } catch (error) {
      console.error("Error sorting products:", error);
    } finally {
      setLoading(false);
    }
  };

  const executeSearch = async () => {
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

  const executePaging = async () => {
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

  const handleExternalFetch = async () => {
    setLoading(true);
    try {
      const exchangeData = await currencyService.getExchangeRate({
        baseCurrency: formData.baseCurrency,
        targetCurrency: formData.targetCurrency,
      });

      if (exchangeData) {
        const formattedData = Array.isArray(exchangeData)
          ? exchangeData
          : [exchangeData].map((item) => ({
              id: item.id || "external-" + Date.now(),
              baseCurrency: item.baseCurrency || formData.baseCurrency,
              targetCurrency: item.targetCurrency || formData.targetCurrency,
              cost: item.rate || item.cost || 0,
              date: item.date || new Date().toISOString().split("T")[0],
            }));

        setProducts(formattedData);
        setDisplayMode("list");
        return;
      }
    } catch (error) {
      console.error("Error fetching exchange rate:", error);
    } finally {
      setLoading(false);
    }
  };

  return {
    fetchAllProducts,
    executeGetById,
    executeSort,
    executeSearch,
    executePaging,
    handleExternalFetch,
  };
}

export default useProductOperations;
