# Equals and HashCode Metotlarinin Incelenmesi

Java da baska kisilerin kodlarini okurken veya interview sorularina bakarken karsilasiriz bu metotlarla. Mesela **equals** metodu ile **== operand (reference equality)** i arasindaki farkliliklar sorula gelmistir. Tam olarak bunlarin farklari nelerdir?
Bu sorularin cevaplari bu calismayi okuyabilirsiniz.

---

## Equals metodu

Bu method, java nin Object class indan tum class lar tarafindan inherit edilir. Object icindeki hali;

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

Yani **== operand** i ile iki object karsilastiriliyor. Bir diger deyisle **reference equality** sine bakiliyor. O zaman sorumuz su; nedir bu reference equality? 

### Reference Equality

**Reference equality**, adi uzerinde, object reference larini karsilastirarak iki object in ayni olup olmadigina karar verir. Iki object in reference i ayni olmasi ne demek? Bunu icin biraz daha java bilgilerimizi karistirmamiz gerekiyor.

Java da bir object olusturuldugunda o object icin *heap* da bir alan allocate edilir ve alana ulasabilmek icin bir *reference* olusuturulur. Iste bu reference bizim bahsettigimiz **reference** dir. Yani **reference equality** nin sonucunu true olmasi ancak ve ancak iki object in heap deki ayni alanin reference i olmasi gerekiyor.

```java
Person person = new Person("james");
```
Yukardaki ornekte `person` object i olusturduk, `new` operand i kullanildiginda heap de bir alan allocate edildi, person degiskenine de bu alanin *reference* ini tutmasi icin gorevlendirildi.

> Reference equality primitive type lar icin calisirken ise ise ayni degere sahip olup olmadigina bakar. Java da primitive type lar ise object gibi tutulmazlar. Bu baska bir konudur.

---

Soyle dusunebiliriz, eger iki object in **reference** i esit ise, bunlarin esit olmasi dogru bir yaklasim olabilir. Bunda ne sakinca var ki?

> Aslinda bir teknik olarak bir sakinca yok. Eger design i ona gore yazacak olursak, gerek olmayabilir. Ama yazdigimiz class i Java nin sagladigi **Collection API** ile kullanacaksak, o zaman bu reference equality yi kullanmamamiz bazi durumlarda sorunlar yaratabilir. Bu durumlara sonradan bakicagiz.

> Bir sakincali durum, soyle dusunelim, kullanicidan bir bilgi aldik, onu object e donusturduk. Yani elimizde bir object oldu. Bu object e karsilik olan bir object de database den alarak olusurduk. Duruma gore bu ikisini esit mi degil mi diye bakabiliriz. Bu durumda ikisi normalde ayni olmasina ragmen (fiziksel olarak) farkli object ler oldugundan reference equality den false donecektir. Bu duruda da object in property lerini karsilastirilabilir ama daha once bahsettigim gibi design a bagli bir durumdur.

Yukaridaki durumlarla ugrasmamak icin object ler icin **equals** method unu override etmek en dogru yaklasim olacaktir. Bu sekilde iki object in esit olmasinin class in hangi property lerine gore olacagini override edilen equals methodunda belirtebiliriz. Bu da object lerin esitligine bakmak icin class property lerini disarida kullanmaktansa iceride kullanarak **encapsulation** mantiginin guzel bir kullanimi olur.

---

## HashCode methodu

**Equals** methodu konusuldugunda **hashCode** method u da hemen pesine gelir. Neden acaba? Oncelikli ne olduguna bakalim.

**hashCode** method u da ayni **equals** gibi java nin **Object** class indan gelir. Tum class lar bunu inherit eder. 

**hashCode** method u ise bize object in *hash code* unu doner. Bu *hash code*, **memory address** inin ***java native library*** leri kullanilarak alinip sonrasinda bir hash method uyla bir integer degere indirgenmis halidir.

> Memory address bilgilerine java native library leriyle ulasilir. Bu library ler C ve C++ ile gelistirilmislerdir. Bu dilleri kullanarak java da yapilamayan bazi islemleri daha performansli sekilde yapilabiliniyor.

