package wlstest.functional.java8; 
    
import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.math3.primes.Primes;

/**
 *
 * @author fwang
 */
public class LambdaTest {
    
    public void lambdaType() {
        Runnable r1 = () -> {System.out.println("Hello Lambda!");};
        r1.run();
        Object obj = r1;
        // You can take it as a type of Object, but sometimes you cannot use it that way, e.g.
        // System.out.println( () -> {} ); //no way
        System.out.println( obj );
        System.out.println( obj.hashCode() );
        
        Runnable r2 = () -> {System.out.println("Hello Lambda!");};
        System.out.println( r2 );
        System.out.println( r2.hashCode() );
        
        System.out.println( r1 == r2 ); //false
        System.out.println( r1.equals(r2) ); //false
        
        Runnable2 r22 = () -> {System.out.println("Hello Lambda!");};
  
        //Object o = () -> { System.out.println("hi"); };		  // Illegal
        Object o = (Runnable) () -> { System.out.println("hi"); };	  // Legal because disambiguated

    }
    
    // Use lambda to define a recursive function
    protected UnaryOperator<Integer> factorial = i -> i == 0 ? 1 : i * this.factorial.apply( i - 1 );
    
    static UnaryOperator<Integer> factorial2 = i -> i == 0 ? 1 : i * LambdaTest.factorial2.apply( i - 1);
    
    public void lambdaUsage() throws Exception {
        // Nested lambda
        Callable<Runnable> c1 = () -> () -> { System.out.println("Nested lambda"); };
        c1.call().run();
        
        // Conditional expression
        Callable<Integer> c2 = true ? (() -> 42) : (() -> 24);
        System.out.println(c2.call());
        
        // Recursive function call
        System.out.println(factorial.apply(3));
    }
    
    /*
     * a local variable is effectively final if its initial value is never changed (including within the body of 
     * a lambda expression)—in other words, declaring it final would not cause a compilation failure.
     *
     * The reason is concurency. as functions may execute in multiple threads. Lambda (along with collection bulk operation)
     * is added to Java mainly for parallel.
    */
    int tmp1 = 1; //member variable
    static int tmp2 = 2; //static member variable
    public void testCapture() {
        int tmp3 = 3; //effectively final local variable
        final int tmp4 = 4; //final local variable
        int tmp5 = 5; //non-final local variable
        
        Function<Integer, Integer> f1 = i -> i + tmp1; 
        Function<Integer, Integer> f2 = i -> i + tmp2; 
        Function<Integer, Integer> f3 = i -> i + tmp3; 
        Function<Integer, Integer> f4 = i -> i + tmp4; 
        Function<Integer, Integer> f5 = i -> {
            //tmp5  += i; // compile error, for it makes tmp5 not effectively final
            return tmp5;
        };
        //tmp5 = 9; //compile error, for it makes tmp5 not effectively final
    }
    
    
    public void methodReference() {
        //c1 and c2 are equivalent (static)
        Comparator<Integer> c1 = Integer::compare;
        Comparator<Integer> c2 = (x, y) -> Integer.compare(x, y);
        
        List<Point> l = Arrays.asList(new Point(1,2), new Point(3,4));
        
        //following two statements are equivalent
        l.forEach(e -> System.out.println(e));
        l.forEach(System.out::println);
        
        //following two statements are equivalent
        l.forEach(p -> p.hashCode());
        l.forEach(Point::hashCode);
        
        /*
        List<String> names = Arrays.asList("Smith", "Adams", "Crawford");
        List<Person> people = peopleDAO.find("London");
 
        // Using anyMatch and method reference
        List<Person> anyMatch = people.stream().filter(p -> (names.stream().anyMatch(p.name::contains))).collect(Collectors.toList());
 
        // Using reduce
        List<Person> reduced = people.stream().filter(p -> names.stream().reduce(false, (Boolean b, String keyword) -> b || p.name.contains(keyword), (l, r) -> l | r)).collect(Collectors.toList()); 
        */
        
        List<String> strList = Arrays.asList("1","2","3");
        //All equivalent
        List<Integer> intList = strList.stream().map(Integer::new).collect(Collectors.toList());
        List<Integer> intList2 = strList.stream().map(s -> new Integer(s)).collect(Collectors.toList());
        List<Integer> intList3 = strList.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> intList4 = strList.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
       
    }
    
    //Given an array, get distinct primary numbers in it.  
    public void distinctPrimary(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        List<Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> Primes.isPrime(e))
                .distinct()
                .collect(Collectors.toList());
        System.out.println("distinctPrimary result is: " + r);
    }
    
    public void distinctPrimary2(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        Map<Integer, Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> Primes.isPrime(e))
                .distinct()
                .collect(Collectors.toMap(e->e, e->e));
        System.out.println("distinctPrimary result is: " + r);
    }
    
    //Equivalent to distinctPrimary, but uses method ref
    public void distinctPrimary_methodRef(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        List<Integer> r = l.stream().map(Integer::new).filter(Primes::isPrime).distinct().
            collect(Collectors.toList());
        System.out.println("distinctPrimary2 result is: " + r);
    }
    
    //Given an array, get the sum of all the distinct primary numbers in it.  
    public void distinctPrimarySum(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        int sum = l.stream().map(Integer::new).filter(Primes::isPrime).distinct()
            .reduce(0, (x,y) -> x+y); // equivalent to .sum()   
        System.out.println("distinctPrimarySum result is: " + sum);
    }
    
    //Given an array, get distinct primary numbers and occurrence in it.  
    public void primaryOccurrence(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        Map<Integer, Integer> r = l.stream().map(Integer::new).filter(Primes::isPrime).collect(
                Collectors.groupingBy(p->p, Collectors.summingInt(p->1))
        );
        System.out.println("primaryOccurrence result is: " + r);
    }
    
    // Find the numbers of male and female of age 25-35
    public void boysAndGirls() {
       
        //populate persons
        List<Person> persons = Person.randomPersons(100);
        System.out.println(persons);
        
        Map<Integer, Integer> result = persons.parallelStream().filter(p -> p.getAge()>=25 && p.getAge()<=35).
            collect(
                Collectors.groupingBy(p->p.getSex(), Collectors.summingInt(p->1))
        );
        System.out.print("boysAndGirls result is " + result);
        System.out.println(", ratio (male : female) is " + (float)result.get(Person.MALE)/result.get(Person.FEMALE));
    }
    
    public void testThread() {
        Thread t1 = new Thread(new Runnable () {
            @Override
            public void run() {
                System.out.println("This is from an anonymous class.");
            }
        });
        
        Thread t2 = new Thread( () -> {
            System.out.println("This is from an anonymous method (lambda exp).");
        });
        
        t1.start();
        t2.start();
    }

    public void generator() {
        //random numbers
        Stream.generate(Math::random).limit(5).forEach(System.out::println);

    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        LambdaTest lt = new  LambdaTest();
        lt.lambdaType();
        lt.lambdaUsage();
        lt.methodReference();
        lt.testCapture();
        lt.distinctPrimary("0", "3", "55", "73", "3", "8", "2", "42");
        lt.distinctPrimarySum("0", "3", "55", "73", "3", "8", "2", "42");
        lt.primaryOccurrence("0", "3", "55", "73", "3", "8", "2", "42");
        
        System.out.println("111");
        Stream<Double> s = Stream.generate( () -> Math.random() );
        System.out.println("222");
        s.toArray();
        System.out.println("333");
        lt.boysAndGirls();
        lt.testThread();
        
        lt.generator();
    }

}//end
