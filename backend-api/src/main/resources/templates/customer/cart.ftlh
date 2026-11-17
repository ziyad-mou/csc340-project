
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Your Cart | PokeVerse</title>

    <link rel="icon"
          type="image/png"
          href="/images/poke.png">

    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/customer.css?v=2">

    <style>

/* CART NOTIFICATIONS */

.cart-message {
    width: min(1100px, calc(100% - 40px));
    max-width: none;
    box-sizing: border-box;

    margin: 0 auto 18px;
    padding: 14px 18px;

    border-radius: 10px;

    font-size: 14px;
    font-weight: 600;
    line-height: 1.5;
    text-align: left;
}

/* Success notification */
.cart-success {
    background: #f0fdf4;
    color: #166534;

    border: 1px solid #bbf7d0;
    border-left: 4px solid #16a34a;
}

/* Error notification */
.cart-error {
    background: #fef2f2;
    color: #991b1b;

    border: 1px solid #fecaca;
    border-left: 4px solid #dc2626;
}


        .cart-empty {
            padding: 40px 24px;
            text-align: center;
        }

        .cart-quantity form {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .cart-quantity input {
            width: 70px;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .cart-update-btn {
            padding: 8px 12px;
            cursor: pointer;
        }

        .checkout-disabled {
            opacity: 0.5;
            cursor: not-allowed;
            text-align: center;
        }
    </style>
</head>

<body class="cart-page">

    <!-- NAVBAR -->
    <header class="navbar">

        <a href="/customer/search" class="logo">
            <img src="/images/PokeVerse-logo.png"
                 alt="PokeVerse Logo">
        </a>

        <nav>
            <a href="/customer">Home</a>

            <a href="/customer/search">
                Shop Cards
            </a>

            <a href="/customer/cart"
               aria-current="page">
                Cart
            </a>

            <a href="/customer/history">Orders</a>

            <a href="/customer/tutorial">Tutorial</a>
        </nav>


        <div class="nav-buttons">

         <#if loggedIn>

        <!-- Customer is logged in -->
        <form action="/customer/logout" method="post">
            <button type="submit"
                    class="login-btn logout-btn">
                Log Out
            </button>
        </form>

        <#else>

        <!-- Customer is not logged in -->
        <a href="/customer/login" class="login-btn">
            Login
        </a>

        <a href="/register" class="signup-btn">
            Sign Up
        </a>

    </#if>

</div>


    </header>


    <!-- CART HEADER -->
    <section class="cart-page-header">

        <p class="section-label">
            YOUR POKEVERSE CART
        </p>

        <h1>Your Cart</h1>

        <p>
            Review the Pokémon products you want
            before checking out.
        </p>

    </section>


    <!-- SUCCESS MESSAGE -->
    <#if success??>
        <div class="cart-message cart-success"
             role="status">
            ${success}
        </div>
    </#if>


    <!-- ERROR MESSAGE -->
    <#if error??>
        <div class="cart-message cart-error"
             role="alert">
            ${error}
        </div>
    </#if>


    <!-- CART -->
    <main class="cart-layout">

        <section class="cart-items">

            <#if cartItems?has_content>

                <!-- DISPLAY REAL CART ITEMS -->
                <#list cartItems as item>

                    <article class="cart-item">

                        <!-- PRODUCT IMAGE -->
                        <div class="cart-item-image">

                            <img
                                src="${item.cardListing.imageUrl!'/images/poke.png'}"
                                alt="${item.cardListing.name}"
                                onerror="this.onerror=null;this.src='/images/poke.png';"
                            >

                        </div>


                        <!-- PRODUCT INFORMATION -->
                        <div class="cart-item-info">

                            <h2>
                                ${item.cardListing.name}
                            </h2>

                            <p>Pokémon TCG</p>

                            <p>
                                Condition:
                                ${item.cardListing.condition!'Not specified'}
                            </p>

                            <p>
                                In stock:
                                ${item.cardListing.stock!0}
                            </p>


                            <!-- REMOVE ITEM -->
                            <form
                                action="/customer/cart/remove"
                                method="post">

                                <input
                                    type="hidden"
                                    name="cartItemId"
                                    value="${item.id?c}"
                                >

                                <button
                                    type="submit"
                                    class="remove-item-btn">
                                    Remove
                                </button>

                            </form>

                        </div>


                        <!-- QUANTITY -->
                        <div class="cart-quantity">

                            <form
                                action="/customer/cart/update"
                                method="post">

                                <input
                                    type="hidden"
                                    name="cartItemId"
                                    value="${item.id?c}"
                                >

                                <label
                                    for="quantity-${item.id?c}">
                                    Qty
                                </label>

                                <input
                                    type="number"
                                    id="quantity-${item.id?c}"
                                    name="quantity"
                                    min="1"
                                    max="${(item.cardListing.stock!0)?c}"
                                    value="${item.quantity?c}"
                                    required
                                >

                                <button
                                    type="submit"
                                    class="cart-update-btn">
                                    Update
                                </button>

                            </form>

                        </div>


                        <!-- ITEM TOTAL -->
                        <p class="cart-item-price">
                            $${(item.cardListing.price * item.quantity)?string["0.00"]}
                        </p>

                    </article>

                </#list>

            <#else>

                <!-- EMPTY CART -->
                <div class="cart-empty">

                    <h2>Your cart is empty</h2>

                    <p>
                        You haven't added any
                        Pokémon cards yet.
                    </p>

                    <a href="/customer/search"
                       class="continue-shopping">
                        Browse Pokémon Cards
                    </a>

                </div>

            </#if>

        </section>


        <!-- ORDER SUMMARY -->
        <aside class="order-summary">

            <h2>Order Summary</h2>

            <div class="summary-row">

                <span>Subtotal</span>

                <span>
                    $${subtotal?string["0.00"]}
                </span>

            </div>

            <div class="summary-row">

                <span>Shipping</span>

                <span>
                    Calculated at checkout
                </span>

            </div>

            <div class="summary-row">

                <span>Tax</span>

                <span>
                    Calculated at checkout
                </span>

            </div>

            <div class="summary-row summary-total">

                <strong>
                    Estimated total
                </strong>

                <strong>
                    $${subtotal?string["0.00"]}
                </strong>

            </div>

            <p>
                Shipping and tax are not included.
            </p>

            <!-- CHECKOUT NOT CONNECTED YET -->
            <span class="checkout-btn checkout-disabled"
                  aria-disabled="true">
                Proceed to Checkout (Coming Soon)
            </span>

            <a href="/customer/search"
               class="continue-shopping">
                Continue Shopping
            </a>

        </aside>

    </main>


    <!-- FOOTER -->
    <footer class="footer">

        <div>
            <strong>PokeVerse</strong>
            <p>Buy. Sell. Collect.</p>
        </div>

        <p>
            CSC 340 Software Engineering Project
        </p>

    </footer>

</body>
</html>
