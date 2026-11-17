<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller inventory | PokeVerse</title>
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/provider.css">
</head>
<body>
    <main class="seller-main" id="content">
        <div class="page-heading">
            <h1>Seller inventory</h1>
            <a class="action-link" href="/seller/profile/${providerId?c}">
                Back to seller profile
            </a>
            <a class="button" href="/seller/profile/${providerId?c}/listings/new">
                New listing
            </a>        
        </div>

        <section class="panel" aria-label="Pack listings">
            <#if listings?has_content>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th scope="col">Pack name</th>
                                <th scope="col">Description</th>
                                <th scope="col">Price</th>
                                <th scope="col">Stock</th>
                                <th scope="col">Availability</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list listings as listing>
                                <tr>
                                    <td>${listing.name!""}</td>
                                    <td>${listing.description!""}</td>
                                    <td>
                                        <#if listing.price??>
                                            ${listing.price?string.currency}
                                        <#else>
                                            —
                                        </#if>
                                    </td>
                                    <td>${listing.stock!"—"}</td>
                                    <td>
                                        <#if !(listing.stock??)>
                                            Unknown
                                        <#elseif listing.stock gt 0>
                                            In stock
                                        <#else>
                                            Out of stock
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            <#else>
                <p>No pack listings yet.</p>
            </#if>
        </section>
    </main>
</body>
</html>