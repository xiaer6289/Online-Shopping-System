import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TestOrderHistory {
    private static final String ORDER_HISTORY_FILE = "database/orderhistory.txt";

    public static void saveToOrderHistoryFile(OrderHistory orderHistory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_HISTORY_FILE, true))) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedTimestamp = orderHistory.getTimestamp().format(formatter);

            // Order and customer
            Order order = orderHistory.getOrder();
            Customer customer = orderHistory.getCustomer();

            writer.write(formattedTimestamp + "," + customer.getCusID() + ",");

            // Products and total amount
            ArrayList<Product> products = order.getOrder();
            double totalPrice = order.getTotalAmount();
            double subtotalAllBooks = 0.0;

            String shippingMethod = "";
            double fee = 0.0;
            if (order instanceof DeliveryOrder) {
                DeliveryOrder deliveryOrder = (DeliveryOrder) order;
                if (deliveryOrder.getDeliveryType().equals("DELIVERY")) {
                    shippingMethod = "Delivery Fee";
                    fee = deliveryOrder.getDeliveryFees();
                }
            } else if (order instanceof SelfPickupOrder) {
                SelfPickupOrder pickupOrder = (SelfPickupOrder) order;
                if (pickupOrder.getDeliveryType().equals("SELF_PICKUP")) {
                    shippingMethod = "Pickup Fee";
                    fee = pickupOrder.calculateFees();
                }
            }
            writer.write(shippingMethod + "," + String.format("%.2f", fee) + ",");

            // Calculate subtotal of all books
            for (Product p : products) {
                double subtotal = p.getPrice() * p.getQuantity();
                subtotalAllBooks += subtotal;
            }

            writer.write(String.format("%.2f", subtotalAllBooks) + ",");

            writer.write(String.format("%.2f", totalPrice) + ",");

            // Book name, price per quantity, quantity, subtotal
            for (Product p : products) {
                double subtotal = p.getPrice() * p.getQuantity();
                writer.write(p.getProductName() + "," +
                        String.format("%.2f", p.getPrice()) + "," +
                        p.getQuantity() + "," +
                        String.format("%.2f", subtotal) + ",");
            }

            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    public static void viewOrderHistory(String customerID) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> userOrders = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(ORDER_HISTORY_FILE));

            String line;

            // Read Each Line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[1].equals(customerID)) {
                    userOrders.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading order history: " + e.getMessage());
            return;
        }

        if (userOrders.isEmpty()) {
            System.out.println("No order history found.");
            return;
        }

        System.out.println("\n============ Your Order History ============");
        int count = 1;
        for (String order : userOrders) {
            String[] parts = order.split(",");
            System.out.println(count + ". " + parts[0] + " -> Total: RM" + parts[5]);
            count++;
        }

        System.out.print("\nEnter the number of the order to view details or 0 to return: ");

        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        for (int i = 1; i <= userOrders.size(); i++) {
            if (choice.equals(String.valueOf(i))) {
                showOrderDetails(userOrders.get(i - 1));
                return;
            }
        }

        System.out.println("Invalid selection. Returning to menu.");

    }

    private static void showOrderDetails(String orderLine) {
        String[] parts = orderLine.split(",");

        String timestamp = parts[0];
        String shippingMethod = parts[2];
        String fee = parts[3];
        String subtotal = parts[4];
        String totalAmount = parts[5];

        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> quantities = new ArrayList<>();
        ArrayList<String> subtotals = new ArrayList<>();

        for (int i = 6; i < parts.length - 3; i += 4) {
            productNames.add(parts[i]);
            prices.add(parts[i + 1]);
            quantities.add(parts[i + 2]);
            subtotals.add(parts[i + 3]);
        }

        System.out.println("\n┌──────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                                 ORDER HISTORY                                │");
        System.out.println("├──────────────────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-40s%-37s│%n", "Date: " + timestamp, "");
        System.out.println("├──────┬──────────────────────────────────┬──────────┬──────────┬──────────────┤");
        System.out.println("│ No.  │           Product Name           │  Price   │ Quantity │   Subtotal   │");
        System.out.println("├──────┼──────────────────────────────────┼──────────┼──────────┼──────────────┤");

        for (int i = 0; i < productNames.size(); i++) {
            String name = productNames.get(i);
            if (name.length() > 32) {
                name = name.substring(0, 29) + "...";
            }
            System.out.printf("│ %-4d │ %-32s │ %-8s │ %-8s │ %-12s │%n", (i + 1), productNames.get(i), prices.get(i),
                    quantities.get(i), subtotals.get(i));
        }

        System.out.println("├──────┴──────────────────────────────────┴──────────┴──────────┴──────────────┤");
        System.out.printf("│ %-40s%-37s│%n", "Subtotal: RM" + subtotal, "");
        System.out.printf("│ %-40s%-37s│%n", shippingMethod + ": RM" + fee, "");
        System.out.printf("│ %-40s%-37s│%n", "Total Amount: RM" + totalAmount, "");
        System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
    }
}