Bir object in **hashCode** method unun, object in memory location i ile ilgili olmasi gibi bir *sorumlulugu yoktur*. Kaldi ki *hash* lenmis bir deger in ana degerine ulasmak imkansizdir. Ayrica, bir object in **hashCode** method unu override ettikten sonra bile o object in hashCode una ulasabiliriz. Su sekilde;

```java
System.identityHashCode(object1);
```

**Hash code** u, bir insanin ismi olarak dusunebiliriz. Mesela Person class imiz olsun, onun da name property si olsun. Sonra John name property sine sahip iki tane instance olusturalim. Bu sekilde normal hayattaki insanlari simule etmis olalim. Bu durumda insanin ismini hash code olarak kullanabiliriz. Ama ayni isme ait iki farkli insan olabilir. Yani ayni isimde iki insani ayni insan olarak dusunemeyiz.

Tum yukaridakileri dusununce hashCode icin bazi prensipler ortaya cikiyor;

1. Object degismedigi surece hashCode u da degismemelidir.
2. Eger iki object equal ise hashCode lari da equal olmalidir.
3. Ayni hashcode a sahip iki object equal olmak zorunda degildir.

Her object in bir **hashCode** u var. Bu da bir **integer** deger. Integer deger ler -2 milyar ile +2 milyar arasinda yaklasik **4 milyar** degerdir. Yani bu durumda **4 milyardan fazla object imiz olamaz.** Ama uygulamalarimizda 4 milyardan fazla object e ihtiyac duyabiliriz. Mesela dunyadaki insanlari simule edecegimizi dusunelim. 4 milyardan sonra her insan object ini yazabilmek icin daha once olusturulmus insan object inin *uzerine yazmamiz gerekecekti*.
Tabi bu da olmamasi gereken bir durum. Daha once olusturulan insannlarin bilgisini kaybetmis oluruz.

> O zaman **hashCode** tam olarak ne ise yariyor? **Equality** ile tam olarak iliskili degil ama biraz var. Olmasa da olur gibi duruyor degil mi?

Aslinda **hashCode** un kullanim amaci farkli; java da kullandigimiz *HashSet, HashSet* gibi bazi *data structure* lar object lerin **hash** lerini kullanarak **hash table** lar olustururlar ve bu sekilde index leme islemlerini yaparlar.
Bu sekilde data structure icinden daha hizli sekilde value dondururler. HashCode un degerinin int value olmasi da bu durumdan dolayidir.
Ornegin HashMap i ele alalim;

---

Java da **HashMap** gibi bazi data structure lar key olarak kullanilan object lerinin **hashCode** larini kullanarak, bir **HashTable** olustururlar. Bu data structure eleman larina bu **HashTable** kullanarak daha *hizli sekilde ulasilir*.
Ornegin, **HashMap** **put ve get** method larinda **hashCode** u kullanir. **HashMap** in genel yapisi ise, kendisi **key-value** lardan olusur. *Her key in* **hashCode** larina gore bir **bucket** olusturulur. *Bucket key-value pair leri tutar.*
Bir *key-value* gelince, **HashTable** a key in **hashCode** u yazilir (index leme icin), sonra **bucket** ile iliskilendirilir. Ayni key den bir object daha gelince, **Hashtable** *a yeni bir entry girilmez*, gelen **key-value** pair ayni **bucket** a yazilir.
Bu sekilde olusuturlan **HashTable** ile element e ulasim cok hizli olur. Time complexity olarak cogunlukla O(1) dir. En iyi durum Q(1) dir, bu durumda ise *data structure daki tum pair lerin key lerin hashCode lari fakli oldugu durumdur.*
Eger birden *fazla pair in key hashCode ayni olursa,* **key-value** pair leri ayni **bucket** icine yazilirlar. Bu durumda ise hepsini birden donmemesi icin equals method una bakilir. Bu sekilde dogru key-value pair e ulasilir.
Adim adim dusunecek olursak, get metodu cagilirsa, key in hashCode u aliniyor. Hash table a bakliyor, ilgili bucket bulunuyor. Sonra icinde birden fazla eleman varse, equals metodu kullaniliyor, yoksa ilk eleman donuluyor.

---

Bunlari ogrendikten sonra hashCode ile equals in hashed data structure larinda ne denli onemli oldugunu anlamis olduk. Ayrica hashed data structure larin bize sagladiklari avantajlardan da haberdar olmus olduk. Ayrica, eger bir object in equals method unu override edeceksek icinde kullanilan class property si ile hashCode unu da override edilmesinin neden best practice oldugunu anlamis olduk.

