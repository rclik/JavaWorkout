package com.rcelik.java.core;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeTest {
    private static Employee james;
    private static Employee anthony;

    @BeforeClass
    public static void initialize(){
        james = new Employee("James", 1L);
        anthony = new Employee("Anthony", 2L);
    }

    /**
     * Equals Method
     *
     * It is used to check if given object is same as the current object. In Object class, it is equivalent to:
     *
     * public boolean equals(Object obj) {
     *   return (this == obj);
     * }
     *
     * It checks if two objects reference is same or not. (This is called reference equality).
     * Reference equity is not related to hashCode. -----
     * */
    @Test
    public final void defaultHashCodeAndEquals_objectHashCodeAndObjectReference_isNotEqual() {
        Assert.assertNotEquals(anthony, anthony.hashCode());
    }

    /**
     * Equals method checks if objects are same or not. But if it is not overridden then works like reference equality.
     * These are equal. And same as == (reference equality)
     * */
    @Test
    public final void defaultHashCodeAndEquals_sameObjectsWithSameProperty_isEqual(){
        Assert.assertTrue(anthony.equals(anthony));
        Assert.assertTrue(anthony == anthony);

        Assert.assertNotSame(anthony, james);
        Assert.assertNotEquals(anthony, james);
    }

    /**
     * == operator checks equality for primitive types like long, int, float;
     * they are not kept on same memory (objects are kept in heap but primitive types are kept on different place)
     *
     * When it i used for Object, then it compares two object`s references. which shows heap pointer to of the object.
     *
     * Think about, object is same but its property is changed, what will happen for equality operator?
     * Its reference is not changed, so reference equality remains same. But its property value is changed.
     * Actually after you edit, the object property is updated with new value. Means object reference remains same.
     * */
    @Test
    public final void defaultHashCodeAndEquals_oneObjectPropertyChanged_equal(){
        Employee edited = anthony;
        edited.setName("henry");
        Assert.assertEquals(edited, anthony);
        Assert.assertEquals("henry", anthony.getName());
    }


    /**
     * When we think of two distinct objects, what will reference equality operator does?
     * Since their references are different, then they are not equal to each other in terms of reference equality.
     * They represents two different objects in heap.
     * */
    @Test
    public final void defaultHashCodeAndEquals_twoDifferentObjectsWithSameProperty_notEqual(){
        Employee employee = new Employee("Jamie", 1L);
        Assert.assertFalse(employee.equals(anthony));
        Assert.assertFalse(employee == anthony);
    }

    /**
     * Simdi equals method u ile ilgili konusalim. Object in method larindan birisidir. Eger override edilmemisse,
     * reference equality operator u ile calisir. Yani iki object in equals olmasinin tek yolu, heap deki reference larinin
     * ayni olmasidir. Yani ayni object olmasidir.
     *
     * Bu durumda, heap deki her object in bir reference i olacagindan, birbirlerinin aynisi olma olasiligi yoktur.
     * Soyle bir senaryomuz olsun, database den adi rahman olan ogrenciyi dondurelim. o bir student class object i olsun, sonra client dan gelen rahman object ile karsilastirma yapacagiz.
     * Bu ikisi de ayri object olacagindan reference equality operator i hicbir zaman ise yaramaz.
     *
     * int, float gibi primitive type lar ve immutable olan String disinda birbirleriyle hicbir object i karsilastiramayiz.
     *
     * Bu tipler disinda, kendi object lerimizi, kendileri uzerinden equality lerine bakabilmek icin equals method unu override etmemiz lazim.
     * Gercek su ki, ileride de gorulebilecegi gibi, en sonunda primitive ve String gibi wrapper class object leri ile kendi object lerimizin equality lerini check edicegiz.
     *
     * Equals method unun su prensiplere gore yazilmis olmasi gerekir;
     * - reflective --> yani a.equals(a) her zaman true dondurmelidir. Buradan a nin equals method u icinde null check yapmamiz gerektigini anliyoruz.
     * - symmetric --> a.equals(b) dogru ise b.equals(a) da dogru olmalidir. yani icinde equals icinda kullandigimiz object lere dikkat etmemiz gerekecek
     * - transitive --> a.equals(b) ve  b.equals(c) ise  b.equals(c) olmalidir.
     * - consistent -->  a.equals(b) her zaman ayni sonucu gosterecek, ama object ler uzerinde degisiklik yoksa. zamana bagli veya random olamaz.
     *
     * Bu prensiplere dikkat gostererek, equals method unu yazalim;
     * 1- ilk olarak reference equality yi check edelim, eger reference lar ayni ise bu iki object kesinlikle equal dir.
     * 2- sonra, null check yapabiliriz. symmetric prensibine gore null degerin tersi NullPointerException dir.
     * 3- simdi elimizde bir object var, cast edebiliriz. ama farkli object de olabilir, o zaman ClassCast exception aliriz.
     *    o zaman object lerin className lerine bakabiliriz. Eger class name leri birbirine esitlerse, o zaman bir sonraki adima gecebiliriz.
     * 4- Class isimleri de ayni ise, case edip, equality icin bakacagimiz property lere bakabiliriz.
     *
     *     @Override
     *     public boolean equals(Object obj) {
     *         if (this == obj)
     *             return true;
     *
     *         if (obj == null)
     *             return false;
     *
     *         if (this.getClass() != obj.getClass())
     *             return false;
     *
     *         EmployeeWithEquals employeeWithEquals = (EmployeeWithEquals) obj;
     *
     *         return this.getName().equals(employeeWithEquals.getName());
     *     }
     *
     * Bu equals method una dikkat etmemiz gereken, equality icin class in tipi cok onemlidir. Sub/super class object leri equals olamaz.
     * Eger requirement imiz akraba class lar esit olduklarini soyluyorsa, getClass() kismi guncellenmelidir.
     * */
    @Test
    public final void defaultHashCodeAndOverriddenEquals_twoDifferentObjectsWithSameProperty_equal(){
        Employee employee = new EmployeeWithEquals("James", 1L);
        Employee employee2 = new EmployeeWithEquals("Johny", 1L);
        Assert.assertTrue(employee.equals(employee2));
        Assert.assertFalse(employee == employee2); // cunku reference lari farklidir. yani heap de ayri object leri gosterirler.
    }

    /**
     * HashCode nedir?
     * HashCode method u ise bir object in memory address inin hash lenmis halidir. Object in memory deki address i native Java libarary sinden alinarak
     * hash lenir ve bir integer value ya donusturulur. Bu da hashCode method u ile return edilir.
     * Memory address bilgisine Java native function larindan den ulasilir. Cunku Java bu isleri native method lari ile yapar.
     * Java native methodlari ise, C ve C++ function lari kullanilarak JVM in ulasamadigi bilgilere daha performansli sekilde ulasilir.
     *
     * Bir object in hashCode u kesinlikle o object in memory location i ile ilgili olacaktir diye bir kisitlama yoktur.
     * Ayrica, bir object in hashCode methodu degistirilse (yani override edilse) dahi, o value ya ulasilabilir. System.identityHashCode method bize o degeri dondurur.
     *  System.identityHashCode(object1)
     *
     * HashCode un da prensipleri vardir.
     * 1- Object degismedigi surece hashCode u da degismemelidir.
     * 2- Eger iki object equal ise hashCode lari da equal olmalidir.
     * 3- Ayni hashcode a sahip iki object equal olmak zorunda degildir.
     *
     * Akla hemen su soru geliyor; ayni olmayan object ler neden ayni hashCode a sahip olsun ki?
     *
     * Her object in bir bir hashCode u var. Bu da bir integer deger. Integer deger ler -2 milyar ile +2 milyar arasinda toplam 4 milyar degerdir.
     * Yani bu durumda 4 milyardan fazla object imiz olamaz. Ama uygulamalarimizda 4 milyardan fazla object e ihtiyac duyabiliriz. Mesela dunyadaki
     * insanlari simule edecegimizi dusunelim. 4 milyardan sonra her insan object ini yazabilmek icin daha once olusturulmus insan object inin uzerine yazmamiz gerekecekti.
     * tabii bu da olmamasi gereken bir durum.
     *
     * Bir sonraki soru ise, equals method u tamam ama hashCode method ne ise yarar?
     *
     * Java da HashMap gibi bazi data structure lar object lerin hashCode larinin kullanarak onlari HashTable larina yazarlar ve onlar uzerinden object lere ulasim saglarlar.
     * Bu sekilde object lere ulasim daha hizli olur.
     * Mesela, HashMap put ve get method larinda hashCode u kullanir. Genel yapisi sudur; HashMap imiz key-value lardan olusur. Her key in hashCode larina gore
     * bir bucket olur. Bucket ise key-value pair leri tutar. Bu sekilde gelen object in hashCode larina gore index leme yapilir. Ve bu index leme sonucunda value ya
     * ulasim hizi cok iyidir. Cogunlukla O(1) dir. Yani her key-value pair indeki key in hashCode unun farkli olmasi durumu.
     * Eger hashCode ayni olursa, o zaman key-value pair leri ayni bucket icine yazilirlar. Simdi ise soyle dusunelim, iki tane key-value pair imiz var,
     * bunlarin keyleri birbirlerinden farkli ama hashCode lari ayni. O zaman bu iki pair, ayni bucket e yazilirlar. get method u cagilirnce key icin, dogru key-value yu bulabilmek icin,
     * ilk once hashCode dan bucket bulunur sonra da key in equals method una bakilarak dogru pair e ulasilir. O da su sekilde olur, key.equals(entry.getKey())
     *
     * Bunlari ogrendikten sonra hashCode ile equals in hashed data structure larinda ne denli onemli oldugunu anlamis olduk. Ayrica hashed data structure larin bize sagladiklari
     * avantajlardan da haberdar olmus olduk.
     *
     * Bu durumda, eger bir object in equals method unu override edeceksek icinde kullanilan class property si ile hashCode unu da override edilmesi best practice dir.
     *
     * Mesela bizim Employee class inda sadece name String property si var. Aslinda mantiksal olarak Employee icin name property si ayirt edici olamaz. Cunku, o zaman ayni isimdeki
     * insanlar da equal olmus olur. O zaman bir id koyalim;
     *
     * Sonrasinda ise bu id field i icin bir hash olusturalim. o da java.utils.Objects class inin sundugu static bir method ile olusturabiliriz:
     *
     * @Override
     * public int hashCode() {
     *    return Objects.hashCode(getId());
     * }
     * */

    /**
     * Sorular;
     *  HashCode u degistirmeden equals method unu degistirmek ise yaramaz mi?
     *  Ise yarar. Ama mantiksal olarak, hashCode un object i represent edecegini dusunuyor olursak, hashCode un de equals da kullanilan property uzerinden olusturulmus olmasi gerekir.
     *  Equals da kullanilan property ler illa bir tane olacak diye birsey yok. onun icin hashCode object i represent eden daha kucuk biri deger.
     */
    @Test
    public final void hashSetImpl_defaultHashCodeAndOverriddenEquals_TwoDistinctObjectWithSameIdProperty_equal(){
        EmployeeWithEquals emp1 = new EmployeeWithEquals("jamie", 1L);
        Employee emp2 = new EmployeeWithEquals("james", 1L);

        Assert.assertEquals(emp1, emp2); // these two object are equal since equals method uses only id property
        Assert.assertNotEquals(emp1.getName(), emp2.getName()); // but their names are different

        Assert.assertFalse(emp1.hashCode() == emp2.hashCode());
    }

    // hashCode override ettikten sonra object in sistemdeki hashCode undan farkli oldugunu gorebiliriz.
    @Test
    public final void hashSetImpl_overriddenHashCodeAndEquals_hashCodeInSystemAndOverriddenOne_differentAndPreserved(){
        EmployeeWithEqualsAndHashCode employee = new EmployeeWithEqualsAndHashCode("anthony", 1L);
        Assert.assertNotEquals(employee.hashCode(), System.identityHashCode(employee));
    }

    // bir hash data structure i olan HashSet ile hashCode ve equals i overriden edilmeyen bir test edelim
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

        // new object with same value is added to the set
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

    // bir hash data structure i olan HashSet ile sadece equals i overriden edilen bir test edelim
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

        // the size should be 6 since all all items hashCode are different
        Assert.assertEquals(6, employees.size());

        // lets create new object with same class properties, then try to check if set contains that object or not
        EmployeeWithEquals emp7 = new EmployeeWithEquals("steven", 5L);
        Assert.assertEquals(employees.contains(emp7), false); // of course false, since both hashCode is different

        // of course, we cannot remove the element which is not included on the set
        Assert.assertEquals(employees.remove(emp7), false);
    }

    // bir hash data structure i olan HashSet ile sadece hashCode u overriden edilen bir test edelim
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

    // bir hash data structure i olan HashSet ile equals ve hashCode u overriden edilen bir test edelim
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

        // the size should be 5 since even emp5 and emp6 hashCodes are hashCode and equals method are same
        Assert.assertEquals(5, employees.size());

        // and value does not changed
        Assert.assertFalse(hashSetImpl_checkIfNameExist(employees, emp6.getName()));
        Assert.assertTrue(hashSetImpl_checkIfNameExist(employees, emp5.getName()));

        // lets create new object with same class properties, hashCode is same with emp6 and emp5.
        // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
        Employee emp7 = new EmployeeWithEqualsAndHashCode("steven", 5L);
        Assert.assertEquals(employees.contains(emp7), true); // of course false, because having same hashCode and equals method returns true means they are equal

        // of course, we cannot remove the element which is not included on the set
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

    // bir hash data structure i olan HashMap ile hashCode ve equals i overriden edilmeyen bir test edelim
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

        Assert.assertTrue(departmentIT.equals(employees.get(emp6)));
        Assert.assertTrue(departmentHR.equals(employees.get(emp5)));

        // the size should be 6 since all items are different (their references are different)
        Assert.assertEquals(6, employees.size());

        // lets create new object with same class properties, then try to check if set contains that object or not
        Employee emp7 = new Employee("steven", 5L);
        Assert.assertNull(employees.get(emp7));

        // of course, we cannot remove the element which is not included on the set, it returns null
        Assert.assertEquals(null, employees.remove(emp7));
    }

    // bir hash data structure i olan HashMap ile sadece equals i overriden edilen bir test edelim
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

        Assert.assertTrue(departmentIT.equals(employees.get(emp6)));
        Assert.assertTrue(departmentHR.equals(employees.get(emp5)));

        // the size should be 6 since all items are different (their references are different)
        Assert.assertEquals(6, employees.size());

        // lets create new object with same class properties, then try to check if hashMap contains that object or not
        EmployeeWithEquals emp7 = new EmployeeWithEquals("steven", 5L);
        Assert.assertNull(employees.get(emp7));

        // of course, we cannot remove the element which is not included on the hashMap
        Assert.assertEquals(null, employees.remove(emp7));
    }

    // bir hash data structure i olan HashSet ile sadece hashCode u overriden edilen bir test edelim
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

        // new object with same value is added to the set, but here hashCode is same as emp5
        // but  it is added to same bucket with emp5
        Employee emp6 = new EmployeeWithHashCode("nash", 5L);
        employees.put(emp6, departmentIT);

        Assert.assertTrue(employees.get(emp6).equals(departmentIT));
        Assert.assertTrue(employees.get(emp5).equals(departmentHR));

        // the size should be 6 since even emp5 and emp6 hashCodes are same because of their equals method are different
        Assert.assertEquals(6, employees.size());

        // lets create new object with same class properties, hashCode is same with emp6 and emp5.
        // while implementation, it finds the bucket with hashCode and uses equals to if objects are equals
        Employee emp7 = new EmployeeWithHashCode("steven", 5L);
        Assert.assertNull(employees.get(emp7)); // of course returns null since having same hashCode is not enough to make object equals

        // of course, we cannot remove the element which is not included on the set
        Assert.assertNull(employees.remove(emp7));
    }

    // bir hash data structure i olan HashSet ile equals ve hashCode u overriden edilen bir test edelim
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
        Assert.assertEquals(employees.get(emp7), departmentIT); //

        // of course, we cannot remove the element which is not included on the set
        String removedEmployeeDepartment = employees.remove(emp7);
        Assert.assertEquals(departmentIT, removedEmployeeDepartment);

        // of course after removing the element then the size of the data structure is decreased
        Assert.assertEquals(4, employees.size());
    }

}