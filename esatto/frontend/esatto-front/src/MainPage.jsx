import "./MainPage.css";

function MainPage() {
  return (
    <>
      <div>
        <div className="Header">Currency Exchanger</div>
        <div className="Columns">
          <div className="LeftColumn">
            <button id="addButton">Add</button>
            <button id="deleteButton">Delete</button>
            <button id="updateButton">Update</button>
            <button id="showAllButton">Show All</button>
          </div>

          <div className="MiddleColumn">
            <div className="MainPanel">siema</div>
            <div className="FieldHolder">
              <form>
                <div className="form-group">
                  <label htmlFor="id">ID</label>
                  <input type="text" id="id" name="id" />
                </div>
                <div className="form-group">
                  <label htmlFor="baseCurrency">Base Currency</label>
                  <input type="text" id="baseCurrency" name="baseCurrency" />
                </div>
                <div className="form-group">
                  <label htmlFor="targetCurrency">Target Currency</label>
                  <input
                    type="text"
                    id="targetCurrency"
                    name="targetCurrency"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="cost">Cost</label>
                  <input type="number" id="cost" name="cost" min="0" />
                </div>
                <div className="form-group">
                  <label htmlFor="page">Page</label>
                  <input type="number" id="page" name="page" min="0" />
                </div>
                <div className="form-group">
                  <label htmlFor="size">Size</label>
                  <input type="number" id="size" name="size" min="0" />
                </div>
                <button type="submit">Submit</button>
              </form>
            </div>
          </div>
          <div className="RightColumn">
            {" "}
            <button id="getButton">Get by ID</button>
            <button id="deleteButton">Sort By</button>
            <button id="updateButton">Search for</button>
            <button id="showAllButton">Page</button>
          </div>
        </div>
      </div>
    </>
  );
}

export default MainPage;