---

Uygulama kismina gecmeden once, birkac soruyu cevaplayalim;

#### Soru-1

(Overrride edilmis bir method yokken) Bir object in reference ile hashCode u ayni midir? - Ayni degildir. Cunku biri object in reference i dir, digeri ise memory address inin hash i alinmis halidir.

```java
@Test
public final void defaultHashCodeAndEquals_objectHashCodeAndObjectReference_isNotEqual() {
    Person anthony = new Employee("Anthony", 2L);
    Assert.assertNotEquals(anthony, anthony.hashCode());
}
```

#### Soru-2

(Overrride edilmis bir method yokken) iyi ayni class property lerine sahip ayri iki object object birbirlerine esit midir? - hayir, cunku equals override edilmemisse, reference equality sine bakiliyordur.

```java
@Test
public final void defaultHashCodeAndEquals_sameObjectsWithSameProperty_isEqual(){
    Person anthony = new Employee("Anthony", 2L);
    Person anthonyTheSecond = new Employee("Anthony", 2L);

    Assert.AssertFalse(anthony.equals(anthonyTheSecond));
    Assert.AssertFalse(anthony == anthonyTheSecond);
}
```

#### Soru-3

(Overrride edilmis bir method yokken) bir object in reference ini yeni bir reference tipi ile alip, reference tipi uzerinden object i guncellersek, yeni object ile eski object ayni olur mu? - Evet, cunku bu sekilde yeni reference tipi de ayni object i gosterecek. Hatta guncelleme de ayni object uzerinde olacak.

```java
@Test
public final void defaultHashCodeAndEquals_oneObjectPropertyChanged_equal(){
    Person anthony = new Employee("Anthony", 2L);
    Employee edited = anthony;
    edited.setName("henry");

    Assert.assertEquals(edited, anthony);
    Assert.assertEquals("henry", anthony.getName());
}
```

---

### Equals Method unu nasil uygulayabiliz?

Equals metodunu yazarken dikkat edilmesi gereken bazi durumlar vardir.
Equals method unun su prensiplere gore yazilmis olmasi gerekir;

- **Reflective:** yani a.equals(a) her zaman true dondurmelidir. Buradan a nin equals method u icinde null check yapmamiz gerektigini anliyoruz.
- **Symmetric:** --> a.equals(b) dogru ise b.equals(a) da dogru olmalidir. yani icinde equals icinda kullandigimiz object lere dikkat etmemiz gerekecek
- **Transitive:** a.equals(b) ve  b.equals(c) ise  b.equals(c) olmalidir.
- **Consistent:**  a.equals(b) her zaman ayni sonucu gosterecek, ama object ler uzerinde degisiklik yoksa. zamana bagli veya random olamaz.

Bu prensiplere dikkat gostererek, equals method unu yeniden yazalim;

1. Ilk olarak **reference equality** yi kontrol edelim; eger **reference** lar ayni ise bu iki object kesinlikle aynidir.
2. **null** kontrolu yapabiliriz. *Symmetric* prensibine gore null degerin tersi *NullPointerException* dir.
3. Ilk iki check ten sonra elimizde bir object var oldugunu biliyoruz, o zaman class i cast edebiliriz. Ama iki  object birbirinden **farkli** class lardan olusturulmus olabilir, o zaman **ClassCastException** aliriz. Bu durumda, object lerin **className** lerine bakabiliriz. Eger class name leri birbirine esitlerse, o zaman bir sonraki adima gecebiliriz. Ya da **instanceof** operator i ile check edebilirsin.
4. Class isimleri de ayni ise, cast edip, equality icin bakacagimiz property lere bakabiliriz.

> Bu equals method una dikkat etmemiz gereken, equality icin class in tipi cok onemlidir. Sub/super class object leri equals olamaz. Eger requirement imiz akraba class lar esit olduklarini soyluyorsa, getClass() kismi guncellenmelidir.

```java
@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (this.getClass() != obj.getClass())
        return false;
    EmployeeWithEquals employeeWithEquals = (EmployeeWithEquals) obj;
    return this.getName().equals(employeeWithEquals.getName());
}
```

---

Sorular kismina devam edebiliriz;

