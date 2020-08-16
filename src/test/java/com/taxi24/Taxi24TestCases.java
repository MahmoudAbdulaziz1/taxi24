package com.taxi24;

import com.taxi24.controller.*;
import com.taxi24.entity.*;
import com.taxi24.enums.*;
import com.taxi24.service.*;
import com.taxi24.repository.*;
import com.taxi24.util.DistanceUtil;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

            switch (repetitionInfo.getCurrentRepetition()) {
                case 1:
                    assertEquals("Mailika Ahmed", rider.getRiderName(), "Rider Name can not be null");
                    break;
                case 2:
                    assertEquals("01100501012", rider.getRiderPhone(), "Rider phone can not be null");
                    break;
                case 5:
                    Rider resultRider = new Rider(1, "Mailika Ahmed", "01100501012",
                            31.0312, 30.011, rider.getCreatedDateTime(), new Date(), 0);
                    Mockito.when(riderRepo.save(rider)).thenReturn(resultRider);
                    assertEquals(resultRider, riderService.addRider(rider).getResponse(), "Saved entity can not be null");
                    break;
            }

        }

        @Test
        @DisplayName("get all riders test method")
        void getAllDriversTest() {

            Mockito.when(riderRepo.findAll()).thenReturn(
                    Stream.of(new Rider(1, "Mailika Ahmed", "01100501012",
                                    31.0312, 30.011, new Date(), new Date(), 0),
                            new Rider(2, "Samir Ali", "01100508012",
                                    31.0302, 30.019, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(2, ((List<Driver>) riderService.getRiders().getResponse()).size(), " riders number must be 2");
        }


        @Test
        @DisplayName("get rider by id test method")
        void getDriverByIdTest() {

            Mockito.when(riderRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(riderRepo.findById((long) 1)).thenReturn(
                    Optional.of(new Rider(1, "Mailika Ahmed", "01100501012",
                            31.0312, 30.011, new Date(), new Date(), 0)));
            System.out.println(riderService.getRiderById(1).getResponse());
            assertEquals(1, ((Rider) riderService.getRiderById(1).getResponse()).getRiderId(), " rider id must be 1");
        }


        @DisplayName("Get The Three Closest Available Drivers For Specific Rider Test Method")
        @Test
        void getTheThreeClosestAvailableDriversForSpecificRiderTest() {

            Mockito.when(driverRepo.findTheThreeClosestAvailableDriversForSpecificRider((long) 1)).thenReturn(
                    Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.031, 31.121, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date()),
                            new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

            assertEquals(3, ((List<Rider>) riderService.findTheThreeClosestAvailableDrivers(1).getResponse()).size(), "Drivers number must be 3");
        }


        @DisplayName("Get The Three Closest Drivers For Specific Rider Test Method")
        @Test
        void getTheThreeClosestDriversForSpecificRiderTest() {

            Mockito.when(driverRepo.findTheThreeClosestDriversForSpecificRider((long) 1)).thenReturn(
                    Stream.of(new Driver(2, "Ahmed Said", "01100509011", DriverStatus.valueOf("AVAILABLE"),
                                    31.031, 31.121, new Date(), new Date()),
                            new Driver(2, "Alaa Ali", "01100509911", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.111, new Date(), new Date()),
                            new Driver(2, "Islam Said", "01100509711", DriverStatus.valueOf("AVAILABLE"),
                                    31.021, 31.211, new Date(), new Date())).collect(Collectors.toList()));

            assertEquals(3, ((List<Rider>) riderService.findTheThreeClosestDrivers(1).getResponse()).size(), "Drivers number must be 3");
        }
    }


    @Nested
    @DisplayName("Trip Test Cases")
    public class TripServiceTests {

        @DisplayName("create trip test method")
        @Test
        void createTripTest() {

            Rider rider = new Rider(1, "Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1, "Mahmoud Ahmed", "01100509012", DriverStatus.valueOf("AVAILABLE"),
                    31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider);
            trip.setCreatedDateTime(new Date());

            Trip resultTrip = new Trip(1, TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, trip.getCreatedDateTime());
            resultTrip.getDriver().setDriverStatus(DriverStatus.UNAVAILABLE);
            Mockito.when(tripRepo.save(trip)).thenReturn(resultTrip);
            assertEquals(resultTrip, tripService.addTrip(trip).getResponse(), "Saved entity can not be null");


        }

        @Test
        @DisplayName("start trip test method")
        void startTripTest() {

            Rider rider = new Rider(1, "Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1, "Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(1, TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Mockito.when(tripRepo.existsByTripIdAndTripStatus(1, TripStatus.ASSIGNED)).thenReturn(true);
            Mockito.when(tripRepo.findById((long) 1)).thenReturn(
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
        void completeTripTest() {

            Rider rider = new Rider(1, "Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver = new Driver(1, "Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Trip trip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Mockito.when(tripRepo.existsByTripIdAndTripStatus(1, TripStatus.ACTIVE)).thenReturn(true);
            Mockito.when(tripRepo.findById((long) 1)).thenReturn(
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

            Rider rider1 = new Rider(1, "Mailika Ahmed", "01100501012",
                    31.0312, 30.011, new Date(), new Date(), 0);
            Driver driver1 = new Driver(1, "Mahmoud Ahmed", "01100509012",
                    DriverStatus.valueOf("UNAVAILABLE"), 31.2312, 30.111, new Date(), new Date());

            Rider rider2 = new Rider(10, "Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2, "Said Ali", "01100505012",
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
            assertEquals(resultPricing, pricingService.addPricing(pricing).getResponse(), "Saved entity can not be null");


        }


        @Test
        @DisplayName("get all invoice prices test method")
        void getAllPricesTest() {

            Mockito.when(pricingRepo.findAll()).thenReturn(
                    Stream.of(new InvoicePricing(1, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new InvoicePricing(2, PricingType.TAXI_1KM, 3.50, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new InvoicePricing(3, PricingType.TAXI_1HOUR_WAITING, 20.0, PricingCurrency.EGP, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(3, ((List<InvoicePricing>) pricingService.getAllPricing().getResponse()).size(), " Invoice Pricing number must be 3");
        }


        @Test
        @DisplayName("get invoice price by id test method")
        void getPriceByIdTest() {

            Mockito.when(pricingRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(pricingRepo.findById((long) 1)).thenReturn(
                    Optional.of(new InvoicePricing(1, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0)));
            assertEquals(1, ((InvoicePricing) pricingService.getPricingById(1).getResponse()).getPriceId(), " price id must be 1");
        }


        @DisplayName("get prices by type test method")
        @Test
        void getPriceByTypesTest() {


            Mockito.when(pricingRepo.findByPriceType(PricingType.TAXI_1HOUR_WAITING)).thenReturn(
                    new InvoicePricing(3, PricingType.TAXI_1HOUR_WAITING, 20.0, PricingCurrency.EGP, new Date(), new Date(), 0));


            assertEquals(PricingType.TAXI_1HOUR_WAITING, ((InvoicePricing) pricingService.getPricingByType("TAXI_1HOUR_WAITING").getResponse()).getPriceType(), "Price Type must be TAXI_1HOUR_WAITING");
        }


    }


    @Nested
    @DisplayName("Invoice Test Cases")
    public class InvoiceServiceTests {

        @Test
        @DisplayName("get all invoices test method")
        void getAllPricesTest() {

            Rider rider2 = new Rider(10, "Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());


            Mockito.when(invoiceRepo.findAll()).thenReturn(
                    Stream.of(new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new Invoice(2, 99.7, trip, PricingCurrency.EGP, new Date(), new Date(), 0),
                            new Invoice(3, 15, trip, PricingCurrency.EGP, new Date(), new Date(), 0)).collect(Collectors.toList()));

            assertEquals(3, ((List<Invoice>) invoiceService.getAllInvoices().getResponse()).size(), " Invoice number must be 3");
        }


        @Test
        @DisplayName("get invoice by id test method")
        void getInvoiceByIdTest() {


            Rider rider2 = new Rider(10, "Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());

            Mockito.when(invoiceRepo.existsById((long) 1)).thenReturn(true);
            Mockito.when(invoiceRepo.findById((long) 1)).thenReturn(
                    Optional.of(new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0)));
            assertEquals(1, ((Invoice) invoiceService.getInvoiceById(1).getResponse()).getInvoiceId(), " invoice id must be 1");
        }


        @Test
        @DisplayName("get Invoice by trip test method")
        void getInvoiceByTripTest() {


            Rider rider2 = new Rider(10, "Mohammed Ahmed", "01100501002",
                    31.0392, 30.0911, new Date(), new Date(), 0);
            Driver driver2 = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Trip trip = new Trip(2, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver2, rider2, new Date());

            Mockito.when(tripRepo.existsById((long) 2)).thenReturn(true);
            Mockito.when(tripRepo.findById((long) 2)).thenReturn(
                    Optional.of(trip));


            Mockito.when(invoiceRepo.findByTrip(trip)).thenReturn(
                    new Invoice(1, 100.50, trip, PricingCurrency.EGP, new Date(), new Date(), 0));

            System.out.println(invoiceService.getInvoiceByTrip(2).getResponse());


            assertEquals(trip.getTripId(), ((Invoice) invoiceService.getInvoiceByTrip(2).getResponse()).getTrip().getTripId(), "trips must be the same");
        }


    }


    ///////////////////////////////////////////////////////////


    @Nested
    @WebMvcTest(value = DriverController.class)
    @WithMockUser
    @DisplayName("Driver Rest APIs test")
    public class DriverRESTFullTestCases {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private DriverService driverService;


        @Test
        @DisplayName("Add Driver RESTFull  API Test")
        public void addDriverRESTFullTest() throws Exception {


            StringBuilder builder = new StringBuilder();
            builder.append("{\"driverName\":\"sameh\", \"driverPhone\":\"01110058710\", ");
            builder.append("\"driverStatus\":\"AVAILABLE\", \"driverLng\":31.201212, \"driverLat\":30.101011}");
            String driverBody = builder.toString();
            System.out.println(driverBody);

            Driver driver = new Driver("sameh", "01110058710",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011);
            Driver responseDriver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.DRIVER,
                    responseDriver);


            when(driverService.addDriver(driver)).thenReturn(response);
            this.mockMvc.perform(post("/driver")
                    .content(driverBody)
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                    .andDo(print()).andExpect(status().isOk());
        }


        @Test
        @DisplayName("Get Driver By ID RESTFull  API Test")
        public void getDriverByIdRESTFullTest() throws Exception {

            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.DRIVER,
                    new Driver(2, "Said Ali", "01100505012",
                            DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date()));

            when(driverService.getDriverById(1)).thenReturn(response);
            this.mockMvc.perform(get("/driver/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get Available Drivers RESTFull  API Test")
        public void getAllAvailableDriversRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            List<Driver> drivers = new ArrayList<>();
            drivers.add(driver);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.DRIVER, drivers);

            when(driverService.getAllAvailableDrivers("AVAILABLE")).thenReturn(response);
            this.mockMvc.perform(get("/driver/status/AVAILABLE")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get All Drivers RESTFull  API Test")
        public void getAllDriversRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            List<Driver> drivers = new ArrayList<>();
            drivers.add(driver);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.DRIVER, drivers);

            when(driverService.getAllDrivers()).thenReturn(response);
            this.mockMvc.perform(get("/driver")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }


        @Test
        @DisplayName("Get Available Drivers Within 3 KM RESTFull  API Test")
        public void getAllAvailableDriversWithin3KMRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "Said Ali", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());

            Driver driver2 = new Driver(2, "Said m.Ali", "01100505013",
                    DriverStatus.valueOf("AVAILABLE"), 31.5312, 30.7111, new Date(), new Date());


            List<Driver> drivers = new ArrayList<>();
            drivers.add(driver);
            drivers.add(driver2);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.DRIVER, drivers);

            when(driverService.getAllAvailableDriversWithin3KM(30, 31))
                    .thenReturn(response);
            this.mockMvc.perform(get("/driver/30/31")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }
    }


    @Nested
    @WebMvcTest(value = RiderController.class)
    @WithMockUser
    @DisplayName("Rider Rest APIs test")
    public class RiderRESTFullTestCases {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RiderService riderService;


        @Test
        @DisplayName("Add Rider RESTFull  API Test")
        public void addRiderRESTFullTest() throws Exception {


            StringBuilder builder = new StringBuilder();
            builder.append("{\"riderName\":\"sameh\", \"riderPhone\":\"01110058710\", ");
            builder.append(" \"riderLng\":31.201212, \"riderLat\":30.101011}");
            String riderBody = builder.toString();
            System.out.println(riderBody);

            Rider rider = new Rider("sameh", "01110058710", 31.201212, 30.101011);
            Rider responseRider = new Rider(2, "sameh", "01100505012",
                    31.201212, 30.101011, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.RIDER,
                    responseRider);


            when(riderService.addRider(rider)).thenReturn(response);
            this.mockMvc.perform(post("/rider")
                    .content(riderBody)
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                    .andDo(print()).andExpect(status().isOk());
        }


        @Test
        @DisplayName("Get Rider By ID RESTFull  API Test")
        public void getRiderByIdRESTFullTest() throws Exception {

            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.RIDER,
                    new Rider(2, "Said Ali", "01100505012",
                            31.5312, 30.7111, new Date(), new Date(), 0));

            when(riderService.getRiderById(1)).thenReturn(response);
            this.mockMvc.perform(get("/rider/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get All Rider RESTFull  API Test")
        public void getAllRidersRESTFullTest() throws Exception {

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            List<Rider> riders = new ArrayList<>();
            riders.add(rider);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.RIDER, rider);

            when(riderService.getRiders()).thenReturn(response);
            this.mockMvc.perform(get("/rider")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }


        @Test
        @DisplayName("Get The Closest 3 Drivers RESTFull  API Test")
        public void getTheThreeClosestDriversRESTFullTest() throws Exception {

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            List<Rider> riders = new ArrayList<>();
            riders.add(rider);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.RIDER, riders);

            when(riderService.findTheThreeClosestDrivers(1)).thenReturn(response);
            this.mockMvc.perform(get("/rider/closest/driver/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get The Closest 3 Available Drivers RESTFull  API Test")
        public void getTheThreeClosestAvailableDrivers() throws Exception {

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            List<Rider> riders = new ArrayList<>();
            riders.add(rider);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.RIDER, riders);

            when(riderService.findTheThreeClosestAvailableDrivers(1)).thenReturn(response);
            this.mockMvc.perform(get("/rider/closest/available/driver/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }
    }


    @Nested
    @WebMvcTest(value = TripController.class)
    @WithMockUser
    @DisplayName("Trip Rest APIs test")
    public class TripRESTFullTestCases {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TripService tripService;


        @Test
        @DisplayName("Add Trip RESTFull  API Test")
        public void addTripRESTFullTest() throws Exception {


            StringBuilder builder = new StringBuilder();


            builder.append("{\"tripStatus\" : \"ASSIGNED\", \"fromLng\" : \"31.333334\", \"fromLat\" : 30.133333, \"toLng\"  : 32.333334, \"toLat\"  :31.133333,\n");
            builder.append("    \"driver\" :{ \"driverId\": 1, \"driverName\": \"Ali Mahmoud\", \"driverPhone\": \"01110059711\",\n");
            builder.append("            \"driverStatus\": \"AVAILABLE\", \"driverLng\": 31.2312,\n");
            builder.append("            \"driverLat\": 30.111, \"createdDateTime\": \"2020-08-13T18:23:09.000+00:00\",\n");
            builder.append("            \"lastUpdateDateTime\": \"2020-08-13T18:23:09.000+00:00\"},\n");
            builder.append("    \"rider\":{ \"riderId\": 1, \"riderName\": \"Ahmed\", \"riderPhone\": \"01110059411\",\n");
            builder.append("        \"riderLng\": 31.3333, \"riderLat\": 30.1333, \"createdDateTime\": \"2020-08-13T19:03:43.000+00:00\",\n");
            builder.append("        \"lastUpdateDateTime\": \"2020-08-13T19:04:07.000+00:00\", \"closed\": 0 }}");


            String tripBody = builder.toString();
            System.out.println(tripBody);

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip trip = new Trip( TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider);

            Trip responseTrip = new Trip(1, TripStatus.ASSIGNED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());


            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.TRIP,
                    responseTrip);


            when(tripService.addTrip(trip)).thenReturn(response);
            this.mockMvc.perform(post("/trip")
                    .content(tripBody)
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                    .andDo(print()).andExpect(status().isOk());
        }


        @Test
        @DisplayName("Start Trip RESTFull  API Test")
        public void startTripRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip responseTrip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());


            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.TRIP,
                    responseTrip);

            when(tripService.startTrip(1)).thenReturn(response);
            this.mockMvc.perform(get("/trip/start/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }


        @Test
        @DisplayName("Complete Trip RESTFull  API Test")
        public void completeTripRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip responseTrip = new Trip(1, TripStatus.COMPLETED, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());


            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.TRIP,
                    responseTrip);

            when(tripService.completeTrip(1)).thenReturn(response);
            this.mockMvc.perform(get("/trip/complete/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get Active Trips RESTFull  API Test")
        public void getActiveTrip() throws Exception {

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip responseTrip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            List<Trip> trips = new ArrayList<>();
            trips.add(responseTrip);

            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.TRIP,
                    trips);

            when(tripService.getActiveTrips("ACTIVE")).thenReturn(response);
            this.mockMvc.perform(get("/trip/active")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }
    }


    @Nested
    @WebMvcTest(value = InvoicePricingController.class)
    @WithMockUser
    @DisplayName("Pricing Rest APIs test")
    public class PricingRESTFullTestCases {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private InvoicePricingService pricingService;


        @Test
        @DisplayName("Add Prices RESTFull  API Test")
        public void addPriceRESTFullTest() throws Exception {


            StringBuilder builder = new StringBuilder();
            builder.append("{\"priceType\":\"TAXI_START\", \"priceCost\":7, ");
            builder.append(" \"priceCurrency\":\"EGP\"}");
            String pricingBody = builder.toString();
            System.out.println(pricingBody);

            InvoicePricing pricing = new InvoicePricing(PricingType.TAXI_START, 7.0, PricingCurrency.EGP);
            InvoicePricing responsePricing = new InvoicePricing(2, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responsePricing);


            when(pricingService.addPricing(pricing)).thenReturn(response);
            this.mockMvc.perform(post("/pricing")
                    .content(pricingBody)
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                    .andDo(print()).andExpect(status().isOk());
        }


        @Test
        @DisplayName("Get Prices By ID RESTFull  API Test")
        public void getPricesByIdRESTFullTest() throws Exception {

            InvoicePricing responsePricing = new InvoicePricing(2, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responsePricing);

            when(pricingService.getPricingById(1)).thenReturn(response);
            this.mockMvc.perform(get("/pricing/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get All Prices RESTFull  API Test")
        public void getAllPricesRESTFullTest() throws Exception {
            InvoicePricing responsePricing = new InvoicePricing(2, PricingType.TAXI_START, 7.0, PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responsePricing);
            List<InvoicePricing> pricings = new ArrayList<>();
            pricings.add(responsePricing);

            when(pricingService.getAllPricing()).thenReturn(response);
            this.mockMvc.perform(get("/pricing")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }


        @Test
        @DisplayName("Get Price by its type RESTFull  API Test")
        public void getPriceByTypeRESTFullTest() throws Exception {

            InvoicePricing responsePricing = new InvoicePricing(2, PricingType.TAXI_START, 7, PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responsePricing);

            when(pricingService.getPricingByType("TAXI_STARTER")).thenReturn(response);
            this.mockMvc.perform(get("/pricing/type/TAXI_START")).andDo(print())
                    .andExpect(status().isOk());
        }

    }


    @Nested
    @WebMvcTest(value = InvoiceController.class)
    @WithMockUser
    @DisplayName("Invoice Rest APIs test")
    public class InvoiceRESTFullTestCases {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private InvoiceService invoiceService;


        @Test
        @DisplayName("Get Invoice By ID RESTFull  API Test")
        public void getInvoiceByIdRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip trip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Invoice responseInvoice = new Invoice(2, 70, trip,  PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responseInvoice);

            when(invoiceService.getInvoiceById(1)).thenReturn(response);
            this.mockMvc.perform(get("/invoice/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get All Invoices RESTFull  API Test")
        public void getAllPricesRESTFullTest() throws Exception {
            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip trip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Invoice responseInvoice = new Invoice(2, 70, trip,  PricingCurrency.EGP, new Date(), new Date(), 0);


            List<Invoice> invoices = new ArrayList<>();
            invoices.add(responseInvoice);

            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    invoices);

            when(invoiceService.getAllInvoices()).thenReturn(response);
            this.mockMvc.perform(get("/invoice")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }

        @Test
        @DisplayName("Get Invoice By Trip Id RESTFull  API Test")
        public void getInvoiceByTripIdRESTFullTest() throws Exception {

            Driver driver = new Driver(2, "sameh", "01100505012",
                    DriverStatus.valueOf("AVAILABLE"), 31.201212, 30.101011, new Date(), new Date());

            Rider rider = new Rider(2, "Said Ali", "01100505012",
                    31.5312, 30.7111, new Date(), new Date(), 0);

            Trip trip = new Trip(1, TripStatus.ACTIVE, 31.0312, 30.011, 31.1111, 30.111, driver, rider, new Date());

            Invoice responseInvoice = new Invoice(2, 70, trip,  PricingCurrency.EGP, new Date(), new Date(), 0);
            RESTResponse response = new RESTResponse(200, "Success", ServiceNames.PRICING,
                    responseInvoice);

            when(invoiceService.getInvoiceByTrip(1)).thenReturn(response);
            this.mockMvc.perform(get("/invoice/trip/1")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Success")));
        }


    }


    }
