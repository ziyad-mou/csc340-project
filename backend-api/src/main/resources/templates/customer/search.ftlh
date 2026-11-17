<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Shop Cards | PokeVerse</title>

    <link rel="icon" type="image/png" href="/images/poke.png">

    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/customer.css">
    <link rel="stylesheet" href="/css/search-polish.css?v=1">

    <style>
        .marketplace-empty {
            grid-column: 1 / -1;
            padding: 35px 20px;
            border: 1px dashed #d1d5db;
            border-radius: 12px;
            background: #fff;
            text-align: center;
            color: #6b7280;
        }

        [hidden] {
            display: none !important;
        }

        .add-cart-btn:disabled {
            cursor: not-allowed;
            opacity: 0.6;
        }

        .add-cart-btn:disabled:hover {
            background: #f4c430;
            transform: none;
        }

        .visually-hidden {
            position: absolute;
            width: 1px;
            height: 1px;
            padding: 0;
            margin: -1px;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
            white-space: nowrap;
            border: 0;
        }
    </style>
</head>

<body>

<#if error??>
<div role="alert" style="padding:15px; background:#ffe8e8; color:#9b2020; text-align:center;">
    ${error}
</div>
</#if>


    <!-- =========================
         NAVBAR
    ========================== -->

<!-- NAVBAR -->
<header class="navbar">

    <a href="/customer/search" class="logo">
        <img src="/images/PokeVerse-logo.png"
             alt="PokeVerse Logo">
    </a>

    <nav>
        <a href="/customer">Home</a>

        <a href="/customer/search"
           aria-current="page">
            Shop Cards
        </a>

        <a href="/customer/cart">Cart</a>

        <a href="/customer/history">Orders</a>

        <a href="/customer/tutorial">Tutorial</a>
    </nav>

    <div class="nav-buttons">

        <#if loggedIn?? && loggedIn>

            <form action="/customer/logout"
                  method="post">

                <button type="submit"
                        class="login-btn logout-btn">
                    Log Out
                </button>

            </form>

        <#else>

            <a href="/customer/login"
               class="login-btn">
                Login
            </a>

            <a href="/register"
               class="signup-btn">
                Sign Up
            </a>

        </#if>

    </div>

</header>



    <!-- =========================
         SEARCH HEADER
    ========================== -->
    <section class="search-page-header">

        <p class="section-label">
            POKEVERSE MARKETPLACE
        </p>

        <h1>
            Find Your Next Pokémon Card
        </h1>

        <p>
            Search Pokémon card listings by name, then narrow down the results.
        </p>

        <!-- FreeMarker search form -->
        <form class="marketplace-search"
              action="/customer/search"
              method="get">

            <input
                type="search"
                name="search"
                value="${search!''}"
                placeholder="Search Pokémon cards and collections..."
                aria-label="Search Pokémon cards"
            >

            <button type="submit">
                Search
            </button>

        </form>

    </section>


    <!-- =========================
         MARKETPLACE
    ========================== -->
    <main class="marketplace-container">

        <!-- FILTERS -->
        <aside class="marketplace-filters">

            <div class="filters-heading">
                <h2>Filters</h2>
            </div>


            <!-- Product Type -->
            <details class="filter-dropdown" open>

                <summary>
                    Product Type
                </summary>

                <div class="filter-options">

                    <label>
                        <input type="checkbox"
                               data-filter="type"
                               value="collection box">
                        Collection Box
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="type"
                               value="elite trainer box">
                        Elite Trainer Box
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="type"
                               value="promo collection">
                        Promo Collection
                    </label>

                </div>

            </details>


            <!-- Price -->
            <details class="filter-dropdown">

                <summary>
                    Price
                </summary>

                <div class="filter-options">

                    <label>
                        <input type="checkbox"
                               data-filter="price"
                               value="under25">
                        Under $25
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="price"
                               value="25to50">
                        $25 - $50
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="price"
                               value="50to100">
                        $50 - $100
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="price"
                               value="100plus">
                        $100+
                    </label>

                </div>

            </details>


            <!-- Collection -->
            <details class="filter-dropdown">

                <summary>
                    Collection
                </summary>

                <div class="filter-options">

                    <label>
                        <input type="checkbox"
                               data-filter="collection"
                               value="30th celebration">
                        30th Celebration
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="collection"
                               value="promo collections">
                        Promo Collections
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="collection"
                               value="trainer boxes">
                        Trainer Boxes
                    </label>

                </div>

            </details>


            <!-- Condition -->
            <details class="filter-dropdown">

                <summary>
                    Condition
                </summary>

                <div class="filter-options">

                    <label>
                        <input type="checkbox"
                               data-filter="condition"
                               value="new">
                        New
                    </label>

                    <label>
                        <input type="checkbox"
                               data-filter="condition"
                               value="near mint">
                        Near Mint
                    </label>

                </div>

            </details>

        </aside>


        <!-- =========================
             RESULTS
        ========================== -->
        <section class="marketplace-results">

            <div class="results-toolbar">

                <div>
                    <h2>Pokémon Products</h2>

                    <p id="results-status"
                       class="visually-hidden"
                       aria-live="polite"></p>
                </div>

                <!-- Sorting -->
                <label class="sort-control">

                    Sort by:

                    <select id="sort-select"
                            aria-label="Sort cards">

                        <option value="featured">
                            Featured
                        </option>

                        <option value="price-low">
                            Price: Low to High
                        </option>

                        <option value="price-high">
                            Price: High to Low
                        </option>

                        <option value="name">
                            Name: A-Z
                        </option>

                    </select>

                </label>

            </div>


            <!-- =========================
                 PRODUCT GRID
                 FREEMARKER DATABASE DATA
            ========================== -->

            <div class="marketplace-card-grid" id="card-grid">

                <#if cards?has_content>

                    <#list cards as card>

                        <article class="marketplace-card"
                                 data-name="${card.name!''}"
                                 data-description="${card.description!''}"
                                 data-condition="${card.condition!''}"
                                 data-price="<#if card.price??>${card.price?c}</#if>">

                            <!-- Card Image -->
                            <div class="marketplace-card-image">

                                <#-- Ignore example.com placeholder images -->

