##   Project Documentation

    ├─ adapters
    ├─ api
    ├─ db
    ├─ paging
    ├─ repository
    ├─ ui
    ├─ utilities
    ├─ viewmodels
    ├─ vo
    | Application
    | AppGlideModule
    | ServiceLocator

* `adapters/*.*` : Adapter, ViewHolder for the RecyclerView
* `api/*.*` : Service, Api
* `db/*.*` : Database, DAO
* `paging/*.*` : Paging, Paging Load States
* `repository/*.*` : App repository
* `ui/*.*` : Activity, Fragment
* `utilities/*.*` : App公用類 e.g. app常數
* `utilities/InjectorUtils.kt` : Static methods used to inject classes needed for various Activities and Fragments
* `viewmodels/*.*` : ViewModel, ViewModelFactory.
* `AppGlideModule` : for GlideModule
* `ServiceLocator` : Super simplified service locator implementation to allow us to replace default implementations * for testing


##  設計概念

* 採 MVVM Design Pattern
* 開啟畫面由 LoginActivity 模擬登入 or 驗證身分頁
* Page from network and database, 同時使用 local db, network 做數據源
* 使用 coroutines, flow 進行異步操作



##  Third Party Library

* ViewModel : ViewModel From Jetpack
* LiveData : LiveData From Jetpack
* Navigation : Fragment 切換相關操作
* OkHttp3 ：網路連線 Logging 相關
* Retrofit2 ：網路連線相關
* Gson ：解析 JSON 資料格式
* Stetho ：本地開發調適工具(檢測local db, 抓包...等)
* Glide ：圖片加載
* Room ：本地資料庫
* Paging ：加載並顯示分頁數據
* navigation.safeargs ：Fragment 間資料傳遞

##  *Bonus
#### * 數據源採用 local db, netwok 結合. 僅在數據庫中沒有更多數據時才向網絡發出請求, 提供用戶在網路不可靠時或離線時擁有更好用戶體驗
#### * Toolbar 提供伸縮摺疊功能, 提高使用者瀏覽時可視範圍

##  備註
#### Local DB 僅儲存所選取之語言 景觀資料
#### DataBinding 未導入: 本人習慣僅用 ViewBinding 開發, 因 DataBinding 相對難追 code

##  畫面呈現

#### SAMSUNG GALAXY Note 5
![image](https://github.com/azrael8576/cathaybk-recruit-android/blob/main/demo1.gif)
![image](https://github.com/azrael8576/cathaybk-recruit-android/blob/main/demo2.gif)
