package com.taxi24;

import com.taxi24.entity.*;
import com.taxi24.enums.DriverStatus;
import com.taxi24.enums.PricingCurrency;
import com.taxi24.enums.PricingType;
import com.taxi24.enums.TripStatus;
import com.taxi24.repository.*;
import com.taxi24.service.*;
import com.taxi24.util.DistanceUtil;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Taxi24 Application Test Cases")
class Taxi24TestCases {


    @Autowired
    private DriverService driverService;

    @Autowired
    private RiderService riderService;

    @Autowired
    private TripService tripService;

    @Autowired
    private InvoicePricingService pricingService;

    @Autowired
    private InvoiceService invoiceService;


    @MockBean
    private InvoiceRepository invoiceRepo;


    @MockBean
    private InvoicePricingRepository pricingRepo;

    @MockBean
    private TripRepository tripRepo;

    @MockBean
    private RiderRepository riderRepo;

    @MockBean
    private DriverRepository driverRepo;


    @Nested
    @DisplayName("Driver Test Cases")
    class DriverServiceTests {


        @DisplayName("save driver test method")
        @RepeatedTest(3)
        void saveTest(RepetitionInfo repetitionInfo) {

            Driver driver = new Driver("Mahmoud Ahmed", "01100509012", DriverStatus.valueOf("AVAILABLE"),
                    31.2312, 30.111);
            driver.setCreatedDateTime(new Date());

            switch (repetitionInfo.getCurrentRepetition()) {
                case 1:
                    assertEquals("Mahmoud Ahmed", driver.getDriverName(), "Driver Name can not be null");
                    break;
                case 2:
                    assertEquals("01100509012", driver.getDriverPhone(), "Driver phone can not be null");
                    break;
                case 3:
                    Driver resultDriver = new Driver(driver, 1);
                    Mockito.when(driverRepo.save(driver)).thenReturn(resultDriver);
                    System.out.println(resultDriver);
                    assertEquals(resultDriver, driverService.addDriver(driver).getResponse(), "Saved entity can not be null");
                    break;
            }

        }

        @Test
        @DisplayName("get all drivers test method")
        void getAllDriversTest() {

            Mockito.when(driverRepo.findAll()).thenReturn(
                    Stream.of(new Driver(1, "Mahmoud Ahmed", "01100509012", DriverStatus.valueOf("UNAVAILABLE"),
                                    31.2312, 30.111, new Date(), new Date()),
                            new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.031, 31.121, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date()),
                            new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

            assertEquals(4, ((List<Driver>) driverService.getAllDrivers().getResponse()).size(), " driver number must be 4");
        }


        @DisplayName("get all available drivers test method")
        @Test
        void getAllAvailableDriversTest() {

            Mockito.when(driverRepo.findAllByDriverStatus(DriverStatus.valueOf("AVAILABLE"))).thenReturn(
                    Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.031, 31.121, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date()),
                            new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

            List<DriverStatus> availableStatus = Arrays.asList(DriverStatus.AVAILABLE, DriverStatus.AVAILABLE, DriverStatus.AVAILABLE);

            List<Driver> availableDrivers = (List<Driver>) driverService.getAllAvailableDrivers("AVAILABLE").getResponse();
            //get list of status of returned available drivers
            List<DriverStatus> driverStatuses =
                    availableDrivers.stream()
                            .map(Driver::getDriverStatus)
                            .collect(Collectors.toList());
            assertEquals(availableStatus, driverStatuses, "Driver Status must be AVAILABLE");
        }

        @DisplayName("get all available drivers test method")
        @RepeatedTest(2)
        void getAllAvailableDriversWithin3KMTest(RepetitionInfo info) {

            double specificLat = 31.112;
            double specificLng = 31.000;
            Mockito.when(driverRepo.findAllAvailableDriversWithin3KM(specificLat, specificLng)).thenReturn(
                    Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.025, 31.110, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date())).collect(Collectors.toList()));