<#if (card.imageUrl)?has_content
    && (card.imageUrl?starts_with("/")
        || card.imageUrl?starts_with("https://")
        || card.imageUrl?starts_with("http://"))
    && !card.imageUrl?contains("example.com")>

                                    <img
                                        src="${card.imageUrl}"
                                        alt="${card.name!'Pokémon card'}"
                                        onerror="this.onerror=null;this.src='/images/poke.png';"
                                    >

                                <#else>

                                    <img
                                        src="/images/poke.png"
                                        alt="Pokémon card image unavailable"
                                    >

                                </#if>

                            </div>


                            <!-- Card Details -->
                            <div class="marketplace-card-content">

                                <p class="card-category">
                                    Pokémon TCG
                                </p>

                                <!-- Name from Neon -->
                                <h3>
                                    ${card.name!'Unnamed card'}
                                </h3>

                                <!-- Condition from Neon -->
                                <p class="card-condition">
                                    Condition: ${card.condition!'Not specified'}
                                </p>

                                <!-- Price from Neon -->
                                <p class="card-price">

                                    <#if card.price??>
                                        $${card.price?string["0.00"]}
                                    <#else>
                                        Price unavailable
                                    </#if>

                                </p>

                                <!-- Inventory from Neon -->
                                <p class="card-condition">

                                    <#if card.stock?? && card.stock gt 0>
                                        In stock: ${card.stock}
                                    <#else>
                                        Out of stock
                                    </#if>

                                </p>

                                <!-- Cart will be implemented in US-3 -->

<form action="/customer/cart/add" method="post">

    <input
        type="hidden"
        name="cardListingId"
        value="${card.id?c}"
    >

    <input
        type="hidden"
        name="quantity"
        value="1"
    >

    <button type="submit" class="add-cart-btn">
        Add to Cart
    </button>

