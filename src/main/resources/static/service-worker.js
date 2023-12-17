//const PRECACHE = 'precache-v2';
//const RUNTIME = 'runtime';
//
//// A list of local resources we always want to be cached.
//const PRECACHE_URLS = [
//  '/',
//  '/css/styles.css'
//];
//
//// The install handler takes care of precaching the resources we always need.
//self.addEventListener('install', event => {
//  event.waitUntil(
//    caches.open(PRECACHE)
//      .then(cache => cache.addAll(PRECACHE_URLS))
//      .then(self.skipWaiting())
//  );
//});
//
//// The activate handler takes care of cleaning up old caches.
//self.addEventListener('activate', event => {
//  const currentCaches = [PRECACHE, RUNTIME];
//  event.waitUntil(
//    caches.keys().then(cacheNames => {
//      return cacheNames.filter(cacheName => !currentCaches.includes(cacheName));
//    }).then(cachesToDelete => {
//      return Promise.all(cachesToDelete.map(cacheToDelete => {
//        return caches.delete(cacheToDelete);
//      }));
//    }).then(() => self.clients.claim())
//  );
//});
//
//// The fetch handler serves responses for same-origin resources from a cache.
//// If no response is found, it populates the runtime cache with the response
//// from the network before returning it to the page.
//self.addEventListener('fetch', event => {
//  // Skip cross-origin requests, like those for Google Analytics.
//  if (event.request.url.startsWith(self.location.origin)) {
//    event.respondWith(
//      caches.match(event.request).then(cachedResponse => {
//        if (cachedResponse) {
//          return cachedResponse;
//        }
//
//        return caches.open(RUNTIME).then(cache => {
//          return fetch(event.request).then(response => {
//            // Put a copy of the response in the runtime cache.
//            return cache.put(event.request, response.clone()).then(() => {
//              return response;
//            });
//          });
//        });
//      })
//    );
//  }
//});


//var cacheName = 'v1.0';
//
//var cacheAssets = [
//    '/',
//    '/js/scripts.js',
//    '/css/styles.css'
//]
//
//// installation
//self.addEventListener('install', e => {
//      console.log('Service Worker: Installed');
//      e.waitUntil(
//            caches
//              .open(cacheName)
//              .then(cache => {
//                console.log('Service Worker: Caching Files');
//                cache.addAll(cacheAssets);
//              })
//              .then(() => self.skipWaiting())
//          );
//});
//
//
//self.addEventListener('fetch', (event) => {
//  event.respondWith(
//    (async function () {
//      try {
//        return await fetch(event.request);
//      } catch (err) {
//        return caches.match(event.request);
//      }
//    })(),
//  );
//});


self.addEventListener('fetch', (event) => {
  event.respondWith(
    (async function () {
      // Try the cache
      const cachedResponse = await caches.match(event.request);
      if (cachedResponse) return cachedResponse;

      try {
        // Fall back to network
        return await fetch(event.request);
      } catch (err) {
        // If both fail, show a generic fallback:
        return caches.match('/offline.html');
        // However, in reality you'd have many different
        // fallbacks, depending on URL & headers.
        // Eg, a fallback silhouette image for avatars.
      }
    })(),
  );
});