            List<Driver> availableDriversWithin3KM = (List<Driver>) driverService.getAllAvailableDriversWithin3KM(31.112, 31.000).getResponse();
            //get list of status of returned available drivers
            List<Double> driversLat =
                    availableDriversWithin3KM.stream()
                            .map(Driver::getDriverLat)
                            .collect(Collectors.toList());
            List<Double> driversLng =
                    availableDriversWithin3KM.stream()
                            .map(Driver::getDriverLng)
                            .collect(Collectors.toList());

            if (info.getCurrentRepetition() == 1) {
                boolean moreThan3KM = false;
                for (int i = 0; i < driversLat.size(); i++) {
                    double distance = DistanceUtil.distance(specificLat, driversLat.get(i), specificLng, driversLng.get(i));
                    System.out.println(distance);
                    if (distance > 3)
                        moreThan3KM = true;
                }

                assertFalse(moreThan3KM, "all drivers must be available and inside 3 km");
            } else {

                List<DriverStatus> availableStatus = Arrays.asList(DriverStatus.AVAILABLE, DriverStatus.AVAILABLE);
                List<DriverStatus> driverStatuses =
                        availableDriversWithin3KM.stream()
                                .map(Driver::getDriverStatus)
                                .collect(Collectors.toList());
                assertEquals(availableStatus, driverStatuses, "all drivers must be available and inside 3 km");


            }
        }


        @Test
        @DisplayName("get driver by id test method")
        void getDriverByIdTest() {

            Mockito.when(driverRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(driverRepo.findById((long) 1)).thenReturn(
                    Optional.of(new Driver(1, "Mahmoud Ahmed", "01100509012", DriverStatus.valueOf("UNAVAILABLE"),
                            31.2312, 30.111, new Date(), new Date())));

            assertEquals(1, ((Driver) driverService.getDriverById(1).getResponse()).getDriverId(), " driver id must be 1");
        }


    }





    @Nested
    @DisplayName("Rider Test Cases")
    public class RiderServiceTests {

        @DisplayName("save rider test method")
        @RepeatedTest(3)
        void saveTest(RepetitionInfo repetitionInfo) {

            Rider rider = new Rider("Mailika Ahmed", "01100501012",
                    31.0312, 30.011);
            rider.setCreatedDateTime(new Date());

            switch (repetitionInfo.getCurrentRepetition()){
                case 1:
                    assertEquals("Mailika Ahmed",  rider.getRiderName(), "Rider Name can not be null");
                    break;
                case 2:
                    assertEquals("01100501012",  rider.getRiderPhone(), "Rider phone can not be null");
                    break;
                case 5:
                    Rider resultRider = new Rider(1, "Mailika Ahmed", "01100501012",
                            31.0312, 30.011, rider.getCreatedDateTime(), new Date(), 0);
                    Mockito.when(riderRepo.save(rider)).thenReturn(resultRider);
                    assertEquals(resultRider,  riderService.addRider(rider).getResponse(), "Saved entity can not be null");
                    break;
            }

        }