</form>


                            </div>

                        </article>

                    </#list>

                <#else>

                    <!-- No database results -->
                    <p class="marketplace-empty">
                        No Pokémon cards found.
                        Try a different search.
                    </p>

                </#if>

            </div>

            <!-- No results after selecting filters -->
            <p class="marketplace-empty"
               id="filter-empty"
               hidden>
                No cards match the selected filters.
            </p>

        </section>

    </main>


    <!-- =========================
         FOOTER
    ========================== -->
    <footer class="footer">

        <div>
            <strong>PokeVerse</strong>
            <p>Buy. Sell. Collect.</p>
        </div>

        <p>
            CSC 340 Software Engineering Project
        </p>

    </footer>


    <!-- =========================
         CLIENT-SIDE FILTERS & SORTING

         FreeMarker renders the cards first.
         JavaScript only filters those cards.
         No fetch() requests are used.
    ========================== -->
    <script>

        const grid =
            document.getElementById("card-grid");

        const cards = Array.from(
            grid.querySelectorAll(".marketplace-card")
        );

        const filters =
            document.querySelector(".marketplace-filters");

        const sortSelect =
            document.getElementById("sort-select");

        const filterEmpty =
            document.getElementById("filter-empty");

        const status =
            document.getElementById("results-status");


        // Get selected checkboxes
        function selected(group) {

            return Array.from(
                filters.querySelectorAll(
                    'input[data-filter="' + group + '"]:checked'
                ),
                input => input.value
            );

        }


        // Check the price range
        function priceMatches(price, range) {

            if (!Number.isFinite(price)) {
                return false;
            }

            if (range === "under25") {
                return price < 25;
            }

            if (range === "25to50") {
                return price >= 25 && price < 50;
            }

            if (range === "50to100") {
                return price >= 50 && price < 100;
            }

            return price >= 100;

        }


        // Check whether a card matches selected filters
        function matchesFilters(card) {

            const name =
                card.dataset.name.toLowerCase();

            const description =
                card.dataset.description.toLowerCase();

            const searchableText =
                name + " " + description;

            const condition =
                card.dataset.condition.toLowerCase().trim();

            const price = card.dataset.price === ""
                ? NaN
                : Number(card.dataset.price);


            // Price filter
            const prices = selected("price");

            if (
                prices.length &&
                !prices.some(range => priceMatches(price, range))
            ) {
                return false;
            }


            // Condition filter
            const conditions = selected("condition");

            if (
                conditions.length &&
                !conditions.includes(condition)
            ) {
                return false;
            }


            // Product type filter:
            // Uses names/descriptions because the API
            // doesn't provide a dedicated product type.
            const types = selected("type");

            if (
                types.length &&
                !types.some(type => searchableText.includes(type))
            ) {
                return false;
            }


            // Collection filter:
            // Also uses names and descriptions.
            const collections = selected("collection");

            if (
                collections.length &&
                !collections.some(collection =>
                    searchableText.includes(collection) ||
                    (
                        collection === "trainer boxes" &&
                        searchableText.includes("trainer box")
                    ) ||
                    (
                        collection === "promo collections" &&
                        searchableText.includes("promo")
                    )
                )
            ) {
                return false;
            }

            return true;

        }


        // Filter, sort, and display cards
        function updateResults() {

            const visible = cards.filter(matchesFilters);

            const sorted = [...visible];


            // Sort: Price low to high
            if (sortSelect.value === "price-low") {

                sorted.sort((a, b) =>

                    (
                        a.dataset.price === ""
                            ? Infinity
                            : Number(a.dataset.price)
                    ) -
                    (
                        b.dataset.price === ""
                            ? Infinity
                            : Number(b.dataset.price)
                    )

                );

            }


            // Sort: Price high to low
            else if (sortSelect.value === "price-high") {

                sorted.sort((a, b) =>

                    (
                        b.dataset.price === ""
                            ? -Infinity
                            : Number(b.dataset.price)
                    ) -
                    (
                        a.dataset.price === ""
                            ? -Infinity
                            : Number(a.dataset.price)
                    )

                );

            }


            // Sort: Name A-Z
            else if (sortSelect.value === "name") {

                sorted.sort((a, b) =>
                    a.dataset.name.localeCompare(b.dataset.name)
                );

            }


            // Hide all cards
            cards.forEach(card => {
                card.hidden = true;
            });


            // Display sorted matching cards
            sorted.forEach(card => {
                card.hidden = false;
                grid.appendChild(card);
            });


            // Empty state
            filterEmpty.hidden =
                cards.length === 0 || visible.length > 0;


            // Accessible result count (not visually displayed)
            status.textContent =
                visible.length +
                (
                    visible.length === 1
                        ? " product found"
                        : " products found"
                );

        }


        filters.addEventListener(
            "change",
            updateResults
        );

        sortSelect.addEventListener(
            "change",
            updateResults
        );

        updateResults();

    </script>

</body>

</html>
