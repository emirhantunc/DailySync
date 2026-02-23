# Like/Unlike Sistemi Kullanım Kılavuzu

## Genel Bakış
Uygulamamızda artık **Feed**, **Post** ve **Thought** olmak üzere üç farklı içerik türü için tek bir fonksiyonla çalışan genel bir like/unlike sistemi bulunmaktadır.

## ContentType Enum
```kotlin
enum class ContentType(val collectionName: String, val likeFieldName: String) {
    POST("posts", "likedPosts"),
    THOUGHT("thoughts", "likedThoughts"),
    FEED("feeds", "likedFeeds")
}
```

## Repository Fonksiyonları

### Like İçerik
```kotlin
suspend fun likeContent(contentId: String, contentType: ContentType): Result<Unit>
```

### Unlike İçerik
```kotlin
suspend fun unlikeContent(contentId: String, contentType: ContentType): Result<Unit>
```

## Kullanım Örnekleri

### ViewModel'de Kullanım

```kotlin
// Post beğenme
viewModel.likeContent(postId, ContentType.POST)

// Thought beğenme
viewModel.likeContent(thoughtId, ContentType.THOUGHT)

// Feed beğenme
viewModel.likeContent(feedId, ContentType.FEED)

// Post beğeniyi kaldırma
viewModel.unlikeContent(postId, ContentType.POST)

// Thought beğeniyi kaldırma
viewModel.unlikeContent(thoughtId, ContentType.THOUGHT)

// Feed beğeniyi kaldırma
viewModel.unlikeContent(feedId, ContentType.FEED)
```

### Toggle Kullanımı (Beğenilmişse kaldır, değilse beğen)

```kotlin
fun toggleLike(contentId: String, contentType: ContentType, isLiked: Boolean) {
    if (isLiked) {
        unlikeContent(contentId, contentType)
    } else {
        likeContent(contentId, contentType)
    }
}
```

## Firebase Yapısı

Sistem otomatik olarak:
1. İlgili koleksiyondaki (`posts`, `thoughts`, veya `feeds`) belgenin `likeNumber` alanını günceller
2. Kullanıcının belgesindeki ilgili like alanını (`likedPosts`, `likedThoughts`, veya `likedFeeds`) günceller
3. **ÖNEMLİ**: Eğer POST veya THOUGHT beğeniliyorsa, ilgili FEED dökümanındaki `likeNumber` da otomatik olarak güncellenir! (Feeds senkronizasyonu)

### Feeds Senkronizasyonu
- Kullanıcı bir **thought** beğendiğinde:
  - ✅ `thoughts` koleksiyonunda ilgili dökümanın `likeNumber`'ı artar
  - ✅ `feeds` koleksiyonunda `targetId` bu thought'a eşit olan dökümanın `likeNumber`'ı da artar
  - ✅ Kullanıcının `likedThoughts` listesine thought ID'si eklenir

- Kullanıcı bir **post** beğendiğinde:
  - ✅ `posts` koleksiyonunda ilgili dökümanın `likeNumber`'ı artar
  - ✅ `feeds` koleksiyonunda `targetId` bu post'a eşit olan dökümanın `likeNumber`'ı da artar
  - ✅ Kullanıcının `likedPosts` listesine post ID'si eklenir

- Kullanıcı direkt **feed** beğendiğinde:
  - ✅ Sadece `feeds` koleksiyonunda ilgili dökümanın `likeNumber`'ı artar
  - ✅ Kullanıcının `likedFeeds` listesine feed ID'si eklenir

## Avantajlar

✅ Tek bir fonksiyon ile tüm içerik türlerini yönetme
✅ Kod tekrarını önleme
✅ Tip güvenli (ContentType enum ile)
✅ Kolay bakım ve genişletme
✅ Otomatik koleksiyon ve alan adı yönetimi
✅ **Feeds ve orijinal içerik arasında otomatik senkronizasyon**
✅ Tutarlı veri durumu (posts/thoughts ile feeds her zaman senkronize)

