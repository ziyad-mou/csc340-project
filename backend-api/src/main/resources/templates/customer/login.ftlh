
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login | PokeVerse</title>

    <link rel="icon" type="image/png" href="/images/poke.png">

    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/auth.css">
</head>

<body>

    <!-- =========================
         NAVBAR
    ========================== -->
    <header class="navbar">

        <a href="/customer/search" class="logo">
            <img
                src="/images/PokeVerse-logo.png"
                alt="PokeVerse Logo"
            >
        </a>

        <nav>
            <a href="/customer/search">Shop Cards</a>
            <a href="/customer/cart">Cart</a>
        </nav>

        <div class="nav-buttons">

            <#if loggedIn?? && loggedIn>

                <!-- LOGGED-IN CUSTOMER -->
                <form action="/customer/logout" method="post">

                    <button type="submit" class="login-btn">
                        Log Out
                    </button>

                </form>

            <#else>

                <!-- GUEST -->
                <a href="/customer/login"
                   class="login-btn"
                   aria-current="page">
                    Login
                </a>

                <a href="/register" class="signup-btn">
                    Sign Up
                </a>

            </#if>

        </div>

    </header>


    <!-- =========================
         LOGIN PAGE
    ========================== -->
    <main class="auth-page">

        <section class="auth-card">

            <p class="auth-eyebrow">
                WELCOME BACK
            </p>

            <h1>Login to PokeVerse</h1>

            <p class="auth-description">
                Sign in to continue exploring the
                Pokémon card marketplace.
            </p>


            <!-- ERROR MESSAGE -->
            <#if error??>

                <div class="auth-message auth-message--error"
                     role="alert">
                    ${error}
                </div>

            </#if>


            <!-- SUCCESS MESSAGE -->
            <#if success??>

                <div class="auth-message auth-message--success"
                     role="status">
                    ${success}
                </div>

            </#if>


            <!-- =========================
                 CUSTOMER LOGIN FORM
            ========================== -->
            <form
                class="auth-form"
                action="/customer/login"
                method="post">

                <!-- EMAIL -->
                <div class="form-group">

                    <label for="login-email">
                        Email
                    </label>

                    <input
                        type="email"
                        id="login-email"
                        name="email"
                        placeholder="Enter your email"
                        autocomplete="username"
                        required
                    >

                </div>


                <!-- PASSWORD -->
                <div class="form-group">

                    <label for="login-password">
                        Password
                    </label>

                    <input
                        type="password"
                        id="login-password"
                        name="password"
                        placeholder="Enter your password"
                        autocomplete="current-password"
                        required
                    >

                </div>


                <!-- LOGIN BUTTON -->
                <button
                    type="submit"
                    class="auth-submit">

                    Login

                </button>

            </form>


            <!-- CREATE ACCOUNT -->
            <p class="auth-switch">

                Don't have an account?

                <a href="/register">
                    Create Account
                </a>

            </p>

        </section>

    </main>


    <!-- =========================
         FOOTER
    ========================== -->
    <footer>

        <h3>PokeVerse</h3>

        <p>Buy. Sell. Collect.</p>

        <p>CSC 340 Software Engineering Project</p>

    </footer>

</body>
</html>
