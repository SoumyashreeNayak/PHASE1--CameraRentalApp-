package camerarentalapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CameraRental {
    public static void main(String[] args) {
        // Instantiate and run the application
        CameraRentalSystem rentalSystem = new CameraRentalSystem();
        rentalSystem.run();
    }
}

class CameraRentalSystem {
    private List<Camera> cameraList;
    private Wallet userWallet;
    private Scanner scanner;
    private String loggedInUser;

    public CameraRentalSystem() {
        cameraList = new ArrayList<>();
        userWallet = new Wallet();
        scanner = new Scanner(System.in);
        loggedInUser = null;
    }

    public void run() {
        displayWelcomeScreen();
    }

    private void displayWelcomeScreen() {
        System.out.println("Welcome to Camera Rental Application");
        System.out.println("Developed by: Soumyashree Nayak\n");

        if (loggedInUser == null) {
            login();
        }

        System.out.println("Welcome, " + loggedInUser + "!");
        System.out.println("Select an option:");
        System.out.println("1. Add a new camera");
        System.out.println("2. Display available cameras for rent");
        System.out.println("3. Rent a camera");
        System.out.println("4. Remove a camera");
        System.out.println("5. View wallet balance");
        System.out.println("6. Deposit funds to the wallet");
        System.out.println("7. Logout");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                addNewCamera();
                break;
            case 2:
                displayCameraListing();
                break;
            case 3:
                rentCamera();
                break;
            case 4:
                removeCamera();
                break;
            case 5:
                viewWalletBalance();
                break;
            case 6:
                depositFunds();
                break;
            case 7:
                loggedInUser = null;
                System.out.println("Logged out successfully.");
                displayWelcomeScreen();
                break;
            case 8:
                System.out.println("Thank you for using rentmycam.io. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayWelcomeScreen();
                break;
        }
    }
    
