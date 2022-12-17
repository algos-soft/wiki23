package it.algos.unit;

import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 19-nov-2020
 * Time: 20:14
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Java - Nuove funzioni Java 17")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JavaTest extends AlgosTest {


    private String ottenuto;

    private static void printNamesConsumer(String consumer) {
        System.out.println(consumer);
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Function (from 8)")
    void function() {
        Function<Long, Long> adder = value -> value + 5;
        Long resultLambda = adder.apply((long) 8);
        System.out.println("resultLambda = " + resultLambda);

        Function<String, String> upper = String::toUpperCase;
        ottenuto = upper.apply("sopra");
        System.out.println("resultLambda = " + ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Lambda (from 8)")
    void lambda() {
        List<Integer> numbers = Arrays.asList(5, 9, 8, 1);
        numbers.forEach(System.out::println);

        List<String> lista = Arrays.asList("alfa", "beta", "gamma", "delta");
        lista.forEach(System.out::println);

        Runnable funzione = () -> System.out.println("Funziona");
        funzione.run();
    }

    @Test
    @Order(3)
    @DisplayName("3 - Supplier (from 8)")
    void supplier() {
        // This function returns a random value.
        Supplier<Double> randomValue = Math::random;

        // Print the random value using get()
        System.out.println(randomValue.get());
        System.out.println(randomValue.get());
    }

    @Test
    @Order(4)
    @DisplayName("4 - Supplier again")
    void supplier4() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Supplier<LocalDateTime> s = LocalDateTime::now;
        LocalDateTime time = s.get();

        System.out.println("Non formattato: " + time);

        Supplier<String> s1 = () -> dtf.format(LocalDateTime.now());
        String time2 = s1.get();

        System.out.println("Formattato: " + time2);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Supplier more")
    void supplier5() {
        Supplier<String> supplier = () -> "Marcella bella";
        System.out.println(supplier.get());
    }

    @Test
    @Order(6)
    @DisplayName("6 - Supplier in stream")
    void supplier6() {
        Supplier<Integer> randomNumbersSupp = () -> new Random().nextInt(10);
        Stream.generate(randomNumbersSupp)
                .limit(5)
                .forEach(System.out::println);
    }

    @Test
    @Order(7)
    @DisplayName("7 - Supplier student")
    void supplier7() {
        Supplier<Student> studentSupplier = () -> new Student(1, "Beretta", "M", 19);
        Student student = studentSupplier.get();
        System.out.println(student);

        studentSupplier = () -> new Student(2, "Mantovani", "F", 21);
        student = studentSupplier.get();
        System.out.println(student);
    }

    //        @Test
    //        @Order(8)
    //        @DisplayName("8 - Supplier strings")
    //        void supplier8() {
    //            System.out.println("Java8 Supplier strings\n");
    //
    //            List<String> names = Arrays.asList("Harry", "Daniel", "Lucifer", "April O' Neil");
    //            names.stream().forEach((item) -> { printNamesSupplier(() -> item); });
    //        }

    @Test
    @Order(9)
    @DisplayName("9 - Consumer (from 8)")
    void consumer() {
        System.out.println("Java8 Consumer\n");

        Consumer<String> consumer = JavaTest::printNamesConsumer;
        consumer.accept("C++");
        consumer.accept("Java");
        consumer.accept("Python");
        consumer.accept("Ruby On Rails");
    }

    @Test
    @Order(10)
    @DisplayName("10 - Var keyword (from 10)")
    void keyword() {
        var lista = List.of("one", "two", "three");
        lista.forEach(System.out::println);
        Object obj = null;

        //        if (obj instanceof String stringa) {
        //            System.out.println(stringa.contains("hello"));
        //        }

        System.out.println("Java10 \n");

        var str = "a test string";
        System.out.println(str);
        var aVariable = "Marco";
        System.out.println(aVariable);
        var anotherVariable = 182;
        System.out.println(anotherVariable);
    }

    @Test
    @Order(11)
    @DisplayName("11 - find regex")
    void regex() {
        boolean status;
        String tag1 = "{|class=\"wikitable";
        String tag2 = "{| class=\"wikitable";
        String tag3 = "{|  class=\"wikitable";
        String tag4 = "{|class=\"sortable wikitable";
        String tag5 = "{| class=\"sortable wikitable";
        String tag6 = "{|  class=\"sortable wikitable";
        String tag7 = "{|   class=\"sortable wikitable";
        String tagEnd = "|}\n";

        String sor0 = "Mario non sapeva cosa fare";
        String sor1 = "Mario non sapeva " + tag1 + " cosa fare";
        String sor2 = "Mario non sapeva " + tag2 + " cosa fare";
        String sor3 = "Mario non sapeva " + tag3 + " cosa fare";
        String sor4 = "Mario non sapeva " + tag4 + " cosa fare";
        String sor5 = "Mario non sapeva " + tag5 + " cosa fare";
        String sor6 = "Mario non sapeva " + tag6 + " cosa fare";
        String sor7 = "Mario non sapeva " + tag7 + " cosa fare";

        List<String> tags = Arrays.asList(tag1, tag2, tag3, tag4, tag5, tag6);
        List<String> sorgs = Arrays.asList(sor1, sor2, sor3, sor4, sor5, sor6);

        for (String sor : sorgs) {
            status = false;
            for (String tag : tags) {
                if (sor.contains(tag)) {
                    status = true;
                }
            }
            assertTrue(status);
        }

        String patterns = Pattern.quote("wikitable");
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher((String) sor1);
        int num = matcher.groupCount();
        matcher.matches();
        //        assertTrue(matcher.matches());
    }

    @Test
    @Order(12)
    @DisplayName("12 - textBlocks")
    void textBlocks() {
        System.out.println("12 - textBlocks");
        System.out.println(VUOTA);

        System.out.println("due spazi interni");
        ottenuto = """
                   {
                      "name":"John Doe",
                      "age":45,
                      "address":"Doe Street, 23, Java Town"
                   }
                """;
        System.out.println(ottenuto);

        System.out.println("zero spazi interni");
        ottenuto = """
                   {
                    "name":"John Doe",
                    "age":45,
                    "address":"Doe Street, 23, Java Town"
                   }
                """;
        System.out.println(ottenuto);

        System.out.println("uno spazio interno");
        ottenuto = """
                   {
                     "name":"John Doe",
                     "age":45,
                     "address":"Doe Street, 23, Java Town"
                   }
                """;
        System.out.println(ottenuto);

        System.out.println("zero spazi esterni");
        System.out.println("notare la posizione dei 3 doppi apici finali nel sorgente java");
        ottenuto = """
                {
                  "name":"John Doe",
                  "age":45,
                  "address":"Doe Street, 23, Java Town"
                }
                """;
        System.out.println(ottenuto);
    }


    @Test
    @Order(13)
    @DisplayName("13 - Switch Expressions")
    void switchExpressions() {
        //        System.out.println("13 - Switch Expressions");
        //        System.out.println(VUOTA);
        //        AEContinente continente = AEContinente.europa;
        //
        //        System.out.println("oldStyleWithoutBreak");
        //        System.out.println("Senza break stampa TUTTI i casi dello switch:");
        //        switch (continente) {
        //            case europa, asia:
        //                System.out.println("Eurasia");
        //            case nordamerica, sudamerica:
        //                System.out.println("America");
        //            default:
        //                System.out.println("Resto del Mondo");
        //        }
        //
        //        System.out.println(VUOTA);
        //        System.out.println("oldStyleWithBreak");
        //        System.out.println("Col break stampa correttamente SOLO il caso previsto:");
        //        switch (continente) {
        //            case europa, asia:
        //                System.out.println("Eurasia");
        //                break;
        //            case nordamerica, sudamerica:
        //                System.out.println("America");
        //                break;
        //            default:
        //                System.out.println("Resto del Mondo");
        //        }
        //
        //        System.out.println(VUOTA);
        //        System.out.println("withReturnValue");
        //        ottenuto = switch (continente) {
        //            case europa, asia -> "Eurasia";
        //            case nordamerica, sudamerica -> "America";
        //            default -> "Resto del Mondo";
        //        };
        //        System.out.println(ottenuto);
        //
        //        System.out.println(VUOTA);
        //        System.out.println("withReturnValueEvenShorter");
        //        System.out.println(switch (continente) {
        //            case europa, asia -> "Eurasia";
        //            case nordamerica, sudamerica -> "America";
        //            default -> "Resto del Mondo";
        //        });
        //
        //        System.out.println(VUOTA);
        //        System.out.println("withYield");
        //        System.out.println("more than only 1 thing in the case. Use brackets to indicate a case block and when returning a value, you use the keyword yield");
        //        String text = switch (continente) {
        //            case europa, asia -> {
        //                System.out.println("the given fruit was: " + continente.getNome());
        //                yield "Eurasia";
        //            }
        //            case nordamerica, sudamerica -> "Exotic fruit";
        //            default -> "Undefined fruit";
        //        };
        //        System.out.println(text);
        //
        //        System.out.println(VUOTA);
        //        System.out.println("oldStyleWithYield");
        //        System.out.println("Senza break:");
        //        System.out.println(switch (continente) {
        //            case europa, asia:
        //                yield "Eurasia";
        //            case nordamerica, sudamerica:
        //                yield "America";
        //            default:
        //                yield "Resto del Mondo";
        //        });
        //
    }

    @Test
    @Order(14)
    @DisplayName("14 - instanceof")
    void instanceofTest() {
        System.out.println("14 - instanceof");
        System.out.println("Creazione della variabile SOLO se instanceof=true");
        System.out.println(VUOTA);

        Object obj = new Student(671, "Mario", "m", 27);
        if (obj instanceof Student studente) {
            System.out.println(String.format("Questo studente si chiama %s ed ha %d anni", studente.getName(), studente.getAge()));
        }

        System.out.println(VUOTA);
        obj = new Student(671, "Francesca", "f", 24);
        if (obj instanceof Student studente && studente.getAge() == 27) {
        }
        else {
            System.out.println(String.format("Questo oggetto è uno studente ma NON ha 27 anni"));
        }
    }

    @Test
    @Order(15)
    @DisplayName("15 - numberFormat")
    void numberFormat() {
        System.out.println("15 - numberFormat");
        NumberFormat fmt;

        System.out.println(VUOTA);
        System.out.println("Inglese short");
        fmt = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
        System.out.println(fmt.format(1000));
        System.out.println(fmt.format(100000));
        System.out.println(fmt.format(1000000));

        System.out.println(VUOTA);
        System.out.println("Inglese long");
        fmt = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.LONG);
        System.out.println(fmt.format(1000));
        System.out.println(fmt.format(100000));
        System.out.println(fmt.format(1000000));

        System.out.println(VUOTA);
        System.out.println("Italiano long");
        fmt = NumberFormat.getCompactNumberInstance(Locale.ITALIAN, NumberFormat.Style.LONG);
        System.out.println(fmt.format(1000));
        System.out.println(fmt.format(100000));
        System.out.println(fmt.format(1000000));
    }


    @Test
    @Order(16)
    @DisplayName("16 - patternB")
    void patternB() {
        System.out.println("16 - patternB");
        DateTimeFormatter dtf;
        String sep = FORWARD;

        System.out.println(VUOTA);
        System.out.println("English Locale");
        dtf = DateTimeFormatter.ofPattern("B").withLocale(Locale.forLanguageTag("EN"));
        System.out.println(String.format("%s%s%s", "8", sep, dtf.format(LocalTime.of(8, 0))));
        System.out.println(String.format("%s%s%s", "13", sep, dtf.format(LocalTime.of(13, 0))));
        System.out.println(String.format("%s%s%s", "20", sep, dtf.format(LocalTime.of(20, 0))));
        System.out.println(String.format("%s%s%s", "23", sep, dtf.format(LocalTime.of(23, 0))));
        System.out.println(String.format("%s%s%s", "24", sep, dtf.format(LocalTime.of(0, 0))));

        System.out.println(VUOTA);
        System.out.println("Dutch Locale");
        dtf = DateTimeFormatter.ofPattern("B").withLocale(Locale.forLanguageTag("NL"));
        System.out.println(String.format("%s%s%s", "8", sep, dtf.format(LocalTime.of(8, 0))));
        System.out.println(String.format("%s%s%s", "13", sep, dtf.format(LocalTime.of(13, 0))));
        System.out.println(String.format("%s%s%s", "20", sep, dtf.format(LocalTime.of(20, 0))));
        System.out.println(String.format("%s%s%s", "23", sep, dtf.format(LocalTime.of(23, 0))));
        System.out.println(String.format("%s%s%s", "24", sep, dtf.format(LocalTime.of(0, 0))));

        System.out.println(VUOTA);
        System.out.println("Italiano Locale");
        dtf = DateTimeFormatter.ofPattern("B").withLocale(Locale.forLanguageTag("IT"));
        System.out.println(String.format("%s%s%s", "8", sep, dtf.format(LocalTime.of(8, 0))));
        System.out.println(String.format("%s%s%s", "13", sep, dtf.format(LocalTime.of(13, 0))));
        System.out.println(String.format("%s%s%s", "20", sep, dtf.format(LocalTime.of(20, 0))));
        System.out.println(String.format("%s%s%s", "23", sep, dtf.format(LocalTime.of(23, 0))));
        System.out.println(String.format("%s%s%s", "24", sep, dtf.format(LocalTime.of(0, 0))));
    }


    @Test
    @Order(17)
    @DisplayName("17 - stream.toList()")
    void streamToList() {
        System.out.println("17 - stream.toList()");
        Stream<String> stringStream;
        List<String> stringList;

        System.out.println(VUOTA);
        System.out.println("oldStyle");
        stringStream = Stream.of("a", "b", "c");
        stringList = stringStream.collect(Collectors.toList());
        for (String s : stringList) {
            System.out.println(s);
        }

        System.out.println(VUOTA);
        System.out.println("streamToList");
        stringStream = Stream.of("a", "b", "c");
        stringList = stringStream.toList();
        for (String s : stringList) {
            System.out.println(s);
        }

        System.out.println(VUOTA);
        System.out.println("algosBetter");
        stringList = Stream.of("a", "b", "c").toList();
        for (String s : stringList) {
            System.out.println(s);
        }

        System.out.println(VUOTA);
        System.out.println("algosMoreBetter");
        for (String s : Stream.of("a", "b", "c").toList()) {
            System.out.println(s);
        }
    }

    @Test
    @Order(17)
    @DisplayName("17 - forEach")
    void forEach() {
        List<String> nomi = new ArrayList<>();
        List<String> nomiBase = Arrays.asList("Mario", "Antonio", "Giovanni", "Roberta", "Carla", "Luigi", "Dario",
                "Beatrice", "Carlotta", "Emilio", "Francesca", "Nicola", "Paolo", "Marco", "Luca"
        );
        long inizio;
        int cicli = 1000;
        String message;
        String message2;
        String message3;

        for (int k = 0; k < cicli; k++) {
            nomi.addAll(nomiBase);
        }

        inizio = System.currentTimeMillis();
        // Old style
        for (String nome : nomi) {
            System.out.println(nome);
        }
        // Old style
        message = dateService.deltaTextEsatto(inizio);

        inizio = System.currentTimeMillis();
        // Lambda Expression
        nomi.forEach(name -> System.out.println(name));
        // Lambda Expression
        message2 = dateService.deltaTextEsatto(inizio);

        inizio = System.currentTimeMillis();
        // Method Reference
        nomi.forEach(System.out::println);
        // Method Reference
        message3 = dateService.deltaTextEsatto(inizio);

        System.out.println(String.format("Ho impiegato esattamente %s millisecondi", message));
        System.out.println(String.format("Ho impiegato esattamente %s millisecondi", message2));
        System.out.println(String.format("Ho impiegato esattamente %s millisecondi", message3));
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
    }


    public class Student {

        private int id;

        private String name;

        private String gender;

        private int age;

        public Student(int id, String name, String gender, int age) {
            super();
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student [id=" + id + ", name=" + name + ", gender=" + gender + ", age=" + age + "]";
        }

    }


}