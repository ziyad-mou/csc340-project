"use strict";
// Milestone 2: local browser state only. No real accounts, sales, or payments.
const key = "pokeverse-seller-demo-v1";
const seed = {
  products: [
    {
      id: "ex",
      name: "30th Celebration Ex Box",
      description: "Sealed Pokémon TCG collection box.",
      price: 49.99,
      stock: 12,
      category: "Sealed box",
      image: "ex-box.png",
    },
    {
      id: "elite",
      name: "Elite Trainer Box",
      description: "Sealed box for your next collection.",
      price: 99.99,
      stock: 4,
      category: "Sealed box",
      image: "elite.png",
    },
    {
      id: "tech",
      name: "Tech Sticker Collection",
      description: "Pokémon packs and collectible stickers.",
      price: 34.99,
      stock: 18,
      category: "Collection",
      image: "tech-sticker.png",
    },
    {
      id: "poster",
      name: "Poster Collection",
      description: "Sealed Pokémon poster collection.",
      price: 24.99,
      stock: 0,
      category: "Collection",
      image: "poster-collection.png",
    },
  ],
  profile: {
    owner: "Anuraj Subedi",
    shop: "Anuraj’s PokeVerse Shop",
    email: "seller@example.com",
    bio: "Sealed Pokémon TCG products for collectors. Explore our boxes, packs, and collections.",
  },
  replies: {},
};
let state = structuredClone(seed);
try {
  const saved = JSON.parse(localStorage.getItem(key));
  if (saved && Array.isArray(saved.products) && saved.replies) state = saved;
} catch {
  /* Start with sample data when storage is unavailable. */
}
const $ = (s) => document.querySelector(s);
const money = (n) =>
  new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(
    n,
  );
const esc = (s) =>
  String(s).replace(
    /[&<>"']/g,
    (c) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        c
      ],
  );
