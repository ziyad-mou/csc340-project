<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New listing | PokeVerse</title>
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/provider.css">
</head>
<body>
    <main class="seller-main" id="content">
        <div class="page-heading">
            <h1>New pack listing</h1>
        </div>

        <form class="panel" method="post"
              action="/seller/profile/${providerId?c}/listings">
            <#if error??>
                <p role="alert">${error}</p>
            </#if>

            <label>
                Pack name
                <input name="name" required maxlength="150" value="${name!''}">
            </label>
            <label>
                Description
                <textarea name="description" required rows="4"
                          maxlength="255">${description!''}</textarea>
            </label>
            <label>
                Condition
                <input name="condition" required maxlength="50" value="${condition!''}">
            </label>
            <label>
                Price ($)
                <input name="price" type="number" required min="0"
                       max="9999999999.99" step="0.01" value="${(price?c)!''}">
            </label>
            <label>
                Stock quantity
                <input name="stock" type="number" required min="0"
                       max="2147483647" step="1" value="${(stock?c)!''}">
            </label>
            <label>
                Image URL (optional)
                <input name="imageUrl" maxlength="255" value="${imageUrl!''}">
            </label>

            <button type="submit">Create listing</button>
            <a href="/seller/profile/${providerId?c}/listings">Cancel</a>
        </form>
    </main>
</body>
</html>