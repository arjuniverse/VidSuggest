const API_BASE = "http://localhost:8080";

const el = {
  addVideoForm: document.getElementById("addVideoForm"),
  videosGrid: document.getElementById("videosGrid"),
  recommendedGrid: document.getElementById("recommendedGrid"),
  categoryFilter: document.getElementById("categoryFilter"),
  recommendedFor: document.getElementById("recommendedFor"),
  refreshBtn: document.getElementById("refreshBtn"),
};

function escapeHtml(s) {
  return String(s)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function cardTemplate(video) {
  const title = escapeHtml(video.title ?? "");
  const category = escapeHtml(video.category ?? "");
  const description = escapeHtml(video.description ?? "");
  const rating = (video.rating ?? 0).toFixed(1);
  const link = escapeHtml(video.youtubeLink ?? "#");

  return `
    <div class="card">
      <div class="card__title" title="${title}">${title}</div>
      <div class="card__meta">
        <div class="chip">${category || "Uncategorized"}</div>
        <div class="rating">Rating: <strong>${rating}</strong></div>
      </div>
      <div class="card__desc">${description}</div>
      <div class="card__actions">
        <a class="btn btn--watch" href="${link}" target="_blank" rel="noopener noreferrer">Watch on YouTube</a>
      </div>
    </div>
  `;
}

function renderGrid(gridEl, videos, emptyMessage) {
  if (!videos || videos.length === 0) {
    gridEl.innerHTML = `<div class="muted">${escapeHtml(emptyMessage || "No videos found.")}</div>`;
    return;
  }
  gridEl.innerHTML = videos.map(cardTemplate).join("");
}

async function fetchJson(url, options) {
  const res = await fetch(url, options);
  const data = await res.json().catch(() => null);

  if (!res.ok) {
    const message = data?.errors?.[0] || data?.message || `Request failed: ${res.status}`;
    throw new Error(message);
  }
  return data;
}

async function fetchAllVideos() {
  return fetchJson(`${API_BASE}/api/videos`);
}

async function fetchVideosByCategory(category) {
  const encoded = encodeURIComponent(category);
  return fetchJson(`${API_BASE}/api/videos/category/${encoded}`);
}

async function fetchRecommendedVideos(category) {
  const encoded = encodeURIComponent(category);
  return fetchJson(`${API_BASE}/api/videos/recommend?category=${encoded}`);
}

function setRecommendedStateText(text) {
  el.recommendedFor.textContent = text;
}

async function refreshCategoriesAndViews() {
  // Load all videos once so we can populate the category dropdown.
  const allVideos = await fetchAllVideos();
  const currentCategory = el.categoryFilter.value;

  const categories = Array.from(new Set(allVideos.map(v => (v.category || "").trim()).filter(Boolean)));
  categories.sort((a, b) => a.localeCompare(b));

  const selectedStillValid = currentCategory && categories.includes(currentCategory);

  el.categoryFilter.innerHTML = `<option value="">All categories</option>` +
    categories.map(c => `<option value="${escapeHtml(c)}">${escapeHtml(c)}</option>`).join("");

  // If current selection no longer exists, pick the first category.
  if (selectedStillValid) {
    el.categoryFilter.value = currentCategory;
  } else if (!currentCategory && categories.length > 0) {
    el.categoryFilter.value = categories[0];
  }

  const categoryToShow = el.categoryFilter.value;

  if (categoryToShow) {
    const videos = await fetchVideosByCategory(categoryToShow);
    renderGrid(el.videosGrid, videos, "No videos in this category yet.");

    const recommended = await fetchRecommendedVideos(categoryToShow);
    renderGrid(el.recommendedGrid, recommended, "No recommendations yet.");
    setRecommendedStateText(`Recommended for: ${categoryToShow}`);
  } else {
    renderGrid(el.videosGrid, allVideos, "No videos yet. Add one above!");
    el.recommendedGrid.innerHTML = "";
    setRecommendedStateText("Pick a category to see recommendations.");
  }
}

el.refreshBtn.addEventListener("click", async () => {
  try {
    await refreshCategoriesAndViews();
  } catch (e) {
    alert(e.message);
  }
});

el.categoryFilter.addEventListener("change", async () => {
  try {
    await refreshCategoriesAndViews();
  } catch (e) {
    alert(e.message);
  }
});

el.addVideoForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const payload = {
    title: document.getElementById("title").value.trim(),
    category: document.getElementById("category").value.trim(),
    description: document.getElementById("description").value.trim(),
    youtubeLink: document.getElementById("youtubeLink").value.trim(),
    rating: Number(document.getElementById("rating").value),
  };

  try {
    await fetchJson(`${API_BASE}/api/videos`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    el.addVideoForm.reset();
    await refreshCategoriesAndViews();
  } catch (e) {
    alert(e.message);
  }
});

async function init() {
  try {
    await refreshCategoriesAndViews();
  } catch (e) {
    alert(
      `Could not load videos. Make sure the backend is running on port 8080.\n\n${e.message}`
    );
  }
}

document.addEventListener("DOMContentLoaded", init);

