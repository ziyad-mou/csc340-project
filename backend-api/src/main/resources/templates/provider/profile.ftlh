<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller profile | PokeVerse</title>
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/provider.css">
</head>
<body>
    <main class="seller-main" id="content">
        <div class="page-heading">
            <h1>Seller profile</h1>
        </div>

        <section class="panel" aria-label="Shop information">
            <#if error??>
                <p role="alert">${error}</p>
            <#else>
                <h2>${storeName}</h2>
                <p>${email}</p>
                <p>${biography!""}</p>

                <a class="action-link" href="/seller/profile/${providerId?c}/edit">
                    Edit profile
                </a>

                <#if deleteError??>
                    <p role="alert">${deleteError}</p>
                </#if>

                <form method="post"
                      action="/seller/profile/${providerId?c}/delete"
                      onsubmit="return confirm('Remove this seller profile?')">
                    <button type="submit" class="danger">Remove profile</button>
                </form>
            </#if>
        </section>
    </main>
</body>
</html>