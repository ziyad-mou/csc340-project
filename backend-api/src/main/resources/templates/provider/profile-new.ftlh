<#assign editing = providerId??>
<#assign heading = editing?then("Edit seller profile", "Create seller profile")>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${heading} | PokeVerse</title>
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/provider.css">
</head>
<body>
    <main class="seller-main" id="content">
        <div class="page-heading">
            <h1>${heading}</h1>
        </div>

            <#if notice??>
                <p role="status">${notice}</p>
            </#if>
            
        <form class="panel" method="post"
              action="/seller/profile<#if editing>/${providerId?c}/edit</#if>">
            <#if error??>
                <p role="alert">${error}</p>
            </#if>

            <label>
                Shop name
                <input name="storeName" required maxlength="100"
                       value="${storeName!''}">
            </label>
            <label>
                Contact email
                <input name="email" type="email" required maxlength="254"
                       value="${email!''}">
            </label>
            <#if !editing>
                <label>
                    Password
                    <input name="password" type="password" required
                           minlength="8" maxlength="72" autocomplete="new-password">
                </label>
            </#if>
            <label>
                About your shop
                <textarea name="biography" rows="4" maxlength="2000">${biography!''}</textarea>
            </label>

            <button type="submit">${editing?then("Save changes", "Create profile")}</button>

            <#if editing>
                <a href="/seller/profile/${providerId?c}">Cancel</a>
            </#if>
        </form>
    </main>
</body>
</html>