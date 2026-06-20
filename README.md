# 📔 Daftari (دفتري)

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

**دفتري - تطبيق شخصي متكامل لتنظيم حياتك وإدارة أيامك** 🚀

[المميزات](#-المميزات) • [التثبيت](#-التثبيت) • [البدء السريع](#-البدء-السريع) • [المساهمة](#-المساهمة)

</div>

---

## 📱 نبذة عن التطبيق

**دفتري** هو تطبيق Android متقدم مصمم لمساعدتك على تنظيم كل جوانب حياتك في تطبيق واحد متكامل. يجمع بين الإنتاجية والتعليم والهوايات والتطوير الذاتي مع واجهة حديثة وسهلة الاستخدام.

يستخدم التطبيق **تقنية Gemini AI** لتقديم اقتراحات ذكية وتحليلات شخصية لمساعدتك على تحقيق أهدافك.

---

## ✨ المميزات

### 👤 **الملف الشخصي**
- إنشاء ملف شخصي فريد بصورتك واسمك
- كتابة نبذة شخصية عن نفسك
- تسجيل هواياتك واهتماماتك

### 📝 **اليوميات (Journal)**
- تسجيل أحداث اليوم يومياً
- إضافة صور وملاحظات
- البحث والفلترة حسب التواريخ
- تنظيم اليوميات بـ Tags و Labels

### ✅ **المهام والأهداف**
- إنشاء قوائم مهام يومية وطويلة الأجل
- تتبع التقدم والإنجازات
- تحديد الأولويات والمواعيد النهائية
- تصنيفات ذكية للمهام

### 💰 **إدارة الميزانية**
- تسجيل النفقات والدخل
- تحليل الإنفاق بـ Dashboard تفاعلي
- تقارير مالية شهرية وسنوية
- تنبيهات للإنفاق الزائد

### 🎨 **الهوايات والاه��مامات**
- تتبع أوقات ممارسة هواياتك
- مشاركة تطورك وإنجازاتك
- مكتبة هواياتك الشخصية

### 📚 **التطوير الذاتي والتعليم**
- متابعة الدورات التعليمية
- تسجيل ملاحظات الدراسة
- تحديد أهداف التعلم
- مقالات ونصائح موصى بها

### 🤖 **Gemini AI المدمج**
- تحليل ذكي لعاداتك
- اقتراحات شخصية للتحسين
- إكمال تلقائي للملاحظات
- رؤى بناءة عن حياتك

### 🔐 **الخصوصية والأمان**
- تشفير البيانات المحلية
- مزامنة آمنة مع Firebase
- التحكم الكامل في بيانات الخصوصية

---

## 🛠️ المتطلبات التقنية

### الحد الأدنى من الإمكانيات
- **Android:** 7.0 (API Level 24) أو أحدث
- **RAM:** 2GB على الأقل
- **التخزين:** 50MB مساحة حرة

### الإصدار المستهدف
- **Android:** 15 (API Level 36)
- **Java:** 11 أو أحدث
- **Kotlin:** 1.9+

---

## 🚀 التثبيت

### المتطلبات المسبقة
```bash
# تأكد من تثبيت:
- Android Studio (Jellyfish أو أحدث)
- Java Development Kit (JDK 11+)
- Git
```

### خطوات التثبيت

**1. استنساخ المستودع**
```bash
git clone https://github.com/ahmed-332216/-.git
cd -
```

**2. فتح المشروع في Android Studio**
```bash
# أو افتح المجلد مباشرة عبر Android Studio
```

**3. إعداد متغيرات البيئة**
```bash
# انسخ ملف البيئة
cp .env.example .env

# حرّر الملف وأضف بيانات اعتماد Firebase والمفاتيح الخاصة بك
nano .env
```

**4. تثبيت الحزم**
```bash
# يتم تلقائياً عند فتح المشروع في Android Studio
./gradlew build
```

**5. تشغيل التطبيق**
```bash
# على جهاز محاكي
./gradlew installDebug

# أو اضغط على زر "Run" في Android Studio
```

---

## 📋 البدء السريع

### إعداد Firebase

1. انتقل إلى [Firebase Console](https://console.firebase.google.com)
2. أنشئ مشروع جديد أو استخدم مشروع موجود
3. أضف تطبيق Android إلى مشروعك
4. حمل ملف `google-services.json`
5. ضع الملف في مجلد `app/`

### إعداد Gemini API

1. انتقل إلى [Google AI Studio](https://aistudio.google.com/app/apikey)
2. أنشأ مفتاح API جديد
3. أضفه في ملف `.env`:
```env
GEMINI_API_KEY=your_api_key_here
FIREBASE_PROJECT_ID=your_firebase_project_id
```

### الاستخدام الأول

1. **أنشئ حسابك الشخصي**
   - أدخل اسمك واختر صورة ملف شخصي
   - اكتب نبذة قصيرة عنك

2. **أضف هواياتك واهتماماتك**
   - حدد أكثر من 3 هوايات
   - سيستخدمها التطبيق للتوصيات الذكية

3. **ابدأ بإنشاء مدخلاتك الأولى**
   - اكتب إدخالة يومية
   - أضف مهمة جديدة
   - سجل نفقة

4. **استكشف تحليلات AI**
   - تحقق من الرؤى الشخصية
   - اقرأ التوصيات المخصصة

---

## 📁 هيكل المشروع

```
daftari/
├── app/                              # تطبيق Android الرئيسي
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aistudio/daftari/
│   │   │   │   ├── ui/               # Jetpack Compose UI
│   │   │   │   ├── viewmodel/        # ViewModel classes
│   │   │   │   ├── data/             # Data models و Repositories
│   │   │   │   ├── database/         # Room database
│   │   │   │   ├── network/          # API integration
│   │   │   │   └── utils/            # Utility functions
│   │   │   └── res/                  # Resources
│   │   ├── test/                     # Unit Tests
│   │   └── androidTest/              # Instrumented Tests
│   └── build.gradle.kts              # App-level build config
│
├── gradle/                           # Gradle wrapper
├── build.gradle.kts                  # Project-level build config
├── settings.gradle.kts               # Gradle settings
├── gradle.properties                 # Gradle properties
├── .env.example                      # متغيرات البيئة (مثال)
└── README.md                         # هذا الملف
```

---

## 🔧 الإعدادات والتكوين

### ملف gradle.properties

تم تحسين الأداء بالإعدادات التالية:
```properties
org.gradle.jvmargs=-Xmx4g              # تحديد ذاكرة JVM
org.gradle.parallel=true               # بناء متوازي
org.gradle.caching=true                # تخزين مؤقت للبناء
org.gradle.configuration-cache=true    # تخزين إعدادات البناء
kotlin.compiler.execution.strategy=in-process
```

### ملف .env

��نشئ ملف `.env` في جذر المشروع:
```env
# Gemini AI
GEMINI_API_KEY=your_gemini_api_key

# Firebase
FIREBASE_PROJECT_ID=your_project_id
FIREBASE_WEB_API_KEY=your_web_api_key

# Database
DATABASE_NAME=daftari_db
```

---

## 🧪 الاختبارات

### تشغيل جميع الاختبارات
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
./gradlew testDebugUnitTest       # Debug unit tests
```

### اختبار UI مع Roborazzi
```bash
./gradlew verifyRoborazziDebug    # التحقق من لقطات الشاشة
./gradlew recordRoborazziDebug    # تسجيل لقطات شاشة جديدة
```

---

## 📦 البناء والإطلاق

### بناء APK
```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

### بناء AAB (للـ Play Store)
```bash
./gradlew bundleRelease
```

**ملاحظة:** قبل البناء للإطلاق، تأكد من إعداد keystore وتوقيع التطبيق بشكل صحيح.

---

## 📚 المستندات والموارد

### التوثيق الرسمية
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Android Architecture](https://developer.android.com/guide/topics/architecture)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Room Database](https://developer.android.com/training/data-storage/room)

### الموارد الإضافية
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit](https://square.github.io/retrofit/)
- [Moshi](https://github.com/square/moshi)

---

## 🐛 حل المشاكل الشائعة

### المشكلة: Build Fails مع خطأ Gradle
**الحل:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### المشكلة: Firebase Google Services Error
**الحل:**
1. تأكد من تحميل `google-services.json` من Firebase Console
2. ضع الملف في مجلد `app/`
3. أعد بناء المشروع

### المشكلة: Gemini API Key غير صحيح
**الحل:**
1. تحقق من ملف `.env`
2. تأكد من نسخ المفتاح بشكل صحيح
3. أعد تشغيل التطبيق

### المشكلة: Keystore أو Password مفقودة
**الحل:**
```bash
# تعيين متغيرات البيئة
export KEYSTORE_PATH=/path/to/keystore.jks
export STORE_PASSWORD=your_password
export KEY_PASSWORD=your_key_password
```

---

## 🤝 المساهمة

نرحب بمساهماتك! إليك كيفية ��لمساهمة:

### خطوات المساهمة

1. **Fork المستودع**
```bash
# انسخ المستودع إلى حسابك
```

2. **أنشئ فرع للميزة**
```bash
git checkout -b feature/amazing-feature
```

3. **قم بالتغييرات والاختبارات**
```bash
# عدّل الملفات
./gradlew test
```

4. **Commit التغييرات**
```bash
git commit -m "feat: إضافة ميزة رائعة"
```

5. **Push للفرع**
```bash
git push origin feature/amazing-feature
```

6. **افتح Pull Request**
   - انتقل إلى المستودع الأصلي
   - اضغط على "New Pull Request"
   - اختر فرعك وأضف وصفاً

### معايير المساهمة
- ✅ اتبع نمط كود Kotlin الرسمي
- ✅ أضف اختبارات لكل ميزة جديدة
- ✅ وثق تغييراتك بوضوح
- ✅ استخدم رسائل commit واضحة

---

## 📝 رسائل Commit

استخدم هذا الشكل لرسائل commit:

```
feat: إضافة ميزة جديدة
fix: إصلاح خطأ معين
docs: تحديث التوثيق
style: تنسيق الكود (بدون تغيير الوظيفة)
refactor: إعادة هيكلة كود بدون تغيير السلوك
test: إضافة أو تحديث الاختبارات
chore: تحديثات البناء والتبعيات
```

---

## 📄 الترخيص

هذا المشروع مرخص تحت [MIT License](LICENSE) - انظر ملف LICENSE للتفاصيل.

---

## 👨‍💻 المطورون

**Ahmed** - المطور الرئيسي
- GitHub: [@ahmed-332216](https://github.com/ahmed-332216)
- البريد الإلكتروني: aaahhhmmm987654321@gmail.com

---

## 💬 الدعم والتواصل

### الحصول على المساعدة

- 🐛 **للأخطاء:** [فتح Issue جديد](https://github.com/ahmed-332216/-/issues)
- 💡 **للاقتراحات:** [ناقش الفكرة](https://github.com/ahmed-332216/-/discussions)
- 📧 **البريد الإلكتروني:** aaahhhmmm987654321@gmail.com

---

## 🗺️ خارطة الطريق (Roadmap)

- [ ] 🎨 دعم الثيمات المظلمة واللاحقة
- [ ] 🌍 دعم لغات متعددة (i18n)
- [ ] 📊 تقارير متقدمة و Analytics
- [ ] 🔔 إشعارات ذكية وتنبيهات
- [ ] 🎯 مزامنة سحابية كاملة
- [ ] 🔐 المصادقة متعددة العوامل (MFA)
- [ ] 📱 تطبيق الويب
- [ ] 👥 ميزات اجتماعية ومشاركة

---

## 📊 الإحصائيات

<!-- تحديث تلقائي -->
- 📦 **الإصدار الحالي:** 1.0.0
- 🎯 **الحالة:** في التطوير النشط
- ⭐ **النجوم:** --
- 🍴 **الـ Forks:** --
- 🐛 **المشاكل المفتوحة:** --

---

## 🙏 شكر خاص

شكراً لـ:
- 🚀 [Jetpack Compose](https://developer.android.com/jetpack/compose) لـ UI رائعة
- 🔥 [Firebase](https://firebase.google.com/) للخدمات السحابية
- 🤖 [Google Gemini AI](https://aistudio.google.com/) للذكاء الاصطناعي
- 📦 جميع مكتبات Kotlin الرائعة

---

<div align="center">

**صُنع بـ ❤️ بواسطة Ahmed**

⭐ إذا أعجبك المشروع، لا تنسَ إضافة نجمة! ⭐

</div>