        @Test
        @DisplayName("get all riders test method")
        void getAllDriversTest(){

            Mockito.when(riderRepo.findAll()).thenReturn(
                    Stream.of(new Rider(1, "Mailika Ahmed", "01100501012",
                                    31.0312, 30.011, new Date(), new Date(), 0),
                            new Rider(2, "Samir Ali", "01100508012",
                                    31.0302, 30.019, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(2, ((List<Driver>)riderService.getRiders().getResponse()).size(), " riders number must be 2");
        }


        @Test
        @DisplayName("get rider by id test method")
        void getDriverByIdTest(){

            Mockito.when(riderRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(riderRepo.findById((long) 1)).thenReturn(
                    Optional.of(new Rider(1, "Mailika Ahmed", "01100501012",
                            31.0312, 30.011, new Date(), new Date(), 0)));
            System.out.println(riderService.getRiderById(1).getResponse());
            assertEquals(1, ((Rider)riderService.getRiderById(1).getResponse()).getRiderId(), " rider id must be 1");
        }





    @DisplayName("Get The Three Closest Available Drivers For Specific Rider Test Method")
    @Test
    void getTheThreeClosestAvailableDriversForSpecificRiderTest(){

        Mockito.when(driverRepo.findTheThreeClosestAvailableDriversForSpecificRider((long) 1)).thenReturn(
                Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                31.031, 31.121, new Date(), new Date()),
                        new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                31.021, 31.111, new Date(), new Date    ()),
                        new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

        assertEquals(3, ((List<Rider>)riderService.findTheThreeClosestAvailableDrivers(1).getResponse()).size(), "Drivers number must be 3");
    }


        @DisplayName("Get The Three Closest Drivers For Specific Rider Test Method")
        @Test
        void getTheThreeClosestDriversForSpecificRiderTest(){

            Mockito.when(driverRepo.findTheThreeClosestDriversForSpecificRider((long) 1)).thenReturn(
                    Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.031, 31.121, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date    ()),
                            new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

            assertEquals(3, ((List<Rider>)riderService.findTheThreeClosestDrivers(1).getResponse()).size(), "Drivers number must be 3");
        }
    }



    @Nested
    @DisplayName("Trip Test Cases")
    public class TripServiceTests {

        @DisplayName("create trip test method")
        @Test
        void createTripTest() {

            Rider rider = new Rider(1,"Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1,"Mahmoud Ahmed", "01100509012", DriverStatus.valueOf("AVAILABLE"),
                    31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider);
            trip.setCreatedDateTime(new Date());

                    Trip resultTrip = new Trip(1, TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, trip.getCreatedDateTime());
                    resultTrip.getDriver().setDriverStatus(DriverStatus.UNAVAILABLE);
                    Mockito.when(tripRepo.save(trip)).thenReturn(resultTrip);
                    assertEquals(resultTrip,  tripService.addTrip(trip).getResponse(), "Saved entity can not be null");


        }

        @Test
        @DisplayName("start trip test method")
        void startTripTest(){

            Rider rider = new Rider(1,"Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1,"Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(1, TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Mockito.when(tripRepo.existsByTripIdAndTripStatus(1, TripStatus.ASSIGNED)).thenReturn(true);
            Mockito.when(tripRepo.findById((long)1)).thenReturn(
                    Optional.of(trip));
            Trip resultTrip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, trip.getCreatedDateTime());
            trip.setStartDateTime(new Date());
            resultTrip.setStartDateTime(new Date());
            Mockito.when(tripRepo.save(trip)).thenReturn(
                    resultTrip);
            assertEquals(resultTrip, tripService.startTrip(1).getResponse(), "started trip must be the same with result Object");
        }



        @Test
        @DisplayName("complete trip test method")
        void completeTripTest(){

            Rider rider = new Rider(1,"Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1,"Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Mockito.when(tripRepo.existsByTripIdAndTripStatus(1, TripStatus.ACTIVE)).thenReturn(true);
            Mockito.when(tripRepo.findById((long)1)).thenReturn(
                    Optional.of(trip));
            Trip resultTrip = new Trip(1, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, trip.getCreatedDateTime());
            trip.setEndDateTime(new Date());
            resultTrip.setEndDateTime(new Date());
            Mockito.when(tripRepo.save(trip)).thenReturn(
                    resultTrip);
            driver.setDriverStatus(DriverStatus.AVAILABLE);
            assertEquals(resultTrip, tripService.completeTrip(1).getResponse(), "Completed trip must be the same with result Object");
        }


        @DisplayName("get all active trips test method")
        @Test
        void getActiveTripsTest() {

            Rider rider1 = new Rider(1,"Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver1 = new Driver(1,"Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Rider rider2 = new Rider(10,"Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2,"Said Ali", "01100505012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.5312, 30.7111, new Date(), new Date());


            Mockito.when(tripRepo.findAllByTripStatus(TripStatus.ACTIVE)).thenReturn(

                    Stream.of(new Trip(1, TripStatus.ACTIVE, 31.1312, 30.211, 31.3111, 30.1141, driver1, rider1, new Date()),
                            new Trip(2, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date())).collect(Collectors.toList()));

            List<TripStatus> activeStatus = Arrays.asList(TripStatus.ACTIVE, TripStatus.ACTIVE);

            List<Trip> availableTrips = (List<Trip>) tripService.getActiveTrips("ACTIVE").getResponse();
            //get list of status of returned available drivers
            List<TripStatus> tripStatuses =
                    availableTrips.stream()
                            .map(Trip::getTripStatus)
                            .collect(Collectors.toList());
            assertEquals(activeStatus, tripStatuses, "Trip Status must be ACTIVE");
        }


    }




    @Nested
    @DisplayName("Pricing Test Cases")
    public class InvoicePricingServiceTests {

        @DisplayName("adding price test method")
        @Test
        void addPriceTest() {

            InvoicePricing pricing = new InvoicePricing(PricingType.TAXI_START, 7.0, PricingCurrency.EGP);
            pricing.setCreatedDateTime(new Date());

            InvoicePricing resultPricing = new InvoicePricing(1, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0);
            Mockito.when(pricingRepo.save(pricing)).thenReturn(resultPricing);
            assertEquals(resultPricing,  pricingService.addPricing(pricing).getResponse(), "Saved entity can not be null");


        }


        @Test
        @DisplayName("get all invoice prices test method")
        void getAllPricesTest(){

            Mockito.when(pricingRepo.findAll()).thenReturn(
                    Stream.of( new InvoicePricing(1, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new InvoicePricing(2, PricingType.TAXI_1KM, 3.50, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new InvoicePricing(3, PricingType.TAXI_1HOUR_WAITING, 20.0, PricingCurrency.EGP, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(3, ((List<InvoicePricing>)pricingService.getAllPricing().getResponse()).size(), " Invoice Pricing number must be 3");
        }


        @Test
        @DisplayName("get invoice price by id test method")
        void getPriceByIdTest(){

            Mockito.when(pricingRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(pricingRepo.findById((long) 1)).thenReturn(
                    Optional.of(new InvoicePricing(1, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0)));
            assertEquals(1, ((InvoicePricing)pricingService.getPricingById(1).getResponse()).getPriceId(), " price id must be 1");
        }



        @DisplayName("get prices by type test method")
        @Test
        void getPriceByTypesTest() {


            Mockito.when(pricingRepo.findByPricetype(PricingType.TAXI_1HOUR_WAITING)).thenReturn(
                    new InvoicePricing(3, PricingType.TAXI_1HOUR_WAITING, 20.0, PricingCurrency.EGP, new Date(), new Date(), 0));



            assertEquals(PricingType.TAXI_1HOUR_WAITING, ((InvoicePricing)pricingService.getPricingByType("TAXI_1HOUR_WAITING").getResponse()).getPrice_type(), "Price Type must be TAXI_1HOUR_WAITING");
        }


    }



    @Nested
    @DisplayName("Invoice Test Cases")
    public class InvoiceServiceTests {

        @Test
        @DisplayName("get all invoices test method")
        void getAllPricesTest(){

            Rider rider2 = new Rider(10,"Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2,"Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());


            Mockito.when(invoiceRepo.findAll()).thenReturn(
                    Stream.of( new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new Invoice(2, 99.7, trip, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new Invoice(3, 15, trip, PricingCurrency.EGP, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(3, ((List<Invoice>)invoiceService.getAllInvoices().getResponse()).size(), " Invoice number must be 3");
        }


        @Test
        @DisplayName("get invoice by id test method")
        void getInvoiceByIdTest(){


            Rider rider2 = new Rider(10,"Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2,"Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());

            Mockito.when(invoiceRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(invoiceRepo.findById((long) 1)).thenReturn(
                    Optional.of(new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0)));
            assertEquals(1, ((Invoice)invoiceService.getInvoiceById(1).getResponse()).getInvoiceId(), " invoice id must be 1");
        }


        @Test
        @DisplayName("get Invoice by trip test method")
        void getInvoiceByTripTest() {


            Rider rider2 = new Rider(10,"Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2,"Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());

            Mockito.when(tripRepo.existsById((long) 2)).thenReturn(true);
            Mockito.when(tripRepo.findById((long) 2)).thenReturn(
                    Optional.of(trip));


            Mockito.when(invoiceRepo.findByTrip(trip)).thenReturn(
                    new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0));

            System.out.println(invoiceService.getInvoiceByTrip(2).getResponse());


            assertEquals(trip.getTripId(), ((Invoice)invoiceService.getInvoiceByTrip(2).getResponse()).getTrip().getTripId(), "trips must be the same");
        }


    }

}
