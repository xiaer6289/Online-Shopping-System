import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class TestCart {
    private ArrayList<Product> cart;
    private static final String CART_FILE = "database/cart.txt";
    private Customer customer;

    public TestCart() {
        cart = new ArrayList<>();
        // Don't load cart here since customer isn't set yet
    }

    // Set the customer ID for this cart
    public void setCustomer(Customer customer) {
        this.customer = customer;
        cart.clear();
        loadCartFromFile();
    }

    public static void displayCartTable(ArrayList<Product> cartItems) {
        System.out.println("┌──────┬──────────────────────────────────┬──────────┬────────┐");
        System.out.println("│  ID  │           Product Name           │  Price   │  Qty   │");
        System.out.println("├──────┼──────────────────────────────────┼──────────┼────────┤");

        for (Product product : cartItems) {
            System.out.printf("│%-5d │ %-32s │ RM%-6.2f │ %-6d │%n",
                    product.getProductID(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getQuantity());
                    
            System.out.println("└──────┴──────────────────────────────────┴──────────┴────────┘");
        }
    }

    public void saveCart(){
        try {
            // Create directory if it doesn't exist
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // First, read all existing cart items
            ArrayList<String> allCartItems = new ArrayList<>();
            File cartFile = new File(CART_FILE);
            if (cartFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            // Keep items from other customers
                            if (!parts[0].equals(customer.getCusID())) {
                                allCartItems.add(line);
                            }
                        }
                    }
                }
            }
            
            // Add current customer's items
            try(FileWriter writer = new FileWriter(CART_FILE)){
                // Write other customers' items first
                for (String item : allCartItems) {
                    writer.write(item + "\n");
                }
                
                // Write current customer's items
                for(Product product : cart){
                    writer.write(customer.getCusID() + "," + 
                    product.getProductID() + "," + 
                    product.getProductName() + "," + 
                    product.getPrice() + "," + 
                    product.getQuantity() + "\n");
                }
            }
        } catch(IOException e){
            System.out.println("Error saving cart: " + e.getMessage());
        }
    }

    public void loadCartFromFile(){
        cart.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 5 && parts[0].equals(customer.getCusID())){
                    // Create a default Novel as a placeholder since we don't have category info in cart file
                    Product product = new Novel();
                    product.setProductID(Integer.parseInt(parts[1]));
                    product.setProductName(parts[2]);
                    product.setPrice(Double.parseDouble(parts[3]));
                    product.setQuantity(Integer.parseInt(parts[4]));
                    cart.add(product);
                }
            }
        } catch(IOException e){
            System.out.println("Error loading cart: " + e.getMessage());
        }
    }


    public void displayCartMenu(ArrayList<Product> products) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCart Menu: ");
            System.out.println("1. Manage cart (Add Items)");
            System.out.println("2. View cart");
            System.out.println("3. Return to main menu");
            System.out.print("Enter your choice(1-3):");

            int choice = 0;
            boolean validChoice = false;

            while(!validChoice){
                try{
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if(choice < 1 || choice > 3){
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    }
                    validChoice = true;
                } catch(java.util.InputMismatchException e){
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear the invalid input
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    break;
                }
            }

            if (!validChoice) {
                continue; // Go back to showing the cart menu
            }

            switch (choice) {
                case 1:
                    // Show book categories for managing cart
                    while (true) {
                        System.out.println("\nBook Categories:");
                        System.out.println("1. View All Products");
                        System.out.println("2. View Textbooks");
                        System.out.println("3. View Comics");
                        System.out.println("4. View Novels");
                        System.out.println("5. Back to Cart Menu");
                        System.out.print("Please choose an option(1-5): ");

                        int categoryChoice = 0;
                        boolean validCategoryChoice = false;
                        
                        while (!validCategoryChoice) {
                            try {
                                categoryChoice = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                
                                if (categoryChoice < 1 || categoryChoice > 5) {
                                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    break; // Break out of the inner while loop to show menu again
                                }
                                validCategoryChoice = true;
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a valid number.");
                                scanner.nextLine(); // Clear the invalid input
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                break;
                            }
                        }
                        
                        if (!validCategoryChoice) {
                            continue; // Go back to showing the book categories menu
                        }
                        
                        Utility.clearScreen();

                        switch (categoryChoice) {
                            case 1:
                                TestProduct.displayProductTable(products);
                                manageCart(products);
                                break;
                            case 2:
                                ArrayList<Product> textbooks = OnlineShoppingSystem.filterProductsByCategory("Textbook");
                                TestProduct.displayProductTable(textbooks);
                                manageCart(textbooks);
                                break;
                            case 3:
                                ArrayList<Product> comics = OnlineShoppingSystem.filterProductsByCategory("Comic");
                                TestProduct.displayProductTable(comics);
                                manageCart(comics);
                                break;
                            case 4:
                                ArrayList<Product> novels = OnlineShoppingSystem.filterProductsByCategory("Novel");
                                TestProduct.displayProductTable(novels);
                                manageCart(novels);
                                break;
                            case 5:
                                Utility.clearScreen();
                                break; // Return to cart menu
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                continue; // Return to book categories menu
                        }
                        Utility.clearScreen();
                        break; // Exit book categories submenu
                    }
                    if (!validChoice) {
                        continue; // Go back to showing the cart menu
                    }

                    Utility.clearScreen();
                case 2:
                    viewCart(products);
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    continue;

                case 3:
                    Utility.clearScreen();
                    return;

                default:
                    System.out.println("Invalid choice selected.");
                    Utility.pauseScreen();
                    continue;
            }
        }
    }

    // allow only can be call by displaycartmenu only to prevent wrong error
    private void manageCart(ArrayList<Product> products) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Utility.clearScreen();
            TestProduct.displayProductTable(products);
            System.out.println("\n1. Add products to cart");
            System.out.println("2. Return to cart menu");
            System.out.print("Enter your choice(1-2): ");

            int choice = 0;
            boolean validChoice = false;
            
            while (!validChoice) {
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    if (choice < 1 || choice > 2) {
                        System.out.println("Invalid choice. Please enter a number between 1 and 2.");
                        Utility.pauseScreen();
                        break;
                    }
                    validChoice = true;
                } catch(java.util.InputMismatchException e){
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear the invalid input
                    Utility.pauseScreen();
                    break;
                }
            }
            
            if (!validChoice) {
                continue; // Go back to showing the manage cart menu
            }

            if (choice == 1) {
                System.out.print("Enter book ID:");
                
                int productID = 0;
                boolean validID = false;
                Product selectedProduct = null;
                
                while (!validID) {
                    try {
                        productID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        // Check if product exists in products list
                        for (Product product : products) {
                            if (product.getProductID() == productID) {
                                selectedProduct = product;
                                validID = true;
                                break;
                            }
                        }
                        
                        if (!validID) {
                            System.out.println("Book ID not found in the system.");
                            Utility.pauseScreen();
                            break;
                        }
                        validID = true;
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); // Clear the invalid input
                        Utility.pauseScreen();
                        break;
                    }
                }
                
                if (!validID) {
                    continue; // Go back to showing the manage cart menu
                }

                if (selectedProduct != null) {
                    System.out.print("Enter quantity: ");
                    
                    int quantity = 0;
                    boolean validQuantity = false;
                    
                    while (!validQuantity) {
                        try {
                            quantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            if (quantity <= 0) {
                                System.out.println("Invalid quantity. Please enter a positive number.");
                                Utility.pauseScreen();
                                break;
                            }
                            
                            if (quantity > selectedProduct.getStockQuantity()) {
                                System.out.println("Out of stock. Sorry for the inconvenience...");
                                System.out.println("Available stock: " + selectedProduct.getStockQuantity());
                                Utility.pauseScreen();
                                break;
                            }
                            
                            validQuantity = true;
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // Clear the invalid input
                            Utility.pauseScreen();
                            break;
                        }
                    }
                    
                    if (!validQuantity) {
                        continue; // Go back to showing the manage cart menu
                    }

                    // Check if product already exists in cart
                    boolean productExists = false;
                    for (Product cartProduct : cart) {
                        if (cartProduct.getProductID() == productID) {
                            // Add to existing quantity if product exists
                            int newQuantity = cartProduct.getQuantity() + quantity;
                            if (newQuantity <= selectedProduct.getStockQuantity()) {
                                cartProduct.setQuantity(newQuantity);
                                productExists = true;
                                System.out.println("Quantity updated in cart\n");
                            } else {
                                System.out.println("Cannot add more than available stock. Current stock: " + selectedProduct.getStockQuantity());
                                Utility.pauseScreen();
                                return;
                            }
                            break;
                        }
                    }

                    if (!productExists) {
                        // Create a new Product object for the cart using the new method
                        Product cartProduct = selectedProduct.createCartCopy(quantity);
                        cart.add(cartProduct);
                        System.out.println("Book is added to cart\n");
                    }

                    saveCart();
                    Utility.pauseScreen();
                    break;
                }
            } else if (choice == 2) {
                return;
            }
        }
    }

    private void viewCart(ArrayList<Product> products) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Returning to cart menu...");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Utility.clearScreen();
            System.out.println("\nYour cart: ");
            displayCartTable(cart);
            System.out.printf("Total amount: RM%.2f", calTotal());

            System.out.println("\nCart Options: ");
            System.out.println("1. Modify books quantity");
            System.out.println("2. Remove books from cart");
            System.out.println("3. Return to cart menu");
            System.out.print("Enter your choice(1-3):");
            
            int choice = 0;
            boolean validChoice = false;
            
            while (!validChoice) {
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    if (choice < 1 || choice > 3) {
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    }
                    validChoice = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear the invalid input
                    Utility.pauseScreen();
                    Utility.clearScreen();
                    break;
                }
            }
            
            if (!validChoice) {
                continue; // Go back to showing the cart options menu
            }
            
            Utility.clearScreen();

            if (choice == 1) {
                displayCartTable(cart);
                System.out.print("Enter book ID:");
                
                int productID = 0;
                boolean validID = false;
                
                while (!validID) {
                    try {
                        productID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        // Check if product exists in cart
                        boolean productFound = false;
                        for (Product product : cart) {
                            if (product.getProductID() == productID) {
                                productFound = true;
                                break;
                            }
                        }
                        
                        if (!productFound) {
                            System.out.println("Book ID not found in cart.");
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            break;
                        }
                        validID = true;
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); // Clear the invalid input
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    }
                }
                
                if (!validID) {
                    continue; // Go back to showing the cart options menu
                }

                for (Product product : cart) {
                    if (product.getProductID() == productID) {
                        System.out.print("Enter new quantity: ");
                        
                        int newQuantity = 0;
                        boolean validQuantity = false;
                        
                        while (!validQuantity) {
                            try {
                                newQuantity = scanner.nextInt();
                                scanner.nextLine(); // Clear the buffer

                                if (newQuantity <= 0) {
                                    System.out.println("Invalid quantity entered. Please try again.");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    break;
                                }
                                
                                // Get the product from the products list to check stock
                                Product originalProduct = null;
                                for (Product product1 : products) {
                                    if (product1.getProductID() == productID) {
                                        originalProduct = product1;
                                        break;
                                    }
                                }
                                
                                if (originalProduct != null && newQuantity <= originalProduct.getStockQuantity()) {
                                    product.setQuantity(newQuantity);
                                    System.out.println("Cart updated");
                                    saveCart();
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    validQuantity = true;
                                } else if (originalProduct != null) {
                                    System.out.println("Quantity cannot exceed available stock (" + originalProduct.getStockQuantity() + ")");
                                    Utility.pauseScreen();
                                    Utility.clearScreen();
                                    break;
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a valid number.");
                                scanner.nextLine(); // Clear the invalid input
                                Utility.pauseScreen();
                                Utility.clearScreen();
                                break;
                            }
                        }
                        
                        if (!validQuantity) {
                            continue; // Go back to showing the cart options menu
                        }
                        break;
                    }
                }
            } else if (choice == 2) {
                displayCartTable(cart);
                System.out.print("Enter book ID:");
                
                int productID = 0;
                boolean validID = false;
                
                while (!validID) {
                    try {
                        productID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        // Check if product exists in cart
                        boolean productFound = false;
                        for (Product product : cart) {
                            if (product.getProductID() == productID) {
                                productFound = true;
                                break;
                            }
                        }
                        
                        if (!productFound) {
                            System.out.println("Book ID not found in cart.");
                            Utility.pauseScreen();
                            Utility.clearScreen();
                            break;
                        }
                        validID = true;
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); // Clear the invalid input
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    }
                }
                
                if (!validID) {
                    continue; // Go back to showing the cart options menu
                }

                boolean removed = false;
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).getProductID() == productID) {
                        cart.remove(i);
                        removed = true;
                        System.out.println("Book is removed from cart");
                        saveCart();
                        Utility.pauseScreen();
                        Utility.clearScreen();
                        break;
                    }
                }

                if (!removed) {
                    System.out.println("Book ID not found in cart.");
                    Utility.pauseScreen();
                    Utility.clearScreen();
                }
            } else if (choice == 3) {
                Utility.clearScreen();
                return;
            } else {
                System.out.println("Invalid choice selected. Please try again...");
                Utility.pauseScreen();
                Utility.clearScreen();
            }
        }
    }

    public double calTotal() {
        double total = 0.00;
        for (Product product : cart) {
            total += (product.getPrice() * product.getQuantity());
        }
        return total;
    }

    public void clearCart() {
        try {
            // Read all cart items except current customer's
            ArrayList<String> otherCustomersItems = new ArrayList<>();
            File cartFile = new File(CART_FILE);
            if (cartFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            // Keep all items except current customer's
                            if (!parts[0].equals(customer.getCusID())) {
                                otherCustomersItems.add(line);
                            }
                        }
                    }
                }
            }
            
            // Write back only other customers' items
            try (FileWriter writer = new FileWriter(CART_FILE)) {
                for (String item : otherCustomersItems) {
                    writer.write(item + "\n");
                }
            }
            
            cart.clear();
        } catch (IOException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    // Add this to display products that added to cart without private in viewCart
public void displayCartContents() {
    if (cart.isEmpty()) {
        System.out.println("Cart is empty.");
        return;
    }
    displayCartTable(cart);
}

    public ArrayList<Product> getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }
}