function notice(message) {
  $("#notice").textContent = message;
  $("#notice").hidden = false;
}
function save() {
  try {
    localStorage.setItem(key, JSON.stringify(state));
    return true;
  } catch {
    notice(
      "Your browser could not save this change. Enable browser storage and try again.",
    );
    return false;
  }
}
function status(p) {
  return `<span class="badge ${p.stock === 0 ? "out" : p.stock <= 5 ? "low" : ""}">${p.stock === 0 ? "Out of stock" : p.stock <= 5 ? "Low stock" : "In stock"}</span>`;
}
function metric(label, value) {
  return `<div class="metric"><span>${label}</span><strong>${value}</strong></div>`;
}
const page = document.body.dataset.page;
if (page === "index") {
  $("#dashboard-metrics").innerHTML =
    metric("Total listings", state.products.length) +
    metric(
      "Units in stock",
      state.products.reduce((s, p) => s + p.stock, 0),
    ) +
    metric(
      "Low-stock listings",
      state.products.filter((p) => p.stock > 0 && p.stock <= 5).length,
    ) +
    metric("Sales · last 7 days", money(1379.76));
  $("#dashboard-products").innerHTML =
    state.products
      .slice(0, 4)
      .map(
        (p) =>
          `<a class="product-row" href="create-listing.html?id=${encodeURIComponent(p.id)}"><img src="../images/${esc(p.image)}" alt=""><span><strong>${esc(p.name)}</strong><br><small>${money(p.price)} · ${p.stock} available</small></span>${status(p)}</a>`,
      )
      .join("") || "<p>No products yet. Create your first listing.</p>";
}
if (page === "inventory") {
  function render() {
    const query = $("#search").value.toLowerCase(),
      filter = $("#stock-filter").value;
    const products = state.products.filter(
      (p) =>
        p.name.toLowerCase().includes(query) &&
        (filter === "all" ||
          (filter === "available" && p.stock > 0) ||
          (filter === "low" && p.stock > 0 && p.stock <= 5) ||
          (filter === "out" && p.stock === 0)),
    );
    $("#inventory-rows").innerHTML = products
      .map(
        (p) =>
          `<tr><td><div class="product-cell"><img src="../images/${esc(p.image)}" alt=""><span>${esc(p.name)}</span></div></td><td>${money(p.price)}</td><td><form data-stock="${esc(p.id)}"><input class="stock-input" type="number" min="0" max="99999" step="1" required value="${p.stock}" aria-label="Stock for ${esc(p.name)}"><button type="submit">Update</button></form></td><td>${status(p)}</td><td><a class="button secondary" href="create-listing.html?id=${encodeURIComponent(p.id)}">Edit</a><button class="danger" data-delete="${esc(p.id)}" aria-label="Delete ${esc(p.name)}">Delete</button></td></tr>`,
      )
      .join("");
    $("#inventory-empty").hidden = products.length > 0;
  }
  $("#search").addEventListener("input", render);
  $("#stock-filter").addEventListener("change", render);
  $("#inventory-rows").addEventListener("submit", (e) => {
    e.preventDefault();
    const p = state.products.find((p) => p.id === e.target.dataset.stock);
    const n = Number(e.target.querySelector("input").value);
    if (!p || !Number.isInteger(n) || n < 0) return;
    p.stock = n;
    if (save()) {
      render();
      notice(`Stock updated for ${p.name}.`);
    }
  });
  $("#inventory-rows").addEventListener("click", (e) => {
    const b = e.target.closest("[data-delete]");
    if (!b) return;
    const p = state.products.find((p) => p.id === b.dataset.delete);
    if (confirm(`Delete “${p.name}” from this demo?`)) {
      state.products = state.products.filter((x) => x.id !== p.id);
      if (save()) {
        render();
        notice("Listing deleted.");
      }
    }
  });
  render();
  if (new URLSearchParams(location.search).has("saved"))
    notice("Listing saved. Your inventory is up to date.");
}
if (page === "create-listing") {
  const form = $("#listing-form"),
    id = new URLSearchParams(location.search).get("id"),
    existing = state.products.find((p) => p.id === id);
  if (id && !existing) {
    notice("This listing no longer exists. You can create a new one.");
  }
  if (existing) {
    $("h1").textContent = "Edit listing";
    document.title = "Edit listing | PokeVerse";
    for (const field of [
      "name",
      "description",
      "price",
      "stock",
      "category",
      "image",
    ])
      form.elements[field].value = existing[field];
  }
  function preview() {
    const f = form.elements;
    $("#preview-name").textContent = f.name.value || "Your product name";
    $("#preview-description").textContent =
      f.description.value || "Your product description will appear here.";
    $("#preview-price").textContent = money(Number(f.price.value) || 0);
    $("#preview-stock").textContent = `${Number(f.stock.value) || 0} available`;
    $("#preview-image").src = "../images/" + f.image.value;
  }
  form.addEventListener("input", preview);
  preview();
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const f = form.elements;
    if (!f.name.value.trim() || !f.description.value.trim()) {
      notice("Enter a product name and description.");
      return;
    }
    const product = {
      id: existing ? existing.id : "p" + Date.now(),
      name: f.name.value.trim(),
      description: f.description.value.trim(),
      price: Number(f.price.value),
      stock: Number(f.stock.value),
      category: f.category.value,
      image: f.image.value,
    };
    if (existing)
      state.products = state.products.map((p) => (p.id === id ? product : p));
    else state.products.push(product);
    if (save()) location.href = "inventory.html?saved=1";
  });
}
if (page === "statistics") {
  function render() {
    const month = $("#period").value === "30";
    const units = month ? [28, 18, 26] : [10, 6, 8];
    const prices = [49.99, 99.99, 34.99],
      names = ["Ex Box", "Elite Trainer Box", "Tech Sticker Collection"];
    const totals = units.map((n, i) => n * prices[i]);
    $("#sales-metrics").innerHTML =
      metric("Revenue", money(totals.reduce((a, b) => a + b, 0))) +
      metric(
        "Units sold",
        units.reduce((a, b) => a + b, 0),
      ) +
      metric("Customers", month ? 58 : 20) +
      metric("Repeat customers", month ? "24%" : "20%");
    $("#sales-bars").innerHTML = names
      .map(
        (name, i) =>
          `<div class="bar-row"><div class="bar-label"><span>${name} · ${units[i]} sold</span><strong>${money(totals[i])}</strong></div><div class="bar-track" aria-hidden="true"><div class="bar" style="width:${(totals[i] / Math.max(...totals)) * 100}%"></div></div></div>`,
      )
      .join("");
  }
  $("#period").addEventListener("change", render);
  render();
}
if (page === "reviews") {
  const reviews = [
    {
      id: "r1",
      buyer: "Alex M.",
      product: "Ex Box",
      stars: 5,
      date: "September 22, 2026",
      text: "Arrived sealed and well packed. Great addition to my collection!",
    },
    {
      id: "r2",
      buyer: "Jordan L.",
      product: "Elite Trainer Box",
      stars: 4,
      date: "September 20, 2026",
      text: "The box looks great. I would appreciate more shipping updates.",
    },
    {
      id: "r3",
      buyer: "Sam K.",
      product: "Tech Sticker Collection",
      stars: 5,
      date: "September 18, 2026",
      text: "Exactly what I was looking for. Thank you!",
    },
  ];
  function render() {
    const filter = $("#review-filter").value;
    const visible = reviews.filter(
      (r) =>
        filter === "all" ||
        (filter === "answered" && state.replies[r.id]) ||
        (filter === "unanswered" && !state.replies[r.id]),
    );
    $("#review-list").innerHTML =
      visible
        .map(
          (r) =>
            `<article class="panel review"><div class="review-header"><h2>${r.buyer}</h2><span class="stars" aria-label="${r.stars} out of 5 stars">${"★".repeat(r.stars)}${"☆".repeat(5 - r.stars)}</span></div><p class="muted">${r.product} · ${r.date}</p><p>${r.text}</p>${state.replies[r.id] ? `<div class="reply"><strong>Your reply</strong><br>${esc(state.replies[r.id])}</div>` : ""}<form data-review="${r.id}"><label>${state.replies[r.id] ? "Edit your reply" : "Reply to this buyer"}<textarea name="reply" required maxlength="500" rows="2">${esc(state.replies[r.id] || "")}</textarea></label><button type="submit">${state.replies[r.id] ? "Save reply" : "Post reply"}</button></form></article>`,
        )
        .join("") ||
      '<section class="panel"><p>No reviews in this category.</p></section>';
  }
  $("#review-filter").addEventListener("change", render);
  $("#review-list").addEventListener("submit", (e) => {
    e.preventDefault();
    const reply = e.target.elements.reply.value.trim();
    if (!reply) {
      notice("Write a reply before posting.");
      return;
    }
    state.replies[e.target.dataset.review] = reply;
    if (save()) {
      render();
      notice("Your reply has been saved.");
    }
  });
  render();
}
if (page === "profile") {
  const form = $("#profile-form");
  function render() {
    const profile = state.profile;
    for (const name of ["owner", "shop", "email", "bio"])
      form.elements[name].value = profile?.[name] || "";
    $("#shop-name").textContent = profile?.shop || "Create your seller profile";
    $("#shop-bio").textContent =
      profile?.bio || "Add your information to introduce your shop.";
    $("#shop-owner").textContent = profile ? "Owned by " + profile.owner : "";
    $("#delete-profile").hidden = !profile;
    form.querySelector("[type=submit]").textContent = profile
      ? "Save profile"
      : "Create profile";
  }
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const profile = Object.fromEntries(new FormData(form));
    if (Object.values(profile).some((v) => !v.trim())) {
      notice("Complete every profile field.");
      return;
    }
    state.profile = profile;
    if (save()) {
      render();
      notice("Seller profile saved.");
    }
  });
  $("#delete-profile").addEventListener("click", () => {
    if (
      confirm(
        "Remove your seller profile from this demo? Your demo listings will remain.",
      )
    ) {
      state.profile = null;
      if (save()) {
        render();
        notice("Profile removed. Fill in the form to create a new one.");
      }
    }
  });
  render();
}