#### Soru-4

Sadece equals i implement ettikten sonra, ayni property (equals da kullanilan) ye sahip  iki farkli object esit midir? - Evet

```java
@Test
public final void defaultHashCodeAndOverriddenEquals_twoDifferentObjectsWithSameProperty_equal(){
    Employee employee = new EmployeeWithEquals("James", 1L);
    Employee employee2 = new EmployeeWithEquals("James", 2L);
    Assert.assertTrue(employee.equals(employee2));
    Assert.assertFalse(employee == employee2); // cunku reference lari farklidir. yani heap de ayri memory aalanlarini gosterirler.
}
```

### HashCode i nasil uygulayabiliz?

Bunun bircok yolu var, onemli olan object e gore bir integer value donmektir. Ama java nin bize sagladigi method u kullanabiliriz;

```java
@Override
public int hashCode() {
    return Objects.hashCode(getId());
}
```

#### Soru-5

HashCode u degistirmeden equals method unu degistirmek ise yarar mi? - Yarar, ama dikkat etmemiz gereken sey, bu class dan olusuturulan object leri java nin hashcode u kullandigi class larinda kullanirken dikkat etmemiz gerektigidir.

```java
@Test
public final void hashSetImpl_defaultHashCodeAndOverriddenEquals_twoDistinctObjectWithSameIdProperty_equal(){
    EmployeeWithEquals emp1 = new EmployeeWithEquals("jamie", 1L);
    Employee emp2 = new EmployeeWithEquals("james", 1L);
    Assert.assertEquals(emp1, emp2); // these two object are equal since equals method uses only id property
    Assert.assertNotEquals(emp1.getName(), emp2.getName()); // but their names are different
    Assert.assertFalse(emp1.hashCode() == emp2.hashCode());
}
```

#### Soru-6

HashCode override ettikten sonra object in sistemdeki hashCode undan farkli midir? - Farklidir, cunku sistem de object in orjinal hashCode u tekrardan hesaplanabilir ve degistirildikten sonraki degerinden farklidir.

```java
@Test
public final void hashSetImpl_overriddenHashCodeAndEquals_hashCodeInSystemAndOverriddenOne_differentAndPreserved(){
    EmployeeWithEqualsAndHashCode employee = new EmployeeWithEqualsAndHashCode("anthony", 1L);
    Assert.assertNotEquals(employee.hashCode(), System.identityHashCode(employee));
}
```

#### Soru-7

Hashed data structure olan HashSet ile hasCode ve equals method u override edilmeyen bir test yapalim;

```java
@Test
public final void hashSetImpl_defaultHashCodeAndEquals_hashSetImplementation_test(){
    Employee emp1 = new Employee("jamie", 1L);
    Employee emp2 = new Employee("james", 2L);
    Employee emp3 = new Employee("anthony", 3L);
    Employee emp4 = new Employee("thomas", 4L);
    Employee emp5 = new Employee("steven", 5L);

    Set<Employee> employees = new HashSet<>();
    employees.add(emp1);
    employees.add(emp2);
    employees.add(emp3);
    employees.add(emp4);
    employees.add(emp5);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same values with emp5 is added to the set
    Employee emp6 = new Employee("steven", 5L);
    employees.add(emp6);

    // the size should be 6 since all items are different (their references are different)
    Assert.assertEquals(6, employees.size());

    // lets create new object with same class properties, then try to check if set contains that object or not
    Employee emp7 = new Employee("steven", 5L);
    Assert.assertEquals(employees.contains(emp7), false); // of course false, since hashCode is different

    // of course, we cannot remove the element which is not included on the set
    Assert.assertEquals(employees.remove(emp7), false);
}
```

#### Soru-8

Hashed data structure olan HashSet ile sadece equals method u override edilen bir test yapalim;

