<h1>Currency Exchanger</h1>
<h3>A simple application that provides up-to-date currency exchange rates.</h3>

<h1>Setting Up</h1>
<h2>📦 Docker is required</h2>
<a href="https://www.docker.com/get-started/" target="_blank">Start with Docker :)</a>

<h3>Clone the repository</h3>
<pre><code class="language-bash">
git clone https://github.com/At1z/esatto.git
</code></pre>

<hr />
<h3>⚙️ Prepare the <code>.env</code> file</h3>
<p>Create a <code>.env</code> file in the <strong>root directory</strong> with the following structure:</p>
<pre><code class="language-env">
DB_URL=jdbc:postgresql://localhost:5432/esatto
DB_USERNAME=postgres
DB_PASSWORD=postgres
CURRENCY_API_KEY=
</code></pre>
<p>💡 You can get your API key from
<a href="https://www.ratexchanges.com/login" target="_blank">this API provider</a>
</p>
<hr />

<h3>🐳 Run the app with Docker</h3>
<p>Open PowerShell, navigate to the root directory, and run:</p>
<pre><code class="language-bash">
docker-compose --env-file .env up --build
</code></pre>
<pre><code class="language-bash">
## next time use
docker-compose --env-file .env up 
</code></pre>

<h1>Testing</h1>
<h3>You can try the application by typing "localhost" in your browser </h3>
<h3>The application works by selecting actions from the left or right column. In the form, inactive fields will turn gray.</h3>

<h2>Fields:</h2>
<ul>
<li><strong>ID:</strong> Positive integer - ID of the record in the database</li>
<li><strong>Base Currency:</strong> 3 uppercase letters (currency abbreviation) - the currency from which we want to convert</li>
<li><strong>Target Currency:</strong> 3 uppercase letters (currency abbreviation) - the currency to which we want to convert</li>
<li><strong>Cost:</strong> Positive double - conversion value</li>
<li><strong>Page:</strong> Positive integer - page number</li>
<li><strong>Size:</strong> Positive integer - number of items per page</li>
<li><strong>Sort By:</strong> List of 3 possible sorting options</li>
</ul>

<h2>Buttons</h2>
    <p>Here are the actions you can take with the buttons:</p>
    <ul>
        <li><strong>Add:</strong> Allows you to add a record to the database by specifying the base currency, target currency, and conversion cost.</li>
        <li><strong>Delete:</strong> Allows you to delete a record from the database using the record's ID.</li>
        <li><strong>Update:</strong> Allows you to modify a specific record by providing the ID along with the base currency, target currency, and conversion cost.</li>
        <li><strong>Show All:</strong> Displays all records from the database.</li>
        <li><strong>External Source:</strong> By providing a base currency and target currency, it retrieves the converted currency from an external source.</li>
        <li><strong>Get by ID:</strong> Allows you to retrieve a specific record by providing the ID, and it will show detailed information about that record.</li>
        <li><strong>Sort By:</strong> Allows you to sort records based on date, base currency, or target currency.</li>
        <li><strong>Search For:</strong> Search for records in the database based on specific criteria (e.g., base currency, target currency, or cost) with a given value to filter the results.</li>
        <li><strong>Page:</strong> Specifies the page number and the number of records per page to display in pagination.</li>
</ul>

<h2>Table</h2>
<p>The table is sorted by date.</p>

<p>It includes the fields mentioned earlier, plus the following:</p>
<ul>
    <li><strong>Cheaper</strong> – a boolean value indicating whether the cost of the latest conversion for the given currency pair is lower than the previous one. This field is updated when using the <em>Add</em> or <em>Update</em> buttons.</li>
    <li><strong>Date</strong> – the date when the record was created or last modified.</li>
</ul>


