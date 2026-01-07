# CreDebt: Debt & Credit Tracker App

## Overview
CreDebt is an Android application designed to help you conveniently manage your debts and credits. Whether you owe someone money or someone owes you, this app provides a simple and efficient way to keep track of your financial transactions.

## Features
- **User-friendly Interface**: CreDebt offers an intuitive interface built with Material Design 3, making it easy for users to navigate and record transactions.
  
- **Summary Dashboard**: View your total debts and credits at a glance with summary cards showing "You Owe" and "Owed to You" amounts.

- **Debt Tracking**: Easily add and manage debts owed by you or to you. Specify details such as the amount owed and the person involved.

- **Credit Tracking**: Keep track of credits you've extended or received. Input details like the amount, creditor/debtor, and relevant dates.

- **Transaction History**: View a comprehensive history of all your transactions, including debts and credits, for better financial transparency. Each transaction shows date, time, description, and amount.

- **Date & Time Selection**: Use built-in date and time pickers to accurately record when transactions occurred.

- **Payment Status**: Mark transactions as paid or unpaid with a simple swipe gesture. Visual indicators show the payment status of each transaction.

- **Currency Selection**: Choose from 7 supported currencies (INR ₹, USD $, EUR €, GBP £, JPY ¥, AUD A$, CAD C$). Your currency preference is saved and remembered across app sessions.

- **Balance Calculation**: Automatic calculation of balances for each person based on all transactions.

- **Edit & Delete**: Edit transaction details or delete transactions and users as needed. Users can only be deleted when their balance is zero.

- **Search Functionality** (to be added soon): Quickly search through your transaction history to find specific records based on various parameters like name, amount, or date.

## Requirements
- Android 7.0 (API level 24) or higher
- Internet connection not required (fully offline app)

## How to Use
1. **Installation**: Download and install the "CreDebt" app from the Play Store or build from source.
2. **Set Your Currency**: Tap the currency symbol in the top right corner to select your preferred currency. The app will remember your choice.
3. **Add Users**: Tap the "New" button (floating action button) to add people you have financial transactions with.
4. **Add Transactions**: Click on a user card to view their details and add transactions. Input relevant details such as:
   - Amount
   - Description
   - Date (using the date picker)
   - Time (using the time picker)
   - Toggle between Debt and Credit modes using the switch
5. **View Summary**: On the home screen, see your total "You Owe" and "Owed to You" amounts at the top.
6. **Manage Transactions**: 
   - Swipe left on transactions to mark them as paid/unpaid
   - Long press on transactions to edit or delete them
7. **Edit or Delete Users**: Long press on a user card to edit their name or delete them (only if their balance is zero).

## Building from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/ariyha/CreDebt.git
   cd CreDebt
   ```

2. Open the project in Android Studio (Arctic Fox or later recommended)

3. Sync Gradle files and build the project

4. Run the app on an emulator or connected device

### Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room Database (SQLite)
- **Architecture**: MVVM pattern with LiveData
- **Build Tool**: Gradle with KSP (Kotlin Symbol Processing)
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Feedback and Support
We value your feedback! If you encounter any issues, have suggestions for improvement, or require assistance, please don't hesitate to contact us. You can reach out to our support team(Which is only me) via email at nithishariyha02467@gmail.com.

## Privacy and Security
We take your privacy and security seriously. All your financial data is stored locally on your device. We do not share your personal information with third parties without your consent.

## About Us
"CreDebt" is developed by Nithishariyha, a student dedicated to creating innovative solutions to simplify everyday tasks. I strive to provide user-friendly and reliable apps to enhance your productivity and efficiency.

Thank you for choosing "CreDebt"! We hope it helps you manage your finances effectively.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing
Contributions are welcome! If you'd like to contribute, please fork the repository and create a pull request. For major changes, please open an issue first to discuss what you would like to change.

## Tags
Debt, Credit, Tracker, Android, Finance, Management, Transactions, Personal Finance, Productivity, Kotlin, Jetpack Compose, Room Database.
