# CreDebt

A modern Android application for tracking debts and credits.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)

## Overview

CreDebt is an offline-first Android application designed to help users manage personal finances by tracking debts and credits. The app provides a simple and efficient way to record, monitor, and manage financial transactions with contacts.

## Screenshots

<div align="center">
  <img src="screenshots/Screenshot_20260107-002224.png" width="200" alt="Home Screen"/>
  <img src="screenshots/Screenshot_20260107-002317.png" width="200" alt="Transaction List"/>
  <img src="screenshots/Screenshot_20260107-002244.png" width="200" alt="User Detail Screen"/>
  <img src="screenshots/Screenshot_20260107-002307.png" width="200" alt="Add Transaction"/>
  <img src="screenshots/Screenshot_20260107-002312.png" width="200" alt="Currency Selection"/>
</div>

## Features

### Core Functionality
- **Dashboard Summary** - Overview of total debts and credits
- **User Management** - Add and manage contacts for financial transactions
- **Transaction Tracking** - Record transactions with descriptions, amounts, dates, and times
- **Debt & Credit Modes** - Track both debts and credits
- **Payment Status** - Mark transactions as paid or unpaid with swipe gestures
- **Date & Time Selection** - Built-in date and time pickers for accurate transaction recording
- **Multi-Currency Support** - Support for 7 currencies: INR, USD, EUR, GBP, JPY, AUD, CAD

### User Experience
- **Material Design 3** - Modern UI following Google's Material Design guidelines
- **Dark Theme** - Dark mode interface
- **Offline First** - All data stored locally, no internet connection required
- **Performance** - Optimized database queries for smooth performance
- **Privacy** - All financial data stored locally on device

### Advanced Features
- **Automatic Balance Calculation** - Real-time balance updates for each contact
- **Edit & Delete** - Modify transactions and user details
- **Transaction History** - Complete history view for each contact
- **Data Protection** - Users can only be deleted when their balance is zero

## Setup

### Prerequisites
- Android 7.0 (API level 24) or higher
- Android Studio (Arctic Fox or later recommended)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/zappvik/CreDebt.git
   cd CreDebt
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select File → Open
   - Navigate to the cloned CreDebt directory
   - Click OK

3. **Sync Gradle**
   - Android Studio will automatically prompt you to sync Gradle
   - Click Sync Now or go to File → Sync Project with Gradle Files
   - Wait for the sync to complete

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the Run button or press `Shift + F10`
   - Select your device/emulator and the app will install and launch

## Tech Stack

- **Kotlin** - Programming language for Android development
- **Jetpack Compose** - Declarative UI framework for building native Android apps
- **Room Database** - Local SQLite database for data persistence
- **MVVM Architecture** - Clean architecture pattern with LiveData
- **Material Design 3** - UI components and theming

## Release

### Download APK

Download the latest release APK from the [Releases](https://github.com/zappvik/CreDebt/releases) page.

**Installation Instructions:**
1. Enable "Install from Unknown Sources" in your device settings
2. Download the APK file
3. Open the downloaded file and follow the installation prompts

## Usage

### Initial Setup

1. **Select Currency**
   - Tap the currency symbol in the top-right corner
   - Choose your preferred currency from the list
   - Your selection is automatically saved

2. **Add Contacts**
   - Tap the "New" floating action button
   - Enter the person's name
   - Tap "Add User"

### Managing Transactions

1. **Add a Transaction**
   - Tap on a user card to open their detail screen
   - Tap the "New" button to add a transaction
   - Fill in the details:
     - Description
     - Amount
     - Date (using the date picker)
     - Time (using the time picker)
     - Type (toggle between Debt and Credit)
   - Tap "Add" to save

2. **Mark as Paid/Unpaid**
   - Swipe left on any transaction
   - The payment status will toggle automatically

3. **Edit or Delete**
   - Long press on a transaction to edit or delete it
   - Long press on a user card to edit their name or delete them
   - Note: Users can only be deleted when their balance is zero

### Viewing Summary

The home screen displays two summary cards:
- **You Owe**: Total amount you owe to others
- **Owed to You**: Total amount others owe you

## Privacy and Security

We take your privacy and security seriously. All your financial data is stored locally on your device. We do not share your personal information with third parties without your consent.

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit with clear messages (`git commit -m 'Add amazing feature'`)
5. Push to your branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

### Contribution Guidelines

- Follow the existing code style
- Write clear commit messages
- Test your changes thoroughly
- Update documentation if needed
- For major changes, open an issue first to discuss

## Contributors

- [ariyha](https://github.com/ariyha) - Project Creator & Maintainer
- [zappvik](https://github.com/zappvik) - Contributor

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Nithishariyha**

- Email: nithishariyha02467@gmail.com
- GitHub: [@ariyha](https://github.com/ariyha)

## Acknowledgments

- Material Design 3 for UI components
- Android Jetpack team for libraries
- All contributors and users of CreDebt

## Feedback & Support

For bug reports, feature requests, or questions:

- Email: nithishariyha02467@gmail.com
- [Report an Issue](https://github.com/zappvik/CreDebt/issues)
- [Request a Feature](https://github.com/zappvik/CreDebt/issues)


