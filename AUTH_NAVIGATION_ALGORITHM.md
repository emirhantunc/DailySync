# Firebase Auth Navigation Algoritması

## 🎯 Amaç
Kullanıcı Firebase ile giriş yapmışsa, uygulama açıldığında otomatik olarak HomeScreen'e yönlendirme yapılır. Giriş yapmamışsa SignInScreen gösterilir.

## 🔧 Nasıl Çalışır?

### 1. **Uygulama Başlatılırken (AppState.kt)**

```kotlin
@Composable
fun DailySyncNavGraph(navController: NavHostController) {
    val auth = remember { FirebaseAuth.getInstance() }
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    var initialRoute by remember { 
        mutableStateOf(
            if (isUserLoggedIn) Routes.Home.route 
            else Routes.SignInScreen.route
        ) 
    }
```

**Adımlar:**
1. Firebase Auth instance oluşturulur
2. `auth.currentUser != null` kontrolü yapılır
3. Kullanıcı giriş yapmışsa → `initialRoute = "home"`
4. Kullanıcı giriş yapmamışsa → `initialRoute = "sign_in"`
5. NavHost bu route ile başlatılır

### 2. **Auth Durumu Değişikliklerini Dinleme**

```kotlin
LaunchedEffect(Unit) {
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        isUserLoggedIn = firebaseAuth.currentUser != null
        
        if (!isUserLoggedIn && navController.currentDestination?.route != Routes.SignInScreen.route) {
            navController.navigate(Routes.SignInScreen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
    }
    auth.addAuthStateListener(authStateListener)
}
```

**Adımlar:**
1. AuthStateListener eklenir
2. Auth durumu değiştiğinde (giriş/çıkış) dinleyici tetiklenir
3. Kullanıcı çıkış yaptıysa → SignInScreen'e yönlendirilir
4. Tüm back stack temizlenir (kullanıcı geri tuşu ile giremez)

### 3. **SignIn Başarılı Olduğunda (SignInScreen.kt)**

```kotlin
LaunchedEffect(uiState.isSuccess) {
    if (uiState.isSuccess) {
        navController.navigate("home") {
            popUpTo("signIn") { inclusive = true }
        }
    }
}
```

**Adımlar:**
1. SignIn başarılı olduğunda `isSuccess = true` olur
2. Otomatik olarak Home ekranına yönlendirme yapılır
3. SignIn ekranı back stack'ten kaldırılır

### 4. **SignUp Başarılı Olduğunda (SignUpScreen.kt)**

```kotlin
LaunchedEffect(uiState.isSuccess) {
    if (uiState.isSuccess) {
        navController.navigate("home") {
            popUpTo(navController.graph.startDestinationId) { 
                inclusive = true 
            }
        }
    }
}
```

**Adımlar:**
1. SignUp başarılı olduğunda `isSuccess = true` olur
2. Otomatik olarak Home ekranına yönlendirme yapılır
3. Tüm auth ekranları back stack'ten kaldırılır

## 🔄 Akış Diyagramı

```
[Uygulama Başlatılır]
        ↓
[Firebase Auth Kontrolü]
        ↓
    ┌───────────┐
    │ Giriş var?│
    └─────┬─────┘
          │
    ┌─────┴─────┐
    │           │
   Evet        Hayır
    │           │
    ↓           ↓
[HomeScreen] [SignInScreen]
                │
                ├──→ [Giriş Yap] ──→ [HomeScreen]
                │
                └──→ [Kayıt Ol] ──→ [SignUpScreen] ──→ [HomeScreen]
```

## ✨ Özellikler

✅ **Otomatik Giriş:** Kullanıcı bir kez giriş yaptıktan sonra uygulama her açıldığında otomatik giriş yapar

✅ **Güvenli Çıkış:** Kullanıcı çıkış yaptığında otomatik olarak SignIn ekranına yönlendirilir

✅ **Back Stack Yönetimi:** Auth ekranlarına geri dönülemez (güvenlik)

✅ **Reaktif Yapı:** Firebase Auth durumu değiştiğinde UI otomatik güncellenir

✅ **Hızlı Başlangıç:** Giriş yapmış kullanıcılar SignIn ekranını görmez

## 🛡️ Güvenlik Notları

1. `popUpTo` ile back stack temizlenir → Kullanıcı geri tuşu ile auth ekranlarına dönemez
2. Auth durumu her değiştiğinde kontrol yapılır
3. Çıkış yapan kullanıcı otomatik olarak SignIn'e yönlendirilir
4. Firebase Auth instance uygulama boyunca tutarlı kalır

## 📝 Test Senaryoları

### Senaryo 1: İlk Kez Açan Kullanıcı
1. Uygulama açılır → SignInScreen gösterilir
2. Kullanıcı giriş yapar → HomeScreen'e yönlendirilir
3. Uygulama kapatılır
4. Uygulama tekrar açılır → Direkt HomeScreen gösterilir ✅

### Senaryo 2: Giriş Yapmış Kullanıcı
1. Uygulama açılır → Direkt HomeScreen gösterilir ✅
2. Kullanıcı çıkış yapar
3. Otomatik olarak SignInScreen'e yönlendirilir ✅

### Senaryo 3: Yeni Kayıt
1. SignUpScreen'de kayıt olunur
2. Başarılı kayıt sonrası direkt HomeScreen'e yönlendirilir ✅
3. Uygulama kapatılır
4. Uygulama açılır → Direkt HomeScreen gösterilir ✅

## 🚀 Sonuç

Artık kullanıcılar:
- Uygulama her açıldığında **otomatik giriş** yapar
- **Gereksiz SignIn ekranı** görmez
- **Güvenli çıkış** yapabilir
- **Sorunsuz navigasyon** deneyimi yaşar