```java
@Test
public final void hashSetImpl_defaultHashCodeAndOverriddenEquals_hashSetImplementation_test(){
    EmployeeWithEquals emp1 = new EmployeeWithEquals("jamie", 1L);
    EmployeeWithEquals emp2 = new EmployeeWithEquals("james", 2L);
    EmployeeWithEquals emp3 = new EmployeeWithEquals("anthony", 3L);
    EmployeeWithEquals emp4 = new EmployeeWithEquals("thomas", 4L);
    EmployeeWithEquals emp5 = new EmployeeWithEquals("steven", 5L);

    Set<EmployeeWithEquals> employees = new HashSet<>();
    employees.add(emp1);
    employees.add(emp2);
    employees.add(emp3);
    employees.add(emp4);
    employees.add(emp5);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the set
    EmployeeWithEquals emp6 = new EmployeeWithEquals("steven", 5L);
    employees.add(emp6);

    // the size should be 6 since all all items hashCode are different, new bucket is created for emp6
    Assert.assertEquals(6, employees.size());

    // lets create new object with same class properties, then try to check if set contains that object or not
    EmployeeWithEquals emp7 = new EmployeeWithEquals("steven", 5L);
    Assert.assertEquals(employees.contains(emp7), false); // of course false, since both hashCode is different

    // of course, we cannot remove the element which is not included on the set
    Assert.assertEquals(employees.remove(emp7), false);
}
```

#### Soru-9

Hashed data structure olan HashSet ile sadece hashCode method u override edilen bir test yapalim;

```java
@Test
public final void hashSetImpl_defaultEqualsAndOverriddenHashCode_hashSetImplementation_test(){
    Employee emp1 = new EmployeeWithHashCode("jamie", 1L);
    Employee emp2 = new EmployeeWithHashCode("james", 2L);
    Employee emp3 = new EmployeeWithHashCode("anthony", 3L);
    Employee emp4 = new EmployeeWithHashCode("thomas", 4L);
    Employee emp5 = new EmployeeWithHashCode("steven", 5L);

    Set<Employee> employees = new HashSet<>();
    employees.add(emp1);
    employees.add(emp2);
    employees.add(emp3);
    employees.add(emp4);
    employees.add(emp5);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the set, but here hashCode is same as emp5
    // but it is added to same bucket with emp5
    Employee emp6 = new EmployeeWithHashCode("steven", 5L);
    employees.add(emp6);

    // the size should be 6 since even emp5 and emp6 hashCodes are same because of their equals method are different
    Assert.assertEquals(6, employees.size());

    // lets create new object with same class properties, hashCode is same with emp6 and emp5.
    // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
    Employee emp7 = new EmployeeWithHashCode("steven", 5L);
    Assert.assertEquals(employees.contains(emp7), false); // of course false, because having same hashCode is not enough to make object equals

    // of course, we cannot remove the element which is not included on the set
    Assert.assertEquals(employees.remove(emp7), false);
}
```

#### Soru-10

Hashed data structure olan HashSet ile hashCode ve equals method u override edilen bir test yapalim;

```java
@Test
public final void hashSetImpl_bothOverriddenEqualsAndHashCode_hashSetImplementation_test(){
    Employee emp1 = new EmployeeWithEqualsAndHashCode("jamie", 1L);
    Employee emp2 = new EmployeeWithEqualsAndHashCode("james", 2L);
    Employee emp3 = new EmployeeWithEqualsAndHashCode("anthony", 3L);
    Employee emp4 = new EmployeeWithEqualsAndHashCode("thomas", 4L);
    Employee emp5 = new EmployeeWithEqualsAndHashCode("steven", 5L);

    Set<Employee> employees = new HashSet<>();
    employees.add(emp1);
    employees.add(emp2);
    employees.add(emp3);
    employees.add(emp4);
    employees.add(emp5);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the set, but here hashCode is same as emp5 and they are equals to each other with respect to equals method
    // so emp6 does not added to HashSet, because HashSet does not allow add new value over
    Employee emp6 = new EmployeeWithEqualsAndHashCode("nash", 5L);

    boolean isAdded = employees.add(emp6);

    Assert.assertFalse(isAdded);

    // the size should be 5 since even emp5 and emp6 hashCodes are hashCode and equals method are same even their name property is not same since equals and hashCode methods are made by using id class property
    Assert.assertEquals(5, employees.size());

    // and value does not changed
    Assert.assertFalse(hashSetImpl_checkIfNameExist(employees, emp6.getName()));
    Assert.assertTrue(hashSetImpl_checkIfNameExist(employees, emp5.getName()));

    // lets create new object with same class properties, hashCode is same with emp6 and emp5.
    // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
    Employee emp7 = new EmployeeWithEqualsAndHashCode("steven", 5L);
    Assert.assertEquals(employees.contains(emp7), true); // of course false, because having same hashCode and equals method returns true means they are equal

    // of course, we can remove the element which is not included on the set
    Assert.assertEquals(employees.remove(emp7), true);

    // of course after removing the element then the size of the data structure is decreased
    Assert.assertEquals(4, employees.size());
}

private boolean hashSetImpl_checkIfNameExist(Set<Employee> employees, String name) {
    for (Employee employee : employees) {
        if (employee.getName().equals(name))
            return true;
    }
    return false;
}
```

