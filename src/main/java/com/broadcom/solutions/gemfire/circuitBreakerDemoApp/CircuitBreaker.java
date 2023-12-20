package com.broadcom.solutions.gemfire.circuitBreakerDemoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Circuit breaker implementation object.
 *
 * @author Gregory Green
 */
public class CircuitBreaker
{

    private final String primaryLocators;
    private final String secondaryLocators;

    private final ExecutorService executor;
    private static ConfigurableApplicationContext context=null;

    public final static String CURRENTCONNECTION_PRIMARY_POOL="PRIMARY";
    public final static String CURRENTCONNECTION_SECONDARY_POOL="SECONDARYPOOL";
    public static String currentConnection = null;

    /**
     * Constructor
     * @param primaryLocators the primary locator connection string
     * @param secondaryLocators the secondary locator connection string
     */
    public CircuitBreaker(String primaryLocators, String secondaryLocators)
    {
        this.primaryLocators = primaryLocators;
        this.secondaryLocators = secondaryLocators;
        this.executor = Executors.newCachedThreadPool();
    }//-------------------------------------------


    /**
     * Set the spring context singleton
     * @param appContext the application context
     */
    public static void setContext(ConfigurableApplicationContext appContext)
    {
        CircuitBreaker.context = appContext;
    }//------------------------------------

//-----------------------------------------------
    /**
     * Open circuit to switch from primary to secondary
     */
    public void openCircuit()
    throws Exception
    {
        System.out.println("current connection:"+CircuitBreaker.currentConnection);
        if(CircuitBreaker.currentConnection.equals(CircuitBreaker.CURRENTCONNECTION_PRIMARY_POOL)){
            this.openToSecondary();
        }else if (CircuitBreaker.currentConnection.equals(CircuitBreaker.CURRENTCONNECTION_SECONDARY_POOL)){
            this.openPrimary();
        }else{
            System.out.println("OTHER clusters");
        }

    }//-------------------------------------------

    public void SwitchToAnotherPool(String newPoolname){
        if (newPoolname.equals(CircuitBreaker.CURRENTCONNECTION_PRIMARY_POOL)){
            this.openPrimary();
        }else if (newPoolname.equals(CircuitBreaker.CURRENTCONNECTION_SECONDARY_POOL)){
            this.openToSecondary();
        }else{
            System.out.println("unrecognized clusters:"+newPoolname);
        }
    }

//    public void openCircuit()
//            throws Exception
//    {
//        System.out.println("CHECKING IF primaur is UP");
//
//        if(this.isPrimaryUp()){
//            System.out.println("Primary is UP, so NOT SWITHING");
//            return;
//        }
//
//        this.openToSecondary();
//    }

    /**
     * Open current connection to secondary
     */
    public void openPrimary()
    {
        System.out.println("Opening to primary");

        Runnable openAndSwitchToPrimary = () -> {
            CircuitBreaker.context.close();

            CircuitBreaker.context = null;
            System.gc();

            String[] sourceArgs = {"--spring.data.gemfire.locators="+this.primaryLocators};

            CircuitBreaker.context = SpringApplication.run(DemoApp.class,sourceArgs );
            CircuitBreaker.currentConnection = CircuitBreaker.CURRENTCONNECTION_PRIMARY_POOL;

        };
        this.executor.submit(openAndSwitchToPrimary);



    }//-------------------------------------------

    /**
     * Open current connection to secondary
     */
    public void openToSecondary()
    {
        System.out.println("Opening to secondary");

        Runnable openAndSwitchToSecondary = () -> {
            CircuitBreaker.context.close();

            CircuitBreaker.context = null;
            System.gc();

            String[] sourceArgs = {"--spring.data.gemfire.locators="+this.secondaryLocators};

            CircuitBreaker.context = SpringApplication.run(DemoApp.class,sourceArgs );
            CircuitBreaker.currentConnection = CircuitBreaker.CURRENTCONNECTION_SECONDARY_POOL;

        };
        this.executor.submit(openAndSwitchToSecondary);

       // startCloseCircuitRunner();


    }//-------------------------------------------
    /**
     * Determine if the secondary cluster is up and running
     * @return true if secondary is up
     */
    public boolean isSecondaryUp()
            throws Exception
    {
        return CircuitBreaker.currentConnection.equals(CircuitBreaker.CURRENTCONNECTION_SECONDARY_POOL);
    }

    /**
     * Determine if the primary cluster is and up
     * @return true if primary is up
     * @throws Exception when unknown error occurs (ex: circuit breaker function not deployed)
     */
    public boolean isPrimaryUp()
            throws Exception
    {

        return CircuitBreaker.currentConnection.equals(CircuitBreaker.CURRENTCONNECTION_PRIMARY_POOL);

    }//-------------------------------------------
}
