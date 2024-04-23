# Network Monitoring System

Welcome to the Network Monitoring System! This system is a command-line tool built using core Java, JDBC connector, and MySQL database. It provides network administrators with the ability to monitor and provision network devices.

## Features:

- **User Authentication**: Users can register or login to access the system.
- **Credential Profile Management**: Users can create, view, and manage credential profiles for network devices.
- **Discovery Profile Management**: Users can create, view, and manage discovery profiles for network devices. A credential profile is required to create a discovery profile.
- **Device Provisioning**: Users can provision network devices based on discovery profiles. A discovery profile is required to provision a device.
- **Device Monitoring**: Once provisioned, the system starts polling various metrics of the device, including CPU and memory usage.
   - CPU metrics (user %, system %, idle %, load average)
   - Memory metrics (free memory, used memory, total memory)
   - Swap memory metrics (free swap, used swap, total swap)

## Prerequisites

- Java Development Kit (JDK) installed
- MySQL Server installed and running
- JDBC connector for MySQL

## Usage:

1. **Setup Database**:
   - Start the MySQL server if it's not already running.
  
2. **Compile and Run**:
   - Compile the Java source files.
   - Run the main program (`Main.java`) from the terminal.
    
3. **User Interaction**:
   - Upon starting the program, users are presented with the following options:
     1. Login
     2. Register
     3. Exit

4. **User Authentication**:
   - Register or login to access the system.

5. **Main Menu**:
   - Upon successful login, users are presented with the following options:
     1. Create Credential Profile
     2. Create Discovery Profile
     3. View Credential Profile
     4. View Discovery Profile
     5. Provision Device
     6. Exit

6. **Metrics Monitoring**:
   - Once a device is provisioned, the system starts polling various metrics of the device, including CPU and memory usage. These metrics can be viewed in the system_metrics table of the databse.

7. **Exit**:
   - To exit the program, simply type choose the appropriate option from the menu.

## Database Configuration:

- Update the database connection settings in the `DatabaseConnection.java` file before running the program. Update the `URL`, `USERNAME`, and `PASSWORD` variables with your MySQL database credentials.


Happy monitoring and provisioning! 🌐🔍