#### Soru-11

Hashed data structure olan HashMap ile hashCode ve equals method u override edilen bir test yapalim;

```java
@Test
public final void hashMapImpl_defaultHashCodeAndEquals_hashSetImplementation_test(){
    Employee emp1 = new Employee("jamie", 1L);
    Employee emp2 = new Employee("james", 2L);
    Employee emp3 = new Employee("anthony", 3L);
    Employee emp4 = new Employee("thomas", 4L);
    Employee emp5 = new Employee("steven", 5L);

    String departmentIT = "IT";
    String departmentHR = "HR";

    Map<Employee, String> employees = new HashMap<>();
    employees.put(emp1, departmentIT);
    employees.put(emp2, departmentIT);
    employees.put(emp3, departmentIT);
    employees.put(emp4, departmentHR);
    employees.put(emp5, departmentHR);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the set
    Employee emp6 = new Employee("steven", 5L);
    employees.put(emp6, departmentIT);

    // the size should be 6 since all items are different (their references are different)
    Assert.assertEquals(6, employees.size());

    // both values should be different since keys are different
    Assert.assertTrue(departmentIT.equals(employees.get(emp6)));
    Assert.assertTrue(departmentHR.equals(employees.get(emp5)));

    // lets create new object with same class properties, then try to check if set contains that object or not
    Employee emp7 = new Employee("steven", 5L);
    Assert.assertNull(employees.get(emp7));

    // of course, we cannot remove the element which is not included on the set, it returns null
    Assert.assertEquals(null, employees.remove(emp7));
}
```

#### Soru-12

Hashed data structure olan HashMap ile sadece equals method u override edilen bir test yapalim;

```java
@Test
public final void hashMapImpl_defaultHashCodeAndOverriddenEquals_hashSetImplementation_test(){
    EmployeeWithEquals emp1 = new EmployeeWithEquals("jamie", 1L);
    EmployeeWithEquals emp2 = new EmployeeWithEquals("james", 2L);
    EmployeeWithEquals emp3 = new EmployeeWithEquals("anthony", 3L);
    EmployeeWithEquals emp4 = new EmployeeWithEquals("thomas", 4L);
    EmployeeWithEquals emp5 = new EmployeeWithEquals("steven", 5L);

    String departmentIT = "IT";
    String departmentHR = "HR";

    Map<Employee, String> employees = new HashMap<>();
    employees.put(emp1, departmentIT);
    employees.put(emp2, departmentIT);
    employees.put(emp3, departmentIT);
    employees.put(emp4, departmentHR);
    employees.put(emp5, departmentHR);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the hashMap
    EmployeeWithEquals emp6 = new EmployeeWithEquals("steven", 5L);
    employees.put(emp6, departmentIT);

    // the size should be 6 since all items hashCode are different
    Assert.assertEquals(6, employees.size());

    Assert.assertTrue(departmentIT.equals(employees.get(emp6)));
    Assert.assertTrue(departmentHR.equals(employees.get(emp5)));

    // lets create new object with same class properties, then try to check if hashMap contains that object or not
    EmployeeWithEquals emp7 = new EmployeeWithEquals("steven", 5L);
    Assert.assertNull(employees.get(emp7));

    // of course, we cannot remove the element which is not included on the hashMap
    Assert.assertEquals(null, employees.remove(emp7));
}
```

#### Soru-13

Hashed data structure olan HashMap ile sadece hashCode method u override edilen bir test yapalim;

