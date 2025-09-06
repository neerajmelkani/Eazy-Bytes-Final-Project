const API_BASE = 'http://localhost:8080/api';

async function api(path){ const res = await fetch(API_BASE+path); return res.json(); }

async function loadInitial(){
  const [news, sponsored, trending] = await Promise.all([api('/news'), api('/sponsored'), api('/trending')]);
  renderTop(news[0]);
  renderList(news);
  renderSponsored(sponsored);
  renderTrending(trending);
}

function renderTop(item){
  const el = document.getElementById('topHeadline');
  el.innerHTML = `<img src="${item.image||'https://via.placeholder.com/220x120'}" alt=""><div><h2>${item.title}</h2><p class='muted'>${item.source} — ${new Date(item.publishedAt).toLocaleString()}</p><p>${item.description}</p></div>`;
}

function renderList(items){
  const list = document.getElementById('newsList');
  list.innerHTML = '';
  items.slice(1).forEach(item => {
    const a = document.createElement('div'); a.className='article card';
    a.innerHTML = `<img src="${item.image||'https://via.placeholder.com/120x80'}"><div class='meta'><h4>${item.title}</h4><p>${item.description}</p><small class='muted'>${item.source}</small></div>`;
    a.onclick = () => window.open(item.url || '#', '_blank');
    list.appendChild(a);
  });
}

function renderSponsored(items){
  const el = document.getElementById('sponsored');
  el.innerHTML = items.map(s=>`<div style="padding:8px 0"><a href="${s.url||'#'}" target="_blank">${s.title}</a><div style="color:var(--muted);font-size:13px">${s.desc||''}</div></div>`).join('');
}

function renderTrending(items){
  const el = document.getElementById('trending');
  el.innerHTML = items.map(t=>`<li>${t}</li>`).join('');
}

document.getElementById('searchBtn').addEventListener('click', async ()=>{
  const q = document.getElementById('search').value.trim();
  const res = await api('/news?q='+encodeURIComponent(q));
  renderTop(res[0]||{});
  renderList(res);
});

document.getElementById('loadMore').addEventListener('click', async ()=>{
  const res = await api('/news?offset=5');
  renderList(res);
});

// nav category clicks
document.querySelectorAll('.navlinks a').forEach(el=> el.addEventListener('click', async (e)=>{
  e.preventDefault();
  const cat = el.dataset.cat || '';
  const res = await api('/news?cat='+cat);
  renderTop(res[0]||{});
  renderList(res);
}));

loadInitial();