    private void login() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("password")) {
            loggedInUser = username;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            login();
        }
    }

    private void addNewCamera() {
        System.out.print("Enter the camera brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter the camera model: ");
        String model = scanner.nextLine();

        System.out.print("Enter the per-day rent amount: ");
        double rentAmount = scanner.nextDouble();
        scanner.nextLine(); 

        Camera camera = new Camera(brand, model, rentAmount);
        cameraList.add(camera);

        System.out.println("Camera added successfully!");

        displayWelcomeScreen();
    }
    
    private void displayCameraListing() {
        if (cameraList.isEmpty()) {
            System.out.println("No Data Present at This Moment");
        } else {
            System.out.println("List of Available Cameras for Rent:");
            System.out.println("-------------------------------------");
            System.out.printf("| %-4s | %-10s | %-10s | %-10s | %-7s |\n", "ID", "Brand", "Model", "Price", "Status");
            System.out.println("-------------------------------------");

            for (int i = 0; i < cameraList.size(); i++) {
                Camera camera = cameraList.get(i);
                String status = "Available";
                if (camera.isRented()) {
                    status = "Rented";
                }
                System.out.printf("| %-4d | %-10s | %-10s | %-10.2f | %-7s |\n",
                        camera.getCameraId(), camera.getBrand(), camera.getModel(), camera.getPerDayRentAmount(), status);
            }
            
            System.out.println("-------------------------------------");
        }

        displayWelcomeScreen();
    }
    
    private void rentCamera() {
        if (cameraList.isEmpty()) {
            System.out.println("No cameras available for rent.");
            displayWelcomeScreen();
            return;
        } else {
            System.out.println("List of Available Cameras for Rent:");
            System.out.println("-------------------------------------");
            System.out.printf("| %-4s | %-10s | %-10s | %-10s | %-7s |\n", "ID", "Brand", "Model", "Price", "Status");
            System.out.println("-------------------------------------");

            for (int i = 0; i < cameraList.size(); i++) {
                Camera camera = cameraList.get(i);
                String status = "Available";
                if (camera.isRented()) {
                    status = "Rented";
                }
                System.out.printf("| %-4d | %-10s | %-10s | %-10.2f | %-7s |\n",
                        camera.getCameraId(), camera.getBrand(), camera.getModel(), camera.getPerDayRentAmount(), status);
            }

            System.out.println("-------------------------------------");
        }
        
        System.out.println("Select a camera to rent:");
        for (int i = 0; i < cameraList.size(); i++) {
            Camera camera = cameraList.get(i);
            System.out.printf("%d. %s %s (Price: %.2f per day)\n", (i + 1), camera.getBrand(), camera.getModel(),
                    camera.getPerDayRentAmount());
        }

        System.out.print("Enter the camera number: ");
        int cameraNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (cameraNumber < 1 || cameraNumber > cameraList.size()) {
            System.out.println("Invalid camera number.");
            displayWelcomeScreen();
            return;
        }

        Camera selectedCamera = cameraList.get(cameraNumber - 1);

        if (selectedCamera.isRented()) {
            System.out.println("Camera is already rented.");
        } else if (userWallet.getBalance() < selectedCamera.getPerDayRentAmount()) {
            System.out.println("Insufficient balance in your wallet. Cannot rent the camera.");
        } else {
            selectedCamera.setRented(true);
            userWallet.deductBalance(selectedCamera.getPerDayRentAmount());
            System.out.println("Camera rented successfully!");
        }

        displayWelcomeScreen();
    }
    
    private void removeCamera() {
        if (cameraList.isEmpty()) {
            System.out.println("No cameras available to remove.");
            displayWelcomeScreen();
            return;
        }else {
            System.out.println("List of Available Cameras for Rent:");
            System.out.println("-------------------------------------");
            System.out.printf("| %-4s | %-10s | %-10s | %-10s | %-7s |\n", "ID", "Brand", "Model", "Price", "Status");
            System.out.println("-------------------------------------");

            for (int i = 0; i < cameraList.size(); i++) {
                Camera camera = cameraList.get(i);
                String status = "Available";
                if (camera.isRented()) {
                    status = "Rented";
                }
                System.out.printf("| %-4d | %-10s | %-10s | %-10.2f | %-7s |\n",
                        camera.getCameraId(), camera.getBrand(), camera.getModel(), camera.getPerDayRentAmount(), status);
            }

            System.out.println("-------------------------------------");
        }
        
        System.out.print("Enter the camera ID to remove: ");
        int cameraId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Camera selectedCamera = findCameraById(cameraId);

        if (selectedCamera == null) {
            System.out.println("Camera not found.");
        } else {
            if (selectedCamera.isRented()) {
                System.out.println("Cannot remove a rented camera.");
            } else {
                cameraList.remove(selectedCamera);
                System.out.println("Camera removed successfully!");
            }
        }

        displayWelcomeScreen();
    }

    private void viewWalletBalance() {
        double balance = userWallet.getBalance();
        System.out.println("Wallet Balance: " + balance);
        displayWelcomeScreen();
    }

    private void depositFunds() {
        System.out.print("Enter the amount to deposit: ");
        double depositAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        userWallet.addBalance(depositAmount);
        System.out.println("Funds deposited successfully!");

        displayWelcomeScreen();
    }
    
    private Camera findCameraById(int cameraId) {
        for (Camera camera : cameraList) {
            if (camera.getCameraId() == cameraId) {
                return camera;
            }
        }
        return null;
    }
}

class Camera {
    private static int lastCameraId = 0;
    private int cameraId;
    private String brand;
    private String model;
    private double perDayRentAmount;
    private boolean rented;

    public Camera(String brand, String model, double perDayRentAmount) {
        this.cameraId = ++lastCameraId;
        this.brand = brand;
        this.model = model;
        this.perDayRentAmount = perDayRentAmount;
        this.rented = false;
    }

    public int getCameraId() {
        return cameraId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPerDayRentAmount() {
        return perDayRentAmount;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}

class Wallet {
    private double balance;

    public Wallet() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void deductBalance(double amount) {
        balance -= amount;
    }
}