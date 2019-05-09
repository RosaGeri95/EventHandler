Megjegyzés: mivel az alkalmazáshoz használt api.fixer.io webszolgáltatás időközben megváltozott (és részben fizetős lett), ezért az alkalmazás nem működik megfelelően amikor pénznemet váltanék a beállításokban vagy pénzösszeget rendelnék az új eseményhez.

# EventHandler

Az "Android alapú szoftverfejlesztés" tárgy házi feladataként elkészített alkalmazásomat események kezelésére lehet használni. A felhasználó által elmentett események nevét, rövid leírását, helyszínét és esetleges költségét egy listában jeleníti meg a program. Időpontot is lehet rendelni az eseményekhez, ilyenkor a megadott idő lejártakor Notificationnal jelez az Android.


# Főbb funkciók
* Az alkalmazás az eseményeket egy RecyclerView-ban jeleníti meg, új elemet például floating action button segítségével lehet felvenni egy felugró DialogFragment segítségével.
* Az eseményeket egy adatbázisban is eltárolja (perzisztencia).
* Amennyiben az eseménynek költsége van (például színházhoz belépőjegy), az árat meg lehet adni dollárban, euróban illetve forintban, de ezt a program automatikusan átváltja a beállított pénznemre. A kívánt valutát egy „Beállítások” fülön lehet megadni (shared preferences). Az aktuális átváltási rátát hálózaton keresztül szerzi meg az esemény felvételekor.
* Amikor egy esemény bekövetkezik (elérkezik a hozzá megadott időpont), a program figyelmeztet minket Notification segítségével. Ezt a funkciót ki/be lehet kapcsolni a beállítások fülön.


![Picture1](https://user-images.githubusercontent.com/31664276/57457068-1b82bd00-726f-11e9-8666-354c1f7e7c9e.png)       ![Picture2](https://user-images.githubusercontent.com/31664276/57457071-1cb3ea00-726f-11e9-84d7-3909668449af.png)

![Picture3](https://user-images.githubusercontent.com/31664276/57457072-1cb3ea00-726f-11e9-86ec-c6b2ff875550.png)       ![Picture4](https://user-images.githubusercontent.com/31664276/57457067-1b82bd00-726f-11e9-9c33-1d1e49e5b6f9.png)
