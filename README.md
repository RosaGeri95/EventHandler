# EventHandler

Az "Android alapú szoftverfejlesztés" tárgy házi feladataként elkészített alkalmazásomat események kezelésére lehet használni. A felhasználó által elmentett események nevét, rövid leírását, helyszínét és esetleges költségét egy listában jeleníti meg a program. Időpontot is lehet rendelni az eseményekhez, ilyenkor a megadott idő lejártakor Notificationnal jelez az Android.


# Főbb funkciók
* Az alkalmazás az eseményeket egy RecyclerView-ban jeleníti meg, új elemet például floating action button segítségével lehet felvenni egy felugró DialogFragment segítségével.
* Az eseményeket egy adatbázisban is eltárolja (perzisztencia).
* Amennyiben az eseménynek költsége van (például színházhoz belépőjegy), az árat meg lehet adni dollárban, euróban illetve forintban, de ezt a program automatikusan átváltja a beállított pénznemre. A kívánt valutát egy „Beállítások” fülön lehet megadni (shared preferences). Az aktuális átváltási rátát hálózaton keresztül szerzi meg az esemény felvételekor.
* Amikor egy esemény bekövetkezik (elérkezik a hozzá megadott időpont), a program figyelmeztet minket Notification segítségével. Ezt a funkciót ki/be lehet kapcsolni a beállítások fülön.