```java
@Test
public final void hashMapImpl_defaultEqualsAndOverriddenHashCode_hashSetImplementation_test(){
    Employee emp1 = new EmployeeWithHashCode("jamie", 1L);
    Employee emp2 = new EmployeeWithHashCode("james", 2L);
    Employee emp3 = new EmployeeWithHashCode("anthony", 3L);
    Employee emp4 = new EmployeeWithHashCode("thomas", 4L);
    Employee emp5 = new EmployeeWithHashCode("steven", 5L);

    String departmentIT = "IT";
    String departmentHR = "HR";

    Map<Employee, String> employees = new HashMap<>();
    employees.put(emp1, departmentIT);
    employees.put(emp2, departmentIT);
    employees.put(emp3, departmentIT);
    employees.put(emp4, departmentHR);
    employees.put(emp5, departmentHR);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // the size should be 6 since even emp5 and emp6 hashCodes are same because of their equals method are different
    Assert.assertEquals(6, employees.size());

    // new object with same value is added to the set, but here hashCode is same as emp5
    // but  it is added to same bucket with emp5
    Employee emp6 = new EmployeeWithHashCode("nash", 5L);
    employees.put(emp6, departmentIT);

    Assert.assertTrue(employees.get(emp6).equals(departmentIT));
    Assert.assertTrue(employees.get(emp5).equals(departmentHR));

    // lets create new object with same class properties, hashCode is same with emp6 and emp5.
    // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
    Employee emp7 = new EmployeeWithHashCode("steven", 5L);
    Assert.assertNull(employees.get(emp7)); // of course returns null since having same hashCode is not enough to make object equals

    // of course, we cannot remove the element which is not included on the set
    Assert.assertNull(employees.remove(emp7));
}
```

#### Soru-14

Hashed data structure olan HashMap ile hashCode ve equals method u override edilen bir test yapalim;

```java
@Test
public final void hashMapImpl_bothOverriddenEqualsAndHashCode_hashSetImplementation_test(){
    Employee emp1 = new EmployeeWithEqualsAndHashCode("jamie", 1L);
    Employee emp2 = new EmployeeWithEqualsAndHashCode("james", 2L);
    Employee emp3 = new EmployeeWithEqualsAndHashCode("anthony", 3L);
    Employee emp4 = new EmployeeWithEqualsAndHashCode("thomas", 4L);
    Employee emp5 = new EmployeeWithEqualsAndHashCode("steven", 5L);

    String departmentIT = "IT";
    String departmentHR = "HR";

    Map<Employee, String> employees = new HashMap<>();
    employees.put(emp1, departmentIT);
    employees.put(emp2, departmentIT);
    employees.put(emp3, departmentIT);
    employees.put(emp4, departmentHR);
    employees.put(emp5, departmentHR);

    // its size should be 5 since all elements are different objects (their references are different)
    Assert.assertEquals(5, employees.size());

    // new object with same value is added to the HashMap, but here hashCode is same as emp5 and they are equals to each other with respect to equals method
    // so emp6 added to the hashMap, and overrides its department value.
    Employee emp6 = new EmployeeWithEqualsAndHashCode("nash", 5L);

    // meanwhile HashMap s put method returns overriden value 
    String overriddenEmployeeDepartment = employees.put(emp6, departmentIT);
    Assert.assertTrue(overriddenEmployeeDepartment.equals(departmentHR));

    // the size should be 5, since emp5 and emp6 hashCodes are hashCode and equals method are same
    Assert.assertEquals(5, employees.size());

    // and values are changed since emp5 is overridden on the HashMap
    Assert.assertTrue(employees.get(emp6).equals(departmentIT));
    Assert.assertFalse(employees.get(emp6).equals(departmentHR));

    // we can get department value by using emp5 since they are same with emp6
    // but department value is not same with when emp5 is added to the hashMap
    Assert.assertTrue(employees.get(emp5).equals(departmentIT));
    Assert.assertFalse(employees.get(emp5).equals(departmentHR));

    // lets create new object with same class properties, hashCode is same with emp6 and emp5.
    // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
    Employee emp7 = new EmployeeWithEqualsAndHashCode("steven", 5L);
    Assert.assertEquals(employees.get(emp7), departmentIT); // because it is the overriden value

    // of course, we cannot remove the element which is not included on the set
    String removedEmployeeDepartment = employees.remove(emp7);
    Assert.assertEquals(departmentIT, removedEmployeeDepartment);

    // of course after removing the element then the size of the data structure is decreased
    Assert.assertEquals(4, employees.size());
}
```
